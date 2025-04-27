package core

import core.Shell.oneshotShell
import core.Shell.simpleShell
import datastore.adbDataStore
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

object AdbRepository {

    private val scope = MainScope()
    private val _stream = MutableStateFlow(AdbInfo())
    val stream = _stream.asStateFlow()

    var adbParentPath: String? = null
    private val selectedDevice get() = _stream.value.selectedDevice

    init {
        scope.launch {
            //首先尝试从环境变量中获取adb路径
            // 获取系统环境变量
            val env = System.getenv()
            val path = env["PATH"] ?: ""
            val home = System.getProperty("user.home")

            // 使用whereis命令获取路径，并确保加载完整的shell环境
            val androidSdkPath = "${home}/Library/Android/sdk"
            val platformToolsPath = "$androidSdkPath/platform-tools"
            val newPath =
                "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/opt/homebrew/bin:$platformToolsPath:$path"

            val shellCommand = """
                export PATH='$newPath'
                whereis adb
            """.trimIndent()

            val adbPath = shellCommand.simpleShell().substringAfter("adb:", "").trim()
            importAdb(adbPath)

            adbDataStore.data.combine(GlobalTimer.tick) { adbInfo, _ -> adbInfo }.collect {
                val file = File(it.toolPath)
                val adbAvailable = file.isFile && file.name.matches("^adb(\\.exe)?$".toRegex())
                if (adbAvailable.not()) _stream.emit(AdbInfo())
                else {
                    adbParentPath = File(it.toolPath).parentFile.absolutePath
                    val devices = "adb devices".oneshotShell { result ->
                        val pattern = Regex("^([a-zA-Z0-9-]+)\\s+device$", RegexOption.MULTILINE)
                        pattern.findAll(result).map { it.groupValues[1] }.toList()
                    }
                    val selectedDevice =
                        if (selectedDevice.isNotBlank() && devices.contains(selectedDevice)) _stream.value.selectedDevice
                        else devices.firstOrNull() ?: ""
                    _stream.emit(AdbInfo(it.toolPath, true, devices, selectedDevice))
                }
            }
        }
    }

    fun importAdb(adbPath: String?) {
        adbPath ?: return
        scope.launch {
            adbDataStore.updateData { it.toBuilder().setToolPath(adbPath).build() }
        }
    }

    fun selectDevice(deviceName: String) {
        _stream.update { it.copy(selectedDevice = deviceName) }
    }

    fun formatIfAdbCmd(input: String): String {
        if (!input.startsWith("adb")) return input

        //是否有选中设备
        var command = input
        if (selectedDevice.isNotBlank()) {
            command = input.replace("adb", "adb -s $selectedDevice")
        }

        //兼容Windows
        if (System.getProperty("os.name").contains("Windows")) {
            command = command.replace("adb", "cmd /c adb").replace("grep", "findstr")
        }

        return "$adbParentPath/$command"
    }
}

data class AdbInfo(
    val adbToolPath: String = "", //adb工具路径
    val adbToolAvailable: Boolean = false, //adb工具是否可用
    val connectingDevices: List<String> = emptyList(), //正在连接的adb备
    val selectedDevice: String = "", //选中连接的adb设备
)
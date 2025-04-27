package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.AdbInfo
import core.AdbRepository
import ui.theme.MaaIcons
import ui.theme.inputChipColorsMd3
import ui.theme.menuItemColorsMd3

/**
 * Adb路径功能区
 */
@Composable
fun AdbPath(
    adbInfo: AdbInfo,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(30.dp)) {
            Text("Adb路径：${adbInfo.adbToolPath}", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.width(2.dp))
            Icon(
                if (adbInfo.adbToolAvailable) MaaIcons.CheckCircle else MaaIcons.Error,
                if (adbInfo.adbToolAvailable) "valid" else "invalid",
                tint = if (adbInfo.adbToolAvailable) Color.Green else Color.Red,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        }
        if (adbInfo.adbToolAvailable) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("已连接设备：", style = MaterialTheme.typography.labelMedium)
                AttachedDeviceDropdownMenu(adbInfo) { AdbRepository.selectDevice(it) }
            }
        }
    }
}

/**
 * 已关联设备列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachedDeviceDropdownMenu(adbInfo: AdbInfo, onSelectDevice: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        InputChip(
            onClick = { expanded = true },
            selected = false,
            shape = MaterialTheme.shapes.small,
            colors = inputChipColorsMd3(),
            label = {
                Text(
                    text = adbInfo.selectedDevice,
                    style = MaterialTheme.typography.labelLarge
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Rounded.ArrowDropDown, "展开收起按钮",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            })

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            adbInfo.connectingDevices.forEach {
                DropdownMenuItem(text = { Text(it) }, colors = menuItemColorsMd3(), onClick = {
                    expanded = false
                    onSelectDevice.invoke(it)
                })
            }
        }
    }
}

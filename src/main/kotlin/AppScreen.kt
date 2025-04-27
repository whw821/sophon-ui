import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.processor.SlotManger
import core.AdbRepository
import core.GlobalTimer
import ui.components.AdbPath
import ui.components.ToolBar

class AppScreen : Screen {

    @Composable
    override fun Content() {

        val adbUiState by AdbRepository.stream.collectAsState()

        LaunchedEffect(Unit) {
            GlobalTimer.start()
        }

        Navigator(HomeScreen()) {
            Column(Modifier.fillMaxSize().background(Color.White)) {
                ToolBar(
                    title = SlotManger.list.find { slot ->
                        slot.second == it.lastItem.javaClass.name
                    }?.first ?: "首页",
                    isHome = !it.canPop
                ) { it.pop() }
                AdbPath(adbUiState, Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                Box(
                    modifier = Modifier.padding(
                        top = if (adbUiState.adbToolAvailable) 0.dp else 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                ) {
                    CurrentScreen()
                }
            }
        }
    }
}
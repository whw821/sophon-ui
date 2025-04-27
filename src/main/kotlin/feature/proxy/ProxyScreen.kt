package feature.proxy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.onClick
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.processor.annotation.Slot
import ui.theme.filledTonalButtonColorsMd3
import ui.theme.listItemColorsMd3
import ui.theme.outlinedTextFieldColorsMd3

/**
 * 设置手机代理
 */
@Slot("设置代理")
class ProxyScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val proxyVM = rememberScreenModel { ProxyViewModel() }
        val proxyInfo by proxyVM.state.collectAsState()
        var inputProxy by remember { mutableStateOf("") }

        Column {
            Text("当前代理：${proxyInfo.current}", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = inputProxy,
                    onValueChange = { inputProxy = it },
                    label = { Text("输入代理地址") },
                    textStyle = MaterialTheme.typography.bodySmall,
                    singleLine = true,
                    modifier = Modifier.height(56.dp).padding(horizontal = 16.dp),
                    colors = outlinedTextFieldColorsMd3()
                )
                FilledTonalButton(
                    onClick = { proxyVM.setProxy(inputProxy) },
                    colors = filledTonalButtonColorsMd3()
                ) {
                    Text("设置代理", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.width(10.dp))
                FilledTonalButton(onClick = { proxyVM.resetProxy() }) {
                    Text("还原代理", style = MaterialTheme.typography.labelLarge)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("本机IP地址", style = MaterialTheme.typography.titleMedium)
            LazyColumn(verticalArrangement = Arrangement.Center) {
                items(proxyInfo.options.count()) {
                    ListItem(
                        headlineText = {
                            Text(
                                proxyInfo.options[it],
                                modifier = Modifier.fillMaxSize()
                                    .onClick { inputProxy = proxyInfo.options[it] }
                            )
                        }, colors = listItemColorsMd3()
                    )
                }
            }
        }
    }

}
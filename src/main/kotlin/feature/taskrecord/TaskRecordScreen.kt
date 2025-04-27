package feature.taskrecord

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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

@Slot("任务栈")
class TaskRecordScreen : Screen {

    @Composable
    override fun Content() {
        val taskVM = rememberScreenModel { TaskRecordViewModel() }
        val record by taskVM.state.collectAsState()
        var selectedComponent by remember { mutableStateOf<LifecycleComponent?>(null) }

        Row(modifier = Modifier.fillMaxSize()) {
            // 左列 - 组件列表 (1/2宽度)
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // 添加标题和分隔线
                    Text(
                        text = "组件列表",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(12.dp)
                    )

                    val listState = rememberLazyListState()
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val lifecycles = record.lifecycleComponents
                        items(lifecycles.size) {
                            val component = lifecycles[it]

                            // 使用自定义扩展函数来渲染组件树
                            ComponentTreeRenderer(
                                component = component,
                                selectedComponent = selectedComponent,
                                onItemClick = { clickedComponent ->
                                    selectedComponent = clickedComponent
                                }
                            )
                        }
                    }
                }
            }

            // 右列 - 详细信息 (1/2宽度)
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                if (selectedComponent != null) {
                    LifecycleDetailCard(
                        component = selectedComponent!!,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    // 未选择组件时的提示
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "请从左侧列表选择一个组件查看详情",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }


}
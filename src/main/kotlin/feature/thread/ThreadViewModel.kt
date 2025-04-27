package feature.thread

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.GlobalTimer
import feature.taskrecord.TopTaskDataSource
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import core.AdbRepository

class ThreadViewModel : StateScreenModel<List<ThreadInfo>>(emptyList()) {

    private val topTaskDataSource = TopTaskDataSource()
    private val threadDataSource = ThreadDataSource()

    init {
        screenModelScope.launch {
            combine(AdbRepository.stream, GlobalTimer.tick) { _, _ ->
                val overview = topTaskDataSource.queryPackageName()
                val pid = threadDataSource.queryPidWithPkg(overview)
                val threadInfo = threadDataSource.queryThreadList(pid)
                mutableState.value = threadInfo
            }.stateIn(this)
        }
    }

}
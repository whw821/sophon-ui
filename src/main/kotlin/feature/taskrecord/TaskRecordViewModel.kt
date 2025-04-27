package feature.taskrecord

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.GlobalTimer
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import core.AdbRepository

class TaskRecordViewModel : StateScreenModel<TaskRecord>(TaskRecord()) {
    private val dataSource = TopTaskDataSource()

    init {
        screenModelScope.launch {
            combine(AdbRepository.stream, GlobalTimer.tick) { _, _ ->
                val overview = dataSource.queryPackageName()
                val detail = dataSource.queryDetail(overview)
                mutableState.value = detail.formatLevel()
            }.stateIn(this)
        }
    }

}
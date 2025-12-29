package com.example.fo_jump_meter.app.screens.singleJump

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fo_jump_meter.app.database.types.Jump
import com.example.fo_jump_meter.app.database.types.Snapshot
import com.example.fo_jump_meter.app.dialogFactory.DialogConfig
import com.example.fo_jump_meter.app.dialogFactory.DialogConfigState
import com.example.fo_jump_meter.app.dialogFactory.DialogFactory
import com.example.fo_jump_meter.app.repositories.JumpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleJumpViewModel @Inject constructor(
    private val jumpsRepository: JumpRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val jumpId: Long = checkNotNull(savedStateHandle["jumpId"])

    private val _jump = MutableStateFlow<Jump?>(null)
    val jump: StateFlow<Jump?> get() = _jump

    private val _snapshots = MutableStateFlow<List<Snapshot>>(emptyList())
    val snapshots: StateFlow<List<Snapshot>> get() = _snapshots

    private val _chartType = MutableStateFlow(ChartType.HEIGHT)
    val chartType: StateFlow<ChartType> get() = _chartType

    val chartPoints: StateFlow<List<ChartPoint>> = combine(_snapshots, _chartType) {
        currSnapshots, currType ->
        if (currSnapshots.isEmpty()) return@combine emptyList()

        val startTime = currSnapshots.first().timestamp
        currSnapshots.map { snapshot ->
            ChartPoint(
                x = (snapshot.timestamp - startTime) / 1000.0f,
                y = when (currType) {
                    ChartType.HEIGHT -> snapshot.height
                    ChartType.VELOCITY -> snapshot.velocity
                    ChartType.ACCELERATION -> snapshot.acceleration
                }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    //dialog
    private val dialogFactory = DialogFactory()
    private val _isInputWeightDialogOpen = MutableStateFlow(false)
    val isInputWeightDialogOpen: StateFlow<Boolean> get() = _isInputWeightDialogOpen
    val isInputWeightDialogConfig: DialogConfig? =
        dialogFactory.create(
            state = DialogConfigState.InputWeight,
            onConfirm = { weight ->
                viewModelScope.launch {
                    saveWeight(weight!!.toShort())
                }
                _isInputWeightDialogOpen.value = false
            },
            onDismiss = { _isInputWeightDialogOpen.value = false }
        )

    init {
        loadJump()
        loadSnapshots()
    }

    private fun loadJump() {
        viewModelScope.launch {
            _jump.emit(jumpsRepository.getJumpById(jumpId))
        }
    }

    private fun loadSnapshots() {
        viewModelScope.launch {
            _snapshots.emit(jumpsRepository.getSnapshotsByJumpId(jumpId))
        }
    }

    fun changeChartType(type: ChartType) {
        _chartType.value = type
    }

    fun inputWeight() {
        _isInputWeightDialogOpen.value = true
    }

    private suspend fun saveWeight(weight: Short) {
        val currentJump = _jump.value ?: return
        val updatedJump = currentJump.copy(weight = weight)
        jumpsRepository.updateJump(updatedJump)
        _jump.emit(updatedJump)
    }

    val work: StateFlow<Float?> = combine(_jump, _snapshots) { jump, snapshots ->
        if (jump == null || jump.weight.toInt() == 0 || snapshots.isEmpty()) {
            return@combine null
        }

        val maxHeight = snapshots.maxOf { it.height }
        val minHeight = snapshots.minOf { it.height }
        val totalDeltaH = maxHeight - minHeight
        val g = 9.81f

        jump.weight * g * totalDeltaH
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
}
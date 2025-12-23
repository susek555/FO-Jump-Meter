package com.example.fo_jump_meter.app.screens.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fo_jump_meter.app.database.types.Jump
import com.example.fo_jump_meter.app.repositories.JumpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val jumpsRepository: JumpRepository
) : ViewModel() {
    private val _jumps = MutableStateFlow<List<Jump>>(emptyList())
    val jumps: StateFlow<List<Jump>> get() = _jumps

    init {
        viewModelScope.launch {
            _jumps.emit(jumpsRepository.getJumps())
        }

    }

    fun deleteJump(jump: Jump) {
        viewModelScope.launch  {
            jumpsRepository.deleteJump(jump)
            _jumps.emit(jumpsRepository.getJumps())
        }
    }
}
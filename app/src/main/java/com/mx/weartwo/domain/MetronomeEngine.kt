package com.mx.weartwo.domain

import com.mx.weartwo.domain.model.MetronomeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MetronomeEngine(
    private val scope: CoroutineScope,
    private val clickOutput: ClickOutput,
    private val hapticOutput: HapticOutput,
    private val clock: ElapsedRealtimeClock,
) {

    private val _state = MutableStateFlow(MetronomeState())
    val state: StateFlow<MetronomeState> = _state.asStateFlow()

    private var tickJob: Job? = null

    fun increaseBpm() = changeBpm(1)

    fun decreaseBpm() = changeBpm(-1)

    fun nextTimeSignature() {
        _state.update { current ->
            val next = if (current.beatsPerBar >= MetronomeState.MAX_BEATS_PER_BAR) {
                MetronomeState.MIN_BEATS_PER_BAR
            } else {
                current.beatsPerBar + 1
            }
            current.copy(beatsPerBar = next)
        }
    }

    fun previousTimeSignature() {
        _state.update { current ->
            val previous = if (current.beatsPerBar <= MetronomeState.MIN_BEATS_PER_BAR) {
                MetronomeState.MAX_BEATS_PER_BAR
            } else {
                current.beatsPerBar - 1
            }
            current.copy(beatsPerBar = previous)
        }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        _state.update { it.copy(hapticsEnabled = enabled) }
    }

    fun toggle() {
        if (_state.value.isPlaying) stop() else start()
    }

    fun stop() {
        _state.update { it.copy(isPlaying = false, currentBeat = 0) }
        tickJob?.cancel()
        tickJob = null
    }

    fun dispose() {
        stop()
        clickOutput.release()
    }

    private fun changeBpm(delta: Int) {
        _state.update { current ->
            val bpm = (current.bpm + delta).coerceIn(MetronomeState.MIN_BPM, MetronomeState.MAX_BPM)
            current.copy(bpm = bpm)
        }
    }

    private fun start() {
        if (_state.value.isPlaying) return
        _state.update { it.copy(isPlaying = true, currentBeat = 0) }
        tickJob = scope.launch {
            var nextBeatAt = clock.nowMillis()
            while (isActive) {
                val beat = _state.value.currentBeat
                emitBeat(accent = beat == 0)
                _state.update { it.copy(currentBeat = (beat + 1) % it.beatsPerBar) }
                nextBeatAt += beatIntervalMillis()
                val wait = nextBeatAt - clock.nowMillis()
                if (wait > 0) delay(wait)
            }
        }
    }

    private fun emitBeat(accent: Boolean) {
        clickOutput.play(accent)
        if (_state.value.hapticsEnabled) {
            hapticOutput.vibrate(accent)
        }
    }

    private fun beatIntervalMillis(): Long = 60_000L / _state.value.bpm
}

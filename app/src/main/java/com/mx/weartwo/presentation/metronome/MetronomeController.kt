package com.mx.weartwo.presentation.metronome

import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MetronomeController(
    private val scope: CoroutineScope,
    private val clickPlayer: ClickPlayer,
    private val vibrator: Vibrator?,
) {

    var bpm by mutableIntStateOf(DEFAULT_BPM)
        private set

    var beatsPerBar by mutableIntStateOf(DEFAULT_BEATS_PER_BAR)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var currentBeat by mutableIntStateOf(0)
        private set

    var hapticsEnabled by mutableStateOf(true)

    private var tickJob: Job? = null

    fun increaseBpm() = changeBpm(1)

    fun decreaseBpm() = changeBpm(-1)

    fun nextTimeSignature() {
        beatsPerBar = if (beatsPerBar >= MAX_BEATS_PER_BAR) MIN_BEATS_PER_BAR else beatsPerBar + 1
    }

    fun previousTimeSignature() {
        beatsPerBar = if (beatsPerBar <= MIN_BEATS_PER_BAR) MAX_BEATS_PER_BAR else beatsPerBar - 1
    }

    fun toggle() {
        if (isPlaying) stop() else start()
    }

    fun dispose() {
        stop()
        clickPlayer.release()
    }

    private fun changeBpm(delta: Int) {
        bpm = (bpm + delta).coerceIn(MIN_BPM, MAX_BPM)
    }

    private fun start() {
        if (isPlaying) return
        isPlaying = true
        currentBeat = 0
        tickJob = scope.launch {
            var nextBeatAt = SystemClock.elapsedRealtime()
            while (isActive) {
                val beat = currentBeat
                playBeat(accent = beat == 0)
                currentBeat = (beat + 1) % beatsPerBar
                nextBeatAt += beatIntervalMillis()
                val wait = nextBeatAt - SystemClock.elapsedRealtime()
                if (wait > 0) delay(wait)
            }
        }
    }

    private fun stop() {
        isPlaying = false
        tickJob?.cancel()
        tickJob = null
        currentBeat = 0
    }

    private fun playBeat(accent: Boolean) {
        clickPlayer.play(accent)
        if (hapticsEnabled) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    HAPTIC_DURATION_MS,
                    if (accent) ACCENT_AMPLITUDE else NORMAL_AMPLITUDE,
                ),
            )
        }
    }

    private fun beatIntervalMillis(): Long = 60_000L / bpm

    companion object {
        const val MIN_BPM = 40
        const val MAX_BPM = 208
        const val DEFAULT_BPM = 100
        const val MIN_BEATS_PER_BAR = 2
        const val MAX_BEATS_PER_BAR = 6
        const val DEFAULT_BEATS_PER_BAR = 4
        private const val HAPTIC_DURATION_MS = 15L
        private const val NORMAL_AMPLITUDE = 60
        private const val ACCENT_AMPLITUDE = 160
    }
}

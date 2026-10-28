package com.mx.weartwo.domain.model

data class MetronomeState(
    val bpm: Int = DEFAULT_BPM,
    val beatsPerBar: Int = DEFAULT_BEATS_PER_BAR,
    val isPlaying: Boolean = false,
    val currentBeat: Int = 0,
    val hapticsEnabled: Boolean = true,
) {
    companion object {
        const val MIN_BPM = 40
        const val MAX_BPM = 208
        const val DEFAULT_BPM = 100
        const val MIN_BEATS_PER_BAR = 2
        const val MAX_BEATS_PER_BAR = 6
        const val DEFAULT_BEATS_PER_BAR = 4
    }
}

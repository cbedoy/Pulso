package com.mx.weartwo.presentation.metronome

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class ClickPlayer {

    private val accentBuffer = generateClick(ACCENT_FREQUENCY)
    private val normalBuffer = generateClick(NORMAL_FREQUENCY)
    private var audioTrack: AudioTrack? = null

    fun play(accent: Boolean) {
        try {
            val track = audioTrack ?: createTrack().also { audioTrack = it }
            if (track.playState != AudioTrack.PLAYSTATE_PLAYING) {
                track.play()
            }
            val buffer = if (accent) accentBuffer else normalBuffer
            track.write(buffer, 0, buffer.size)
        } catch (_: Exception) {
        }
    }

    fun release() {
        audioTrack?.let {
            runCatching { it.stop() }
            it.release()
        }
        audioTrack = null
    }

    private fun createTrack(): AudioTrack {
        val minBufferSize = AudioTrack.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        )
        return AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build(),
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(SAMPLE_RATE)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
            )
            .setBufferSizeInBytes(minBufferSize.coerceAtLeast(2048) * 2)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    private fun generateClick(frequency: Double): ShortArray {
        val sampleCount = SAMPLE_RATE * CLICK_DURATION_MS / 1000
        return ShortArray(sampleCount) { index ->
            val t = index.toDouble() / SAMPLE_RATE
            val envelope = exp(-t * DECAY_RATE)
            val sample = sin(2.0 * PI * frequency * t) * envelope
            (sample * Short.MAX_VALUE * CLICK_VOLUME).toInt().toShort()
        }
    }

    private companion object {
        const val SAMPLE_RATE = 44100
        const val CLICK_DURATION_MS = 30
        const val DECAY_RATE = 140.0
        const val CLICK_VOLUME = 0.6
        const val ACCENT_FREQUENCY = 1760.0
        const val NORMAL_FREQUENCY = 880.0
    }
}

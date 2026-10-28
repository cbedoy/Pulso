package com.mx.weartwo.data

import android.os.VibrationEffect
import android.os.Vibrator
import com.mx.weartwo.domain.HapticOutput

class AndroidHapticOutput(private val vibrator: Vibrator?) : HapticOutput {

    override fun vibrate(accent: Boolean) {
        try {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    HAPTIC_DURATION_MS,
                    if (accent) ACCENT_AMPLITUDE else NORMAL_AMPLITUDE,
                ),
            )
        } catch (_: SecurityException) {
        }
    }

    private companion object {
        const val HAPTIC_DURATION_MS = 15L
        const val NORMAL_AMPLITUDE = 60
        const val ACCENT_AMPLITUDE = 160
    }
}

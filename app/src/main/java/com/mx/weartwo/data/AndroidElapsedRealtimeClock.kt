package com.mx.weartwo.data

import android.os.SystemClock
import com.mx.weartwo.domain.ElapsedRealtimeClock

object AndroidElapsedRealtimeClock : ElapsedRealtimeClock {
    override fun nowMillis(): Long = SystemClock.elapsedRealtime()
}

package com.mx.weartwo.domain

fun interface ElapsedRealtimeClock {
    fun nowMillis(): Long
}

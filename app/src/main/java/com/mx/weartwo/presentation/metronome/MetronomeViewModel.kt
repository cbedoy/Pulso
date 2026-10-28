package com.mx.weartwo.presentation.metronome

import android.app.Application
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import com.mx.weartwo.data.AndroidElapsedRealtimeClock
import com.mx.weartwo.data.AndroidHapticOutput
import com.mx.weartwo.data.AudioClickOutput
import com.mx.weartwo.domain.MetronomeEngine
import com.mx.weartwo.domain.model.MetronomeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow

class MetronomeViewModel(application: Application) : AndroidViewModel(application) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val engine = MetronomeEngine(
        scope = scope,
        clickOutput = AudioClickOutput(),
        hapticOutput = AndroidHapticOutput(application.getSystemService(Vibrator::class.java)),
        clock = AndroidElapsedRealtimeClock,
    )

    val state: StateFlow<MetronomeState> = engine.state

    fun increaseBpm() = engine.increaseBpm()

    fun decreaseBpm() = engine.decreaseBpm()

    fun nextTimeSignature() = engine.nextTimeSignature()

    fun previousTimeSignature() = engine.previousTimeSignature()

    fun setHapticsEnabled(enabled: Boolean) = engine.setHapticsEnabled(enabled)

    fun toggle() = engine.toggle()

    fun stop() = engine.stop()

    override fun onCleared() {
        engine.dispose()
        scope.cancel()
    }
}

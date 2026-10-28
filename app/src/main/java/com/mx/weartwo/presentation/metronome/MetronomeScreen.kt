package com.mx.weartwo.presentation.metronome

import android.content.Context
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.IconButton
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.SwitchButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.mx.weartwo.R

@Composable
fun MetronomeScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val controller = remember {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        MetronomeController(scope, ClickPlayer(), vibrator)
    }

    DisposableEffect(controller) {
        onDispose { controller.dispose() }
    }

    BackHandler(onBack = onBack)

    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(scrollState = listState) { contentPadding ->
        TransformingLazyColumn(contentPadding = contentPadding, state = listState) {
            item {
                ListHeader(
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                ) {
                    Text(text = stringResource(R.string.metronome))
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(onClick = controller::decreaseBpm) {
                        Text(text = "−")
                    }
                    Column(
                        modifier = Modifier.size(width = 96.dp, height = 68.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = controller.bpm.toString(),
                            style = MaterialTheme.typography.displayLarge,
                        )
                        Text(
                            text = stringResource(R.string.bpm_label),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    IconButton(onClick = controller::increaseBpm) {
                        Text(text = "+")
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(controller.beatsPerBar) { beat ->
                        val isCurrent = beat == controller.currentBeat
                        val isAccent = beat == 0
                        val color = when {
                            isCurrent -> MaterialTheme.colorScheme.primary
                            isAccent -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.outline
                        }
                        Box(
                            modifier = Modifier
                                .size(if (isCurrent) 14.dp else 10.dp)
                                .background(color = color, shape = CircleShape),
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(onClick = controller::previousTimeSignature) {
                        Text(text = "−")
                    }
                    Column(
                        modifier = Modifier.size(width = 96.dp, height = 56.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "${controller.beatsPerBar}/4",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = stringResource(R.string.time_signature),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    IconButton(onClick = controller::nextTimeSignature) {
                        Text(text = "+")
                    }
                }
            }
            item {
                SwitchButton(
                    checked = controller.hapticsEnabled,
                    onCheckedChange = { controller.hapticsEnabled = it },
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                    label = { Text(text = stringResource(R.string.vibration)) },
                )
            }
            item {
                Button(
                    onClick = controller::toggle,
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (controller.isPlaying) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                        contentColor = if (controller.isPlaying) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        },
                    ),
                ) {
                    Text(
                        text = stringResource(
                            if (controller.isPlaying) R.string.stop else R.string.start,
                        ),
                    )
                }
            }
        }
    }
}

@WearPreviewDevices
@Composable
private fun MetronomeScreenPreview() {
    MetronomeScreen(onBack = {})
}

package com.mx.weartwo.presentation.metronome

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.mx.weartwo.domain.model.MetronomeState

@Composable
fun MetronomeScreen(
    onBack: () -> Unit,
    viewModel: MetronomeViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()

    DisposableEffect(viewModel) {
        onDispose { viewModel.stop() }
    }

    BackHandler(onBack = onBack)

    MetronomeContent(
        state = state,
        onDecreaseBpm = viewModel::decreaseBpm,
        onIncreaseBpm = viewModel::increaseBpm,
        onPreviousTimeSignature = viewModel::previousTimeSignature,
        onNextTimeSignature = viewModel::nextTimeSignature,
        onHapticsChange = viewModel::setHapticsEnabled,
        onToggle = viewModel::toggle,
    )
}

@Composable
fun MetronomeContent(
    state: MetronomeState,
    onDecreaseBpm: () -> Unit,
    onIncreaseBpm: () -> Unit,
    onPreviousTimeSignature: () -> Unit,
    onNextTimeSignature: () -> Unit,
    onHapticsChange: (Boolean) -> Unit,
    onToggle: () -> Unit,
) {
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
                    IconButton(onClick = onDecreaseBpm) {
                        Text(text = "−")
                    }
                    Column(
                        modifier = Modifier.size(width = 96.dp, height = 68.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = state.bpm.toString(),
                            style = MaterialTheme.typography.displayLarge,
                        )
                        Text(
                            text = stringResource(R.string.bpm_label),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    IconButton(onClick = onIncreaseBpm) {
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
                    repeat(state.beatsPerBar) { beat ->
                        val isCurrent = beat == state.currentBeat
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
                    IconButton(onClick = onPreviousTimeSignature) {
                        Text(text = "−")
                    }
                    Column(
                        modifier = Modifier.size(width = 96.dp, height = 56.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "${state.beatsPerBar}/4",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = stringResource(R.string.time_signature),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    IconButton(onClick = onNextTimeSignature) {
                        Text(text = "+")
                    }
                }
            }
            item {
                SwitchButton(
                    checked = state.hapticsEnabled,
                    onCheckedChange = onHapticsChange,
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                    label = { Text(text = stringResource(R.string.vibration)) },
                )
            }
            item {
                Button(
                    onClick = onToggle,
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isPlaying) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                        contentColor = if (state.isPlaying) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        },
                    ),
                ) {
                    Text(
                        text = stringResource(
                            if (state.isPlaying) R.string.stop else R.string.start,
                        ),
                    )
                }
            }
        }
    }
}

@WearPreviewDevices
@Composable
private fun MetronomeContentPreview() {
    MetronomeContent(
        state = MetronomeState(),
        onDecreaseBpm = {},
        onIncreaseBpm = {},
        onPreviousTimeSignature = {},
        onNextTimeSignature = {},
        onHapticsChange = {},
        onToggle = {},
    )
}

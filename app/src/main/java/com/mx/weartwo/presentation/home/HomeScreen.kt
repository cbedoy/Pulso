package com.mx.weartwo.presentation.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.mx.weartwo.R

@Composable
fun HomeScreen(onMetronomeClick: () -> Unit) {
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(scrollState = listState) { contentPadding ->
        TransformingLazyColumn(contentPadding = contentPadding, state = listState) {
            item {
                ListHeader(
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                ) {
                    Text(text = stringResource(R.string.app_name))
                }
            }
            item {
                Button(
                    onClick = onMetronomeClick,
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                ) {
                    Text(text = "🥁")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.metronome))
                }
            }
            item {
                Button(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                ) {
                    Text(text = "🎵")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.note_identifier))
                }
            }
            item {
                Text(
                    text = stringResource(R.string.app_credit),
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@WearPreviewDevices
@Composable
private fun HomeScreenPreview() {
    HomeScreen(onMetronomeClick = {})
}

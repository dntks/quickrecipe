package com.dtks.quickrecipe.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.dtks.quickrecipe.R


// composable for convenience, it show the different content based on the given states
@Composable
fun LoadingContent(
    loading: Boolean,
    empty: Boolean,
    error: Boolean,
    emptyContent: @Composable () -> Unit,
    errorContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    when {
        empty && loading -> LoadingScreen()
        empty -> emptyContent()
        error -> errorContent()
        else -> content()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .width(dimensionResource(id = R.dimen.progress_indicator_size)),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview
@Composable
fun PreviewLoadingContentLoadingTrue() {
    LoadingContent(
        loading = true,
        empty = true,
        error = false,
        errorContent = { Text("error") },
        emptyContent = { Text("empty") },
        content = { Text("real content") }
    )
}

@Preview
@Composable
fun PreviewLoadingContentEmptyTrue() {
    LoadingContent(
        loading = false,
        empty = true,
        error = false,
        errorContent = { Text("error") },
        emptyContent = { Text("empty") },
        content = { Text("real content") }
    )
}

@Preview
@Composable
fun PreviewLoadingContentWithContent() {
    LoadingContent(
        loading = false,
        empty = false,
        error = false,
        errorContent = { Text("error") },
        emptyContent = { Text("empty") },
        content = { Text("real content") }
    )
}

@Preview
@Composable
fun PreviewLoadingContentWithErrorContent() {
    LoadingContent(
        loading = false,
        empty = false,
        error = true,
        errorContent = { Text("error") },
        emptyContent = { Text("empty") },
        content = { Text("real content") }
    )
}
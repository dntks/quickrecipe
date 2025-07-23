package com.dtks.quickrecipe.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dtks.quickrecipe.R
import com.dtks.quickrecipe.data.repository.model.RecipeDetails
import com.dtks.quickrecipe.ui.common.LoadingContent
import com.dtks.quickrecipe.ui.common.NoResultsScreen
import com.dtks.quickrecipe.ui.theme.Link
import com.dtks.quickrecipe.ui.util.toAnnotatedString
import com.dtks.quickrecipe.ui.viewmodel.details.DetailsError
import com.dtks.quickrecipe.ui.viewmodel.details.DetailsViewModel
import com.dtks.quickrecipe.ui.viewmodel.details.Loading
import com.dtks.quickrecipe.ui.viewmodel.details.RecipeDetailsLoaded
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val recipeDetails by viewModel.detailsFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize(),
        topBar = {
            RecipeDetailsTopBar(
                onBack = onBack,
            )
        },
        floatingActionButton = {}
    ) { paddingValues ->
        val isEmpty = when (recipeDetails) {
            is RecipeDetailsLoaded -> false
            else -> true
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            LoadingContent(
                empty = isEmpty,
                loading = recipeDetails is Loading,
                error = recipeDetails is DetailsError,
                errorContent = {
                    NoResultsScreen(R.string.details_error)
                },
                emptyContent = {
                    NoResultsScreen(R.string.no_content)
                }
            ) {
                (recipeDetails as? RecipeDetailsLoaded)?.let { detailsLoaded ->
                    RecipeDetailsComposable(detailsLoaded.details)

                    detailsLoaded.message?.let {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = it
                            )
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecipeDetailsComposable(recipeDetails: RecipeDetails) {

    val genericPadding = dimensionResource(id = R.dimen.generic_padding)
    Column(
        modifier = Modifier
            .padding(genericPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = recipeDetails.title,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = R.string.ingredients),
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = recipeDetails.ingredients,
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = genericPadding, bottom = genericPadding)
        ) {

            GlideImage(
                contentScale = ContentScale.FillWidth,
                model = recipeDetails.imageUrl,
                modifier = Modifier
                    .fillMaxWidth(),
                contentDescription = stringResource(id = R.string.recipe_image)
            )
        }

        val uriHandler = LocalUriHandler.current


        Text(
            text = recipeDetails.urlToSource,
            style = TextStyle(
                color = Link,
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable {
                uriHandler.openUri(recipeDetails.urlToSource)
            }
        )
        val spanned =
            HtmlCompat.fromHtml(recipeDetails.instructions, HtmlCompat.FROM_HTML_MODE_COMPACT)

        Text(
            text = spanned.toAnnotatedString(),
        )
    }
}


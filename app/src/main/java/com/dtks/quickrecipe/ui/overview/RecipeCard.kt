package com.dtks.quickrecipe.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dtks.quickrecipe.R
import com.dtks.quickrecipe.domain.RecipeDomainEntity
import com.dtks.quickrecipe.ui.common.LoadingScreen
import com.dtks.quickrecipe.ui.theme.CardBackground
import com.dtks.quickrecipe.ui.theme.Typography

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecipeCard(
    recipe: RecipeDomainEntity,
    onRecipeClick: (RecipeDomainEntity) -> Unit,
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = CardBackground,
        ),
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onRecipeClick(recipe) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.minimum_image_height))
                    .fillMaxWidth()
            ) {
                LoadingScreen()
                GlideImage(
                    model = recipe.imageUrl,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = dimensionResource(id = R.dimen.minimum_image_height))
                        .wrapContentHeight(),
                    contentDescription = stringResource(id = R.string.recipe_image)
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(dimensionResource(id = R.dimen.grid_text_padding))
            ) {
                Text(
                    text = recipe.title,
                    color = Color.White,
                    style = Typography.labelLarge
                )
            }
        }
    }
}

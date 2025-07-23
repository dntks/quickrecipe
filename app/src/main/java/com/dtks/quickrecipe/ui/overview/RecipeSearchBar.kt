package com.dtks.quickrecipe.ui.overview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dtks.quickrecipe.R
import com.dtks.quickrecipe.data.repository.model.SearchType
import com.dtks.quickrecipe.data.repository.model.SearchType.BY_INGREDIENT
import com.dtks.quickrecipe.data.repository.model.SearchType.BY_TITLE
import com.dtks.quickrecipe.ui.theme.Typography
import com.dtks.quickrecipe.viewmodel.overview.SearchState

@Composable
fun RecipeSearchBar(
    searchState: SearchState,
    onSearchTextChange: (String) -> Unit,
    onSearchTypeChange: (SearchType) -> Unit,
) {

    val searchType = searchState.searchType
    val searchText = searchState.searchText
    val isSearching = searchState.searchText.isNotEmpty()
    Box(modifier = Modifier.padding(dimensionResource(id = R.dimen.generic_padding))) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.card_padding))
            ) {
                if (isSearching) {
                    //Current search can be cleared with the back button
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                onSearchTextChange("")
                            },
                        contentDescription = stringResource(id = R.string.back)
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
                TextField(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    value = searchText,
                    onValueChange = {
                        onSearchTextChange(it)
                    },

                    textStyle = Typography.labelMedium,
                    colors = TextFieldDefaults.colors(

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,

                        //setting the text field background when it is unfocused or initial state
                        unfocusedContainerColor = Color.Transparent,

                        //setting the text field background when it is disabled
                        disabledContainerColor = Color.Transparent,
                    ),
                    placeholder = {
                        Text(
                            style = Typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = when (searchType) {
                                BY_TITLE -> stringResource(id = R.string.search_by_recipe_title)
                                BY_INGREDIENT -> stringResource(id = R.string.search_by_ingredients)
                            }
                        )
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Checkbox(
                    checked = searchType == BY_INGREDIENT,
                    onCheckedChange = { checked ->
                        onSearchTypeChange(
                            if (checked) BY_INGREDIENT else BY_TITLE
                        )
                    }
                )
                Text(
                    style = Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(id = R.string.search_by_ingredients)
                )
            }

        }
    }
}

package com.example.tracker_presentation.tracker_search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorietracker.R
import com.example.core.util.UiEvent
import com.example.core_ui.LocalSpacing
import com.example.tracker_presentation.tracker_search.composnents.SearchTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                    keyboardController?.hide()
                }
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = R.string.add_meal, mealName),
            style = MaterialTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        SearchTextField(
            text = state.query,
            onValueChange = {
            viewModel.onEvent(SearchEvent.OnQueryChange(it))
        },  onSearch = {
            viewModel.onEvent(SearchEvent.OnSearch)
        }, onFocusChange = {
            viewModel.onEvent(SearchEvent.OnSearchFocusChange(it.isFocused))
        })
    }
}
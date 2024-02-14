package com.assessment.walmart.presentation.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.assessment.walmart.R
import com.assessment.walmart.domain.model.Country
import com.assessment.walmart.presentation.ui.screens.home.UiState.Error


/**
 * Composable displaying a list of countries. Displays Info based on the uiState.
 *
 * @param homeViewModel An instance of HomeScreenViewModel used to manage the data and UI state for the home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeScreenViewModel = hiltViewModel()){
    val countriesList = homeViewModel.countriesList.collectAsState()
    val uiState by homeViewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.topBar_title)) },
                modifier = Modifier.shadow(20.dp)
            )
        }
    ) {
        paddingValues ->

        when (uiState) {
            // Showing loading text while getting the info
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.loading_information))
                }
            }
            // If Successful, populating the lazyColumn with the list of countries obtained
            is UiState.Success -> {
                LazyColumn(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding(), start = 5.dp, end = 5.dp)
                    .fillMaxSize(),
                state = lazyListState
                ) {
                    items(countriesList.value, key = { country -> country.code }) {
                        CountryInfoComposable(it)
                    }
                }
            }
            // Showing appropriate error message
            is Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = (uiState as Error).message)
                }
            }
        }
    }
}

/**
 * Composable to display country information in a Card layout
 *
 * @param countryInfo An instance of CountryDTO
 */
@Composable
fun CountryInfoComposable(countryInfo: Country) {
    Card(
        modifier = Modifier.padding(3.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Region will be displayed as "N/A" if empty
                val region = countryInfo.region.ifBlank { "N/A" }
                val formattedText = "${countryInfo.name}, $region"
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = formattedText)
                }
                Column(modifier = Modifier.width(50.dp)) {
                    Text(text = countryInfo.code)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Capital will be displayed as "N/A" if empty
                val capitalName = countryInfo.capital.ifBlank { "N/A" }
                Text(text = capitalName)
            }
        }
    }
}
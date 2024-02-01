package com.assessment.walmart.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assessment.walmart.data.model.CountryDTO
import com.assessment.walmart.data.repository.DataRepository
import com.assessment.walmart.utils.getHttpErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val dataRepo: DataRepository): ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _countriesList = MutableStateFlow(listOf<CountryDTO>())
    val countriesList = _countriesList.asStateFlow()

    init {
        getCountriesInfo()
    }

    fun getCountriesInfo(){
        val coroutineExceptionHandler = CoroutineExceptionHandler {
            coroutineContext, throwable ->  Log.e(TAG, "Exception while getting countryInfo: context: $coroutineContext throwable:$throwable")
            val errorMessage = when (throwable) {
                is HttpException -> getHttpErrorMessage(throwable.code()).message
                else -> throwable.localizedMessage ?: "Unknown error"
            }
            _uiState.value = UiState.Error(errorMessage)
        }
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val investmentList = dataRepo.getCountries()
            _countriesList.value = investmentList
            _uiState.value = UiState.Success
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Canceling coroutines in this viewModel scope
        viewModelScope.cancel()
    }
}
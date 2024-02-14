package com.assessment.walmart.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assessment.walmart.domain.model.Country
import com.assessment.walmart.domain.usecase.GetCountriesUseCase
import com.assessment.walmart.utils.Result
import com.assessment.walmart.presentation.ui.utils.getHttpErrorMessage
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
class HomeScreenViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase
): ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _countriesList = MutableStateFlow(listOf<Country>())
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
            when (val result = getCountriesUseCase.execute()) {

                is Result.Success -> {
                    _countriesList.value = result.data
                    _uiState.value = UiState.Success
                }

                is Result.Error -> {
                    val exception = result.message
                    Log.d(TAG, exception)
                    _uiState.value = UiState.Error(exception)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Canceling coroutines in this viewModel scope
        viewModelScope.cancel()
    }
}
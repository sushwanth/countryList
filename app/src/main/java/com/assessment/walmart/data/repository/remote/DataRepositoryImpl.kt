package com.assessment.walmart.data.repository.remote

import android.util.Log
import com.assessment.walmart.data.datasource.remote.CountriesAPI
import com.assessment.walmart.data.model.CountryDTO
import com.assessment.walmart.utils.Result
import java.io.IOException
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(private val countriesAPI: CountriesAPI): DataRepository {
    companion object {
        const val TAG = "DataRepositoryImpl"
    }

    override suspend fun getCountries(): Result<List<CountryDTO>> {
        return try {
            val countries = countriesAPI.getCountries()
            Result.Success(countries)
        } catch (e: IOException) {
            Log.e(TAG, "No internet connection", e)
            Result.Error("Internet connection is not available")
        } catch (e: Throwable) {
            Log.e(TAG, "Unknown error", e)
            Result.Error("Unknown error occurred")
        }
    }
}
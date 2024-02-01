package com.assessment.walmart.data.repository

import com.assessment.walmart.data.datasource.remote.CountriesAPI
import com.assessment.walmart.data.model.CountryDTO
import javax.inject.Inject

class DataRepository @Inject constructor(private val countriesAPI: CountriesAPI) {

    suspend fun getCountries(): List<CountryDTO> {
        return countriesAPI.getCountries()
    }
}
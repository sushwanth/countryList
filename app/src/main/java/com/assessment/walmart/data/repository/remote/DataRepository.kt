package com.assessment.walmart.data.repository.remote

import com.assessment.walmart.data.model.CountryDTO
import com.assessment.walmart.utils.Result

interface DataRepository {
    suspend fun getCountries(): Result<List<CountryDTO>>
}
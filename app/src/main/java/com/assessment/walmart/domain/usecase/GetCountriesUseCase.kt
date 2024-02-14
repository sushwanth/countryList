package com.assessment.walmart.domain.usecase

import com.assessment.walmart.data.model.CountryDTO
import com.assessment.walmart.data.repository.remote.DataRepository
import com.assessment.walmart.domain.model.Country
import com.assessment.walmart.utils.Result
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(private val dataRepository: DataRepository) {
    companion object {
        const val TAG = "GetCountriesUseCase"
    }
    suspend fun execute(): Result<List<Country>> {
        try {
            val countrieslist = (dataRepository.getCountries() as Result.Success).data.map{ mapToCountry(it)}
            return Result.Success(countrieslist)
        } catch (e: Exception){
            return Result.Error("Unknown Exception")
        }
    }

    fun mapToCountry(countryDTO: CountryDTO): Country {
        return Country(
            name = countryDTO.name,
            region = countryDTO.region,
            code = countryDTO.code,
            capital = countryDTO.capital
        )
    }
}
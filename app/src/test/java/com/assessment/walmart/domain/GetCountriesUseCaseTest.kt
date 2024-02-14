package com.assessment.walmart.domain

import com.assessment.walmart.data.model.CountryDTO
import com.assessment.walmart.data.repository.remote.DataRepository
import com.assessment.walmart.domain.model.Country
import com.assessment.walmart.domain.usecase.GetCountriesUseCase
import com.assessment.walmart.utils.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

val afghanistan = CountryDTO(
    capital = "Kabul",
    code = "AF",
    currency = CountryDTO.CurrencyDTO(
        code = "AFN",
        name = "Afghan afghani",
        symbol = "؋"
    ),
    flag = "https://restcountries.eu/data/afg.svg",
    language = CountryDTO.LanguageDTO(
        code = "ps",
        name = "Pashto"
    ),
    name = "Afghanistan",
    region = "AS"
)

val alandIslands = CountryDTO(
    capital = "Mariehamn",
    code = "AX",
    currency = CountryDTO.CurrencyDTO(
        code = "EUR",
        name = "Euro",
        symbol = "€"
    ),
    flag = "https://restcountries.eu/data/ala.svg",
    language = CountryDTO.LanguageDTO(
        code = "sv",
        name = "Swedish"
    ),
    name = "Åland Islands",
    region = "EU"
)

val albania = CountryDTO(
    capital = "Tirana",
    code = "AL",
    currency = CountryDTO.CurrencyDTO(
        code = "ALL",
        name = "Albanian lek",
        symbol = "L"
    ),
    flag = "https://restcountries.eu/data/alb.svg",
    language = CountryDTO.LanguageDTO(
        code = "sq",
        name = "Albanian"
    ),
    name = "Albania",
    region = "EU"
)

@RunWith(MockitoJUnitRunner::class)
class GetCountriesUseCaseTest{

    @Mock
    private lateinit var mockDataRepository: DataRepository

    @InjectMocks
    private lateinit var getCountriesUseCase: GetCountriesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getCountriesUseCase = GetCountriesUseCase(mockDataRepository)
    }

    @Test
    fun `when get countries then return mapped countries`() = runTest {
        // Setting up mock data
        val mockCountryDTOs = listOf(
            afghanistan, alandIslands, albania
        )

        // Setting mock behavior
        `when`(mockDataRepository.getCountries()).thenReturn(Result.Success(mockCountryDTOs))

        // execution
        val result = getCountriesUseCase.execute()

        // validation
        assertTrue(result is Result.Success)
        val countries = (result as Result.Success).data
        assertEquals(3, countries.size)
        assertEquals(mockCountryDTOs.map { Country(it.name, it.region, it.code, it.capital) }, countries)
    }

    @Test
    fun `test mapToCountry converts ToDomainObject`() {
        // execution
        val country = getCountriesUseCase.mapToCountry(afghanistan)

        //validation
        assertEquals(afghanistan.name, country.name)
        assertEquals(afghanistan.region, country.region)
        assertEquals(afghanistan.code, country.code)
        assertEquals(afghanistan.capital, country.capital)
    }

    @Test
    fun execute_throwsExceptionWhenRepositoryFails() = runTest {
        // Throwing a mock exception from Repository
        val errorMessage = "Error fetching countries"
        val mockException = RuntimeException(errorMessage)
        `when`(mockDataRepository.getCountries()).thenThrow(mockException)

        // execution
        val result = getCountriesUseCase.execute()

        // validating that the exception is propagated through Result object
        assertTrue(result is Result.Error)
        val exception = (result as Result.Error).message
        assertEquals(exception, "Unknown Exception")

    }
}
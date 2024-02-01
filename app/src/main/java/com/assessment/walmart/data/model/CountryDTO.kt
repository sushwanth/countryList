package com.assessment.walmart.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDTO(
    @SerialName("capital")
    val capital: String,
    @SerialName("code")
    val code: String,
    @SerialName("currency")
    val currency: CurrencyDTO,
    @SerialName("flag")
    val flag: String,
    @SerialName("language")
    val language: LanguageDTO,
    @SerialName("name")
    val name: String,
    @SerialName("region")
    val region: String
){
    @Serializable
    data class CurrencyDTO(
        @SerialName("code")
        val code: String,
        @SerialName("name")
        val name: String,
        @SerialName("symbol")
        val symbol: String
    )

    @Serializable
    data class LanguageDTO(
        @SerialName("code")
        val code: String,
        @SerialName("name")
        val name: String
    )
}

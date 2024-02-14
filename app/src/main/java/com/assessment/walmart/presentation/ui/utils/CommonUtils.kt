package com.assessment.walmart.presentation.ui.utils

sealed class HttpErrorMessage(val message: String) {
    object BadRequest : HttpErrorMessage("Bad Request: The server could not understand the request")
    object Unauthorized : HttpErrorMessage("Unauthorized: Authentication failed or user lacks necessary permissions")
    object Forbidden : HttpErrorMessage("Forbidden: Access is forbidden to the requested resource")
    object NotFound : HttpErrorMessage("Not Found: The requested resource could not be found")
    object InternalServerError : HttpErrorMessage("Internal Server Error: An unexpected condition was encountered by the server")
    class Unknown(val errorCode: Int) : HttpErrorMessage("HTTP error $errorCode")
}

fun getHttpErrorMessage(errorCode: Int): HttpErrorMessage {
    return when (errorCode) {
        400 -> HttpErrorMessage.BadRequest
        401 -> HttpErrorMessage.Unauthorized
        403 -> HttpErrorMessage.Forbidden
        404 -> HttpErrorMessage.NotFound
        500 -> HttpErrorMessage.InternalServerError
        else -> HttpErrorMessage.Unknown(errorCode)
    }
}
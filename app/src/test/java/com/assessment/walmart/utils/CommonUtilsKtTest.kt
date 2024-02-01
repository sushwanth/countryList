package com.assessment.walmart.utils

import org.junit.Test
import org.junit.Assert.*

class GetHttpErrorMessageTest {

    @Test
    fun `should return correct message for 400 error code`() {
        val errorMessage = getHttpErrorMessage(400)
        assertEquals(HttpErrorMessage.BadRequest, errorMessage)
    }

    @Test
    fun `should return correct message for 401 error code`() {
        val errorMessage = getHttpErrorMessage(401)
        assertEquals(HttpErrorMessage.Unauthorized, errorMessage)
    }

    @Test
    fun `should return correct message for 403 error code`() {
        val errorMessage = getHttpErrorMessage(403)
        assertEquals(HttpErrorMessage.Forbidden, errorMessage)
    }

    @Test
    fun `should return correct message for 404 error code`() {
        val errorMessage = getHttpErrorMessage(404)
        assertEquals(HttpErrorMessage.NotFound, errorMessage)
    }

    @Test
    fun `should return correct message for 500 error code`() {
        val errorMessage = getHttpErrorMessage(500)
        assertEquals(HttpErrorMessage.InternalServerError, errorMessage)
    }

    @Test
    fun `should return generic message for other error codes`() {
        val errorCode = 302
        val errorMessage = "HTTP error $errorCode"
        assertEquals(HttpErrorMessage.Unknown(errorCode).message, errorMessage)
    }
}
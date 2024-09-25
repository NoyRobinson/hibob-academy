package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class ResponseDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val responseDao = ResponseDao(sql)
    private val responseTable = ResponseTable.instance

    @Test
    fun `Submit a new response successfully`() {
        val newResponse  = ResponseForSubmission(14, 12,
            "You're a great worker, we're lucky to have you!")

        val responseId = responseDao.submitResponse(newResponse)
        val responseFromDb = responseDao.viewResponseById(responseId)

        val expected = FeedbackResponse(responseId, 14, responseFromDb!!.dateOfResponse,
                                        12,"You're a great worker, we're lucky to have you!")

        val actual = responseDao.viewResponseById(responseId)
        assertEquals(expected, actual)
    }

    @Test
    fun `Get response that exists by id`(){
        val newResponse  = ResponseForSubmission(14, 12,
            "You're a great worker, we're lucky to have you!")

        val responseId = responseDao.submitResponse(newResponse)
        val actual = responseDao.viewResponseById(responseId)
        val expected = FeedbackResponse(responseId, 14, actual!!.dateOfResponse,
            12,"You're a great worker, we're lucky to have you!")
        assertEquals(expected, actual)
    }

    @Test
    fun `Try get response by id of feedback that doesn't exist and return null`(){
        val actual = responseDao.viewResponseById(100)
        assertNull(actual)
    }
}




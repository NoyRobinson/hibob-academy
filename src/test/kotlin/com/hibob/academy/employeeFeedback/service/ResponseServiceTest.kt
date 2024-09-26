package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.sql.Date

class ResponseServiceTest {
    private val responseDao = mock<ResponseDao> {}
    private val feedbackDao = mock<FeedbackDao> {}
    private val responseService = ResponseService(responseDao, feedbackDao)

    @Test
    fun `Submit response to a feedback that doesnt exist should throw an exception`() {
        whenever(feedbackDao.getFeedbackById(100, 1)).thenReturn(null)
        assertThrows<NotFoundException> {
            responseService.submitResponse(100, 13,
                "You're a great worker, we're lucky to have you!", 1)
        }
    }

    @Test
    fun `Submit response to an anonymous feedback should throw an exception`() {
        val feedbackToRespond = FeedbackInfo(100, null, 1, Date.valueOf("2024-09-25"),
                                             AnonymityType.ANONYMOUS, false,
                                    "I love my work place, i'm treated very well. There are good facilities")
        whenever(feedbackDao.getFeedbackById(100, 1)).thenReturn(feedbackToRespond)
        assertThrows<BadRequestException> { responseService.submitResponse(100, 12,
                    "You're a great worker, we're lucky to have you!", 1)
        }
    }

    @Test
    fun `Submit response with a length of less than 30 characters`() {
        val feedbackToRespond = FeedbackInfo(100, 12, 1, Date.valueOf("2024-09-25"),
            AnonymityType.IDENTIFIED, false,
            "I love my work place, i'm treated very well. There are good facilities")
        whenever(feedbackDao.getFeedbackById(100, 1)).thenReturn(feedbackToRespond)
        assertThrows<BadRequestException> { responseService.submitResponse(100, 12,
                                        "ok", 1) }
    }

    @Test
    fun `Submit response successfully`() {
        val feedbackToRespond = FeedbackInfo(100, 12, 1, Date.valueOf("2024-09-25"),
            AnonymityType.IDENTIFIED, false,
            "I love my work place, i'm treated very well. There are good facilities")
        whenever(feedbackDao.getFeedbackById(100, 1)).thenReturn(feedbackToRespond)
        val output = responseService.submitResponse(100, 12,
                    "You're a great worker, we're lucky to have you!", 1)
        assertTrue(output)
    }
}

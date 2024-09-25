package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.sql.Date

class FeedbackServiceTest{

    private val feedbackDao = mock<FeedbackDao>{}
    private val feedbackService = FeedbackService(feedbackDao)

    @Test
    fun `Submit identified feedback successfully`(){
        val anonymity = AnonymityType.IDENTIFIED
        val feedback = "I'm very happy with my workspace, i'm treated well"
        val feedbackForSubmission = FeedbackForSubmission(12,1, anonymity, feedback)
        val output = feedbackService.submitFeedback(12, 1, anonymity, feedback)
        assertTrue(output)
        verify(feedbackDao).submitFeedback(feedbackForSubmission)
    }

    @Test
    fun `Submit feedback with a length of less than 30 characters`(){
        val anonymity = AnonymityType.IDENTIFIED
        val feedback = "ok"
        val output = feedbackService.submitFeedback(12, 1, anonymity, feedback)
        assertFalse(output)
    }

    @Test
    fun `View all submitted feedback successfully`(){
        val expectedFeedbackInfo = FeedbackInfo(1, 12, 1, Date.valueOf("2024-09-24"), AnonymityType.IDENTIFIED,false, "I'm very happy with my workspace, i'm treated well")
        whenever(feedbackDao.viewAllSubmittedFeedback(1)).thenReturn(listOf(expectedFeedbackInfo))
        val output = feedbackService.viewAllSubmittedFeedback(12, 1)
        assertEquals(output, listOf(expectedFeedbackInfo))
        verify(feedbackDao).viewAllSubmittedFeedback(1)
    }

    @Test
    fun `View all of my submitted feedback successfully`(){
        val feedbackInfo1 = FeedbackInfo(1, 12, 1, Date.valueOf("2024-09-24"), AnonymityType.IDENTIFIED,false, "I'm very happy with my workspace, i'm treated well")
        whenever(feedbackDao.viewFeedbackOfEmployee(12, 1)).thenReturn(listOf(feedbackInfo1))
        val output = feedbackService.viewFeedbackOfEmployee(12, 1)
        assertEquals(output, listOf(feedbackInfo1))
        verify(feedbackDao).viewFeedbackOfEmployee(12, 1)
    }

    @Test
    fun `view status of one of my feedbacks successfully`(){
        val expectedFeedback = FeedbackInfo(20, 12, 1,
            Date.valueOf("2024-09-24"), AnonymityType.IDENTIFIED,false,
            "I'm very happy with my workspace, i'm treated well")
        whenever(feedbackDao.getFeedbackById(20, 1)).thenReturn(expectedFeedback)
        val feedbackStatus = FeedbackStatusData(1,12,20)
        whenever(feedbackDao.viewStatusOfMyFeedback(feedbackStatus)).thenReturn(mapOf(20 to false))
        val output = feedbackService.viewStatusOfMyFeedback(12,1, 20)
        assertEquals(mapOf(20 to false), output)
        verify(feedbackDao).viewStatusOfMyFeedback(feedbackStatus)
        verify(feedbackDao).getFeedbackById(20, 1)
    }

    @Test
    fun `viewing status of an anonymous feedback should throw an exception`(){
        val expectedFeedback = FeedbackInfo(20, null, 1,
            Date.valueOf("2024-09-24"), AnonymityType.ANONYMOUS,false,
            "I'm very happy with my workspace, i'm treated well")
        whenever(feedbackDao.getFeedbackById(20, 1)).thenReturn(expectedFeedback)
        assertThrows<BadRequestException> { feedbackService.viewStatusOfMyFeedback(12,
                                1, 20) }
    }

    @Test
    fun `Try view status of a feedback that doesnt exist should throw an exception`(){
        whenever(feedbackDao.getFeedbackById(100, 1)).thenReturn(null)
        assertThrows<NotFoundException> { feedbackService.viewStatusOfMyFeedback(12,
            1, 100) }
    }

    @Test
    fun `View statuses of all my feedbacks successfully`() {
        val feedbackStatus = FeedbackStatusData(1, 12, null)
        whenever(feedbackDao.viewStatusOfMyFeedback(feedbackStatus)).thenReturn(mapOf(20 to false, 21 to false))
        val output = feedbackService.viewStatusOfMyFeedback(12, 1, null)
        assertEquals(mapOf(20 to false, 21 to false), output)
        verify(feedbackDao).viewStatusOfMyFeedback(feedbackStatus)
    }

    @Test
    fun `View statuses of my feedbacks should return an empty map if there are no feedbacks`(){
        val output = feedbackService.viewStatusOfMyFeedback(12, 1, null)
        val feedbackStatus = FeedbackStatusData(1, 12,null)
        assertEquals(emptyMap<Int, Boolean>(), output)
        verify(feedbackDao).viewStatusOfMyFeedback(feedbackStatus)
    }

    @Test
    fun `Change to reviewed should update the field of reviewed successfully`(){
        whenever(feedbackDao.changeReviewedStatus(20, 1, true)).thenReturn(1)
        feedbackService.changeReviewedStatus(20, 1, 12, true)
        verify(feedbackDao).changeReviewedStatus(20, 1, true)
    }

    @Test
    fun `Change to reviewed should return an exception if the feedback doesnt exist`(){
        whenever(feedbackDao.changeReviewedStatus(100, 1, true)).thenReturn(0)
        assertThrows<NotFoundException> { feedbackService.changeReviewedStatus(100, 1,
                                        14, true) }
    }
}

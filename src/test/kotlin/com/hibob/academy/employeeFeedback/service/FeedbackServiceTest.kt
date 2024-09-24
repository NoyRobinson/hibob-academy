package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.sql.Date

class FeedbackServiceTest{

    private val feedbackDao = mock<FeedbackDao>{}
    private val employeeDao = mock<EmployeeDao>{}
    private val feedbackService = FeedbackService(feedbackDao, employeeDao)

    @Test
    fun `Submit identified feedback successfully`(){
        val anonymity = AnonymityType.IDENTIFIED
        val feedback = "I'm very happy with my workspace, i'm treated well"
        val expectedEmployee = EmployeeInfo(12, RoleType.convertStringToRoleType("ADMIN"),
                                    "dev", 1)
        whenever(employeeDao.getEmployeeById(12)).thenReturn(expectedEmployee)
        val feedbackForSubmission = FeedbackForSubmission(expectedEmployee.id,
                                    expectedEmployee.companyId, anonymity, feedback)
        val output = feedbackService.submitFeedback(expectedEmployee.id, anonymity, feedback)
        assertTrue(output)
        verify(feedbackDao).submitFeedback(feedbackForSubmission)
        verify(employeeDao).getEmployeeById(12)
    }

    @Test
    fun `Submit anonymous feedback successfully`(){
        val anonymity = AnonymityType.ANONYMOUS
        val feedback = "I'm very happy with my workspace, i'm treated well"
        val expectedEmployee = EmployeeInfo(12, RoleType.convertStringToRoleType("ADMIN"),
                                            "dev", 1)
        whenever(employeeDao.getEmployeeById(12)).thenReturn(expectedEmployee)
        val feedbackForSubmission = FeedbackForSubmission(null,
                                        expectedEmployee.companyId, anonymity, feedback)
        val output = feedbackService.submitFeedback(expectedEmployee.id, anonymity, feedback)
        assertTrue(output)
        verify(feedbackDao).submitFeedback(feedbackForSubmission)
        verify(employeeDao).getEmployeeById(12)
    }

    @Test
    fun `Submit feedback with a length of less than 30 characters`(){
        val anonymity = AnonymityType.IDENTIFIED
        val feedback = "ok"
        val expectedEmployee = EmployeeInfo(12, RoleType.convertStringToRoleType("ADMIN"),
                                            "dev", 1)
        whenever(employeeDao.getEmployeeById(12)).thenReturn(expectedEmployee)
        val output = feedbackService.submitFeedback(expectedEmployee.id, anonymity, feedback)
        assertFalse(output)
        verify(employeeDao).getEmployeeById(12)
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
    fun `view status of one of my feedbacks successfully`(){
        val expectedFeedback = FeedbackInfo(20, 12, 1,
            Date.valueOf("2024-09-24"), AnonymityType.IDENTIFIED,false,
            "I'm very happy with my workspace, i'm treated well")
        whenever(feedbackDao.getFeedbackById(20, 1)).thenReturn(expectedFeedback)
        val feedbackStatus = FindFeedbackStatus(1,12,20)
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
        assertThrows<Exception> { feedbackService.viewStatusOfMyFeedback(12,
                                1, 20) }
    }

    @Test
    fun `viewing a status of a feedback of a different employee should throw an exception`(){
        val expectedFeedback = FeedbackInfo(20, 14, 1,
            Date.valueOf("2024-09-24"), AnonymityType.IDENTIFIED,false,
            "I'm very happy with my workspace, i'm treated well")
        whenever(feedbackDao.getFeedbackById(20, 1)).thenReturn(expectedFeedback)
        assertThrows<Exception> { feedbackService.viewStatusOfMyFeedback(12,
            1, 20) }
    }

    @Test
    fun `Try view status of a feedback that doesnt exist should throw an exception`(){
        whenever(feedbackDao.getFeedbackById(100, 1)).thenReturn(null)
        assertThrows<Exception> { feedbackService.viewStatusOfMyFeedback(12,
            1, 100) }
    }

    @Test
    fun `View statuses of all my feedbacks successfully`() {
        val feedbackStatus = FindFeedbackStatus(1, 12, null)
        whenever(feedbackDao.viewStatusOfMyFeedback(feedbackStatus)).thenReturn(mapOf(20 to false, 21 to false))
        val output = feedbackService.viewStatusesOfAllMySubmittedFeedback(12, 1)
        assertEquals(mapOf(20 to false, 21 to false), output)
        verify(feedbackDao).viewStatusOfMyFeedback(feedbackStatus)
    }

    @Test
    fun `View statuses of my feedbacks should return an empty map if there are no feedbacks`(){
        val output = feedbackService.viewStatusesOfAllMySubmittedFeedback(12,1)
        val feedbackStatus = FindFeedbackStatus(1, 12,null)
        assertEquals(emptyMap<Int, Boolean>(), output)
        verify(feedbackDao).viewStatusOfMyFeedback(feedbackStatus)
    }
}

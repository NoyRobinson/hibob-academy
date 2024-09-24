package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
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
    fun `Submit feedback should handle the exception from feedbackDao`() {
        val anonymity = AnonymityType.IDENTIFIED
        val feedback = "I'm very happy with my workspace, i'm treated well"
        val expectedEmployee = EmployeeInfo(12, RoleType.convertStringToRoleType("ADMIN"),
                                            "dev", 1)
        whenever(employeeDao.getEmployeeById(12)).thenReturn(expectedEmployee)
        val feedbackForSubmission =
            FeedbackForSubmission(expectedEmployee.id, expectedEmployee.companyId,
                                    anonymity, feedback)
        whenever(feedbackDao.submitFeedback(feedbackForSubmission))
                .thenThrow(BadRequestException("Unable to create feedback"))
        val exception = assertThrows<BadRequestException> {
            feedbackService.submitFeedback(expectedEmployee.id, anonymity, feedback)
        }
        assertEquals("Unable to create feedback", exception.message)
        verify(feedbackDao).submitFeedback(feedbackForSubmission)
        verify(employeeDao).getEmployeeById(12)
    }

    @Test
    fun `View all submitted feedback successfully`(){
        val expectedEmployee = EmployeeInfo(12, RoleType.convertStringToRoleType("HR"),
            "dev", 1)
        whenever(employeeDao.getEmployeeById(12)).thenReturn(expectedEmployee)
        val expectedFeedbackInfo = FeedbackInfo(1, 12, 1, Date.valueOf("2024-09-24"), AnonymityType.IDENTIFIED,false, "I'm very happy with my workspace, i'm treated well")
        whenever(feedbackDao.viewAllSubmittedFeedback(expectedEmployee.id)).thenReturn(listOf(expectedFeedbackInfo))
        val output = feedbackService.viewAllSubmittedFeedback(expectedEmployee.id)
        assertTrue(output)
        verify(employeeDao).getEmployeeById(12)
        verify(feedbackDao).viewAllSubmittedFeedback(expectedEmployee.companyId)
    }

    @Test
    fun `Employee which is not an admin or an hr that wants to see all feedbacks shouldn't be able to `(){
        val expectedEmployee = EmployeeInfo(12, RoleType.convertStringToRoleType("EMPLOYEE"),
            "dev", 1)
        whenever(employeeDao.getEmployeeById(12)).thenReturn(expectedEmployee)
        val output = feedbackService.viewAllSubmittedFeedback(expectedEmployee.id)
        assertFalse(output)
        verify(employeeDao).getEmployeeById(12)
    }

    @Test
    fun `view status of one of my feedbacks successfully`(){
        val expectedEmployee = EmployeeInfo(12, RoleType.EMPLOYEE, "dev", 1)
        whenever(employeeDao.getEmployeeById(12)).thenReturn(expectedEmployee)
        whenever(feedbackDao.getFeedbackEmployeeId(20, 1)).thenReturn(12)
        val feedbackStatus = FeedbackStatus(1, 12,20)
        whenever(feedbackDao.viewStatusOfMyFeedback(feedbackStatus)).thenReturn(mapOf(20 to false))
        val output = feedbackService.viewStatusOfMyFeedback(12, 20)
        assertEquals(mapOf(20 to false), output)
        verify(feedbackDao).viewStatusOfMyFeedback(feedbackStatus)
        verify(feedbackDao).getFeedbackEmployeeId(20, 1)
        verify(employeeDao).getEmployeeById(12)
    }


    //    fun viewStatusOfMyFeedback(employeeId: Int, feedbackId: Int): Map<Int, Boolean> {
//        val employeeInfo = employeeDao.getEmployeeById(employeeId)
//        val feedbackEmployeeId = feedbackDao.getFeedbackEmployeeId(feedbackId, employeeInfo.companyId)
//            ?: throw Exception("Can't check status of anonymous feedback")
//        if (employeeId != feedbackEmployeeId)
//            throw BadRequestException("Unauthorized to view this feedback status")
//        val feedbackStatus = FeedbackStatus(employeeInfo.companyId, employeeId, feedbackId)
//        return feedbackDao.viewStatusOfMyFeedback(feedbackStatus)

    @Test
    fun `viewing status of an anonymous feedback should throw an exception`(){

    }

    @Test
    fun `viewing a status of a feedback of a different employee should throw an exception`(){

    }

    @Test
    fun `viewStatusOfMyFeedback should handle the exception from feedbackDao`(){}
}
package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Service

@Service
class FeedbackService(private val feedbackDao: FeedbackDao, private val employeeDao: EmployeeDao) {
    fun submitFeedback(employeeId: Int, anonymity: AnonymityType, feedback: String): Boolean {
        val employeeInfo = employeeDao.getEmployeeById(employeeId)

        var employeeIdForFeedback: Int? = employeeId
        if (anonymity == AnonymityType.ANONYMOUS)
            employeeIdForFeedback = null

        if (feedback.length < 30 || feedback.length > 500)
            return false

        val feedbackForSubmission =
            FeedbackForSubmission(employeeIdForFeedback, employeeInfo.companyId, anonymity, feedback)
        feedbackDao.submitFeedback(feedbackForSubmission)
        return true
    }

    fun viewAllSubmittedFeedback(employeeId: Int): Boolean {
        val employeeInfo = employeeDao.getEmployeeById(employeeId)
        val roles: List<RoleType> =
            listOf(RoleType.convertStringToRoleType("ADMIN"), RoleType.convertStringToRoleType("HR"))
        if (employeeInfo.role !in roles)
            return false
        feedbackDao.viewAllSubmittedFeedback(employeeInfo.companyId)
        return true
    }

    fun viewStatusOfMyFeedback(employeeId: Int, feedbackId: Int): Map<Int, Boolean> {
        val employeeInfo = employeeDao.getEmployeeById(employeeId)
        val feedbackEmployeeId = feedbackDao.getFeedbackEmployeeId(feedbackId, employeeInfo.companyId)
            ?: throw Exception("Can't check status of anonymous feedback")
        if (employeeId != feedbackEmployeeId)
            throw BadRequestException("Unauthorized to view this feedback status")
        val feedbackStatus = FeedbackStatus(employeeInfo.companyId, employeeId, feedbackId)
        return feedbackDao.viewStatusOfMyFeedback(feedbackStatus)
    }

    fun viewStatusesOfAllMySubmittedFeedback(employeeId: Int): Map<Int, Boolean> {
        val employeeInfo = employeeDao.getEmployeeById(employeeId)
        val feedbackStatus = FeedbackStatus(employeeInfo.companyId, employeeInfo.id, null)
        return feedbackDao.viewStatusOfMyFeedback(feedbackStatus)
    }
}

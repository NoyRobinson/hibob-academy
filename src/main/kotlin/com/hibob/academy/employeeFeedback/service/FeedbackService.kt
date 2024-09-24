package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
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
            FeedbackForSubmission(employeeIdForFeedback, employeeInfo!!.companyId, anonymity, feedback)
        feedbackDao.submitFeedback(feedbackForSubmission)
        return true
    }

    fun viewAllSubmittedFeedback(employeeId: Int, companyId: Int): List<FeedbackInfo> {
        return feedbackDao.viewAllSubmittedFeedback(companyId)
    }

    fun viewStatusOfMyFeedback(employeeId: Int, companyId: Int, feedbackId: Int): Map<Int, Boolean> {
        val feedbackInfo = feedbackDao.getFeedbackById(feedbackId, companyId)
        feedbackInfo?.let{
            val idOfEmployeeThatWroteFeedbackDao = feedbackInfo.employeeId
                ?: throw Exception("Can't check status of anonymous feedback")
            if (employeeId != idOfEmployeeThatWroteFeedbackDao)
                throw BadRequestException("Unauthorized to view this feedback status")
            val feedbackStatus = FindFeedbackStatus(companyId, employeeId, feedbackId)
            return feedbackDao.viewStatusOfMyFeedback(feedbackStatus)
        } ?: throw Exception("Feedback not found")
    }

    fun viewStatusesOfAllMySubmittedFeedback(employeeId: Int, companyId: Int): Map<Int, Boolean> {
            val feedbackStatus = FindFeedbackStatus(employeeId, companyId,null)
            return feedbackDao.viewStatusOfMyFeedback(feedbackStatus)
    }
}

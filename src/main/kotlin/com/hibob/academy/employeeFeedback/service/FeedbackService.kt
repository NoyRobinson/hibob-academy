package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import com.hibob.academy.employeeFeedback.dao.AnonymityType
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.FeedbackForSubmission
import com.hibob.academy.employeeFeedback.dao.FeedbackStatus
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Service

@Service
class FeedbackService(private val feedbackDao: FeedbackDao) {
    fun feedbackLengthValidation(feedback: String): Boolean {
        val minCharacters = 30
        val maxCharacters = 500

        return !(feedback.length < minCharacters || feedback.length > maxCharacters)
    }

    fun anonymityStatusValidation(employeeId: Int?): Int{
        return employeeId ?: throw BadRequestException("Can't check status of anonymous feedback")
    }

    fun authorizationToViewValidation(employeeId: Int, feedbacksEmployeeId: Int) {
        if (feedbacksEmployeeId != employeeId)
            throw BadRequestException("Unauthorized to view this feedback status")
    }

    fun submitFeedback(employeeId: Int, companyId: Int, anonymity: AnonymityType, feedback: String): Boolean {
        var employeeIdForFeedback: Int? = employeeId

        if (anonymity == AnonymityType.ANONYMOUS)
            employeeIdForFeedback = null

        val validation = feedbackLengthValidation(feedback)

        if (!validation) return false

        val feedbackForSubmission = FeedbackForSubmission(
            employeeIdForFeedback,
            companyId,
            anonymity,
            feedback)
        feedbackDao.submitFeedback(feedbackForSubmission)

        return true
    }

    fun viewAllSubmittedFeedback(employeeId: Int, companyId: Int): List<FeedbackInfo> {
        return feedbackDao.viewAllSubmittedFeedback(companyId)
    }

    fun viewStatusOfMyFeedback(employeeId: Int, companyId: Int, feedbackId: Int): Map<Int, Boolean> {
        val feedbackInfo = feedbackDao.getFeedbackById(feedbackId, companyId)

        feedbackInfo?.let{

            val feedbacksEmployeeId = anonymityStatusValidation(feedbackInfo.employeeId)
            authorizationToViewValidation(employeeId, feedbacksEmployeeId)

            val feedbackStatus = FeedbackStatusData(companyId, employeeId, feedbackId)

            return feedbackDao.viewStatusOfMyFeedback(feedbackStatus)

        } ?: throw NotFoundException("Feedback not found")
    }

    fun viewStatusesOfMyFeedback(employeeId: Int, companyId: Int): Map<Int, Boolean> {
         val feedbackStatus = FeedbackStatusData(companyId, employeeId, null)

        return feedbackDao.viewStatusOfMyFeedback(feedbackStatus)
    }
}

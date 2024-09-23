package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.AnonymityType
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.FeedbackForSubmission
import com.hibob.academy.employeeFeedback.dao.FeedbackStatus
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Service

@Service
class FeedbackService(private val feedbackDao: FeedbackDao) {
    fun submitFeedback(employeeId: Int, anonymity: AnonymityType, feedback: String): Boolean {
        val employeeInfo = employeeDao.getInfoById(employeeId)

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

    fun viewAllSubmittedFeedback(employeeId: Int, companyId: Int): Boolean {
        val employeeInfo = employeeDao.getInfoById(employeeId)
        if (employeeInfo.role != "ADMIN" && employeeInfo.role != "HR")
            return false
        if (employeeInfo.companyId != companyId)
            return false
        feedbackDao.viewAllSubmittedFeedback(employeeInfo.companyId)
        return true
    }

    fun viewStatusOfMyFeedback(employeeId: Int, feedbackId: Int, companyId: Int): Boolean {
        val employeeInfo = employeeDao.getInfoById(employeeId)
        val feedbackEmployeeId = feedbackDao.getFeedbackEmployeeId(feedbackId, employeeInfo.companyId)
        if(feedbackEmployeeId == null)
            throw Exception("Can't check status of anonymous feedback")
        if(employeeId != feedbackEmployeeId)
            throw BadRequestException("Unauthorized to view this feedback status")
        if(companyId != employeeInfo.companyId)
            throw BadRequestException("Unauthorized to view this feedback status")
        val feedbackStatus = FeedbackStatus(companyId, employeeId, feedbackId)
        return feedbackDao.viewStatusOfMyFeedback(feedbackStatus)
    }

    fun viewStatusesOfAllMySubmittedFeedback(companyId: Int, employeeId: Int) {
        val employeeInfo = employeeDao.getInfoById(employeeId)
        if (employeeInfo.companyId != companyId)
            throw BadRequestException("Unauthorized to view feedback of a different company")
        feedbackDao.viewStatusesOfAllMySubmittedFeedback(employeeInfo.companyId, employeeInfo.employeeId)
    }
}

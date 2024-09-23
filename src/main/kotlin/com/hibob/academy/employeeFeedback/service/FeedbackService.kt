package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.AnonymityType
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.FeedbackForSubmission
import com.hibob.academy.employeeFeedback.dao.FeedbackStatus
import org.springframework.stereotype.Service

@Service
class FeedbackService(private val feedbackDao: FeedbackDao) {

    fun submitFeedback(employeeId: Int, anonymity: AnonymityType, feedback: String) {
        val employeeInfo = employeeDao.getInfoById(employeeId)
        val feedbackForSubmission = FeedbackForSubmission(employeeId, employeeInfo.companyId, anonymity, feedback)
        feedbackDao.submitFeedback(feedbackForSubmission)
    }

    fun viewAllSubmittedFeedback(companyId: Int) =
        feedbackDao.viewAllSubmittedFeedback(companyId)

    fun viewStatusOfMyFeedback(feedbackStatus: FeedbackStatus) =
        feedbackDao.viewStatusOfMyFeedback(feedbackStatus)

    fun viewStatusesOfAllMySubmittedFeedback(companyId: Int, employeeId: Int) =
        feedbackDao.viewStatusesOfAllMySubmittedFeedback(companyId, employeeId)
}
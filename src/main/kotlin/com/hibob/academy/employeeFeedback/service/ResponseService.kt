package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Service

@Service
class ResponseService(private val responseDao: ResponseDao, private val feedbackDao: FeedbackDao) {
    fun responseLengthValidation(response: String) {
        val minCharacters = 30
        val maxCharacters = 500

        if (response.length < minCharacters || response.length > maxCharacters) throw BadRequestException("Invalid response length")
    }

    fun isWriterIdentified(id: Int?) =
        id ?: throw BadRequestException("Unable to respond to anonymous feedback")

    fun doesFeedbackExist(feedbackInfo: FeedbackInfo?) =
        feedbackInfo ?: throw NotFoundException("This feedback doesn't exist")

    fun validateResponse(feedbackId: Int, companyId: Int, response: String){
        val feedbackToRespond = feedbackDao.getFeedbackById(feedbackId, companyId)
        doesFeedbackExist(feedbackToRespond)

        val feedbackWriterId = feedbackToRespond?.employeeId
        isWriterIdentified(feedbackWriterId)

        responseLengthValidation(response)
    }

    fun submitResponse(feedbackId: Int, reviewerId: Int, response: String, companyId: Int): Boolean {

        validateResponse(feedbackId, companyId, response)

        val responseForSubmission = ResponseForSubmission(
            feedbackId,
            reviewerId,
            response)
        responseDao.submitResponse(responseForSubmission)

        return true
    }
}
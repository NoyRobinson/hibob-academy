package com.hibob.academy.employeeFeedback.dao

import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Repository

@Repository
class ResponseDao(private val sql: DSLContext) {
    private val responseTable = ResponseTable.instance

    private val responseMapper = RecordMapper<Record, FeedbackResponse> { record ->
        FeedbackResponse(
            record[responseTable.id],
            record[responseTable.feedbackId],
            record[responseTable.dateOfResponse],
            record[responseTable.reviewerId],
            record[responseTable.response]
        )
    }

    fun submitResponse(responseToSubmit: ResponseForSubmission): Int {
        val id = sql.insertInto(responseTable)
            .set(responseTable.feedbackId, responseToSubmit.feedbackId)
            .set(responseTable.reviewerId, responseToSubmit.reviewerId)
            .set(responseTable.response, responseToSubmit.response)
            .returning(responseTable.id)
            .fetchOne()

        return id!!.get(responseTable.id)
    }

    fun viewResponseById(responseId: Int): FeedbackResponse? {
        val responseFromDb = sql.select(
                        responseTable.id, responseTable.feedbackId, responseTable.dateOfResponse,
                        responseTable.reviewerId, responseTable.response)
            .from(responseTable)
            .where(responseTable.id.eq(responseId))
            .fetchOne(responseMapper)

        return responseFromDb
    }
}

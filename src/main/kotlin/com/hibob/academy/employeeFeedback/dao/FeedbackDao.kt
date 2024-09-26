package com.hibob.academy.employeeFeedback.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class FeedbackDao(private val sql: DSLContext) {
    private val feedbackTable = FeedbackTable.instance
    private val employeeTable = EmployeeTable.instance

    private val feedbackMapper = RecordMapper<Record, FeedbackInfo> { record ->
        FeedbackInfo(
            record[feedbackTable.id],
            record[feedbackTable.employeeId],
            record[feedbackTable.companyId],
            record[feedbackTable.dateOfFeedback],
            AnonymityType.convertToAnonymityType(record[feedbackTable.anonymity]),
            record[feedbackTable.reviewed],
            record[feedbackTable.feedback]
        )
    }

    fun submitFeedback(feedbackToSubmit: FeedbackForSubmission): Int {
        val id = sql.insertInto(feedbackTable)
            .set(feedbackTable.employeeId, feedbackToSubmit.employeeId)
            .set(feedbackTable.companyId, feedbackToSubmit.companyId)
            .set(feedbackTable.anonymity, AnonymityType.convertAnonymityTypeToString(feedbackToSubmit.anonymity))
            .set(feedbackTable.feedback, feedbackToSubmit.feedback)
            .returning(feedbackTable.id)
            .fetchOne()

        return id!!.get(feedbackTable.id)
    }

    fun viewAllSubmittedFeedback(filterRequest: FeedbackFilterInputs, companyId: Int): List<FeedbackInfo> {
        val query = sql.select(feedbackTable.id, feedbackTable.employeeId,
                                feedbackTable.companyId, feedbackTable.dateOfFeedback,
                                feedbackTable.anonymity, feedbackTable.reviewed,
                                feedbackTable.feedback)

                        .from(feedbackTable)
                        .leftJoin(employeeTable)
                        .on(employeeTable.id.eq(feedbackTable.employeeId))
                        .where(feedbackTable.companyId.eq(companyId))

        filterRequest.department?.let{
            query.and(employeeTable.department.eq(filterRequest.department))
        }

        filterRequest.date?.let{
            query.and(feedbackTable.dateOfFeedback.eq(filterRequest.date))
        }

        filterRequest.anonymity?.let{
            query.and(feedbackTable.anonymity.eq(AnonymityType.convertAnonymityTypeToString(filterRequest.anonymity)))
        }

        return query.fetch(feedbackMapper)
    }

    fun viewStatusOfMyFeedback(feedbackStatus: FeedbackStatusData): Map<Int, Boolean> {
        val query = sql.select(feedbackTable.id, feedbackTable.reviewed)
            .from(feedbackTable)
            .where(feedbackTable.companyId.eq(feedbackStatus.companyId))
            .and(feedbackTable.employeeId.eq(feedbackStatus.employeeId))

        feedbackStatus.feedbackId?.let{
            query.and(feedbackTable.id.eq(feedbackStatus.feedbackId))
        }

        val status = query.fetch()

        if (status.isEmpty())
            return emptyMap()

        return status.associate { record ->
            record[feedbackTable.id] to record[feedbackTable.reviewed]
        }
    }

    fun getFeedbackById(feedbackId: Int, companyId: Int): FeedbackInfo? {
        val feedbackFromDb = sql.select(feedbackTable.id, feedbackTable.employeeId, feedbackTable.companyId,
                    feedbackTable.dateOfFeedback, feedbackTable.anonymity, feedbackTable.reviewed,
                    feedbackTable.feedback)
            .from(feedbackTable)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .fetchOne(feedbackMapper)

        return feedbackFromDb
    }

    fun changeReviewedStatus(feedbackId: Int, companyId: Int, reviewed: Boolean): Int {
        return sql.update(feedbackTable)
            .set(feedbackTable.reviewed, reviewed)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .execute()
    }
}
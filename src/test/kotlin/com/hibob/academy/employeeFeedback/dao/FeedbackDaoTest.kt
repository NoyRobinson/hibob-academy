package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class FeedbackDaoTest@Autowired constructor(private val sql: DSLContext){
    private val feedbackDao = FeedbackDao(sql)
    private val feedbackTable = FeedbackTable.instance
    private val companyId = 1

    @Test
    fun `Submit a new feedback successfully`() {
        val newFeedback  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED,
                                                    "I'm very happy at my workspace!")

        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val feedbackFromDb = feedbackDao.getFeedbackById(feedbackId, companyId)

        val expected = listOf(FeedbackInfo(feedbackId, newFeedback.employeeId, newFeedback.companyId,
                                feedbackFromDb!!.dateOfFeedback, newFeedback.anonymity, false,
                                newFeedback.feedback))

        val actual = feedbackDao.viewAllSubmittedFeedback(newFeedback.companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `View all submitted feedback`(){
        val newFeedback1  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED,
                                                    "I'm very happy at my workspace!")

        val feedbackId1 = feedbackDao.submitFeedback(newFeedback1)
        val feedbackFromDb1 = feedbackDao.getFeedbackById(feedbackId1, companyId)

        val feedback1 = FeedbackInfo(feedbackId1, newFeedback1.employeeId, newFeedback1.companyId,
                                    feedbackFromDb1!!.dateOfFeedback, newFeedback1.anonymity,
                                    false, newFeedback1.feedback)

        val newFeedback2  = FeedbackForSubmission(13, companyId, AnonymityType.IDENTIFIED,
                                                "I'm treated very well")

        val feedbackId2 = feedbackDao.submitFeedback(newFeedback2)
        val feedbackFromDb2 = feedbackDao.getFeedbackById(feedbackId2, companyId)

        val feedback2 = FeedbackInfo(feedbackId2, newFeedback2.employeeId, newFeedback2.companyId,
                                        feedbackFromDb2!!.dateOfFeedback, newFeedback2.anonymity,
                                        false, newFeedback2.feedback)

        val expected = listOf(feedback1, feedback2)
        val actual = feedbackDao.viewAllSubmittedFeedback(companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `View feedbacks of company without any feedbacks`(){
        val actual = feedbackDao.viewAllSubmittedFeedback(companyId)
        assertEquals(emptyList<FeedbackInfo>(), actual)
    }

    @Test
    fun `View status of my feedback`(){
        val newFeedback = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED,
                                                "I'm very happy at my workspace!")

        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val feedbackToCheck = FeedbackStatusData(companyId, 12, feedbackId)
        val actual = feedbackDao.viewStatusOfMyFeedback(feedbackToCheck)
        val expected = mapOf(feedbackId to false)
        assertEquals(expected, actual)
    }

    @Test
    fun `View status of feedback that doesn't exist`(){
        val feedbackToCheck = FeedbackStatusData(companyId, 12, 1)
        val actual = feedbackDao.viewStatusOfMyFeedback(feedbackToCheck)
        val expected = emptyMap<Int, Boolean>()
        assertEquals(expected, actual)
    }

    @Test
    fun `View statuses of all my submitted feedback`(){
        val newFeedback1  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED,
                                                    "I'm very happy at my workspace!")

        val feedbackId1 = feedbackDao.submitFeedback(newFeedback1)

        val newFeedback2  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED,
                                                    "I'm treated very well")

        val feedbackId2 = feedbackDao.submitFeedback(newFeedback2)

        val feedbackToCheck = FeedbackStatusData(companyId, 12, null)

        val expected = mapOf(feedbackId1 to false, feedbackId2 to false)
        val actual = feedbackDao.viewStatusOfMyFeedback(feedbackToCheck)
        assertEquals(expected, actual)
    }

    @Test
    fun `Get employee id of feedback`(){
        val newFeedback  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED,
                                                "I'm very happy at my workspace!")

        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val feedbackInfo = feedbackDao.getFeedbackById(feedbackId, companyId)
        val actual = feedbackInfo!!.employeeId
        assertEquals(12, actual)
    }

    @Test
    fun `Try get employee id of anonymous feedback and return null`(){
        val newFeedback  = FeedbackForSubmission(null, companyId, AnonymityType.IDENTIFIED,
                                                    "I'm very happy at my workspace!")

        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val feedbackInfo = feedbackDao.getFeedbackById(feedbackId, companyId)
        val actual = feedbackInfo!!.employeeId
        assertNull(actual)
    }

    @Test
    fun `Get feedback that exists by id`(){
        val newFeedback  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED,
                                                    "I'm very happy at my workspace!")

        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val actual = feedbackDao.getFeedbackById(feedbackId, companyId)
        val expected = FeedbackInfo(feedbackId, newFeedback.employeeId, newFeedback.companyId, actual!!.dateOfFeedback, newFeedback.anonymity, false, newFeedback.feedback)
        assertEquals(expected, actual)
    }

    @Test
    fun `Try get feedback by id of feedback that doesn't exist and return null`(){
        val actual = feedbackDao.getFeedbackById(1, companyId)
        assertNull(actual)
    }

    @AfterEach
    fun cleanup() {
        sql.deleteFrom(feedbackTable).where(feedbackTable.companyId.eq(companyId)).execute()
    }
}
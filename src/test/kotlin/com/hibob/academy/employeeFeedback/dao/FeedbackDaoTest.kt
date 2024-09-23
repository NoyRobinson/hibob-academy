package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Date

@BobDbTest
class FeedbackDaoTest@Autowired constructor(private val sql: DSLContext){
    private val feedbackDao = FeedbackDao(sql)
    private val feedbackTable = FeedbackTable.instance
    private val companyId = 1

    @Test
    fun `Submit a new feedback successfully`() {
        val newFeedback  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED, "I'm very happy at my workspace!")
        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val expected = listOf(FeedbackInfo(feedbackId, newFeedback.employeeId, newFeedback.companyId, Date.valueOf("2024-09-23"), newFeedback.anonymity, false, newFeedback.feedback))
        val actual = feedbackDao.viewAllSubmittedFeedback(newFeedback.companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `View all submitted feedback`(){
        val newFeedback1  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED, "I'm very happy at my workspace!")
        val feedbackId1 = feedbackDao.submitFeedback(newFeedback1)
        val feedback1 = FeedbackInfo(feedbackId1, newFeedback1.employeeId, newFeedback1.companyId, Date.valueOf("2024-09-23"), newFeedback1.anonymity, false, newFeedback1.feedback)

        val newFeedback2  = FeedbackForSubmission(13, companyId, AnonymityType.IDENTIFIED, "I'm treated very well")
        val feedbackId2 = feedbackDao.submitFeedback(newFeedback2)
        val feedback2 = FeedbackInfo(feedbackId2, newFeedback2.employeeId, newFeedback2.companyId, Date.valueOf("2024-09-23"), newFeedback2.anonymity, false, newFeedback2.feedback)

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
        val newFeedback = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED, "I'm very happy at my workspace!")
        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val feedbackToCheck = FeedbackStatus(companyId, 12, feedbackId)
        val actual = feedbackDao.viewStatusOfMyFeedback(feedbackToCheck)
        assertEquals(false, actual)
    }

    @Test
    fun `View status of feedback that doesn't exist`(){
        val feedbackToCheck = FeedbackStatus(companyId, 12, 1)
        assertThrows<BadRequestException>{ feedbackDao.viewStatusOfMyFeedback(feedbackToCheck) }
    }

    @Test
    fun `View statuses of all my submitted feedback`(){
        val newFeedback1  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED, "I'm very happy at my workspace!")
        val feedbackId1 = feedbackDao.submitFeedback(newFeedback1)

        val newFeedback2  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED, "I'm treated very well")
        val feedbackId2 = feedbackDao.submitFeedback(newFeedback2)

        val expected = mapOf(feedbackId1 to false, feedbackId2 to false)
        val actual = feedbackDao.viewStatusesOfAllMySubmittedFeedback(companyId, 12)
        assertEquals(expected, actual)
    }

    @Test
    fun `Get employee id of feedback`(){
        val newFeedback  = FeedbackForSubmission(12, companyId, AnonymityType.IDENTIFIED, "I'm very happy at my workspace!")
        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val actual = feedbackDao.getFeedbackEmployeeId(feedbackId, companyId)
        assertEquals(12, actual)
    }

    @Test
    fun `Try get employee id of anonymous feedback and return null`(){
        val newFeedback  = FeedbackForSubmission(null, companyId, AnonymityType.IDENTIFIED, "I'm very happy at my workspace!")
        val feedbackId = feedbackDao.submitFeedback(newFeedback)
        val actual = feedbackDao.getFeedbackEmployeeId(feedbackId, companyId)
        assertEquals(null, actual)
    }

    @AfterEach
    fun cleanup() {
        sql.deleteFrom(feedbackTable).where(feedbackTable.companyId.eq(companyId)).execute()
    }
}
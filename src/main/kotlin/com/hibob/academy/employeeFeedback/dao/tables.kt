package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.JooqTable

class EmployeeTable(tableName: String = "employee"): JooqTable(tableName) {
    val id = createIntField("id")
    val firstName = createVarcharField("first_name")
    val lastName = createVarcharField("last_name")
    val role = createVarcharField("role")
    val department = createVarcharField("department")
    val companyId = createIntField("company_id")

    companion object {
        val instance = EmployeeTable()
    }
}

class FeedbackTable(tableName: String = "feedback"): JooqTable(tableName) {
    val id = createIntField("id")
    val employeeId = createIntField("employee_id")
    val companyId = createIntField("company_id")
    val dateOfFeedback = createDateField("date_of_feedback")
    val anonymity = createVarcharField("anonymity")
    val reviewed = createBooleanField("reviewed")
    val feedback = createVarcharField("feedback")

    companion object {
        val instance = FeedbackTable()
    }
}

class ResponseTable(tableName: String = "response"): JooqTable(tableName) {
    val id = createIntField("id")
    val feedbackId = createIntField("feedback_id")
    val dateOfResponse = createDateField("date_of_response")
    val reviewerId = createIntField("reviewer_id")
    val response = createVarcharField("response")

    companion object {
        val instance = ResponseTable()
    }
}
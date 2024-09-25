package com.hibob.academy.employeeFeedback.dao

import java.sql.Date

data class EmployeeCreationRequest(val firstName: String, val lastName: String, val role: RoleType, val department: String, val companyId: Int)

data class LoggedInEmployeeInfo(val id: Int, val firstName: String, val lastName: String, val companyId: Int, val role: RoleType)

data class LoginParams(val id: Int, val firstName: String, val lastName: String)

data class EmployeeLoginRequest(val id: Int, val firstName: String, val lastName: String)

data class FeedbackInfo(val id: Int, val employeeId: Int?, val companyId: Int, val dateOfFeedback: Date, val anonymity: AnonymityType, val reviewed: Boolean, val feedback: String)

data class FeedbackSubmitRequest(val anonymity: String, val feedback: String)

data class FeedbackForSubmission(val employeeId: Int?, val companyId: Int, val anonymity: AnonymityType, val feedback: String)

data class FeedbackStatusData(val companyId: Int, val employeeId: Int, val feedbackId: Int?)

data class FeedbackResponse(val id: Int, val feedbackId: Int, val dateOfResponse: Date, val reviewerId: Int, val response: String)

data class ResponseForSubmission(val feedbackId: Int, val reviewerId: Int, val response: String)

data class ResponseSubmitRequest(val response: String)


enum class RoleType {
    HR, ADMIN, EMPLOYEE;

    companion object {
        fun convertRoleTypeToString(role: RoleType): String =
            role.toString()

        fun convertStringToRoleType(role: String): RoleType =
            valueOf(role.toUpperCase())
    }
}

enum class AnonymityType {
    ANONYMOUS, IDENTIFIED;

    companion object {
        fun convertAnonymityTypeToString(anonymity: AnonymityType): String =
            anonymity.toString()

        fun convertStringToAnonymityType(anonymity: String): AnonymityType =
            valueOf(anonymity.toUpperCase())
    }
}

enum class FilterType {
    DATE, DEPARTMENT, ANONYMITY_STATUS;

    companion object {
        fun convertFilterTypeToString(filterBy: FilterType): String =
            filterBy.toString()

        fun convertStringToAnonymityType(filterBy: String): FilterType =
            valueOf(filterBy.toUpperCase())
    }
}
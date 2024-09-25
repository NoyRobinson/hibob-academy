package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.RoleType
import com.hibob.academy.employeeFeedback.dao.RoleType.Companion.convertStringToRoleType
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Service

@Service
class AuthenticatedUsersService {
    fun getLoggedInEmployeeId(@Context request: ContainerRequestContext) : Int {
        val loggedInEmployeeId = request.getProperty("employeeId") as? Int
            ?: throw WebApplicationException("Employee id not found", Response.Status.UNAUTHORIZED)

        return loggedInEmployeeId
    }

    fun getLoggedInCompanyId(@Context request: ContainerRequestContext) : Int {
        val companyId = request.getProperty("companyId") as? Int
            ?: throw WebApplicationException("Company id not found", Response.Status.UNAUTHORIZED)

        return companyId
    }

    fun getLoggedInRole(@Context request: ContainerRequestContext) : RoleType {
        val role = request.getProperty("role") as? String
            ?: throw WebApplicationException("Role not found", Response.Status.UNAUTHORIZED)

        return convertStringToRoleType(role)
    }

    fun validateRole(role: RoleType, validRoles: List<RoleType>): Boolean {
        return validRoles.contains(role)
    }
}
package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.RoleType
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.container.ContainerRequestContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class AuthenticatedUsersServiceTest{
    private val authenticatedUserService = AuthenticatedUsersService()
    private val requestContext: ContainerRequestContext = mock(ContainerRequestContext::class.java)

    @Test
    fun `getLoggedInEmployeeId should return employeeId when authenticated`() {
        val expectedId = 123
        whenever(requestContext.getProperty("employeeId")).thenReturn(expectedId)
        val actual = authenticatedUserService.getLoggedInEmployeeId(requestContext)
        assertEquals(expectedId, actual)
    }

    @Test
    fun `getLoggedInEmployeeId should throw an exception when user isn't authenticated`() {
        whenever(requestContext.getProperty("employeeId")).thenReturn(null)
        assertThrows<WebApplicationException> { authenticatedUserService.getLoggedInEmployeeId(requestContext) }
    }

    @Test
    fun `getLoggedInCompanyId should return companyId when authenticated`() {
        val expectedId = 123
        whenever(requestContext.getProperty("companyId")).thenReturn(expectedId)
        val actual = authenticatedUserService.getLoggedInCompanyId(requestContext)
        assertEquals(expectedId, actual)
    }

    @Test
    fun `getLoggedInCompanyId should throw an exception when user isn't authenticated`() {
        whenever(requestContext.getProperty("companyId")).thenReturn(null)
        assertThrows<WebApplicationException> { authenticatedUserService.getLoggedInCompanyId(requestContext) }
    }

    @Test
    fun `getLoggedInRole should return a role when authenticated`() {
        val expectedRole = RoleType.ADMIN
        whenever(requestContext.getProperty("role")).thenReturn("ADMIN")
        val actual = authenticatedUserService.getLoggedInRole(requestContext)
        assertEquals(expectedRole, actual)
    }

    @Test
    fun `getLoggedInRole should throw an exception when user isn't authenticated`() {
        whenever(requestContext.getProperty("role")).thenReturn(null)
        assertThrows<WebApplicationException> { authenticatedUserService.getLoggedInRole(requestContext) }
    }

    @Test
    fun `validateHr should return true when role is hr`() {
        val role = RoleType.HR
        val result = authenticatedUserService.validateHr(role)
        assertTrue(result)
    }
}

package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.EmployeeDao
import com.hibob.academy.employeeFeedback.dao.EmployeeInfo
import com.hibob.academy.employeeFeedback.dao.RoleType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class EmployeeServiceTest{
    private val employeeDao = mock<EmployeeDao>{}
    private val employeeService = EmployeeService(employeeDao)

    @Test
    fun `Get employee by id should return info of employee`(){
        val expectedEmployee = EmployeeInfo(12, RoleType.convertStringToRoleType("ADMIN"), "dev", 1)
        whenever(employeeDao.getEmployeeById(12)).thenReturn(expectedEmployee)
        val actualEmployee = employeeService.getEmployeeById(12)
        assertEquals(expectedEmployee, actualEmployee)
        verify(employeeDao).getEmployeeById(12)
    }

    @Test
    fun `Get employee should return null if the employee does not exist`() {
        whenever(employeeDao.getEmployeeById(12)).thenReturn(null)
        val actualEmployee = employeeService.getEmployeeById(12)
        assertEquals(null, actualEmployee)
        verify(employeeDao).getEmployeeById(12)
    }
}
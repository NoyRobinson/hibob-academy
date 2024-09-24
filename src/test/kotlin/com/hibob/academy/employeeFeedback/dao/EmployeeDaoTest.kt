package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeDao = EmployeeDao(sql)
    private val employeeTable = EmployeeTable.instance
    private val companyId = 1

    @Test
    fun `find existing employee from db with login params`(){
        val newEmployee = EmployeeCreation("Rachel", "Green", RoleType.ADMIN, "dev", companyId)
        val employeeId = employeeDao.insertNewEmployee(newEmployee)
        employeeId?.let{
            val loginParams = LoginParams(employeeId, "Rachel", "Green")
            val actual = employeeDao.findEmployeeByLoginParams(loginParams)
            val expected = LoggedInEmployeeInfo(employeeId, "Rachel", "Green", companyId, RoleType.ADMIN)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `findEmployeeByLoginParams should return null when this employee does not exist`(){
        val loginParams = LoginParams(12, "Noy", "Robinson")
        val actual = employeeDao.findEmployeeByLoginParams(loginParams)
        assertNull(actual)
    }

    @AfterEach
    fun cleanup() {
        sql.deleteFrom(employeeTable).where(employeeTable.companyId.eq(companyId)).execute()
    }
}

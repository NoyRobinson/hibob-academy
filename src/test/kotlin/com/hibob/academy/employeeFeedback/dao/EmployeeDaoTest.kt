package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeDao = EmployeeDao(sql)
    private val employeeTable = EmployeeTable.instance

    @Test
    fun `find existing employee from db with login params`(){
        val loginParams = LoginParams(1, "Rachel", "Green")
        val actual = employeeDao.findEmployeeByLoginParams(loginParams)
        val expected = LoggedInEmployeeInfo(1, "Rachel", "Green", 1, RoleType.ADMIN)
        assertEquals(expected, actual)
    }

    @Test
    fun `findEmployeeByLoginParams should return null when this employee does not exist`(){
        val loginParams = LoginParams(12, "Noy", "Robinson")
        val actual = employeeDao.findEmployeeByLoginParams(loginParams)
        assertNull(actual)
    }
}

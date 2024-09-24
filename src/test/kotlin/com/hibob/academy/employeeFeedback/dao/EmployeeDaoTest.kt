package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    @Autowired
    private val employeeDao = EmployeeDao(sql)
    private val employeeTable = EmployeeTable.instance

//    @Test
//    fun `find existing employee from db with login params`(){
//       // val employee = EmployeeInfo(12, RoleType.EMPLOYEE, "dev", 1)
////        val loginParams = LoginParams(12, "Noy", "Robinson")
////        val actual = employeeDao.findEmployeeByLoginParams(loginParams)
////        val expected = LoggedInEmployeeInfo(12, "Noy", "Robinson", 1, RoleType.EMPLOYEE)
////        assertEquals(expected, actual)
//    }

    @Test
    fun `findEmployeeByLoginParams should return null when this employee does not exist`(){
        val loginParams = LoginParams(12, "Noy", "Robinson")
        val actual = employeeDao.findEmployeeByLoginParams(loginParams)
        assertNull(actual)
    }
}

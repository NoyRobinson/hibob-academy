package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeDao = EmployeeDao(sql)
    private val employeeTable = EmployeeTable.instance

    @Test
    fun `Get employee info by id`(){
        val employee = EmployeeInfo(1, RoleType.ADMIN, "dev", 1)
        val actual = employeeDao.getEmployeeById(1)
        assertEquals(employee, actual)
    }

    @Test
    fun `Employee with this id doesn't exist`(){
        assertThrows<BadRequestException>{ employeeDao.getEmployeeById(100) }
    }
}

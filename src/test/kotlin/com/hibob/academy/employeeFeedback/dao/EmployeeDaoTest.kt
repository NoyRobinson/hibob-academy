package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeDao = EmployeeDao(sql)
    private val employeeTable = EmployeeTable.instance

    @Test
    fun `Employee with this id doesn't exist`(){
        assertThrows<BadRequestException>{ employeeDao.getEmployeeById(100) }
    }
}

package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeDao = EmployeeDao(sql)

    @Test
    fun `Employee with this id doesn't exist`(){
        val actual = employeeDao.getEmployeeById(100)
        assertEquals(null, actual)
    }
}

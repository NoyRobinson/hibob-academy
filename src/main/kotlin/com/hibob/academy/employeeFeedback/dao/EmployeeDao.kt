package com.hibob.academy.employeeFeedback.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class EmployeeDao(private val sql: DSLContext) {
    private val employeeTable = EmployeeTable.instance

    private val employeeMapper = RecordMapper<Record, EmployeeInfo> { record ->
        EmployeeInfo(
            record[employeeTable.id],
            RoleType.convertStringToRoleType(record[employeeTable.role]),
            record[employeeTable.department],
            record[employeeTable.companyId]
        )
    }

    fun getEmployeeById(id: Int): EmployeeInfo? {
        val employee = sql.select(employeeTable.id, employeeTable.role, employeeTable.department, employeeTable.companyId, employeeTable.role)
            .from(employeeTable)
            .where(employeeTable.id.eq(id))
            .fetchOne(employeeMapper)

        return employee
    }
}
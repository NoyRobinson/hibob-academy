package com.hibob.academy.employeeFeedback.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class EmployeeDao(private val sql: DSLContext) {
    private val employeeTable = EmployeeTable.instance

    private val employeeMapper = RecordMapper<Record, LoggedInEmployeeInfo> { record ->
        LoggedInEmployeeInfo(
            record[employeeTable.id],
            record[employeeTable.firstName],
            record[employeeTable.lastName],
            record[employeeTable.companyId],
            RoleType.convertStringToRoleType(record[employeeTable.role])
        )
    }

    fun findEmployeeByLoginParams(loginParams: LoginParams): LoggedInEmployeeInfo? {
        val loggedInEmployeeInfo = sql.select(employeeTable.id, employeeTable.firstName,
                                            employeeTable.lastName, employeeTable.companyId,
                                            employeeTable.role)
            .from(employeeTable)
            .where(employeeTable.id.eq(loginParams.id))
            .and(employeeTable.firstName.eq(loginParams.firstName))
            .and(employeeTable.lastName.eq(loginParams.lastName))
            .fetchOne(employeeMapper)
        return loggedInEmployeeInfo
    }
}
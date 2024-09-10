package com.hibob.academy.dao

import com.hibob.academy.utils.JooqTable

class petTable(tableName: String = "pet"): JooqTable(tableName) {

    val name = createVarcharField("name")
    val type = createVarcharField("type")
    val companyId = createBigIntField("companyId")
    val dateOfArrival = createDateField("date_of_arrival")

    companion object {
        val instance = petTable()
    }
}

class ownerTable(tableName: String = "owner"): JooqTable(tableName) {

    val name = createVarcharField("name")
    val companyId = createBigIntField("company_id")
    val employeeId = createVarcharField("employee_id")

    companion object {
        val instance = ownerTable()
    }
}

package com.hibob.academy.dao

import jakarta.inject.Inject
import org.jooq.DSLContext

class VaccineToPetDao @Inject constructor(private val sql: DSLContext){

    private val vaccineToPetTable = VaccineToPetTable.instance
}
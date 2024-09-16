package com.hibob.academy.dao

import jakarta.inject.Inject
import org.jooq.DSLContext

class VaccineDao @Inject constructor(private val sql: DSLContext){

    private val vaccineTable = VaccineTable.instance
}
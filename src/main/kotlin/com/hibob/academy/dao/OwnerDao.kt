package com.hibob.academy.dao

import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper

class OwnerDao @Inject constructor(private val sql: DSLContext) {

    private val owner = ownerTable.instance

    private val ownerMapper = RecordMapper<Record, OwnerData> { record ->
        OwnerData(
            record[owner.name],
            record[owner.companyId].toBigInteger(),
            record[owner.employeeId]
        )
    }

    fun getOwner(): List<OwnerData> =
        sql.select(owner.name, owner.companyId, owner.employeeId)
            .from(owner)
            .fetch(ownerMapper)

    fun createOwner(ownerName: String, companyId: Long, employeeId: String) =
        sql.insertInto(owner)
            .set(owner.name, ownerName)
            .set(owner.companyId, companyId)
            .set(owner.employeeId, employeeId)
            .onConflict(owner.companyId, owner.employeeId)
            .doNothing()
            .execute()
}
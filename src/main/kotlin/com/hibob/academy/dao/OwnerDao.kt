package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Repository

@Repository
class OwnerDao(private val sql: DSLContext) {

    private val ownerTable = OwnerTable.instance
    private val pet = PetTable.instance

    private val ownerMapper = RecordMapper<Record, OwnerData> { record ->
        OwnerData(
            record[ownerTable.id],
            record[ownerTable.name],
            record[ownerTable.companyId],
            record[ownerTable.employeeId]
        )
    }

    fun createOwner(ownerInfo: OwnerCreationRequest): Long {
        val id = sql.insertInto(ownerTable)
            .set(ownerTable.name, ownerInfo.name)
            .set(ownerTable.companyId, ownerInfo.companyId)
            .set(ownerTable.employeeId, ownerInfo.employeeId)
            .onConflict(ownerTable.companyId, ownerTable.employeeId)
            .doNothing()
            .returning(ownerTable.id)
            .fetchOne()

        return id?.get(ownerTable.id)
            ?: throw RuntimeException("Failed to insert owner and retrieve ID")
    }

    fun getAllOwners(companyId: Long): List<OwnerData> =
        sql.select(ownerTable.id, ownerTable.name, ownerTable.companyId, ownerTable.employeeId)
            .from(ownerTable)
            .where(ownerTable.companyId.eq(companyId))
            .fetch(ownerMapper)

    fun getOwnerByPetId(petId: Long, companyId: Long): OwnerData? =
        sql.select(ownerTable.id, ownerTable.name, ownerTable.companyId, ownerTable.employeeId)
            .from(ownerTable)
            .leftJoin(pet)
            .on(pet.ownerId.eq(ownerTable.id))
            .where(pet.id.eq(petId))
            .and(pet.companyId.eq(ownerTable.companyId))
            .and(pet.companyId.eq(companyId))
            .fetchOne(ownerMapper)
}
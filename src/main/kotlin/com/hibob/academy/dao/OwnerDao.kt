package com.hibob.academy.dao

import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Repository

@Repository
class OwnerDao @Inject constructor(private val sql: DSLContext) {

    private val owner = OwnerTable.instance
    private val pet = PetTable.instance

    private val ownerMapper = RecordMapper<Record, OwnerData> { record ->
        OwnerData(
            record[owner.id],
            record[owner.name],
            record[owner.companyId],
            record[owner.employeeId]
        )
    }

    fun createOwner(ownerInfo: Owner): Long {
        val id = sql.insertInto(owner)
            .set(owner.name, ownerInfo.name)
            .set(owner.companyId, ownerInfo.companyId)
            .set(owner.employeeId, ownerInfo.employeeId)
            .onConflict(owner.companyId, owner.employeeId)
            .doNothing()
            .returning(owner.id)
            .fetchOne()

        return id?.get(owner.id)
            ?: throw RuntimeException("Failed to insert owner and retrieve ID")
    }

    fun getOwner(companyId: Long): List<OwnerData> =
        sql.select(owner.id, owner.name, owner.companyId, owner.employeeId)
            .from(owner)
            .where(owner.companyId.eq(companyId))
            .fetch(ownerMapper)

    fun getOwnerByPetId(petId: Long): OwnerData? =
        sql.select(owner.id, owner.name, owner.companyId, owner.employeeId)
            .from(owner)
            .leftJoin(pet)
            .on(pet.ownerId.eq(owner.id))
            .where(pet.id.eq(petId))
            .and(pet.companyId.eq(owner.companyId))
            .fetchOne(ownerMapper)
}
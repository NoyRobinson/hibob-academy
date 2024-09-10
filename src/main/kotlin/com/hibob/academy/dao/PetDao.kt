package com.hibob.academy.dao

import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component

@Component
class PetDao @Inject constructor(private val sql: DSLContext) {

    private val pet = petTable.instance

    private val petMapper = RecordMapper<Record, PetData> { record ->
        PetData(
            record[pet.name],
            record[pet.type],
            record[pet.companyId].toBigInteger(),
            record[pet.dateOfArrival]
        )
    }

    fun getPets(petType: PetType): List<PetData> =
        sql.select(pet.name, pet.companyId, pet.dateOfArrival)
            .from(pet)
            .where(pet.type.eq(getType(petType)))
            .fetch(petMapper)

    enum class PetType {
        DOG, CAT
    }

    fun getType(petType: PetType) =
        when (petType) {
            PetType.DOG -> "Dog"
            PetType.CAT -> "Cat"
        }
}
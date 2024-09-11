package com.hibob.academy.dao

import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import java.sql.Date


@Component
class PetDao @Inject constructor(private val sql: DSLContext) {

    private val pet = petTable.instance

    private val petMapper = RecordMapper<Record, PetData> { record ->
        PetData(
            record[pet.name],
            convertStringToPetType(record[pet.type]),
            record[pet.companyId],
            Date(record[pet.dateOfArrival].time)
        )
    }

    fun getPets(petType: PetType): List<PetData> =
        sql.select(pet.name, pet.companyId, pet.dateOfArrival)
            .from(pet)
            .where(pet.type.eq(convertPetTypeToPetString(petType)))
            .fetch(petMapper)


    fun createPet(name: String, type: String, companyId: Long, dateOfArrival: Date) =
        sql.insertInto(pet)
            .set(pet.name, name)
            .set(pet.type, type)
            .set(pet.companyId, companyId)
            .set(pet.dateOfArrival, dateOfArrival)
            .onConflict(pet.companyId)
            .doNothing()
            .execute()

    fun convertPetTypeToPetString(petType: PetType) =
        when (petType) {
            PetType.DOG -> "Dog"
            PetType.CAT -> "Cat"
            PetType.BIRD -> "Bird"
        }

    fun convertStringToPetType(petType: String) =
        when (petType) {
            "Dog" -> PetType.DOG
            "Cat" -> PetType.CAT
            else -> PetType.BIRD
        }
}
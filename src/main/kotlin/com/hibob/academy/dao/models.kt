package com.hibob.academy.dao

import java.util.*

data class Example(val id: Long, val companyId: Long, val data: String)

data class OwnerData(val id: Long, val name: String, val companyId: Long, val employeeId: String)

data class PetData(val id: Long, val name: String, val type: PetType, val companyId: Long, val dateOfArrival: Date, val ownerId: Long)

data class PetDataWithoutType(val id: Long, val name: String, val companyId: Long, val dateOfArrival: Date, val ownerId: Long)

enum class PetType {
    DOG, CAT, BIRD;

    companion object {
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
}
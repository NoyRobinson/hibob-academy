package com.hibob.academy.dao

import java.util.*

data class Example(val id: Long, val companyId: Long, val data: String)

data class OwnerData(val id: Long, val name: String, val companyId: Long, val employeeId: String)

data class PetData(val id: Long, val name: String, val type: PetType, val companyId: Long, val dateOfArrival: Date, val ownerId: Long?)

enum class PetType {
    DOG, CAT, BIRD;

    companion object {
        fun convertPetTypeToPetString(petType: PetType): String =
            petType.toString()

        fun convertStringToPetType(petType: String): PetType =
            valueOf(petType.uppercase())
    }
}
package com.hibob.academy.dao

import java.sql.Date

data class Example(val id: Long, val companyId: Long, val data: String)

data class OwnerData(val name: String, val companyId: Long, val employeeId: String)

data class PetData(val name: String, val type: PetType, val companyId: Long, val dateOfArrival: Date)

data class PetDataWithoutType(val name: String, val companyId: Long, val dateOfArrival: Date)


enum class PetType {
    DOG, CAT, BIRD
}
package com.hibob.academy.dao

import java.math.BigInteger
import java.util.Date
import java.util.UUID

data class Example(val id: Long, val companyId: Long, val data: String)

data class OwnerData(val name: String, val companyId: BigInteger, val employeeId: String)

data class PetData(val name: String, val type: String, val companyId: BigInteger, val dateOfArrival: Date)
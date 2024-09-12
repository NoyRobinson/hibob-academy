package com.hibob.academy.types

import java.time.LocalDateTime
import java.util.*

data class Owner(
    val id: Long,
    val companyId: Long,
    val employeeId: String,
    var name: String?,
    var firstName: String?,
    var lastName: String?,
    var type: String,
    val dateOfArrival: Date
)
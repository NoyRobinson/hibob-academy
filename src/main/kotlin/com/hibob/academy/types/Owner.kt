package com.hibob.academy.types

import java.time.LocalDateTime
import java.util.*

data class Owner(
    val id: UUID,
    val companyId: UUID,
    val employeeId: UUID,
    val name: String?,
    val firstName: String?,
    val lastName: String?,
    var type: String,
    val dateOfArrival: Date
)
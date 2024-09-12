package com.hibob.academy.types

import java.util.*

data class Pet (
    val petId: UUID,
    var name: String,
    val type: String,
    val companyId: UUID,
    val dateOfArrival: Date,
)
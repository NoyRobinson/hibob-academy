package com.hibob.academy.dao

import PetDao
import com.hibob.academy.utils.BobDbTest

import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Date
import kotlin.random.Random


@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext){

    private val petDao = PetDao(sql)
    private val table = PetTable.instance
    val companyId = 12L
    val ownerId = Random.nextLong()

    @Test
    fun `create a new pet that doesn't exist in the database`() {
        val petName = "Angie"
        val petTypeString = "Dog"
        val petType = petDao.convertStringToPetType(petTypeString)
        val dateOfArrival = Date.valueOf("2010-05-20")
        val ownerId = ownerId

        petDao.createPet(petName, petTypeString, companyId, dateOfArrival, ownerId)
        val expected = listOf(PetData(petName, petType, companyId, dateOfArrival, ownerId))
        assertEquals(expected, petDao.getAllPets())
    }

    @Test
    fun `get pets by type`() {
        val petName = "Angie"
        val petTypeString = "Dog"
        val dateOfArrival = Date.valueOf("2010-05-20")

        petDao.createPet(petName, petTypeString, companyId, dateOfArrival, ownerId)

        val actual = petDao.getPetsByType(petDao.convertStringToPetType("Cat")).size
        assertEquals(0, actual)
    }

    @Test
    fun `get pets by owner`(){

    }


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }
}
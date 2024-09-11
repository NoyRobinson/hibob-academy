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
    val companyId = Random.nextLong()

    @Test
    fun `create a new pet that doesn't exist in the database`() {
        val petName = "Angie"
        val petTypeString = "Dog"
        val petType = petDao.convertStringToPetType(petTypeString)
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")

        petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival)
        val expected = listOf(PetData(petName, petType, petCompanyId, dateOfArrival))
        assertEquals(expected, petDao.getAllPets())
    }

    @Test
    fun `create a new pet that exists in the database`() {
        val petName1 = "Angie"
        val petTypeString1 = "Dog"
        val petCompanyId1 = companyId
        val dateOfArrival1 = Date.valueOf("2010-05-20")
        petDao.createPet(petName1, petTypeString1, petCompanyId1, dateOfArrival1)

        val petName2 = "Nessy"
        petDao.createPet(petName2, petTypeString1, petCompanyId1, dateOfArrival1)

        val actual = petDao.getAllPets().size
        assertEquals(1, actual)
    }

    @Test
    fun `get pets by type`() {
        val petName = "Angie"
        val petTypeString = "Dog"
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")

        petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival)

        val actual = petDao.getPetsByType(petDao.convertStringToPetType("Cat")).size
        assertEquals(0, actual)
    }


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }
}
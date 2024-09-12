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
import java.util.*
import kotlin.random.Random


@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext){

    private val petDao = PetDao(sql)
    private val table = PetTable.instance
    val companyId = 12L
    val id = 1L
    val ownerId = null

    @Test
    fun `create a new pet that doesn't exist in the database`() {
        val petName = "Angie"
        val petTypeString = "Dog"
        val petType = PetType.convertStringToPetType(petTypeString)
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")

        petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival, ownerId)
        val expected = listOf(PetData(id, petName, petType, petCompanyId, dateOfArrival, ownerId))
        assertEquals(expected, petDao.getAllPets(companyId))
    }

    @Test
    fun `create a new pet that exists in the database`() {
        val petName1 = "Angie"
        val petTypeString1 = "Dog"
        val petType1 = PetType.convertStringToPetType(petTypeString1)
        val petCompanyId1 = companyId
        val dateOfArrival1 = Date.valueOf("2010-05-20")
        petDao.createPet(petName1, petTypeString1, petCompanyId1, dateOfArrival1, ownerId)

        val petName2 = "Nessy"
        petDao.createPet(petName2, petTypeString1, petCompanyId1, dateOfArrival1, ownerId)

        val expected = listOf(PetData(id, petName1, petType1, petCompanyId1, dateOfArrival1, ownerId))
        val actual = petDao.getAllPets(companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `get pets by type`() {
        val petName = "Angie"
        val petTypeString = "Dog"
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")

        petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival, ownerId)

        val expected = emptyList<PetData>()
        val actual = petDao.getPetsByType(PetType.convertStringToPetType("Cat"), companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `update an owner for a pet`(){
        val petName = "Angie"
        val petTypeString = "Dog"
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")
        val petData = PetData(id, petName, PetType.convertStringToPetType(petTypeString), petCompanyId, dateOfArrival, ownerId)
        val newOwnerId = 4L
        petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival, ownerId)

        assertEquals(1, petDao.updatePetOwner(petData, newOwnerId, companyId))
    }

    @Test
    fun `pet has an owner so don't update`(){
        val petName = "Angie"
        val petTypeString = "Dog"
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")
        val originOwnerId = 11L
        val petData = PetData(id, petName, PetType.convertStringToPetType(petTypeString), petCompanyId, dateOfArrival, originOwnerId)
        petDao.createPet(petName, petTypeString, companyId, dateOfArrival, ownerId)

        petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival, originOwnerId)

        val newOwnerId = 12L
        assertEquals(0, petDao.updatePetOwner(petData, newOwnerId, companyId))
    }

    @Test
    fun `get pets by owner`(){
        val petName1 = "Angie"
        val petTypeString = "Dog"
        val dateOfArrival = Date.valueOf("2010-05-20")
        petDao.createPet(petName1, petTypeString, companyId, dateOfArrival, ownerId)

        val petName2 = "Angie"
        petDao.createPet(petName2, petTypeString, companyId, dateOfArrival, ownerId)

        val actual = petDao.getPetsByOwner(ownerId).size
        assertEquals(2, actual)
    }

    @Test
    fun `count pets by type`(){
        val petName1 = "Angie"
        val petTypeString = "Dog"
        val dateOfArrival = Date.valueOf("2010-05-20")
        petDao.createPet(petName1, petTypeString, companyId, dateOfArrival, ownerId)

        val petName2 = "Angie"
        petDao.createPet(petName2, petTypeString, companyId, dateOfArrival, ownerId)

        val petName3 = "Max"
        val petTypeString2 = "Cat"
        petDao.createPet(petName3, petTypeString2, companyId, dateOfArrival, ownerId)

        val expectedCount = mapOf("Dog" to 2, "Cat" to 1)
        val actualCount = petDao.countPetsByType()
        assertEquals(expectedCount, actualCount)
    }


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }
}
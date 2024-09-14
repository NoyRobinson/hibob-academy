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
    val ownerId = null

    @Test
    fun `create a new pet that doesn't exist in the database`() {
        val petName = "Angie"
        val petTypeString = "Dog"
        val petType = PetType.convertStringToPetType(petTypeString)
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")

        val petId = petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival, ownerId)
        val expected = listOf(PetData(petId, petName, petType, petCompanyId, dateOfArrival, ownerId))
        assertEquals(expected, petDao.getAllPets(companyId))
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
        val petId = petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival, ownerId)
        val petData = PetData(petId, petName, PetType.convertStringToPetType(petTypeString), petCompanyId, dateOfArrival, ownerId)
        val newOwnerId = 4L

        assertEquals(1, petDao.updatePetOwner(petData, newOwnerId, companyId))
    }

    @Test
    fun `pet has an owner so don't update`(){
        val petName = "Angie"
        val petTypeString = "Dog"
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")
        val originOwnerId = 11L

        val petId = petDao.createPet(petName, petTypeString, companyId, dateOfArrival, originOwnerId)
        val petData = PetData(petId, petName, PetType.convertStringToPetType(petTypeString), petCompanyId, dateOfArrival, originOwnerId)

        val newOwnerId = 12L
        assertEquals(0, petDao.updatePetOwner(petData, newOwnerId, companyId))
    }

    @Test
    fun `get pets by owner`(){
        val petName1 = "Angie"
        val petTypeString = "Dog"
        val dateOfArrival = Date.valueOf("2010-05-20")
        petDao.createPet(petName1, petTypeString, companyId, dateOfArrival, 123L)

        val petName2 = "Nessy"
        petDao.createPet(petName2, petTypeString, companyId, dateOfArrival, 123L)

        val actual = petDao.getPetsByOwner(123L, companyId).size
        assertEquals(2, actual)
    }

    @Test
    fun `get pet by id`(){
        val petName = "Angie"
        val petTypeString = "Dog"
        val dateOfArrival = Date.valueOf("2010-05-20")
        val petId = petDao.createPet(petName, petTypeString, companyId, dateOfArrival, 123L)
        val pet = petDao.getPetById(petId, companyId)
        val actual = PetData(petId, petName, PetType.convertStringToPetType(petTypeString), companyId, dateOfArrival, 123L )
        assertEquals(pet, actual)
    }

    @Test
    fun `count pets by type`(){
        val petName1 = "Angie"
        val petTypeString = "Dog"
        val petType1 = PetType.convertStringToPetType(petTypeString)
        val dateOfArrival = Date.valueOf("2010-05-20")
        petDao.createPet(petName1, petTypeString, companyId, dateOfArrival, ownerId)

        val petName2 = "Angie"
        petDao.createPet(petName2, petTypeString, companyId, dateOfArrival, ownerId)

        val petName3 = "Max"
        val petTypeString2 = "Cat"
        val petType2 = PetType.convertStringToPetType(petTypeString2)
        petDao.createPet(petName3, petTypeString2, companyId, dateOfArrival, ownerId)

        val expectedCount = mapOf(petType1 to 2, petType2 to 1)
        val actualCount = petDao.countPetsByType(companyId)

        println("Expected Count: $expectedCount")
        println("Actual Count: $actualCount")

        assertEquals(expectedCount, actualCount)
    }

    @Test
    fun `update pets name`(){
        val petName = "Kaia"
        val petTypeString = "Dog"
        val petType = PetType.convertStringToPetType(petTypeString)
        val dateOfArrival = Date.valueOf("2024-09-13")
        val newName = "Charlie"
        val petId = petDao.createPet(petName, petTypeString, companyId, dateOfArrival, ownerId)
        petDao.updatePetName(petId, newName, companyId)
        val expected = PetData(petId, newName, petType, companyId, dateOfArrival, ownerId)
        val updatedPet = petDao.getPetById(petId, companyId)
        assertEquals(expected, updatedPet)
    }

    @Test
    fun `delete a pet`(){
        val petName = "Max"
        val petTypeString = "Cat"
        val dateOfArrival = Date.valueOf("2024-09-13")
        val petId = petDao.createPet(petName, petTypeString, companyId, dateOfArrival, ownerId)
        petDao.deletePet(petId, companyId)
        assertEquals(null, petDao.getPetById(petId, companyId))
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }
}
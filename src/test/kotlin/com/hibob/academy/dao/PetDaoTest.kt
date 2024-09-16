package com.hibob.academy.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Date

@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext){

    private val petDao = PetDao(sql)
    private val table = PetTable.instance
    val companyId = 12L
    val ownerId = null

    @Test
    fun `create a new pet that doesn't exist in the database`() {
        val pet = PetrCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), ownerId = null )
        val petId = petDao.createPet(pet)
        val expected = listOf(PetData(petId, pet.name, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId))
        assertEquals(expected, petDao.getAllPets(companyId))
    }

    @Test
    fun `get pets by type`() {
        val pet = PetrCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), ownerId)
        petDao.createPet(pet)
        val expected = emptyList<PetData>()
        val actual = petDao.getPetsByType(PetType.convertStringToPetType("Cat"), companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `update an owner for a pet`(){
        val pet = PetrCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), ownerId)
        val petId = petDao.createPet(pet)
        val newOwnerId = 4L
        assertEquals(1, petDao.updatePetOwner(petId, newOwnerId, companyId))
    }

    @Test
    fun `pet has an owner so don't update`(){
        val originOwnerId = 11L
        val pet = PetrCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), originOwnerId )
        val petId = petDao.createPet(pet)
        val newOwnerId = 12L
        assertEquals(0, petDao.updatePetOwner(petId, newOwnerId, companyId))
    }

    @Test
    fun `get pets by owner`(){
        val pet1 = PetrCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), 123L)
        petDao.createPet(pet1)
        val pet2 = PetrCreationRequest("Nessy", pet1.type, pet1.companyId, pet1.dateOfArrival, pet1.ownerId)
        petDao.createPet(pet2)
        val actual = petDao.getPetsByOwner(123L, companyId).size
        assertEquals(2, actual)
    }

    @Test
    fun `get pet by id`(){
        val pet = PetrCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), 123L)
        val petId = petDao.createPet(pet)
        val petById = petDao.getPetById(petId, companyId)
        val actual = PetData(petId, pet.name, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId )
        assertEquals(petById, actual)
    }

    @Test
    fun `count pets by type`(){
        val petType1 = PetType.convertStringToPetType("Dog")
        val petType2 = PetType.convertStringToPetType("Cat")

        val pet = PetrCreationRequest("Angie", petType1, companyId, Date.valueOf("2010-05-20"), ownerId)
        petDao.createPet(pet)

        val pet2 = PetrCreationRequest("Nessy", petType1, companyId, Date.valueOf("2010-05-20"), ownerId)
        petDao.createPet(pet2)

        val pet3 = PetrCreationRequest("Max", petType2, companyId, Date.valueOf("2010-05-20"), ownerId )
        petDao.createPet(pet3)

        val expectedCount = mapOf(petType1 to 2, petType2 to 1)
        val actualCount = petDao.countPetsByType(companyId)

        println("Expected Count: $expectedCount")
        println("Actual Count: $actualCount")

        assertEquals(expectedCount, actualCount)
    }

    @Test
    fun `update pets name`(){
        val pet = PetrCreationRequest("Kaia", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2024-09-13"), ownerId)
        val petId = petDao.createPet(pet)
        val newName = "Charlie"
        petDao.updatePetName(petId, newName, companyId)
        val expected = PetData(petId, newName, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId)
        val updatedPet = petDao.getPetById(petId, companyId)
        assertEquals(expected, updatedPet)
    }

    @Test
    fun `delete a pet`(){
        val pet = PetrCreationRequest("Max", PetType.convertStringToPetType("Cat"), companyId, Date.valueOf("2024-09-13"), ownerId )
        val petId = petDao.createPet(pet)
        petDao.deletePet(petId, companyId)
        assertEquals(null, petDao.getPetById(petId, companyId))
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }
}
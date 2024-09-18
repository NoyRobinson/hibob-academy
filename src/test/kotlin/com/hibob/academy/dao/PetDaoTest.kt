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
    private val petTable = PetTable.instance
    val companyId = 12L

    @Test
    fun `create a new pet that doesn't exist in the database`() {
        val pet = PetCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), ownerId = null )
        val petId = petDao.createPet(pet)
        val expected = listOf(PetData(petId, pet.name, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId))
        assertEquals(expected, petDao.getAllPets(companyId))
    }

    @Test
    fun `get pets by type when there is no pet of this type`() {
        val pet = PetCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), null)
        petDao.createPet(pet)
        val expected = emptyList<PetData>()
        val actual = petDao.getPetsByType(PetType.convertStringToPetType("Cat"), companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `get pets by type when there is a pet of this type`() {
        val pet1 = PetCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), null)
        petDao.createPet(pet1)
        val pet2 = PetCreationRequest("Max", PetType.convertStringToPetType("Cat"), companyId, Date.valueOf("2010-05-20"), null)
        val id2 = petDao.createPet(pet2)
        val expected = listOf(PetData(id2, pet2.name, pet2.type, pet2.companyId, pet2.dateOfArrival, pet2.ownerId))
        val actual = petDao.getPetsByType(PetType.convertStringToPetType("Cat"), companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `update an owner for a pet`(){
        val pet = PetCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), null)
        val petId = petDao.createPet(pet)
        val newOwnerId = 4L
        assertEquals(1, petDao.updatePetOwner(petId, newOwnerId, companyId))
    }

    @Test
    fun `pet has an owner so don't update`(){
        val pet = PetCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), 11L )
        val petId = petDao.createPet(pet)
        val newOwnerId = 12L
        assertEquals(0, petDao.updatePetOwner(petId, newOwnerId, companyId))
    }

    @Test
    fun `get pets by owner`(){
        val pet1Creation = PetCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), 123L)
        val id1 = petDao.createPet(pet1Creation)
        val pet1 = PetData(id1, pet1Creation.name, pet1Creation.type, pet1Creation.companyId, pet1Creation.dateOfArrival, pet1Creation.ownerId)

        val pet2Creation = PetCreationRequest("Nessy", pet1Creation.type, pet1Creation.companyId, pet1Creation.dateOfArrival, pet1Creation.ownerId)
        val id2 = petDao.createPet(pet2Creation)
        val pet2 = PetData(id2, pet2Creation.name, pet2Creation.type, pet2Creation.companyId, pet2Creation.dateOfArrival, pet2Creation.ownerId)

        val expected = listOf(pet1, pet2)
        val actual = petDao.getPetsByOwner(123L, companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `get pet by id`(){
        val pet = PetCreationRequest("Angie", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), 123L)
        val petId = petDao.createPet(pet)
        val petById = petDao.getPetById(petId, companyId)
        val actual = PetData(petId, pet.name, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId )
        assertEquals(petById, actual)
    }

    @Test
    fun `count pets by type`(){
        val petType1 = PetType.convertStringToPetType("Dog")
        val petType2 = PetType.convertStringToPetType("Cat")

        val pet = PetCreationRequest("Angie", petType1, companyId, Date.valueOf("2010-05-20"), null)
        petDao.createPet(pet)

        val pet2 = PetCreationRequest("Nessy", petType1, companyId, Date.valueOf("2010-05-20"), null)
        petDao.createPet(pet2)

        val pet3 = PetCreationRequest("Max", petType2, companyId, Date.valueOf("2010-05-20"), null)
        petDao.createPet(pet3)

        val expectedCount = mapOf(petType1 to 2, petType2 to 1)
        val actualCount = petDao.countPetsByType(companyId)

        println("Expected Count: $expectedCount")
        println("Actual Count: $actualCount")

        assertEquals(expectedCount, actualCount)
    }

    @Test
    fun `update pets name`(){
        val pet = PetCreationRequest("Kaia", PetType.convertStringToPetType("Dog"), companyId, Date.valueOf("2024-09-13"), null)
        val petId = petDao.createPet(pet)
        val newName = "Charlie"
        petDao.updatePetName(petId, newName, companyId)
        val expected = PetData(petId, newName, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId)
        val updatedPet = petDao.getPetById(petId, companyId)
        assertEquals(expected, updatedPet)
    }

    @Test
    fun `delete a pet`(){
        val pet = PetCreationRequest("Max", PetType.convertStringToPetType("Cat"), companyId, Date.valueOf("2024-09-13"), null)
        val petId = petDao.createPet(pet)
        petDao.deletePet(petId, companyId)
        assertEquals(null, petDao.getPetById(petId, companyId))
    }

    @Test
    fun `adopt multiple pets`(){
        val listOfPetsIds = mutableListOf<Long>()
        val ownerIdThatAdopts = 20L

        val pet1 = PetCreationRequest("Angie", PetType.convertStringToPetType("DOG"), companyId, Date.valueOf("2010-05-20"), 18L)
        val id1 = petDao.createPet(pet1)

        val pet2 = PetCreationRequest("Nessy", PetType.convertStringToPetType("DOG"), companyId, Date.valueOf("2010-05-20"), null)
        val id2 = petDao.createPet(pet2)

        val pet3 = PetCreationRequest("Max", PetType.convertStringToPetType("CAT"), companyId, Date.valueOf("2010-05-20"), null )
        val id3 = petDao.createPet(pet3)

        listOfPetsIds.add(id1)
        listOfPetsIds.add(id2)
        listOfPetsIds.add(id3)

        val actual = listOfPetsIds.associate { petId ->
            val success = petDao.updatePetOwner(petId, ownerIdThatAdopts, companyId)
            petId to (success == 1)
        }
        val expected = mapOf(id1 to false, id2 to true, id3 to true)
        assertEquals(expected, actual)
    }

    @Test
    fun `create multiple pets`(){
        val listOfPetsWithNoIds = mutableListOf<PetCreationRequest>()

        val pet1ToCreate = PetCreationRequest("Angie", PetType.convertStringToPetType("DOG"), companyId, Date.valueOf("2010-05-20"), 18L)
        val pet2ToCreate = PetCreationRequest("Nessy", PetType.convertStringToPetType("DOG"), companyId, Date.valueOf("2010-05-20"), null)
        val pet3ToCreate = PetCreationRequest("Max", PetType.convertStringToPetType("CAT"), companyId, Date.valueOf("2010-05-20"), null )

        petDao.createMultiplePets(listOf(pet1ToCreate, pet2ToCreate, pet3ToCreate))

        val petsCreated = petDao.getAllPets(companyId)
        petsCreated.forEach { pet ->
            val newPet = PetCreationRequest(pet.name, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId)
            listOfPetsWithNoIds.add(newPet)
        }
        assertEquals(listOf(pet1ToCreate, pet2ToCreate, pet3ToCreate) ,listOfPetsWithNoIds)
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(petTable).where(petTable.companyId.eq(companyId)).execute()
    }
}
package com.hibob.academy.dao

import com.hibob.academy.dao.PetType.Companion.convertStringToPetType
import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Date
import kotlin.random.Random
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException


@BobDbTest
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext){

    private val ownerDao = OwnerDao(sql)
    private val petDao = PetDao(sql)
    private val ownerTable = OwnerTable.instance
    private val petTable = PetTable.instance

    val companyId = Random.nextLong()

    @Test
    fun `create a new owner that doesn't exist in the database`() {
        val owner = OwnerCreationRequest("Noy", companyId, "123")
        val ownerId = ownerDao.createOwner(owner)
        val expected = listOf(OwnerData(ownerId, owner.name, owner.companyId, owner.employeeId))
        assertEquals(expected, ownerDao.getAllOwners(companyId))
    }

    @Test
    fun `create a new owner with an existing employee id`() {
        val owner1 = OwnerCreationRequest("Noy", companyId, "123")
        ownerDao.createOwner(owner1)
        val owner2 = OwnerCreationRequest("Tom", companyId, "123")
        assertThrows<RuntimeException> { ownerDao.createOwner(owner2) }
    }

    @Test
    fun `get owner information by pet id`(){
        val owner = OwnerCreationRequest("Noy", companyId, "123")
        val ownerId = ownerDao.createOwner(owner)
        val pet = PetCreationRequest("Angie", convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), ownerId)
        val petId = petDao.createPet(pet)
        val expected = OwnerData(ownerId, owner.name, owner.companyId, owner.employeeId)
        val actual = ownerDao.getOwnerByPetId(petId, companyId)
        assertEquals(expected, actual)
    }

    @Test
    fun `try get information of owner by pet id for a pet that doesnt have an owner`(){
        val pet = PetCreationRequest("Angie", convertStringToPetType("Dog"), companyId, Date.valueOf("2010-05-20"), null)
        val petId = petDao.createPet(pet)
        val actual = ownerDao.getOwnerByPetId(petId, companyId)
        assertNull(actual)
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(ownerTable).where(ownerTable.companyId.eq(companyId)).execute()
        sql.deleteFrom(petTable).where(petTable.companyId.eq(companyId)).execute()
    }
}
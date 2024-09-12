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
import org.junit.jupiter.api.assertThrows


@BobDbTest
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext){

    private val ownerDao = OwnerDao(sql)
    private val petDao = PetDao(sql)
    private val ownerTable = OwnerTable.instance
    private val petTable = PetTable.instance

    val companyId = Random.nextLong()

    @Test
    fun `create a new owner that doesn't exist in the database`() {
        val ownerName = "Noy"
        val ownerCompanyId = companyId
        val ownerEmployeeId = "123"
        val ownerId = ownerDao.createOwner(ownerName, ownerCompanyId, ownerEmployeeId)
        val expected = listOf(OwnerData(ownerId, ownerName, ownerCompanyId, ownerEmployeeId))
        assertEquals(expected, ownerDao.getOwner(ownerCompanyId))
    }

    @Test
    fun `create a new owner that exists in the database`() {
        val ownerName1 = "Noy"
        val ownerCompanyId1 = companyId
        val ownerEmployeeId1 = "123"

        ownerDao.createOwner(ownerName1, ownerCompanyId1, ownerEmployeeId1)
        val ownerName2 = "Tom"
        assertThrows<RuntimeException> { ownerDao.createOwner(ownerName2, ownerCompanyId1, ownerEmployeeId1) }
    }

    @Test
    fun `get owner information by pet id`(){
        val ownerName = "Noy"
        val ownerCompanyId = companyId
        val ownerEmployeeId = "123"
        val ownerId1 = ownerDao.createOwner(ownerName, ownerCompanyId, ownerEmployeeId)

        val petName = "Angie"
        val petTypeString = "Dog"
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")
        val petId = petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival, ownerId1)

        val expected = OwnerData(ownerId1, ownerName, ownerCompanyId, ownerEmployeeId)
        val actual = ownerDao.getOwnerByPetId(petId)
        assertEquals(expected, actual)
    }

    @Test
    fun `try get information of owner by pet id for a pet that doesnt have an owner`(){

        val petName = "Angie"
        val petTypeString = "Dog"
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")
        val petId = petDao.createPet(petName, petTypeString, petCompanyId, dateOfArrival, null)

        val actual = ownerDao.getOwnerByPetId(petId)
        assertEquals(null, actual)
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(ownerTable).where(ownerTable.companyId.eq(companyId)).execute()
        sql.deleteFrom(petTable).where(petTable.companyId.eq(companyId)).execute()
    }
}
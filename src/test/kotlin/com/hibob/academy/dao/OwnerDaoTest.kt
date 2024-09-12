package com.hibob.academy.dao

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
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext){

    private val ownerDao = OwnerDao(sql)
    private val table = OwnerTable.instance

    val companyId = Random.nextLong()
    val id = 1L

    @Test
    fun `create a new owner that doesn't exist in the database`() {
        val ownerName = "Noy"
        val ownerCompanyId = companyId
        val ownerEmployeeId = "123"
        ownerDao.createOwner(ownerName, ownerCompanyId, ownerEmployeeId)
        val expected = listOf(OwnerData(id, ownerName, ownerCompanyId, ownerEmployeeId))
        assertEquals(expected, ownerDao.getOwner(ownerCompanyId))
    }

    @Test
    fun `create a new owner that exists in the database`() {
        val ownerName1 = "Noy"
        val ownerCompanyId1 = companyId
        val ownerEmployeeId1 = "123"
        ownerDao.createOwner(ownerName1, ownerCompanyId1, ownerEmployeeId1)

        val ownerName2 = "Tom"
        ownerDao.createOwner(ownerName2, ownerCompanyId1, ownerEmployeeId1)

        val expected = listOf(OwnerData(id, ownerName1, ownerCompanyId1, ownerEmployeeId1))
        val actual = ownerDao.getOwner(ownerCompanyId1)
        assertEquals(expected, actual)
    }

    // fix 2 tests under
    @Test
    fun `get owner information by pet id`(){

        val ownerName = "Noy"
        val ownerCompanyId = companyId
        val ownerEmployeeId = "123"
        ownerDao.createOwner(id, ownerName, ownerCompanyId, ownerEmployeeId)

        val petId = 2L
        val petName = "Angie"
        val petTypeString = "Dog"
        val petType = PetType.convertStringToPetType(petTypeString)
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")
        createPet(petId, petName, petTypeString, petCompanyId, dateOfArrival, id)

        val expected = OwnerData(id, ownerName, ownerCompanyId, ownerEmployeeId)
        val actual = ownerDao.getOwnerByPetId(petId)
        assertEquals(expected, actual)
    }

    fun `try get information of owner by pet id for a pet that doesnt have an owner`(){

        val petId = 2L
        val petName = "Angie"
        val petTypeString = "Dog"
        val petType = PetType.convertStringToPetType(petTypeString)
        val petCompanyId = companyId
        val dateOfArrival = Date.valueOf("2010-05-20")
        createPet(petId, petName, petTypeString, petCompanyId, dateOfArrival, OL)

        val actual = ownerDao.getOwnerByPetId(petId)
        assertEquals(null, actual)
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }
}
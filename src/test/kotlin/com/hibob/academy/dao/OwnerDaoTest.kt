package com.hibob.academy.dao

import com.hibob.academy.utils.BobDbTest

import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.random.Random


@BobDbTest
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext){

    private val ownerDao = OwnerDao(sql)
    private val table = ownerTable.instance
    val companyId = Random.nextLong()

    @Test
    fun `create a new owner that doesn't exist in the database`() {
        val ownerName = "Noy"
        val ownerCompanyId = companyId
        val ownerEmployeeId = "123"
        ownerDao.createOwner(ownerName, ownerCompanyId, ownerEmployeeId)
        val expected = listOf(OwnerData(ownerName, ownerCompanyId, ownerEmployeeId))
        assertEquals(expected, ownerDao.getOwner())
    }

    @Test
    fun `create a new owner that exists in the database`() {
        val ownerName_1 = "Noy"
        val ownerCompanyId_1 = companyId
        val ownerEmployeeId_1 = "123"
        ownerDao.createOwner(ownerName_1, ownerCompanyId_1, ownerEmployeeId_1)

        val ownerName_2 = "Tom"
        ownerDao.createOwner(ownerName_2, ownerCompanyId_1, ownerEmployeeId_1)

        val actual = ownerDao.getOwner().size
        assertEquals(1, actual)
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }
}
package com.hibob.academy.service

import com.hibob.academy.dao.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class OwnerServiceTest{
    private val ownerDao = mock<OwnerDao>{}
    private val ownerService = OwnerService(ownerDao)

    @Test
    fun `createOwner should return the owner ID`(){
        val ownerToCreate = OwnerCreationRequest("Noy", 14L, "123")
        whenever(ownerDao.createOwner(ownerToCreate)).thenReturn(1L)
        val ownerId = ownerService.createOwner(ownerToCreate)
        verify(ownerDao).createOwner(ownerToCreate)
        assertEquals(1L, ownerId)
    }

    @Test
    fun `createOwner should handle the exception from ownerDao`() {
        val ownerToCreate = OwnerCreationRequest("Noy", 14L, "123")
        whenever(ownerDao.createOwner(ownerToCreate)).thenThrow(RuntimeException("Failed to insert owner and retrieve ID"))
        val exception = assertThrows<RuntimeException> { ownerService.createOwner(ownerToCreate) }
        assertEquals("Failed to insert owner and retrieve ID", exception.message)
    }

    @Test
    fun `getAllOwners should return a list of owners when there are owners`() {
        val expectedOwners = listOf(
            OwnerData(1L, "Noy", 14L, "123"),
            OwnerData(2L, "Tom", 14L, "456")
        )
        whenever(ownerDao.getAllOwners(14L)).thenReturn(expectedOwners)
        val actualOwners = ownerService.getAllOwners(14L)
        verify(ownerDao).getAllOwners(14L)
        assertEquals(expectedOwners, actualOwners)
    }

    @Test
    fun `getAllOwners should return an empty list when there are no owners`() {
        whenever(ownerDao.getAllOwners(14L)).thenReturn(emptyList())
        val actualOwners = ownerService.getAllOwners(14L)
        verify(ownerDao).getAllOwners(14L)
        assertEquals(emptyList<PetData>(), actualOwners)
    }

    @Test
    fun `getOwnerByPetId should return information about the owner of the pet`(){
        val expectedOwner = OwnerData(1L, "Noy", 14L, "123")
        whenever(ownerDao.getOwnerByPetId(1L, 14L)).thenReturn(expectedOwner)
        val actualOwner = ownerService.getOwnerByPetId(1L, 14L)
        assertEquals(expectedOwner, actualOwner)
        verify(ownerDao).getOwnerByPetId(1L, 14L)
    }

    @Test
    fun `getOwnerByPetId should return null when the pet doesn't have an owner`(){
        whenever(ownerDao.getOwnerByPetId(1L, 14L)).thenReturn(null)
        val result = ownerService.getOwnerByPetId(1L, 14L)
        assertNull(result)
        verify(ownerDao).getOwnerByPetId(1L, 14L)
    }
}
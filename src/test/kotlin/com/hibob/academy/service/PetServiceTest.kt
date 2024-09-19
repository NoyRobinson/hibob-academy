package com.hibob.academy.service

import com.hibob.academy.dao.PetCreationRequest
import com.hibob.academy.dao.PetDao
import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.sql.Date

class PetServiceTest{
    private val petDao = mock<PetDao>{}
    private val petService = PetService(petDao)

    @Test
    fun `createPet should return the pet ID`(){
        val petToCreate = PetCreationRequest("Angie", PetType.convertStringToPetType("DOG"), 14L, Date.valueOf("2010-05-20"), null)
        whenever(petDao.createPet(petToCreate)).thenReturn(1L)
        val petId = petService.createPet(petToCreate)
        verify(petDao).createPet(petToCreate)
        assertEquals(1L, petId)
    }

    @Test
    fun `createPet should handle the exception from petDao`() {
        val petToCreate = PetCreationRequest("Angie", PetType.convertStringToPetType("DOG"), 14L, Date.valueOf("2010-05-20"), null)
        whenever(petDao.createPet(petToCreate)).thenThrow(RuntimeException("Failed to insert pet and retrieve ID"))
        val exception = assertThrows<RuntimeException> { petService.createPet(petToCreate) }
        assertEquals("Failed to insert pet and retrieve ID", exception.message)
    }

    @Test
    fun `getPetsByType should return a list of pets of the same type`() {
        val expectedPets = listOf(
            PetData(1L, "Angie", PetType.DOG, 14L, Date.valueOf("2024-09-18"), null),
            PetData(2L, "Nessy", PetType.DOG, 14L, Date.valueOf("2024-09-18"), null)
        )
        whenever(petDao.getPetsByType(PetType.DOG, 14L)).thenReturn(expectedPets)
        val actualPets = petService.getPetsByType(PetType.DOG, 14L)
        verify(petDao).getPetsByType(PetType.DOG, 14L)
        assertEquals(expectedPets, actualPets)
    }

    @Test
    fun `getPetById should return the pet when it exists`() {
        val expectedPet = PetData(1L, "Angie", PetType.DOG, 14L, Date.valueOf("2024-09-18"), null)
        whenever(petDao.getPetById(1L, 14L)).thenReturn(expectedPet)
        val actualPet = petService.getPetById(1L, 14L)
        verify(petDao).getPetById(1L, 14L)
        assertEquals(expectedPet, actualPet)
    }

    @Test
    fun `getPetById should throw an exception when the pet does not exist`() {
        whenever(petDao.getPetById(1L, 14L)).thenReturn(null)
        val exception = assertThrows<Exception> { petService.getPetById(1L, 14L) }
        assertEquals("Pet not found", exception.message)
    }

    @Test
    fun `getAllPets should return a list of pets when there are pets`() {
        val expectedPets = listOf(
            PetData(1L, "Angie", PetType.DOG, 14L, Date.valueOf("2024-09-18"), null),
            PetData(2L, "Max", PetType.CAT, 14L, Date.valueOf("2024-09-18"), null)
        )
        whenever(petDao.getAllPets(14L)).thenReturn(expectedPets)
        val actualPets = petService.getAllPets(14L)
        verify(petDao).getAllPets(14L)
        assertEquals(expectedPets, actualPets)
    }

    @Test
    fun `getAllPets should return an empty list when there are no pets`() {
        whenever(petDao.getAllPets(14L)).thenReturn(emptyList())
        val actualPets = petService.getAllPets(14L)
        verify(petDao).getAllPets(14L)
        assertEquals(emptyList<PetData>(), actualPets)
    }

    @Test
    fun `updatePetOwner should successfully update the pet owner`() {
        whenever(petDao.updatePetOwner(1L, 15L, 14L)).thenReturn(1)
        petService.updatePetOwner(1L, 15L, 14L)
        verify(petDao).updatePetOwner(1L, 15L, 14L)
    }

    @Test
    fun `updatePetOwner should throw an exception when can't update the pets owner`() {
        whenever(petDao.updatePetOwner(1L, 15L, 14L)).thenReturn(0)
        val exception = assertThrows<IllegalStateException> { petService.updatePetOwner(1L, 15L, 14L) }
        assertEquals("Pet doesn't exist or pet already has an owner", exception.message)
    }

    @Test
    fun `updatePetName should update the pets name when a new name is provided`() {
        petService.updatePetName(1L, "Angie", 12L)
        verify(petDao).updatePetName(1L, "Angie", 12L)
    }

    @Test
    fun `updatePetName should not call the dao function when no new name is provided`(){
        petService.updatePetName(1L, null, 12L)
        verify(petDao, never()).updatePetName(eq(1L), anyString(), eq(12L))
    }

    @Test
    fun `deletePet should successfully delete the pet`() {
        whenever(petDao.deletePet(1L, 14L)).thenReturn(1)
        petService.deletePet(1L, 14L)
        verify(petDao).deletePet(1L, 14L)
    }

    @Test
    fun `getPetsByOwner should return a list of pets for the given owner`() {
        val expectedPets = listOf(
            PetData(1L, "Angie", PetType.DOG, 14L, Date.valueOf("2024-09-18"), 18L),
            PetData(2L, "Max", PetType.CAT, 14L, Date.valueOf("2022-09-18"), 18L)
        )
        whenever(petDao.getPetsByOwner(18L, 14L)).thenReturn(expectedPets)
        val actualPets = petService.getPetsByOwner(18L, 14L)
        assertEquals(expectedPets, actualPets)
        verify(petDao).getPetsByOwner(18L, 14L)
    }

    @Test
    fun `getPetsByOwner should return an empty list when no pets are found for the owner`() {
        val expectedPets = emptyList<PetData>()
        whenever(petDao.getPetsByOwner(18L, 14L)).thenReturn(expectedPets)
        val actualPets = petService.getPetsByOwner(18L, 14L)
        assertEquals(expectedPets, actualPets)
        verify(petDao).getPetsByOwner(18L, 14L)
    }

    @Test
    fun `countPetsByType should return a map of pet types and the amount of pets of that type`() {
        val expected = mapOf(PetType.DOG to 2, PetType.CAT to 1)
        whenever(petDao.countPetsByType(14L)).thenReturn(expected)
        val actual = petService.countPetsByType(14L)
        assertEquals(expected, actual)
        verify(petDao).countPetsByType(14L)
    }

    @Test
    fun `adoptPets should return a map of pet IDs and if they were adopted`() {
        val petsIds = listOf(1L, 2L, 3L)
        whenever(petDao.updatePetOwner(1L, 18L, 14L)).thenReturn(1)
        whenever(petDao.updatePetOwner(2L, 18L, 14L)).thenReturn(1)
        whenever(petDao.updatePetOwner(3L, 18L, 14L)).thenReturn(0)
        val result = petService.adoptPets(18L, 14L, petsIds)
        val expected = mapOf(
            1L to true,
            2L to true,
            3L to false
        )
        assertEquals(expected, result)
    }

    @Test
    fun `createMultiplePets should creates multiple lists`(){
        val pet1 = PetCreationRequest("Angie", PetType.convertStringToPetType("DOG"), 14L, Date.valueOf("2024-09-18"), null)
        val pet2 = PetCreationRequest("Max", PetType.convertStringToPetType("CAT"), 14L, Date.valueOf("2024-09-18"), null)
        val petsToCreate = listOf(pet1, pet2)
        petService.createMultiplePets(petsToCreate)
        verify(petDao).createMultiplePets(petsToCreate)
    }
}
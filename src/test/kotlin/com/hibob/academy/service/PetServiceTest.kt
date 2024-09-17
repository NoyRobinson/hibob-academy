package com.hibob.academy.service

import com.hibob.academy.dao.PetDao
import org.junit.jupiter.api.Test
import org.mockito.Mockito.never
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class PetServiceTest{
    private val petDao = mock<PetDao>{}
    private val petService = PetService(petDao)

    @Test
    fun `updatePetName should update the pets name when a new name is provided`() {
        petService.updatePetName(1L, "Angie", 12L)
        verify(petDao).updatePetName(1L, "Angie", 12L)
    }

    @Test
    fun `updatePetName should not call the dao function when no new name is provided`(){
        petService.updatePetName(1L, null, 12L)
        verify(petDao, never()).updatePetName(1L, null, 12L)
    }
}
package com.hibob.academy.service

import com.hibob.academy.dao.*
import jakarta.inject.Inject
import org.springframework.stereotype.Service

@Service
class PetService @Inject constructor(private val petDao: PetDao) {

    fun createPet(pet: PetrCreationRequest): Long =
        petDao.createPet(pet)

    fun getPetsByType(petType: PetType, companyId: Long) =
        petDao.getPetsByType(petType, companyId)

    fun getPetById(petId: Long, companyId: Long): PetData {
        val pet = petDao.getPetById(petId, companyId)
        pet?.let{
            return pet
        } ?: throw Exception("Pet not found")
    }

    fun getAllPets(companyId: Long) =
        petDao.getAllPets(companyId)

    fun updatePetOwner(petId: Long, ownerId: Long, companyId: Long) {
        val success = petDao.updatePetOwner(petId, ownerId, companyId) > 0
        if(success) return
        throw IllegalStateException("Pet doesn't exist or pet already has an owner")
    }

    fun updatePetName(petId: Long, newName: String?, companyId: Long) {
        newName?.let {
            petDao.updatePetName(petId, newName, companyId)
        } ?: return
    }

    fun deletePet(petId: Long, companyId: Long) =
        petDao.deletePet(petId, companyId)
}
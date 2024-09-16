package com.hibob.academy.service

import com.hibob.academy.dao.*
import jakarta.inject.Inject
import org.springframework.stereotype.Service

@Service
class PetService @Inject constructor(private val petDao: PetDao) {

    fun createPet(pet: PetrCreationRequest): Long = petDao.createPet(pet)

    fun getPetsByType(petType: PetType, companyId: Long) = petDao.getPetsByType(petType, companyId)

    fun getPetById(petId: Long, companyId: Long): PetData? = petDao.getPetById(petId, companyId)

    fun getAllPets(companyId: Long) = petDao.getAllPets(companyId)

    fun updatePetOwner(petId: Long, ownerId: Long, companyId: Long) {
        val success = petDao.updatePetOwner(petId, ownerId, companyId)
        if(success == 1) return
        throw IllegalStateException("Pet doesn't exist or pet already has an owner")
    }

    fun updatePetName(petId: Long, newName: String, companyId: Long) = petDao.updatePetName(petId, newName, companyId)

    fun deletePet(petId: Long, companyId: Long) = petDao.deletePet(petId, companyId)
}
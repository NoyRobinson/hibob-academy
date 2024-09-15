package com.hibob.academy.service

import com.hibob.academy.dao.Pet
import com.hibob.academy.dao.PetDao
import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetType
import jakarta.inject.Inject
import org.springframework.stereotype.Service

@Service
class PetService @Inject constructor(private val petDao: PetDao) {

    fun createPet(pet: Pet): Long = petDao.createPet(pet)
    fun getPetsByType(petType: PetType, companyId: Long) = petDao.getPetsByType(petType, companyId)
    fun getPetById(petId: Long, companyId: Long): PetData? = petDao.getPetById(petId, companyId)
    fun getAllPets(companyId: Long) = petDao.getAllPets(companyId)
    fun updatePetOwner(pet: PetData, petId: Long, ownerId: Long) = petDao.updatePetOwner(pet, petId, ownerId)
    fun updatePetName(petId: Long, newName: String, companyId: Long) = petDao.updatePetName(petId, newName, companyId)
    fun deletePet(petId: Long, companyId: Long) = petDao.deletePet(petId, companyId)
}
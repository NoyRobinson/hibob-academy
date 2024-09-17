package com.hibob.academy.service

import com.hibob.academy.dao.OwnerCreationRequest
import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.dao.OwnerData
import jakarta.inject.Inject
import org.springframework.stereotype.Service

@Service
class OwnerService @Inject constructor(private val ownerDao: OwnerDao) {

    fun createOwner(owner: OwnerCreationRequest) =
        ownerDao.createOwner(owner)

    fun getAllOwners(companyId: Long) =
        ownerDao.getAllOwners(companyId)

    fun getOwnerByPetId(petId: Long, companyId: Long): OwnerData {
        val ownerInfo = ownerDao.getOwnerByPetId(petId, companyId)
        ownerInfo?.let{
            return ownerInfo
        } ?: throw IllegalArgumentException("Pet doesn't have an owner")
    }
}
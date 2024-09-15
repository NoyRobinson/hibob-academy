package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import jakarta.inject.Inject
import org.springframework.stereotype.Service


@Service
class OwnerService @Inject constructor(private val ownerDao: OwnerDao) {

    fun createOwner(ownerName: String, companyId: Long, employeeId: String) = ownerDao.createOwner(ownerName, companyId, employeeId)
    fun getOwner(companyId: Long) = ownerDao.getOwner(companyId)
    fun getOwnerByPetId(petId: Long) = ownerDao.getOwnerByPetId(petId)
}
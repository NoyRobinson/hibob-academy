package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import org.springframework.stereotype.Component


@Component
class OwnerService(private val ownerDao: OwnerDao) {

    fun createOwner(ownerName: String, companyId: Long, employeeId: String) = ownerDao.createOwner(ownerName, companyId, employeeId)
    fun getOwner(companyId: Long) = ownerDao.getOwner(companyId)
    fun getOwnerByPetId(petId: Long) = ownerDao.getOwnerByPetId(petId)
}
package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.EmployeeDao
import com.hibob.academy.employeeFeedback.dao.EmployeeInfo
import org.springframework.stereotype.Service

@Service
class EmployeeService(private val employeeDao: EmployeeDao) {
    fun getEmployeeById(id: Int): EmployeeInfo? {
        return employeeDao.getEmployeeById(id)
    }
}
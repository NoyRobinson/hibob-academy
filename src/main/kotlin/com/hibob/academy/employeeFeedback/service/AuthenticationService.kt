package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.EmployeeDao
import com.hibob.academy.employeeFeedback.dao.LoggedInEmployeeInfo
import com.hibob.academy.employeeFeedback.dao.LoginParams
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Component
class AuthenticationService(private val employeeDao: EmployeeDao) {

    companion object{
        const val SECRET_KEY = "kgjhdkjgbnsdjgbejr43775384957934579fhdjbdjsnks"
    }

    fun authenticate(id: Int, firstName: String, lastName: String): LoggedInEmployeeInfo {
        val loginParams = LoginParams(id, firstName, lastName)
        val loggedInEmployeeInfo = employeeDao.findEmployeeByLoginParams(loginParams)
            ?: throw Exception("Authentication failed")

        return LoggedInEmployeeInfo(id = loggedInEmployeeInfo.id, firstName = loggedInEmployeeInfo.firstName,
            lastName = loggedInEmployeeInfo.lastName, companyId = loggedInEmployeeInfo.companyId,
            role = loggedInEmployeeInfo.role
        )
    }

    fun createJwtToken(loggedInEmployee: LoggedInEmployeeInfo): String {
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("id", loggedInEmployee.id)
            .claim("firstName", loggedInEmployee.firstName)
            .claim("lastName", loggedInEmployee.lastName)
            .claim("companyId", loggedInEmployee.companyId)
            .claim("role", loggedInEmployee.role)
            .setExpiration(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }
}
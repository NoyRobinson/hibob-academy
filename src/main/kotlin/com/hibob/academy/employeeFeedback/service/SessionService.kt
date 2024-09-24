package com.hibob.academy.employeeFeedback.service

//import com.hibob.academy.resource.EmployeeLogin
//import io.jsonwebtoken.Jwts
//import io.jsonwebtoken.SignatureAlgorithm
//import org.springframework.stereotype.Component
//import java.time.LocalDateTime
//import java.time.ZoneOffset
//import java.util.*
//
//@Component
//class SessionService {
//
//    companion object{
//        const val SECRET_KEY = "kgjhdkjgbnsdjgbejr43775384957934579fhdjbdjsnks"
//    }
//
//    fun createJwtToken(employee: EmployeeLogin): String {
//
//        return Jwts.builder()
//            .setHeaderParam("typ", "JWT")
//            .claim("id", employee.id)
//            .claim("firstName", employee.firstName)
//            .claim("lastName", employee.lastName)
//            .setExpiration(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
//            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//            .compact()
//    }
//}
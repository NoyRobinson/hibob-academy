package com.hibob.academy.service

import com.hibob.academy.resource.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Component
class SessionService {

    companion object{
        const val SECRET_KEY = "kgjhdkjgbnsdjgbejr43775384957934579fhdjbdjsnks"
    }

    fun createJwtToken(user: User): String {

        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("username", user.username)
            .claim("email", user.email)
            .claim("isAdmin", user.isAdmin)
            .setExpiration(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }
}
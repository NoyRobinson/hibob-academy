package com.hibob.academy.filters

import com.hibob.academy.service.SessionService.Companion.SECRET_KEY
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.springframework.stereotype.Component

@Component
@Provider
class AuthenticationFilter: ContainerRequestFilter {

    override fun filter(requestContext: ContainerRequestContext) {

        if(requestContext.uriInfo.path == "session/login") return

        val cookies = requestContext.cookies
        val jwtCookie = cookies["JWT"]?.value

        val claims = verify(jwtCookie)

        if(claims == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build())
        }
    }

    fun verify(cookie: String?): Jws<Claims>? =
        cookie?.let {
            try{
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(it)
            } catch (e: Exception) {
                null
            }
        }
}

package com.hibob.academy.filters

import com.hibob.academy.service.SessionService.Companion.SECRET_KEY
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.springframework.stereotype.Component

@Component
@Provider
class AuthenticationFilter: ContainerRequestFilter {

    companion object {
        const val LOGIN_PATH: String = "session/login"
        const val COOKIE: String = "JWT"
    }

    override fun filter(requestContext: ContainerRequestContext) {

        if(requestContext.uriInfo.path == LOGIN_PATH) return

        val cookies = requestContext.cookies
        val jwtCookie = cookies[COOKIE]?.value

        verify(jwtCookie)
    }

    fun verify(cookie: String?): Jws<Claims>? =
        cookie?.let {
            try{
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(it)
            } catch (e: Exception) {
                throw WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Cookie").build())
            }
        }
}

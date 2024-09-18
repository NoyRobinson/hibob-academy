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
        const val LOGIN_PATH: String = "api/login"
        const val COOKIE_NAME = "JWT"
    }

    override fun filter(requestContext: ContainerRequestContext) {

        if(requestContext.uriInfo.path == LOGIN_PATH) return

        val cookies = requestContext.cookies
        val jwtCookie = cookies[COOKIE_NAME]?.value

        verify(jwtCookie)
    }

    fun verify(cookie: String?) {
        if (cookie.isNullOrEmpty())
            throw WebApplicationException(
                Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Cookie").build()
            )

        cookie.let {
            try {
                Jwts.parser().setSigningKey(SECRET_KEY)
            } catch (e: Exception) {
                throw WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Cookie").build()
                )
            }
        }
    }
}

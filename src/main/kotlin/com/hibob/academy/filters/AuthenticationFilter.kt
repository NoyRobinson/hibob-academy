package com.hibob.academy.filters

import com.hibob.academy.service.SessionService.Companion.SECRET_KEY
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
        if (requestContext.uriInfo.path == LOGIN_PATH) return

        val cookies = requestContext.cookies
        val jwtCookie = cookies[COOKIE_NAME]?.value

        verify(jwtCookie, requestContext)
    }

    fun verify(cookie: String?, requestContext: ContainerRequestContext) {
        if (cookie.isNullOrEmpty())
            throw WebApplicationException(
                Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Cookie").build()
            )

        cookie.let {
            try {
                val claims = Jwts.parser()
                                    .setSigningKey(SECRET_KEY)
                                    .parseClaimsJws(cookie)
                                    .body
                requestContext.setProperty("employeeId", claims["id"])
                requestContext.setProperty("companyId", claims["companyId"])
                requestContext.setProperty("role", claims["role"])

            } catch (e: Exception) {
                throw WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Cookie").build()
                )
            }
        }
    }
}

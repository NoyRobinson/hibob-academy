package com.hibob.academy.resource

import com.hibob.academy.service.SessionService
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
@Produces(MediaType.APPLICATION_JSON)
@Path("/session")
class SessionResource(private val sessionService: SessionService) {

    companion object {
        const val COOKIE_NAME = "JWT"
    }

    @POST
    @Path("/login")
    fun login(@RequestBody user: User): Response {
        Response.ok().build()
        val cookie = NewCookie.Builder(COOKIE_NAME).value(sessionService.createJwtToken(user)).build()
        return Response.ok().cookie(cookie).build()
    }

    @GET
    @Path("/getUserName")
    fun getUserName(): Response {
        return Response.ok("ok").build()
    }
}


data class User(
    val username: String,
    val email: String,
    val isAdmin: Boolean
)
package com.hibob.academy.resource

import com.hibob.academy.service.SessionService
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
@Path("/api")
class SessionResource(private val sessionService: SessionService) {

    companion object {
        const val COOKIE_NAME = "JWT"
    }

    @POST
    @Path("/login")
    fun login(@RequestBody employee: Employee): Response {
        Response.ok().build()
        val cookie = NewCookie.Builder(COOKIE_NAME).value(sessionService.createJwtToken(employee)).path("/api/").build()
        return Response.ok().cookie(cookie).build()
    }
}


data class Employee(
    val id: Int,
    val firstName: String,
    val lastName: String
)
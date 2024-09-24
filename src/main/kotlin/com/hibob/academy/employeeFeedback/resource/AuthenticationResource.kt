package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.service.AuthenticationService
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
class SessionResource(private val authenticationService: AuthenticationService) {
    companion object {
        const val COOKIE_NAME = "JWT"
    }

    @POST
    @Path("/login")
    fun login(@RequestBody employeeLoginRequest: EmployeeLoginRequest): Response {
        val employee = authenticationService.authenticate(employeeLoginRequest.id,
                            employeeLoginRequest.firstName, employeeLoginRequest.lastName)
        val cookie = NewCookie.Builder(COOKIE_NAME)
                        .value(authenticationService.createJwtToken(employee))
                        .path("/api/")
                        .build()
        return Response.ok().cookie(cookie).build()
    }
}

data class EmployeeLoginRequest(
    val id: Int,
    val firstName: String,
    val lastName: String,
)
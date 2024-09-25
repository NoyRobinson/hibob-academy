package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.AnonymityType.Companion.convertStringToAnonymityType
import com.hibob.academy.employeeFeedback.dao.FeedbackSubmitRequest
import com.hibob.academy.employeeFeedback.dao.ResponseSubmitRequest
import com.hibob.academy.employeeFeedback.dao.RoleType
import com.hibob.academy.employeeFeedback.service.AuthenticatedUsersService
import com.hibob.academy.employeeFeedback.service.ResponseService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
@Path("/api/response")
@Produces(MediaType.APPLICATION_JSON)
class ResponseResource(private val responseService: ResponseService, private val authenticatedUsersService: AuthenticatedUsersService) {
    @POST
    @Path("/feedbackId/{feedbackId}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun submitResponse(@RequestBody responseRequest: ResponseSubmitRequest, @PathParam ("feedbackId") feedbackId: Int, @Context request: ContainerRequestContext): Response {
        val reviewerId = authenticatedUsersService.getLoggedInEmployeeId(request)
        val companyId = authenticatedUsersService.getLoggedInCompanyId(request)
        val role = authenticatedUsersService.getLoggedInRole(request)
        val validRoles = listOf(RoleType.HR)

        authenticatedUsersService.validateRole(role, validRoles)

        val success = responseService.submitResponse(feedbackId, reviewerId, responseRequest.response, companyId)

        if (success) return Response.ok("Response created Successfully").build()

        return Response.status(Response.Status.BAD_REQUEST).entity("Unable to create response").build()
    }
}

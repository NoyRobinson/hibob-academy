package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.AnonymityType.Companion.convertStringToAnonymityType
import com.hibob.academy.employeeFeedback.dao.FeedbackSubmitRequest
import com.hibob.academy.employeeFeedback.dao.RoleType
import com.hibob.academy.employeeFeedback.dao.RoleType.Companion.convertStringToRoleType
import jakarta.ws.rs.core.Response
import com.hibob.academy.employeeFeedback.service.FeedbackService
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody


@Controller
@Path("/api/feedback")
@Produces(MediaType.APPLICATION_JSON)
class FeedbackResource(private val feedbackService: FeedbackService) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun submitFeedback(@RequestBody feedbackRequest: FeedbackSubmitRequest, @Context request: ContainerRequestContext): Response {
        val employeeId = getLoggedInEmployeeId(request)
        val companyId = getLoggedInCompanyId(request)
        val anonymity = convertStringToAnonymityType(feedbackRequest.anonymity)

        val success = feedbackService.submitFeedback(employeeId, companyId, anonymity, feedbackRequest.feedback)

        if (success) return Response.ok("Feedback created Successfully").build()

        return Response.status(Response.Status.BAD_REQUEST).entity("Unable to create feedback").build()
    }

    @GET
    @Path("/allFeedback")
    fun viewFeedback(@Context request: ContainerRequestContext): Response {
        val employeeId = getLoggedInEmployeeId(request)
        val companyId = getLoggedInCompanyId(request)
        val role = getLoggedInRole(request)

        if (!validateRole(role)) return Response.status(Response.Status.UNAUTHORIZED).build()

        val allFeedback = feedbackService.viewAllSubmittedFeedback(employeeId, companyId)

        return Response.ok(allFeedback).build()
    }

    @GET
    @Path("/checkStatus")
    fun checkFeedbackStatus(@QueryParam ("feedbackId") feedbackId: Int?, @Context request: ContainerRequestContext): Response {
        val employeeId = getLoggedInEmployeeId(request)
        val companyId = getLoggedInCompanyId(request)

        feedbackId?.let {

            return Response.ok(feedbackService.viewStatusOfMyFeedback(employeeId, companyId, feedbackId)).build()

        } ?: return Response.ok(feedbackService.viewStatusesOfMyFeedback(employeeId, companyId)).build()
    }

    fun getLoggedInEmployeeId(@Context request: ContainerRequestContext) : Int {
        val loggedInEmployeeId = request.getProperty("employeeId") as? Int
            ?: throw WebApplicationException("Employee id not found", Response.Status.UNAUTHORIZED)

        return loggedInEmployeeId
    }

    fun getLoggedInCompanyId(@Context request: ContainerRequestContext) : Int {
        val companyId = request.getProperty("companyId") as? Int
            ?: throw WebApplicationException("Company id not found", Response.Status.UNAUTHORIZED)

        return companyId
    }

    fun getLoggedInRole(@Context request: ContainerRequestContext) : RoleType {
        val role = request.getProperty("role") as? String
            ?: throw WebApplicationException("Role not found", Response.Status.UNAUTHORIZED)

        return convertStringToRoleType(role)
    }

    fun validateRole(role: RoleType): Boolean {
        return role == RoleType.HR || role == RoleType.ADMIN
    }
}

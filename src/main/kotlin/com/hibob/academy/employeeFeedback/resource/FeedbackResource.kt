package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.AnonymityType.Companion.convertStringToAnonymityType
import com.hibob.academy.employeeFeedback.dao.FeedbackSubmitRequest
import com.hibob.academy.employeeFeedback.dao.RoleType
import com.hibob.academy.employeeFeedback.service.AuthenticatedUsersService
import jakarta.ws.rs.core.Response
import com.hibob.academy.employeeFeedback.service.FeedbackService
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody


@Controller
@Path("/api/feedback")
@Produces(MediaType.APPLICATION_JSON)
class FeedbackResource(private val feedbackService: FeedbackService, private val authenticatedUsersService: AuthenticatedUsersService) {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun submitFeedback(@RequestBody feedbackRequest: FeedbackSubmitRequest, @Context request: ContainerRequestContext): Response {
        val employeeId = authenticatedUsersService.getLoggedInEmployeeId(request)
        val companyId = authenticatedUsersService.getLoggedInCompanyId(request)
        val anonymity = convertStringToAnonymityType(feedbackRequest.anonymity)

        val success = feedbackService.submitFeedback(employeeId, companyId, anonymity, feedbackRequest.feedback)

        if (success) return Response.ok("Feedback created Successfully").build()

        return Response.status(Response.Status.BAD_REQUEST).entity("Unable to create feedback").build()
    }

    @GET
    @Path("/allFeedback")
    fun viewFeedback(@Context request: ContainerRequestContext): Response {
        val employeeId = authenticatedUsersService.getLoggedInEmployeeId(request)
        val companyId = authenticatedUsersService.getLoggedInCompanyId(request)
        val role = authenticatedUsersService.getLoggedInRole(request)
        val validRoles = listOf(RoleType.ADMIN, RoleType.HR)

        authenticatedUsersService.validateRole(role, validRoles)

        val allFeedback = feedbackService.viewAllSubmittedFeedback(employeeId, companyId)

        return Response.ok(allFeedback).build()
    }

    @GET
    @Path("/checkStatus")
    fun checkFeedbackStatus(@QueryParam ("feedbackId") feedbackId: Int?, @Context request: ContainerRequestContext): Response {
        val employeeId = authenticatedUsersService.getLoggedInEmployeeId(request)
        val companyId = authenticatedUsersService.getLoggedInCompanyId(request)

        val statusOfMyFeedback = feedbackService.viewStatusOfMyFeedback(employeeId, companyId, feedbackId)

        return Response.ok(statusOfMyFeedback).build()
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/feedbackId/{feedbackId}/markReviewed")
    fun markReviewedO(@RequestBody reviewed: Boolean, @PathParam("feedbackId") feedbackId: Int, @Context request: ContainerRequestContext): Response {
        val employeeId = authenticatedUsersService.getLoggedInEmployeeId(request)
        val companyId = authenticatedUsersService.getLoggedInCompanyId(request)
        val role = authenticatedUsersService.getLoggedInRole(request)
        val validRoles = listOf(RoleType.HR)

        authenticatedUsersService.validateRole(role, validRoles)
        feedbackService.changeToReviewedOrUnreviewed(feedbackId, companyId, employeeId, reviewed)

        return Response.ok("Changed successfully").build()
    }

}

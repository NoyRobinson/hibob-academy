package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.FeedbackSubmitRequest
import com.hibob.academy.employeeFeedback.dao.Response
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.*
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
        val LoggedInEmployeeId = request.getProperty("id") as? Int
        val success = feedbackService.submitFeedback(LoggedInEmployeeId, feedbackRequest.anonymity, feedbackRequest.feedback)
        if (success)
            return Response.ok("Feedback created Successfully").build()
        return Response.status(Response.Status.BAD_REQUEST).build()
    }

    //    @GET
//    @Path("company/{companyId}/getOwners")
//    fun getAllOwners(@PathParam("companyId") companyId: Long): Response {
//        val owner = ownerService.getAllOwners(companyId)
//        return Response.ok(owner).build()
//    }

}



//
//    @GET
//    @Path("company/{companyId}/{petId}/ownerInformation")
//    fun getOwnerForPet(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long): Response {
//        val ownerInfo = ownerService.getOwnerByPetId(petId, companyId)
//        return Response.ok(ownerInfo).build()
//    }



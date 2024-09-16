package com.hibob.academy.resource

import com.hibob.academy.dao.OwnerCreationRequest
import com.hibob.academy.service.OwnerService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
@Path("/api/noy/owners")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource(private val ownerService: OwnerService) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addOwner(@RequestBody owner: OwnerCreationRequest): Response {
        val ownerId = ownerService.createOwner(owner)
        return Response.ok().entity("new owner created with id $ownerId").build()
    }

    @GET
    @Path("/{companyId}/getOwner")
    fun getAllOwners(@PathParam("companyId") companyId: Long): Response {
        val owner = ownerService.getAllOwners(companyId)
        return Response.ok().entity(owner).build()
    }

    @GET
    @Path("/{petId}/ownerInformation")
    fun getOwnerForPet(@PathParam("petId") petId: Long, @QueryParam("companyId") companyId: Long): Response {
        val ownerInfo = ownerService.getOwnerByPetId(petId, companyId)
        return Response.ok(ownerInfo).build()
    }
}
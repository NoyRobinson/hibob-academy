package com.hibob.academy.resource

import com.hibob.academy.dao.Owner
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
    fun addOwner(@RequestBody owner: Owner): Response {
        val ownerId = ownerService.createOwner(owner)
        return Response.status(Response.Status.CREATED).entity("new owner created with id $ownerId").build()
    }

    @GET
    @Path("/{companyId}/getOwner")
    fun getOwner(@PathParam("companyId") companyId: Long): Response {
        val owner = ownerService.getOwner(companyId)
        return Response.status(Response.Status.OK).entity(owner).build()
    }

    @GET
    @Path("/{petId}/ownerInformation")
    fun getOwnerForPet(@PathParam("petId") petId: Long): Response {
        val ownerInfo = ownerService.getOwnerByPetId(petId)
            ?: return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Pet doesn't have an owner").build()
        return Response.ok(ownerInfo).build()
    }
}
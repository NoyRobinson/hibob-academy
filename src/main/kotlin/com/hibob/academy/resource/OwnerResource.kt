package com.hibob.academy.resource

import com.hibob.academy.types.Owner
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Controller
@Path("/api/noy/owners")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource {

        // create
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/newOwner")
        fun addOwner(@RequestBody owner: Owner): Response {

            if(owner.name.isNullOrEmpty())
                owner.name = owner.firstName + " " + owner.lastName

            if(owner.firstName.isNullOrEmpty() || owner.lastName.isNullOrEmpty()) {
                owner.firstName = owner.name!!.split(" ")[0]
                owner.lastName = owner.name!!.split(" ")[1]
            }
            Response.status(Response.Status.CREATED).build()
            return Response.ok(owner).build()
        }

        // update v1
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/updateOwnerType/{ownerId}/v1")
        fun updateOwnerTypeV1(@PathParam("ownerId") ownerId: Long, @QueryParam("newType") newType: String?): Response {
            val owner = Owner(id = ownerId, companyId = 12L, employeeId = "123", name = "Noy Robinson", firstName = null, lastName = null, type = "dog", dateOfArrival = Date())
            owner.type = newType ?: owner.type
            Response.status(Response.Status.ACCEPTED).build()
            return Response.ok(owner).build()
        }

        // update v2
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/updateOwnerType/{ownerId}/v2")
        fun updateOwnerTypeV2(@PathParam("ownerId") ownerId: Long, @QueryParam("newType") newType: String?): Response {
            val owner = Owner(id = ownerId, companyId = 12L, employeeId = "123", name = null, firstName = "Noy", lastName = "Robinson", type = "dog", dateOfArrival = Date())
            owner.type = newType ?: owner.type
            Response.status(Response.Status.ACCEPTED).build()
            return Response.ok(owner).build()
        }

        // retrieve
        @GET
        @Path("/allOwners")
        fun getAllOwnersV1(): Response {
            Response.status(Response.Status.OK).build()
            return Response.ok(
                listOf(Owner(id= 1L, companyId = 12L, employeeId = "123", name = "Noy Robinson", firstName = null, lastName = null, type = "dog", dateOfArrival = Date()),
                        Owner(id = 2L, companyId = 12L, employeeId = "234", name = null, firstName = "Ohad", lastName = "Akler", type = "dog", dateOfArrival = Date()),)
            ).build()
        }

        // delete
        @DELETE
        @Path("/{ownerId}")
        fun deletePet(@PathParam("ownerId") ownerId: UUID): Response {
            Response.status(Response.Status.OK).build()
            return Response.ok("Deleted").build()
        }
}
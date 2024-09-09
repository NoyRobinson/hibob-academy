package com.hibob.academy.resource

import com.hibob.academy.types.Owner
import com.hibob.academy.types.Pet
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

        // create v1
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/newOwner/v1")
        fun addOwnerV1(@RequestBody owner: Owner): Response {
            Response.status(Response.Status.CREATED).build()
            return Response.ok(owner).build()
        }

        // create v2
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/newOwner/v2")
        fun addOwnerV2(@RequestBody owner: Owner): Response {
            Response.status(Response.Status.CREATED).build()
            return Response.ok(owner).build()
        }

        // update v1
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/updateOwnerType/{ownerId}/v1")
        fun updateOwnerTypeV1(@PathParam("ownerId") ownerId: UUID, @QueryParam("newType") newType: String?): Response {
            val owner = Owner(id = ownerId, companyId = UUID.randomUUID(), employeeId = UUID.randomUUID(), name = "Noy Robinson", firstName = null, lastName = null, type = "dog", dateOfArrival = Date())
            owner.type = newType ?: owner.type
            Response.status(Response.Status.ACCEPTED).build()
            return Response.ok(owner).build()
        }

        // update v2
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/updateOwnerType/{ownerId}/v2")
        fun updateOwnerTypeV2(@PathParam("ownerId") ownerId: UUID, @QueryParam("newType") newType: String?): Response {
            val owner = Owner(id = ownerId, companyId = UUID.randomUUID(), employeeId = UUID.randomUUID(), name = null, firstName = "Noy", lastName = "Robinson", type = "dog", dateOfArrival = Date())
            owner.type = newType ?: owner.type
            Response.status(Response.Status.ACCEPTED).build()
            return Response.ok(owner).build()
        }

        // retrieve v1
        @GET
        @Path("/allOwners/v1")
        fun getAllOwnersV1(): Response {
            Response.status(Response.Status.OK).build()
            return Response.ok(
                listOf(Owner(id = UUID.randomUUID(), companyId = UUID.randomUUID(), employeeId = UUID.randomUUID(), name = "Noy Robinson", firstName = null, lastName = null, type = "dog", dateOfArrival = Date()))
            ).build()
        }

        // retrieve v2
        @GET
        @Path("/allOwners/v2")
        fun getAllOwnersV2(): Response {
            Response.status(Response.Status.OK).build()
            return Response.ok(
                listOf(Owner(id = UUID.randomUUID(), companyId = UUID.randomUUID(), employeeId = UUID.randomUUID(), name = null, firstName = "Noy", lastName = "Robinson", type = "dog", dateOfArrival = Date()))
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

}
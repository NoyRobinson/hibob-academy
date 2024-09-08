package com.hibob.academy.resource

import com.hibob.academy.types.Pet
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Controller
@Path("api/noy/pets")
class PetsResource {

// create
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/newPet")
    fun addPet(@RequestBody pet: Pet): Response {
        Response.status(Response.Status.CREATED).build()
        return Response.ok(pet).build()
    }

// update
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updatePetName")
    fun updateName(@RequestBody pet: Pet, name: String): Response {
        pet.name = name
        Response.status(Response.Status.ACCEPTED).build()
        return Response.ok(name).build()
    }

// retrieve
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{petId}/type")
    fun getPetType(@PathParam("petId") petId: UUID): Response {
        Response.status(Response.Status.OK).build()
        return Response.ok("dog").build()
    }

// delete
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{petId}")
    fun deletePet(@PathParam("petId") petId: UUID): Response {
        Response.status(Response.Status.OK).build()
        return Response.ok("Deleted").build()
    }
}


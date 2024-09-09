package com.hibob.academy.resource

import com.hibob.academy.types.Pet
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Controller
@Path("/api/noy/pets")
@Produces(MediaType.APPLICATION_JSON)
class PetsResource {

// create
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/newPet")
    fun addPet(@RequestBody pet: Pet): Response {
        Response.status(Response.Status.CREATED).build()
        return Response.ok(pet).build()
    }

// update
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updatePetName/{petId}")
    fun updateName(@PathParam("petId") petId: UUID , @QueryParam("newName") newName: String?): Response {
        val pet = Pet(petId = petId, name = "Angie", type = "dog", companyId = UUID.randomUUID(), dateOfArrival = Date())
        pet.name = newName ?: pet.name
        Response.status(Response.Status.ACCEPTED).build()
        return Response.ok(pet).build()
    }

    // http://localhost:8080/api/noy/pets/updatePetName/3983f705-e3c4-4515-b46e-b8c74936ffd9?newName=Nessy


// retrieve
    @GET
    @Path("/allPets")
    fun getAllPets(): Response {
        Response.status(Response.Status.OK).build()
        return Response.ok(
            listOf(Pet(petId = UUID.randomUUID(), name = "Angie", type = "dog", companyId = UUID.randomUUID(), dateOfArrival = Date()))
        ).build()
    }

// delete
    @DELETE
    @Path("/{petId}")
    fun deletePet(@PathParam("petId") petId: UUID): Response {
        Response.status(Response.Status.OK).build()
        return Response.ok("Deleted").build()
    }
}

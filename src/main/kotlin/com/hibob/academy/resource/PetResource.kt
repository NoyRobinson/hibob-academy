package com.hibob.academy.resource

import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetType
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

class PetResource {


    // Get pets by owner: Given an owner ID, return all the information of the pets this owner adopted.
    // Count pets by type: This API does not receive any information but return a map of the type to how many pets of that type we have in the DB

    @Controller
    @Path("/api/noy/pets")
    @Produces(MediaType.APPLICATION_JSON)
    class PetsResource {

        // create
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        fun addPet(@RequestBody pet: PetData): Response {
            Response.status(Response.Status.CREATED).build()
            return Response.ok(pet).build()
        }

        // update
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/{petId}/updatePetName")
        fun updateName(@PathParam("petId") petId: Long, @QueryParam("newName") newName: String?): Response {
            val pet = PetData(id = petId, name = "Angie", type = PetType.DOG, companyId = 12L, dateOfArrival = Date(), ownerId = null)
            pet.name = newName ?: pet.name
            Response.status(Response.Status.ACCEPTED).build()
            return Response.ok(pet).build()
        }

        // http://localhost:8080/api/noy/pets/updatePetName/3983f705-e3c4-4515-b46e-b8c74936ffd9?newName=Nessy


        // retrieve
        @GET
        @Path("/{petId}/type")
        fun getPetType(@PathParam("petId") petId: Long): Response {
            val pets = listOf(
                PetData(id = petId, name = "Angie", type = PetType.DOG, companyId = 12L, dateOfArrival = Date(), null),
                PetData(id = petId, name = "Nessy", type = PetType.DOG, companyId = 11L, dateOfArrival = Date(), null))
            if(pets.filter{ pet ->
                    pet.id == petId
                }.isEmpty())
                return Response.status(Response.Status.NOT_FOUND).build()
            Response.status(Response.Status.OK).build()
            return Response.ok("dog").build()
        }

        // retrieve
        @GET
        @Path("/allPets")
        fun getAllPets(): Response {
            Response.status(Response.Status.OK).build()
            return Response.ok(
                listOf(PetData(id = 1L, name = "Angie", type = PetType.DOG, companyId = 12L, dateOfArrival = Date(), null))
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
}
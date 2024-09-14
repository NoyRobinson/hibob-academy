package com.hibob.academy.resource

import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetType
import com.hibob.academy.service.PetService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
    @Path("/api/noy/pets")
    @Produces(MediaType.APPLICATION_JSON)
    class PetsResource(private val petService: PetService) {

        // create
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        fun addPet(@RequestBody pet: PetData): Response {
            val petId = petService.createPet(pet.name, pet.type.toString(), pet.companyId, pet.dateOfArrival, pet.ownerId)
            Response.status(Response.Status.CREATED).entity("new pet created with id $petId").build()
            return Response.ok(pet).build()
        }

        // update
        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/{petId}/updatePetName")
        fun updateName(@PathParam("petId") petId: Long, @QueryParam("newName") newName: String, @QueryParam("companyId") companyId: Long): Response {
            petService.updatePetName(petId, newName, companyId)
            return Response.status(Response.Status.ACCEPTED).entity("Pets name changed successfully").build()
        }

        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/{petId}/{ownerId}/updatePetsOwner")
        fun updateOwnerForPet(@PathParam("petId") petId: Long, @PathParam("ownerId") ownerId: Long, @QueryParam("companyId") companyId: Long): Response {
            val pet = petService.getPetById(petId, companyId)
            val success = pet?.let { petService.updatePetOwner(it, petId, ownerId) }
            if (success == 1)
                return Response.ok().build()
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Pet already has an owner").build()
        }

        // retrieve
        @GET
        @Path("/{petType}")
        fun getPetType(@PathParam("petType") petType: PetType, @QueryParam("companyId") companyId: Long): Response {
            val petsByType = petService.getPetsByType(petType, companyId)
            return Response.status(Response.Status.OK).entity(petsByType).build()
        }

        // retrieve
        @GET
        @Path("{companyId}/allPets")
        fun getAllPets(@PathParam("companyId") companyId: Long): Response {
            val listOfPets = petService.getAllPets(companyId)
            return Response.status(Response.Status.OK).entity(listOfPets).build()
        }

        // delete
        @DELETE
        @Path("/{petId}")
        fun deletePet(@PathParam("petId") petId: Long, @QueryParam("companyId") companyId: Long): Response {
            petService.deletePet(petId, companyId)
            return Response.status(Response.Status.OK).entity("Deleted").build()
        }
    }
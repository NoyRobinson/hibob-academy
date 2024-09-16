package com.hibob.academy.resource

import com.hibob.academy.dao.Pet
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

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        fun addPet(@RequestBody pet: Pet): Response {
            val petId = petService.createPet(pet)
            return Response.status(Response.Status.CREATED).entity("new pet created with id $petId").build()
        }

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
            val success = petService.updatePetOwner(petId, ownerId, companyId)
            if(success == 1)
                return Response.status(Response.Status.OK).entity("Pet $petId got a new owner!").build()
            return Response.status(Response.Status.BAD_REQUEST).entity("Pet doesn't exist or pet $petId already has an owner!").build()
        }

        @GET
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/{petType}")
        fun getPetType(@PathParam("petType") petType: PetType, @QueryParam("companyId") companyId: Long): Response {
            val petsByType = petService.getPetsByType(petType, companyId)
            return Response.status(Response.Status.OK).entity(petsByType).build()
        }

        @GET
        @Path("{companyId}/allPets")
        fun getAllPets(@PathParam("companyId") companyId: Long): Response {
            val listOfPets = petService.getAllPets(companyId)
            return Response.status(Response.Status.OK).entity(listOfPets).build()
        }

        @DELETE
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/{petId}")
        fun deletePet(@PathParam("petId") petId: Long, @QueryParam("companyId") companyId: Long): Response {
            petService.deletePet(petId, companyId)
            return Response.status(Response.Status.OK).entity("Deleted").build()
        }
    }
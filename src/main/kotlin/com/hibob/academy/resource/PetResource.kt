package com.hibob.academy.resource

import com.hibob.academy.dao.PetrCreationRequest
import com.hibob.academy.dao.PetType
import com.hibob.academy.service.PetService
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
    @Path("/api/pets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    class PetsResource @Inject constructor(private val petService: PetService) {

        @POST
        fun addPet(@RequestBody pet: PetrCreationRequest): Response {
            val petId = petService.createPet(pet)
            return Response.ok().entity("new pet created with id $petId").build()
        }

        @PUT
        @Path("company/{companyId}/{petId}/updatePetName")
        fun updateName(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long, @QueryParam("newName") newName: String?): Response {
            petService.updatePetName(petId, newName, companyId)
            return Response.ok().entity("Pets name changed successfully").build()
        }

        @PUT
        @Path("company/{companyId}/{petId}/{ownerId}/updatePetsOwner")
        fun updateOwnerForPet(@PathParam("petId") petId: Long, @PathParam("ownerId") ownerId: Long, @PathParam("companyId") companyId: Long): Response {
            petService.updatePetOwner(petId, ownerId, companyId)
            return Response.ok().entity("Pet $petId got a new owner!").build()
        }

        @GET
        @Path("company/{companyId}/{petType}")
        fun getPetType(@PathParam("petType") petType: PetType, @PathParam("companyId") companyId: Long): Response {
            val petsByType = petService.getPetsByType(petType, companyId)
            return Response.ok(petsByType).build()
        }

        @GET
        @Path("company/{companyId}/allPets")
        fun getAllPets(@PathParam("companyId") companyId: Long): Response {
            val listOfPets = petService.getAllPets(companyId)
            return Response.ok(listOfPets).build()
        }

        @GET
        @Path("company/{companyId}{petId}")
        fun getPetById(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long): Response {
            val pet = petService.getPetById(petId, companyId)
            return Response.ok(pet).build()
        }

        @DELETE
        @Path("company/{companyId}/{petId}")
        fun deletePet(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long): Response {
            petService.deletePet(petId, companyId)
            return Response.ok().entity("Deleted").build()
        }

        @GET
        @Path("owner/{ownerId}/company/{companyId}")
        fun getAllPets(@PathParam("ownerId") ownerId: Long, @PathParam("companyId") companyId: Long): Response {
            val listOfPets = petService.getPetsByOwner(ownerId, companyId)
            return Response.ok(listOfPets).build()
        }

        @GET
        @Path("types/count/company/{companyId}")
        fun countPetsByType(@PathParam("companyId") companyId: Long): Response {
            val typeCounter = petService.countPetsByType(companyId)
            return Response.ok(typeCounter).build()
        }

        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("{companyId}/{ownerId}/adoptPets")
        fun adoptMultiplePets(@PathParam("ownerId") ownerId: Long, @PathParam("companyId") companyId: Long, @RequestBody petsIds: List<Long>): Response {
            val petsAdopted = petService.adoptPets(ownerId, companyId, petsIds)
            return Response.ok(petsAdopted).build()
        }
    }
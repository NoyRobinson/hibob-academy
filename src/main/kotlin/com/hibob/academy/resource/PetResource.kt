package com.hibob.academy.resource

import com.hibob.academy.dao.PetCreationRequest
import com.hibob.academy.dao.PetType
import com.hibob.academy.service.PetService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
    @Path("/api/pets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    class PetsResource(private val petService: PetService) {

        @POST
        fun addPet(@RequestBody pet: PetCreationRequest): Response {
            val petId = petService.createPet(pet)
            return Response.ok(petId).build()
        }

        @PUT
        @Path("company/{companyId}/{petId}/updatePetName")
        fun updateName(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long, @QueryParam("newName") newName: String?): Response {
            petService.updatePetName(petId, newName, companyId)
            return Response.ok("Pets name changed successfully").build()
        }

        @PUT
        @Path("company/{companyId}/{petId}/{ownerId}/updatePetsOwner")
        fun updateOwnerForPet(@PathParam("petId") petId: Long, @PathParam("ownerId") ownerId: Long, @PathParam("companyId") companyId: Long): Response {
            petService.updatePetOwner(petId, ownerId, companyId)
            return Response.ok(petId).build()
        }

        @GET
        @Path("company/{companyId}/{petType}/petsByType")
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
        @Path("company/{companyId}/{petId}")
        fun getPetById(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long): Response {
            val pet = petService.getPetById(petId, companyId)
            return Response.ok(pet).build()
        }

        @DELETE
        @Path("company/{companyId}/{petId}")
        fun deletePet(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long): Response {
            petService.deletePet(petId, companyId)
            return Response.ok("Deleted").build()
        }

        @GET
        @Path("owner/{ownerId}/company/{companyId}")
        fun getPetsByOwner(@PathParam("ownerId") ownerId: Long, @PathParam("companyId") companyId: Long): Response {
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
        @Path("{companyId}/adoptPets/{ownerId}")
        fun adoptMultiplePets(@PathParam("ownerId") ownerId: Long, @PathParam("companyId") companyId: Long, @RequestBody petsIds: List<Long>): Response {
            petService.adoptPets(ownerId, companyId, petsIds)
            return Response.ok("Pets were adopted!").build()
        }

        @POST
        @Path("multiplePets")
        fun createMultiplePets(@RequestBody pets: List<PetCreationRequest>): Response {
            petService.createMultiplePets(pets)
            return Response.ok("multiple pets were created").build()
        }
    }
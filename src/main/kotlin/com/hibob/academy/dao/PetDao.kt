
import com.hibob.academy.dao.*
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import java.sql.Date
import kotlin.random.Random

@Component

class PetDao @Inject constructor(private val sql: DSLContext) {

    private val pet = PetTable.instance
    private val companyId = Random.nextLong()

    private val petMapper = RecordMapper<Record, PetData> { record ->
        PetData(
            record[pet.name],
            convertStringToPetType(record[pet.type]),
            record[pet.companyId],
            Date(record[pet.dateOfArrival].time)
        )
    }

    private val petMapperWithoutType = RecordMapper<Record, PetDataWithoutType> { record ->
        PetDataWithoutType(
            record[pet.name],
            record[pet.companyId],
            Date(record[pet.dateOfArrival].time)
        )
    }

    fun getPetsByType(petType: PetType): List<PetDataWithoutType> =
        sql.select(pet.name, pet.companyId, pet.dateOfArrival)
            .from(pet)
            .where(pet.type.eq(convertPetTypeToPetString(petType))
            .and(pet.companyId.eq(companyId)))
            .fetch(petMapperWithoutType)

    fun getAllPets(): List<PetData> =
        sql.select(pet.name, pet.type, pet.companyId, pet.dateOfArrival)
            .from(pet)
            .where(pet.companyId.eq(companyId))
            .fetch(petMapper)


    fun createPet(name: String, type: String, companyId: Long, dateOfArrival: Date) =
        sql.insertInto(pet)
            .set(pet.name, name)
            .set(pet.type, type)
            .set(pet.companyId, companyId)
            .set(pet.dateOfArrival, dateOfArrival)
            .onConflict(pet.companyId)
            .doNothing()
            .execute()

    fun updatePetOwner(ownerId: ) =



//    Update the pet with the ownerID
//    What should you do if the pet already have an owner Id?


    fun convertPetTypeToPetString(petType: PetType) =
        when (petType) {
            PetType.DOG -> "Dog"
            PetType.CAT -> "Cat"
            PetType.BIRD -> "Bird"
        }

    fun convertStringToPetType(petType: String) =
        when (petType) {
            "Dog" -> PetType.DOG
            "Cat" -> PetType.CAT
            else -> PetType.BIRD
        }
}

import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetDataWithoutType
import com.hibob.academy.dao.PetTable
import com.hibob.academy.dao.PetType
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
    private val companyId = 12L

    private val petMapper = RecordMapper<Record, PetData> { record ->
        PetData(
            record[pet.name],
            convertStringToPetType(record[pet.type]),
            record[pet.companyId],
            Date(record[pet.dateOfArrival].time),
            record[pet.ownerId]
        )
    }

    private val petMapperWithoutType = RecordMapper<Record, PetDataWithoutType> { record ->
        PetDataWithoutType(
            record[pet.name],
            record[pet.companyId],
            Date(record[pet.dateOfArrival].time),
            record[pet.ownerId]
        )
    }

    fun getPetsByType(petType: PetType): List<PetDataWithoutType> =
        sql.select(pet.name, pet.companyId, pet.dateOfArrival, pet.ownerId)
            .from(pet)
            .where(pet.type.eq(convertPetTypeToPetString(petType))
            .and(pet.companyId.eq(companyId)))
            .fetch(petMapperWithoutType)

    fun getAllPets(): List<PetData> =
        sql.select(pet.name, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId)
            .from(pet)
            .where(pet.companyId.eq(companyId))
            .fetch(petMapper)

    fun createPet(name: String, type: String, companyId: Long, dateOfArrival: Date, ownerId: Long) =
        sql.insertInto(pet)
            .set(pet.name, name)
            .set(pet.type, type)
            .set(pet.companyId, companyId)
            .set(pet.dateOfArrival, dateOfArrival)
            .set(pet.ownerId, ownerId)

            .execute()

    fun getPetsByOwner(ownerId: Long) =
        sql.select(pet.name, pet.type, pet.companyId, pet.dateOfArrival, pet.ownerId)
            .from(pet)
            .where(pet.ownerId.eq(ownerId))
            .and(pet.companyId.eq(companyId))
            .fetch()

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
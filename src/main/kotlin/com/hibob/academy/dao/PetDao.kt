
import com.hibob.academy.dao.*
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import java.sql.Date
import java.util.*
import kotlin.random.Random

@Component

class PetDao @Inject constructor(private val sql: DSLContext) {

    private val pet = PetTable.instance

    private val petMapper = RecordMapper<Record, PetData> { record ->
        PetData(
            record[pet.id],
            record[pet.name],
            PetType.convertStringToPetType(record[pet.type]),
            record[pet.companyId],
            record[pet.dateOfArrival],
            record[pet.ownerId]
        )
    }

    private val petMapperWithoutType = RecordMapper<Record, PetDataWithoutType> { record ->
        PetDataWithoutType(
            record[pet.id],
            record[pet.name],
            record[pet.companyId],
            record[pet.dateOfArrival],
            record[pet.ownerId]
        )
    }

    fun getPetsByType(petType: PetType, companyId: Long): List<PetDataWithoutType> =
        sql.select(pet.name, pet.companyId, pet.dateOfArrival, pet.ownerId)
            .from(pet)
            .where(pet.type.eq(PetType.convertPetTypeToPetString(petType))
            .and(pet.companyId.eq(companyId)))
            .fetch(petMapperWithoutType)

    fun getAllPets(companyId: Long): List<PetData> =
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
            .onConflict(pet.companyId)
            .doNothing()
            .execute()

    fun updatePetOwner(petData: PetData, ownerId: Long) =
        sql.update(pet)
            .set(pet.ownerId, ownerId)
            .where(pet.id.eq(petData.id))
            .and(pet.ownerId.isNull())
            .execute()
}
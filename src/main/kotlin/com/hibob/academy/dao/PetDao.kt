
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
@Component

class PetDao @Inject constructor(private val sql: DSLContext) {
    private val pet = PetTable.instance
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
            .where(pet.type.eq(convertPetTypeToPetString(petType)))
            .fetch(petMapperWithoutType)

    fun getAllPets(): List<PetData> =
        sql.select(pet.name, pet.type, pet.companyId, pet.dateOfArrival)
            .from(pet)
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
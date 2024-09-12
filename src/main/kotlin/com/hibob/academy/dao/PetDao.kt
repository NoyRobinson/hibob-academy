
import com.hibob.academy.dao.*
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import java.sql.Date
import java.util.*
import kotlin.random.Random

@Component

class PetDao @Inject constructor(private val sql: DSLContext) {

    private val petTable = PetTable.instance

    private val petMapper = RecordMapper<Record, PetData> { record ->
        PetData(
            record[petTable.id],
            record[petTable.name],
            PetType.convertStringToPetType(record[petTable.type]),
            record[petTable.companyId],
            record[petTable.dateOfArrival],
            record[petTable.ownerId]
        )
    }


    fun getPetsByType(petType: PetType, companyId: Long): List<PetData> =
        sql.select(petTable.name, petTable.type, petTable.companyId, petTable.dateOfArrival, petTable.ownerId)
            .from(petTable)
            .where(petTable.type.eq(PetType.convertPetTypeToPetString(petType))
            .and(petTable.companyId.eq(companyId)))
            .fetch(petMapper)

    fun getAllPets(companyId: Long): List<PetData> =
        sql.select(petTable.name, petTable.type, petTable.companyId, petTable.dateOfArrival, petTable.ownerId)
            .from(petTable)
            .where(petTable.companyId.eq(companyId))
            .fetch(petMapper)

    fun createPet(name: String, type: String, companyId: Long, dateOfArrival: Date, ownerId: Long?) =
        sql.insertInto(petTable)
            .set(petTable.name, name)
            .set(petTable.type, type)
            .set(petTable.companyId, companyId)
            .set(petTable.dateOfArrival, dateOfArrival)
            .set(petTable.ownerId, ownerId)
            .execute()

    fun getPetsByOwner(ownerId: Long, companyId: Long): List<PetData> =
        sql.select(petTable.name, petTable.type, petTable.companyId, petTable.dateOfArrival, petTable.ownerId)
            .from(petTable)
            .where(petTable.ownerId.eq(ownerId))
            .and(petTable.companyId.eq(companyId))
            .fetch(petMapper)


    val count = DSL.count(petTable.type)

    fun countPetsByType(companyId: Long): Map<PetType, Int> =
        sql.select(petTable.type, count)
            .from(petTable)
            .where(petTable.companyId.eq(companyId))
            .groupBy(petTable.type)
            .fetch()
            .associate {
                val petType = PetType.convertStringToPetType(it[petTable.type])
                val count = it[count] as Int
                petType to count
            }

    fun updatePetOwner(petData: PetData, ownerId: Long, companyId: Long) =
        sql.update(petTable)
            .set(petTable.ownerId, ownerId)
            .where(petTable.id.eq(petData.id))
            .and(petTable.companyId.eq(companyId))
            .and(petTable.ownerId.isNull())
            .execute()
}
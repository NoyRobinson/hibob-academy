package com.hibob.kotlinBasics

data class Participant(val name: String, val email: String)

abstract class Location {
    abstract val street: String
    abstract val city: String
    abstract val country: String
}

class UsLocations(override val street: String, override val city: String,
                 override val country: String, val zipCode: String) : Location()

class UkLocation(override val street: String, override val city: String,
                 override val country: String, val postCode: String) : Location()

open class Meeting(val name: String, val location: Location, val participants: List<Participant>) {
    fun addParticipantToMeeting(participant: Participant) {
        Meeting(name, location, participants + participant)
    }
}

class PersonalReview(
        name: String, location: Location, participant: Participant,
        val reviewers: List<Participant>) : Meeting(name, location, reviewers) {
        init {
            println("Personal Review created successfully!")
        }
}

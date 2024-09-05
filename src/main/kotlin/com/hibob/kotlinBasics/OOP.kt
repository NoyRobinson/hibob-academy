package com.hibob.kotlinBasics

data class Participant(val name: String, val email: String)

open class Location(val street: String, val city: String, val country: String)

class UsLocations() : Location {

}

class UkLocation(val postCode: String) : Location()

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

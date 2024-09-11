package com.hibob.unittests

data class Person(val name: String, val age: Int)

data class PeopleStatistics(
    val averageAge: Double,
    val youngest: Person,
    val oldest: Person,
    val ageCount: Map<Int, Int>
)

class ListManager {
    private val people: MutableList<Person> = mutableListOf()

    fun addPerson(person: Person): Boolean {
        if (people.any { it.name == person.name && it.age == person.age }) {
            throw IllegalArgumentException("Duplicate person cannot be added.")
        }
        return people.add(person)
    }

    fun removePerson(person: Person): Boolean {
        return people.remove(person)
    }

    fun getPeopleSortedByAgeAndName(): List<Person> {
        return people.sortedWith(compareBy<Person> { it.age }.thenBy { it.name })
    }

    fun calculateStatistics(): PeopleStatistics? {
        if (people.isEmpty()) {
            return null
        }
        val averageAge = people.map { it.age }.average()
        val youngest = people.minByOrNull { it.age }
        val oldest = people.maxByOrNull { it.age }
        val ageCount = people.groupingBy { it.age }.eachCount()

        return youngest?.let { young ->
            oldest?.let { old ->
                PeopleStatistics(averageAge, young, old, ageCount)
            }
        }
    }
}
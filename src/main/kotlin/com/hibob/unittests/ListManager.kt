package com.hibob.unittests

data class Person(val name: String, val age: Int)

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
}
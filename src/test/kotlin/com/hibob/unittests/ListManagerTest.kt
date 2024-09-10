package com.hibob.unittests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test

/**
 * Exercise Instructions:
 *
 * Write tests for addPerson method:
 *
 * Test adding a unique person.
 * Test adding a duplicate person and ensure it throws the expected exception.
 * Test adding multiple people, checking that the list grows appropriately.
 *
 *
 * Write tests for removePerson method:
 *
 * Test removing a person that exists in the list.
 * Test trying to remove a person that does not exist, ensuring it returns false.
 * Test the state of the list after multiple add and remove operations.
 *
 *
 * Write tests for getPeopleSortedByAgeAndName method:
 *
 * Test with an empty list.
 * Test with one person.
 * Test with multiple people to ensure they are sorted first by age, then by name.
 * Test with edge cases like people with the same name but different ages and vice versa.
 *
 */

class ListManagerTest{

    private val listManager = ListManager()

    @Test
    fun `adding a unique person`() {
        val person1 = Person("Noy", 28)
        val person2 = Person("Tom", 32)
        listManager.addPerson(person1)
        assertEquals(true, listManager.addPerson(person2))
    }

    @Test
    fun `adding a duplicate person and ensure it throws the expected exception`() {
        val person = Person("Noy", 28)
        listManager.addPerson(person)
        assertThrows<IllegalArgumentException> { listManager.addPerson(person) }
    }

    @Test
    fun `adding multiple people, checking that the list grows appropriately`() {
        val person1 = Person("Noy", 28)
        val person2 = Person("Tom", 32)
        val amountOfPeopleBeforeAddition = listManager.getPeopleSortedByAgeAndName().size
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        assertEquals(amountOfPeopleBeforeAddition + 2, listManager.getPeopleSortedByAgeAndName().size)
    }

    @Test
    fun `removing a person that exists in the list`(){
        val person = Person("Noy", 28)
        listManager.addPerson(person)
        assertEquals( true, listManager.removePerson(person))
    }

    @Test
    fun `trying to remove a person that does not exist`() {
        val person = Person("Noy", 28)
        assertEquals( false, listManager.removePerson(person))
    }

    @Test
    fun `state of the list after multiple add and remove operations`(){
        val person1 = Person("Noy", 28)
        val person2 = Person("Tom", 32)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.removePerson(person1)
        assertEquals(listOf(Person("Tom", 32)), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `empty list`(){
        assertEquals(emptyList<Person>(), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `one person in list`(){
        val person = Person("Noy", 28)
        listManager.addPerson(person)
        assertEquals(listOf(person), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `multiple people to ensure they are sorted first by age, then by name`(){
        val person1 = Person("Noy", 28)
        val person2 = Person("Tom", 32)
        val person3 = Person("Tal", 32)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)
        assertEquals(listOf(person1, person3, person2), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `people with the same name but different ages`(){
        val person1 = Person("Noy", 40)
        val person2 = Person("Noy", 28)
        val person3 = Person("Noy", 32)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)
        assertEquals(listOf(person2, person3, person1), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `people with the same age but different names`(){
        val person1 = Person("Tal", 28)
        val person2 = Person("Noy", 28)
        val person3 = Person("Tom", 28)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)
        assertEquals(listOf(person2, person1, person3), listManager.getPeopleSortedByAgeAndName())
    }
}
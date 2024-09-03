package com.hibob.kotlinBasics

/**
 * Modify the main function to calculate and print the sum of all non-null integers in the list numbers.
 * Use safe calls and/or the Elvis operator where appropriate.
 **/
fun main() {
    val numbers: List<Int?> = listOf(1, null, 3, null, 5)
    // Task: Calculate the sum of all non-null numbers

    var sum: Int = 0
    for(i in numbers) {
        sum += i ?: 0
    }

    println(sum)
}
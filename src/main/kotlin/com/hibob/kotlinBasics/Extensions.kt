package com.hibob.kotlinBasics

// Exercise 1

// change the 'sum' function so that it was declared as an extenion to List<int>
// fun sum(list: List<int>): int {
//       var result = 0
//       for(i in list){
//           result += i
//       }
//       return result
//  }
//
//  fun main(args: Array<string>) {
//      val sum = sum(listOf(1,2,3))
//      print(sun) // 6
//  }

fun List<Int>.sum(): Int {
    var result = 0
    this.map( { result += it } )
    return result
}


// Exercise 2

// Write the following program method as an infix extension function
// fun toPowerOf(num: Number, exponent: Number): Double{
//      return 0
// }

infix fun Number.toPowerOf(exponent: Number): Double {
    return Math.pow(this.toDouble(), exponent.toDouble())
}
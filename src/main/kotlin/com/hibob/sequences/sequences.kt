package com.hibob.sequences

fun ex1() {
    // change the program to
    // 1. reuse the filter / map function
    // 2. println each call to track the diffs between List and Seq

    val list = listOf(1, 2, 3, 4)

    fun isEven(elementFromList: Int): Boolean = elementFromList % 2 == 0

//    val maxOddSquare = list
//        .map {
//            println("mapping $it")
//            it * it
//        }
//        .filter {
//            println("filtering $it")
//            isEven(it)
//        }
//        .find { it == 4 }
//
//    println()
//
//    val maxOddSquare2 = list.asSequence()
//        .map {
//            println("mapping $it")
//            it * it
//        }
//        .filter {
//            println("filtering $it")
//            isEven(it)
//        }
//        .find { it == 4 }
}


fun ex2() {
    // how many times filterFunc was called
    fun filterFunc(it: Int): Boolean {
        println("filterFunc was called")
        return it < 3
    }
    sequenceOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .filter { filterFunc(it) } // - will print 0 times because there was no terminal action
        //.filter { filterFunc(it) }.forEach(::println) - will print 10 times
}

fun ex3() {
    // create the list of the first 10 items of (2, 4, 8, 16, 32 ...) seq

    // Solution 1
    generateSequence(2) {  it * 2  }.take(10).toList().forEach(::println)

    // Solution 2 - Omri's solution
//    fun crazySeq(): Sequence<Int> = sequence {
//        var a = 2
//        yield(a) // yield brings next element in the right order
//
//        while(true){
//            a *= 2
//            yield(a)
//        }
//    }
//
//    crazySeq().take(10).toList().forEach(::println)
}

fun ex4() {
    // create the list of the first 10 items of the Fibonacci seq

    fun fibonacciSeq(): Sequence<Int> = sequence {
        var a = 1
        var b = 1
        yield(a)
        yield(b)

        while(true){
           val next = a + b
            yield(next)
            a = b
            b = next
        }
    }

    val fibSeq = fibonacciSeq().take(10).toList()
    println(fibSeq)
}

fun ex5() {
    // try to minimize the number of operations:

//    val engToHeb: Map<String, String> = mapOf() // assume the dictionary is real :-)
//    val b = "today was a good day for walking in the park. Sun was shining and birds were chirping"
//        .split(" ")
//        .mapNotNull {engToHeb[it]}
//        .filter {it.length <= 3 }
//        .size > 4


    // the function should return true if there are more than 4 words that if we translate them to hebrew their length is at most 3

    val engToHeb: Map<String, String> = mapOf(
        "today" to "היום",
        "was" to "היה",
        "good" to "טוב",
        "day" to "יום",
        "for" to "בשביל",
        "walking" to "ללכת",
        "in" to "ב",
        "the" to "ה",
        "park" to "פארק",
        "sun" to "שמש",
        "was" to "הייתה",
        "shining" to "זורחת",
        "and" to "ו",
        "birds" to "ציפורים",
        "were" to "היו",
        "chirping" to "מצייצות",
    )

  //  var count = 1
    println("today was a good day for walking in the park. Sun was shining and birds were chirping"
       .splitToSequence(" ")
       .mapNotNull {engToHeb[it]}
       .filter {
//           println("Filtering ${count++}")
         //  println("Filtering $it")
           val isValid = it.length <= 3
           if(isValid) println(it)
           isValid
       }
       .take(5)
       .count() > 4 // terminal action
    )
}
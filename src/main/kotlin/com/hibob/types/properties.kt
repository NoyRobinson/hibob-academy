package com.hibob.types

import java.time.DayOfWeek

// task 1
class Store (val day: DayOfWeek, val products: List<Product>) {

    // task 2
    val open: Boolean
        get() = day != DayOfWeek.SATURDAY

    // task 3
    val numberOfProducts: Int
        get() = products.size

    // task 4
//    val getReceipts: List<String> by lazy {
//        println("Long task")
//        listOf("receipt1", "receipt2", "receipt3", "receipt4")
//    }

    // task 5
    var count: Int = 0

    val getReceipts: List<String>
        get() {
            count++
            return listOf("receipt1", "receipt2", "receipt3", "receipt4")
        }


    // task 6
    lateinit var toBeInit: String

    fun onCreate() {
        println("Creating..")
        toBeInit = "Initialized!"
    }
}


//// tasks:
//
////1. create class called Store that initialize by day and list of products
////2. add property to that indicate if the store is open the store is open all the day expect saturday
////3. add property to that indicate number of product
////4. add val that get receipts //no need to implement the method, but it's a heavy task
////5. I want to count number of calling get receipts
////6. write variable that initialize only when calling create method
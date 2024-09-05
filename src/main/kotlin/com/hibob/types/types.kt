package com.hibob.types

import java.time.LocalDate

//1. create data class of Cart that include: client Id and list of Products
//2. product class contain id, name, price and custom - custom can be either int, string or any other type
//3. create sealed class of payment that can be use by the following classes:
//    CreditCard - contains number, expiryDate, type and limit (type can be VISA, MASTERCARD, DISCOVER and AMERICAN_EXPRESS)
//    PayPal - contain email
//    Cash - without args
//4. add fail function that get message an argument and throw IllegalStateException
//5. write function check if custom is true and only if its true its a valid product.
//6. write function called checkout and get cart and payment that pay the money
//   * only custom with true are valid
//   * cash payment is not valid to pay so if the payment is cash use fail function
//   * in case of credit card need to validate the expiryDate is after the current date + limit is bigger than the total we need to pay and we allow to use only  VISA or MASTERCARD
//      number needs to be of size 10
//   * in case of payPal validate we have @
///  * the return value of this function, should be a data class with employee id, status (success or failed) and total called Check
// 7. implement pay method

data class Cart(val clientId: String, val products: List<Product>)

class Product(val id: String, val name: String, val price: Double, val custom: Any)

enum class CreditCardType{
    VISA, MASTERCARD, DISCOVER, AMERICAN_EXPRESS
}

sealed class Payment {
    data class CreditCard(val number: String, val expiryDate: LocalDate, val type: CreditCardType, val limit: Double) : Payment()
    data class PayPal(val email: String) : Payment()
    data object Cash : Payment()
}

data class Check(val employeeId: String, val status: Statuses, val total: Double)

enum class Statuses{
    SUCCESS, FAILURE
}
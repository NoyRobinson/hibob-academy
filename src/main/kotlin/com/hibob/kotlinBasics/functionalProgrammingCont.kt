package com.hibob.kotlinBasics

import java.math.BigDecimal

data class BankAccount(
    val customerId: Long,
    val customerType: CustomerType,
    val balance: BigDecimal
)

data class BankAccountWithAnalysis(
    val bankAccount: BankAccount,
    val analysis: Analysis
)

sealed class CustomerType {
    object Small : CustomerType()
    object Medium : CustomerType()
    object Large : CustomerType()
    object VIP : CustomerType()
}

sealed class Analysis {
    object GoodAnalysis : Analysis()
    object BadAnalysis : Analysis()
    object NoAnalysis : Analysis()
}


// Don't change state in functional programming

//    case class BankAccount(customerId: Long, customerType: CustomerType, balance: BigDecimal)
//
//
//    case class BankAccountWithAnalysis(bankAccount: BankAccount, analysis: Analysis)
//
//
//    sealed trait CustomerType  --- like an enum
//
//    case object Small extends CustomerType
//
//    case object Medium extends CustomerType
//
//    case object Large extends CustomerType
//
//    case object VIP extends CustomerType
//
//    sealed trait Analysis
//
//    case object GoodAnalysis extends Analysis
//
//    case object BadAnalysis extends Analysis
//
//    case object NoAnalysis extends Analysis


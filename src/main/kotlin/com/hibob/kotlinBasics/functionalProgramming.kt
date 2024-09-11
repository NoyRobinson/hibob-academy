package com.hibob.kotlinBasics

import jakarta.inject.Singleton
import java.math.BigDecimal

@Singleton
class BankAccountHandler {

    /**
     * This method goes over all bank accounts.
     * If the balance is positive it considers a good account, if negative it's bad and if zero it's
     * for those with no Bad analysts, it promotes their type and also returns the account with the new type and its analysis
     */
    fun handleAccounts(bankAccounts: List<BankAccount>): List<BankAccountWithAnalysis> {


        return giveAnalysis(bankAccounts)
    }

    private fun giveAnalysis(bankAccounts: List<BankAccount>): List<BankAccountWithAnalysis> {
        val accountsWithAnalysis = mutableListOf<BankAccountWithAnalysis>()
        for (account in bankAccounts) {
            when {
                account.balance > BigDecimal.ZERO -> {
                    val newBalance = account.balance.multiply(BigDecimal("1.01"))
                    val updatedCustomerType = upgrade(account)
                    accountsWithAnalysis.add(
                        BankAccountWithAnalysis(
                            account.copy(balance = newBalance, customerType = updatedCustomerType),
                            Analysis.GoodAnalysis
                        )
                    )
                }
                account.balance < BigDecimal.ZERO -> {
                    val updatedCustomerType = downgrade(account)
                    accountsWithAnalysis.add(
                        BankAccountWithAnalysis(
                            account.copy(customerType = updatedCustomerType),
                            Analysis.BadAnalysis
                        )
                    )
                }
                else -> {
                    accountsWithAnalysis.add(
                        BankAccountWithAnalysis(
                            account,
                            Analysis.NoAnalysis
                        )
                    )
                }
            }
        }
        return accountsWithAnalysis
    }

    private fun upgrade(bankAccount: BankAccount): CustomerType {
        return when (bankAccount.customerType) {
            CustomerType.Small -> CustomerType.Medium
            CustomerType.Medium -> CustomerType.Large
            CustomerType.Large -> CustomerType.VIP
            CustomerType.VIP -> CustomerType.VIP
        }
    }

    private fun downgrade(bankAccount: BankAccount): CustomerType {
        return when (bankAccount.customerType) {
            CustomerType.Small -> CustomerType.Small
            CustomerType.Medium -> CustomerType.Small
            CustomerType.Large -> CustomerType.Medium
            CustomerType.VIP -> CustomerType.Large
        }
    }
}





//// function edited
//    def handleAccounts(bankAccounts: List[BankAccount]): List[BankAccountWithAnalysis] = {
//        bankAccounts.map(account => {
//            if (account.balance > 0) {
//                account.balance * 1.01
//            } else if (account.balance < 0) {
//                val updatedCustomerType = downgrade(account)
//                accountsWithAnalysis += BankAccountWithAnalysis(account.copy(customerType = updatedCustomerType), BadAnalysis)
//            } else accountsWithAnalysis += BankAccountWithAnalysis(account, NoAnalysis)
//        })
//
//
//        val accountsWithAnalysis = giveAnalysis(bankAccounts)
//        accountsWithAnalysis
//    }
//
//
//// new function added
//private def getNewBalance(balance: BigDecimal) : BigDecimal= {
//    if(balance > 0)
//        balance * 1.01
//    else balance
//}
//
//
//    private def giveAnalysis(bankAccounts: List[BankAccount]): List[BankAccountWithAnalysis] = {
//        val accountsWithAnalysis = new ListBuffer[BankAccountWithAnalysis]()
//        for (account <- bankAccounts) {
//            if (account.balance > 0) {
//                val newBalance = account.balance * 1.01
//                val updatedCustomerType = upgrade(account)
//                accountsWithAnalysis += BankAccountWithAnalysis(account.copy(balance = newBalance, customerType = updatedCustomerType), GoodAnalysis)
//            } else if (account.balance < 0) {
//                val updatedCustomerType = downgrade(account)
//                accountsWithAnalysis += BankAccountWithAnalysis(account.copy(customerType = updatedCustomerType), BadAnalysis)
//            } else accountsWithAnalysis += BankAccountWithAnalysis(account, NoAnalysis)
//        }
//        accountsWithAnalysis.toList
//    }
//
//
//    private def upgrade(bankAccount: BankAccount): CustomerType =
//    bankAccount.customerType match {
//        case Small => Medium
//                case Medium => Large
//                case Large => VIP
//                case VIP => VIP
//    }
//
//
//    private def downgrade(bankAccount: BankAccount): CustomerType =
//    bankAccount.customerType match {
//        case Small => Small
//                case Medium => Small
//                case Large => Medium
//                case VIP => Large
//    }

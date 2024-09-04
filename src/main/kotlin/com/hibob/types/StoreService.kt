package com.hibob.types

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class StoreService {

    fun checkProductCustom(custom: Any): Boolean{
        (custom as? Boolean)?.let{
            when(custom){
                true -> return true
                else -> return false
            }
        } ?: return false
    }

    fun fail(message: String): Nothing {
        throw IllegalStateException(message)
    }

    fun calculateTotal(products: List<Product>): Double {
        return products.map {it.price}.sum()
    }

    fun findStatus(payment: Payment, total: Double): Statuses {
        return when(payment){
            is Payment.Cash -> fail("Can't pay with cash")

            is Payment.CreditCard -> {
                if(payment.number.length == 10 &&
                    payment.expiryDate.isAfter(LocalDate.now()) &&
                    payment.limit > total &&
                    (payment.type == CreditCardType.VISA || payment.type == CreditCardType.MASTERCARD))
                    Statuses.SUCCESS
                else
                    Statuses.FAILURE
            }

            is Payment.PayPal -> {
                if(payment.email.contains('@'))
                    Statuses.SUCCESS
                else
                    Statuses.FAILURE
            }
        }
    }

    fun checkout(cart: Cart, payment: Payment): Check {
        val productsWithCustomTrue = cart.products.filter { checkProductCustom(it.custom) }
        val total = calculateTotal(productsWithCustomTrue)
        val status = findStatus(payment, total)

        if(status == Statuses.FAILURE)
            return Check(cart.clientId, status, 0.0)

        return Check(cart.clientId, status, total)
    }

    fun pay(cart: List<Cart>, payment: Payment): Map<String, Check> {
        return cart.associate { it ->
                it.clientId to checkout(it, payment)
        }
    }
}
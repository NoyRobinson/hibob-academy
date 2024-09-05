package com.hibob.types

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class StoreService {

    private fun checkProductCustom(custom: Any): Boolean =
        (custom as? Boolean) ?: false

    private fun fail(message: String): Nothing {
        throw IllegalStateException(message)
    }

    private fun calculateTotal(products: List<Product>): Double =
        products.map { it.price }.sum()

    private fun isCreditCardValid(payment: Payment.CreditCard, price: Double): Statuses  {
        if (isSupportedType(payment.type) && isCardDetailsValid(payment) && isNotOverTheLimit(payment, price))
            return Statuses.SUCCESS
        return Statuses.FAILURE
    }

    private fun isSupportedType(cardType: CreditCardType): Boolean =
         cardType == CreditCardType.VISA || cardType == CreditCardType.MASTERCARD

    private fun isCardDetailsValid(payment: Payment.CreditCard): Boolean =
        payment.expiryDate.isAfter(LocalDate.now()) && payment.number.length == 10

    private fun isNotOverTheLimit(payment: Payment.CreditCard, price: Double): Boolean =
        payment.limit > price


    private fun checkPayment(payment: Payment, price: Double): Statuses {
        return when (payment) {
            is Payment.Cash -> fail("Can't pay with cash")

            is Payment.CreditCard -> isCreditCardValid(payment, price)

            is Payment.PayPal -> {
                if (payment.email.contains('@'))
                    Statuses.SUCCESS
                else
                    Statuses.FAILURE
            }
        }
    }

    private fun checkout(cart: Cart, payment: Payment): Check {
        val productsWithCustomTrue = cart.products.filter { checkProductCustom(it.custom) }
        val total = calculateTotal(productsWithCustomTrue)
        val paymentValid = checkPayment(payment, total)

        if (paymentValid == Statuses.FAILURE)
            return Check(cart.clientId, paymentValid, 0.0)

        return Check(cart.clientId, paymentValid, total)
    }

    fun pay(cart: List<Cart>, payment: Payment): Map<String, Check> {
        return cart.associate { it ->
            it.clientId to checkout(it, payment)
        }
    }
}
import com.hibob.bootcamp.BankAccount
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Write unit tests to verify that the deposit and withdraw methods function correctly.
 * Handle edge cases, such as invalid inputs (e.g., negative amounts).
 * Ensure that the getBalance method returns the correct balance after a series of deposits and withdrawals.
 */

class BankAccountTest {

    private val bankAccount = BankAccount(30.0)

    @Test
    fun `deposit valid amount increases balance`() {
        bankAccount.deposit(50.0)
        assertEquals(80.0, bankAccount.getBalance())
    }

    @Test
    fun `deposit negative or zero amount throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> { bankAccount.deposit(-20.0) }
    }

    @Test
    fun `withdraw valid amount decreases balance`() {
        bankAccount.withdraw(20.0)
        assertEquals(10.0, bankAccount.getBalance())
    }

    @Test
    fun `withdraw amount greater than balance throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException>{ bankAccount.withdraw(50.0) }
    }

    @Test
    fun `withdraw negative or zero amount throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> { bankAccount.withdraw(0.0) }
    }

    @Test
    fun `getBalance returns the correct balance`() {
        assertEquals(30.0, bankAccount.getBalance())
    }
}

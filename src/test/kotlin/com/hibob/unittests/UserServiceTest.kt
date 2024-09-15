package com.hibob.unittests

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.Mockito.*

class UserServiceTest{

    private val userDao = mock<UserDao>()
    private val notificationService = mock<NotificationService>()
    private val emailVerificationService = mock<EmailVerificationService>()
    private val userService = UserService(userDao, notificationService, emailVerificationService)

    @Test
    fun `Registering a user who is registered already`() {
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(1L)).thenReturn(user)
        assertThrows<IllegalArgumentException> { userService.registerUser(user) }
    }

    @Test
    fun `User registration fails`() {
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", false)
        whenever(userDao.findById(1L)).thenReturn(null)
        whenever(userDao.save(user)).thenReturn(false)
        assertThrows<IllegalStateException> { userService.registerUser(user) }
    }

    @Test
    fun `Verification email not sent`(){
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", false)
        whenever(userDao.findById(1L)).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationService.sendVerificationEmail(user.email)).thenReturn(false)
        assertThrows<IllegalStateException> { userService.registerUser(user) }
    }

    @Test
    fun `Registering a new user`(){
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(user.id)).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationService.sendVerificationEmail(user.email)).thenReturn(true)
        assertTrue(userService.registerUser(user))
    }

    @Test
    fun `Verify user email when user not found`(){
        whenever(userDao.findById(1L)).thenReturn(null)
        assertThrows<IllegalArgumentException>{ userService.verifyUserEmail(1L, "token") }
    }

    @Test
    fun `Verify user email fails`() {
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(1L)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, "token")).thenReturn(false)
        assertThrows<IllegalArgumentException> { userService.verifyUserEmail(1L, "token") }
    }

    @Test
    fun `User is not updated so email not sent`(){
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(1L)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, "token")).thenReturn(true)
        whenever(userDao.update(user.copy(isEmailVerified = true))).thenReturn(false)
        whenever(notificationService.sendEmail(user.email, "token")).thenReturn(true)
        assertFalse(userService.verifyUserEmail(1L, "token"))
        verify(notificationService, never()).sendEmail(user.email, "Welcome ${user.name}!")
    }

    @Test
    fun `Verify user email successful`(){
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(1L)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, "token")).thenReturn(true)
        whenever(userDao.update(user.copy(isEmailVerified = true))).thenReturn(true)
        whenever(notificationService.sendEmail(user.email, "token")).thenReturn(true)
        assertTrue(userService.verifyUserEmail(1L, "token"))
        verify(notificationService).sendEmail(user.email, "Welcome ${user.name}!")
    }
}
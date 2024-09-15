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
    fun `registerUser should throw an exception if user already exists`() {
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(1L)).thenReturn(user)
        val exception = assertThrows<IllegalArgumentException> { userService.registerUser(user) }
        assertEquals("User already exists", exception.message)
    }

    @Test
    fun `registerUser should throw an exception when user registration fails`() {
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", false)
        whenever(userDao.findById(1L)).thenReturn(null)
        whenever(userDao.save(user)).thenReturn(false)
        val exception = assertThrows<IllegalStateException> { userService.registerUser(user) }
        assertEquals("User registration failed", exception.message)
    }

    @Test
    fun `registerUser should throw an exception when failed to send a verification email`(){
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", false)
        whenever(userDao.findById(1L)).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationService.sendVerificationEmail(user.email)).thenReturn(false)
        val exception = assertThrows<IllegalStateException> { userService.registerUser(user) }
        assertEquals("Failed to send verification email", exception.message)
    }

    @Test
    fun `registerUser should register a new user`(){
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(user.id)).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationService.sendVerificationEmail(user.email)).thenReturn(true)
        assertTrue(userService.registerUser(user))
    }

    @Test
    fun `verifyUserEmail should throw an exception if user not found`(){
        whenever(userDao.findById(1L)).thenReturn(null)
        val exception = assertThrows<IllegalArgumentException>{ userService.verifyUserEmail(1L, "token") }
        assertEquals("User not found", exception.message)
    }

    @Test
    fun `verifyUserEmail should throw an exception if email verification failed`() {
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(1L)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, "token")).thenReturn(false)
        val exception = assertThrows<IllegalArgumentException> { userService.verifyUserEmail(1L, "token") }
        assertEquals("Email verification failed", exception.message)
    }

    @Test
    fun `verifyUserEmail should return false and not send an email if user isn't updated`(){
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(1L)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, "token")).thenReturn(true)
        whenever(userDao.update(user.copy(isEmailVerified = true))).thenReturn(false)
        whenever(notificationService.sendEmail(user.email, "token")).thenReturn(true)
        assertFalse(userService.verifyUserEmail(1L, "token"))
        verify(notificationService, never()).sendEmail(user.email, "Welcome ${user.name}!")
    }

    @Test
    fun `verifyUserEmail should send an email if user is updated`(){
        val user = User(1L, "Noy", "noy.robinson@hibob.io", "123456", true)
        whenever(userDao.findById(1L)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, "token")).thenReturn(true)
        whenever(userDao.update(user.copy(isEmailVerified = true))).thenReturn(true)
        whenever(notificationService.sendEmail(user.email, "token")).thenReturn(true)
        assertTrue(userService.verifyUserEmail(1L, "token"))
        verify(notificationService).sendEmail(user.email, "Welcome ${user.name}!")
    }
}
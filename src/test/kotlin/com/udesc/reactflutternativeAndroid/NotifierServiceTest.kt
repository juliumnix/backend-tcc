package com.udesc.reactflutternativeAndroid

import com.udesc.reactflutternativeAndroid.engine.NotifierService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NotifierServiceTest {
    private lateinit var notifierService: NotifierService

    @BeforeEach
    fun setUp() {
        notifierService = NotifierService()
    }

    @Test
    fun testCreateOrUpdateNotifier() {
        notifierService.createOrUpdateNotifier("1", "active")
        val notifier = notifierService.getNotifier("1")

        Assertions.assertNotNull(notifier)
        Assertions.assertEquals("active", notifier?.getNotifyStatus("1"))

        notifierService.createOrUpdateNotifier("1", "inactive")
        val updatedNotifier = notifierService.getNotifier("1")

        Assertions.assertNotNull(updatedNotifier)
        Assertions.assertEquals("inactive", updatedNotifier?.getNotifyStatus("1"))
    }

    @Test
    fun testGetNotifier() {
        Assertions.assertNull(notifierService.getNotifier("nonexistent"))

        notifierService.createOrUpdateNotifier("2", "active")
        val notifier = notifierService.getNotifier("2")

        Assertions.assertNotNull(notifier)
        Assertions.assertEquals("active", notifier?.getNotifyStatus("2"))
    }

    @Test
    fun testDeleteNotifier() {
        notifierService.createOrUpdateNotifier("3", "active")

        val beforeDelete = notifierService.getNotifier("3")
        Assertions.assertNotNull(beforeDelete)

        notifierService.deleteNotifier("3")

        val afterDelete = notifierService.getNotifier("3")
        Assertions.assertNull(afterDelete)
    }
}
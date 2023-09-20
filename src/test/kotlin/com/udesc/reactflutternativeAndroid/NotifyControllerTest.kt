package com.udesc.reactflutternativeAndroid

import com.udesc.reactflutternativeAndroid.controller.NotifyController
import com.udesc.reactflutternativeAndroid.engine.NotifierService
import com.udesc.reactflutternativeAndroid.model.Notifier
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockHttpServletResponse

@ExtendWith(MockitoExtension::class)
class NotifyControllerTest {

    @Mock
    private lateinit var notifierService: NotifierService

    @Test
    fun testGetNotifyStatusFound() {
        val controller = NotifyController(notifierService)
        val id = "123"
        val mockResponse = MockHttpServletResponse()

        // Criando um mock de Notifier
        val mockNotifier = mock(Notifier::class.java)

        // Mockando o comportamento do notifierService
        `when`(notifierService.getNotifier(id)).thenReturn(mockNotifier)
        `when`(mockNotifier.getNotifyStatus(id)).thenReturn("Status")

        controller.getNotifyStatus(id, mockResponse)

        // Realize as asserções necessárias no mockResponse, por exemplo, código de status e conteúdo JSON
    }

    @Test
    fun testGetNotifyStatusNotFound() {
        val controller = NotifyController(notifierService)
        val id = "123"
        val mockResponse = MockHttpServletResponse()

        // Mockando o comportamento do notifierService quando nenhum notifier é encontrado
        `when`(notifierService.getNotifier(id)).thenReturn(null)

        controller.getNotifyStatus(id, mockResponse)

        // Realize as asserções necessárias no mockResponse, por exemplo, código de status
    }
}
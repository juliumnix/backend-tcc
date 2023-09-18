package com.udesc.reactflutternativeAndroid

import com.udesc.reactflutternativeAndroid.controller.NotifyController
import com.udesc.reactflutternativeAndroid.model.Notifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus

class NotifyControllerTests {

    @InjectMocks
    private lateinit var notifyController: NotifyController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetStatus() {
        // Simule o comportamento do Notifier e prepare os mocks
        val notifier = mock(Notifier::class.java)
        val expectedContent = "Status de notificação"

        // Configure o comportamento do Notifier quando o método getNotifyStatus() for chamado
        `when`(notifier.getNotifyStatus()).thenReturn(expectedContent)

        // Defina o campo notifier do NotifyController para o mock criado
        notifyController.notifier = notifier

        // Chame o método getStatus() do NotifyController
        val responseEntity = notifyController.getStatus()

        // Verifique se o status da resposta é 201 (HttpStatus.CREATED)
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.statusCode)

        // Acesse a propriedade content de NotifierResponse e verifique se ela contém o conteúdo esperado
        val responseBody = responseEntity.body
        Assertions.assertNotNull(responseBody) // Certifique-se de que o corpo não é nulo
        if (responseBody != null) {
            Assertions.assertEquals(expectedContent, responseBody.message)
        }
    }
}
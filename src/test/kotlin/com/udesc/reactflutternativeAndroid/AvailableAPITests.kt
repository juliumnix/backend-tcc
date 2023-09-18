package com.udesc.reactflutternativeAndroid

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class AvailableAPITests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testGetAvailableAPI() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/available"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
    }
}
import com.fasterxml.jackson.databind.ObjectMapper
import com.udesc.reactflutternativeAndroid.model.ProjectArtifact
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post


@SpringBootTest
@AutoConfigureMockMvc
class GenerateProjectIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Configuration
    class ObjectMapperConfig {
        @Bean
        fun objectMapper(): ObjectMapper {
            return ObjectMapper()
        }
    }

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun testCreateProject() {
        val projectRequest = ProjectArtifact(name = "TesteUnitario",
                repositoryKey = "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",
                ownerName = "juliumnix", id = "id123456",
                architecture = "mvp", reactDependencies = listOf(
                mapOf("name" to "@react-navigation/stack", "version" to "6.3.17"),
                mapOf("name" to "axios", "version" to "1.5.0")),
                flutterDependencies = listOf(mapOf("name" to "url_launcher", "version" to "6.1.14")),
                needZIPFile = false)

        val requestContent = objectMapper.writeValueAsString(projectRequest)

        val result: ResultActions = mockMvc.perform(
                post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent)
        )

        // Verifique se a resposta HTTP está correta
        result.andExpect(status().isNotFound)
    }
}
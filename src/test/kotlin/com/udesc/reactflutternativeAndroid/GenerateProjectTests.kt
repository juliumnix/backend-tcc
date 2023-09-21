import com.udesc.reactflutternativeAndroid.controller.GenerateProject
import com.udesc.reactflutternativeAndroid.engine.EngineOrchestrator
import com.udesc.reactflutternativeAndroid.model.Notifier
import com.udesc.reactflutternativeAndroid.model.ProjectArtifact
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus

// Importações e anotações necessárias aqui

class GenerateProjectTests {

    @InjectMocks
    private lateinit var generateProject: GenerateProject

    @Mock
    private lateinit var engineOrchestrator: EngineOrchestrator

    @Mock
    private lateinit var notifier: Notifier

    @Value("\${git.localCloneDirectory}")
    private val localCloneDirectory: String? = null

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testCreateProjectWithZIPSuccess() {
        val projectRequest = ProjectArtifact(name = "TesteUnitario",
            repositoryKey = "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",
            ownerName = "juliumnix", id = "organization",
            architecture = "mvp", reactDependencies = listOf(
                mapOf("name" to "@react-navigation/stack", "version" to "6.3.17"),
                mapOf("name" to "axios", "version" to "1.5.0")),
            flutterDependencies = listOf(mapOf("name" to "url_launcher", "version" to "6.1.14")),
            needZIPFile = false)
        val randomName = "random_name"

        Mockito.doNothing().`when`(engineOrchestrator).init(projectRequest.architecture,
            "$localCloneDirectory/${randomName}",
            projectRequest.reactDependencies,
            projectRequest.flutterDependencies,
            projectRequest.repositoryKey,
            projectRequest.name,
            projectRequest.ownerName,
            projectRequest.needZIPFile)
        Mockito.doNothing().`when`(engineOrchestrator).deleteClonedRepository("$localCloneDirectory/$randomName")

        val responseEntity = generateProject.createProject(projectRequest)

        Assertions.assertEquals(HttpStatus.OK, responseEntity.statusCode)
    }
}
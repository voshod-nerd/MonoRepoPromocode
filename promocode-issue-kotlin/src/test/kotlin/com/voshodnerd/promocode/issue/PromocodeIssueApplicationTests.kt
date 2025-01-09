package com.voshodnerd.promocode.issue;

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.voshodnerd.promocode.issue.repository.CampaignRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class PromoCodeIssueKotlinApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var campaignRepository: CampaignRepository

    companion object {
        @Container
        private val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.liquibase.enabled") { true }
            registry.add("spring.liquibase.change-log") { "classpath:liquibase/changelog-master.xml" }
            registry.add("spring.liquibase.default-schema") { "public" }
        }
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun testPerformRequestCampaignList() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/campaign-list"))
            .andExpect(status().is2xxSuccessful())
            .andReturn()

        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"))

        Assertions.assertEquals(
            result.getResponse().getContentAsString(),
            mapper.writeValueAsString(campaignRepository.findAll())
        )
    }

    @Test
    @Throws(Exception::class)
    fun testFileUpload() {
        // Create a mock CSV file
        val campaignId = "1"
        val csvContent = """
             D12BPUNM
             EF3XVUJK
             TTZ6CKQF
             UKGMT5S5
             GS7IOHGP
             D4J7WOKB
             1UO1XEPR
             RRLNZ02Y
             IRM5CN6I
             HB2E2WRG
             V2SIF4NK
             0SP8PQDP
             MRIC7QM9
             ISCZE8B1
             EJDF1M45
             J1879225
             6CV6VJ8O
             ISOAWM7J
             TAN7IWJT
             G26PNZRQ       
             """.trimIndent()
        val file = MockMultipartFile(
            "filename",  // name of the @RequestParam in the controller
            "test.csv",  // original file name
            "text/csv",  // content type
            csvContent.toByteArray() // file content
        )
        // Perform the file upload request
        val result = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/upload")
                .file(file).param("campaignId", campaignId)
        ) // Add campaignId as a request param)
            .andExpect(status().isOk()).andReturn()
        // Assert the response
        Assertions.assertEquals(
            result.response
                .getContentAsString(StandardCharsets.UTF_8), "Загружено"
        )
    }



}

package com.voshodnerd.promocode.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.voshodnerd.promocode.issue.repository.CampaignRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class PromoCodeIssueApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CampaignRepository campaignRepository;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.enabled", () -> true);
        registry.add("spring.liquibase.change-log", () -> "classpath:liquibase/changelog-master.xml");
        registry.add("spring.liquibase.default-schema", () -> "public");
        registry.add("spring.mandatory-file-encoding", () -> "UTF-8");
        registry.add("spring.http.encoding.charset", () -> "UTF-8");
        registry.add("spring.http.encoding.enabled", () -> true);
    }


    @Test
    void contextLoads() {
    }

    @Test
    @SneakyThrows
    void testPerformRequestCampaignList() {
        var result = mockMvc.perform(get("http://localhost:8080/campaign-list"))
                .andExpect(status().is2xxSuccessful()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));

        assertEquals(result.getResponse().getContentAsString(),
                mapper.writeValueAsString(campaignRepository.findAll()));
    }

    @Test
    public void testFileUpload() throws Exception {
        // Create a mock CSV file
        String campaignId = "1";
        String csvContent = "D12BPUNM\n" +
                "EF3XVUJK\n" +
                "TTZ6CKQF\n" +
                "UKGMT5S5\n" +
                "GS7IOHGP\n" +
                "D4J7WOKB\n" +
                "1UO1XEPR\n" +
                "RRLNZ02Y\n" +
                "IRM5CN6I\n" +
                "HB2E2WRG\n" +
                "V2SIF4NK\n" +
                "0SP8PQDP\n" +
                "MRIC7QM9\n" +
                "ISCZE8B1\n" +
                "EJDF1M45\n" +
                "J1879225\n" +
                "6CV6VJ8O\n" +
                "ISOAWM7J\n" +
                "TAN7IWJT\n" +
                "G26PNZRQ\n";
        MockMultipartFile file = new MockMultipartFile(
                "filename", // name of the @RequestParam in the controller
                "test.csv", // original file name
                "text/csv", // content type
                csvContent.getBytes() // file content
        );
        // Perform the file upload request
        var result = mockMvc.perform(multipart("/upload")
                        .file(file).param("campaignId", campaignId)) // Add campaignId as a request param)
                .andExpect(status().isOk()).andReturn();
        // Assert the response
        assertEquals(result.getResponse()
                .getContentAsString(StandardCharsets.UTF_8), "Загружено");
    }

}

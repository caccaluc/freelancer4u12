package ch.zhaw.freelancer4u.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private static ObjectMapper objectMapper = new ObjectMapper();
        private static String jobId;

        @BeforeAll
        public static void setup() {
            // Initialize any static resources if needed
        }

        // d1
        @Test
        public void testCreateJob() throws Exception {
            String jobJson = "{ \"title\": \"Developer\", \"description\": \"Java Developer\" }";

            MvcResult result = mockMvc.perform(post("/api/job")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jobJson))
                    .andExpect(status().isCreated())
                    .andReturn();

            String responseContent = result.getResponse().getContentAsString();
            jobId = objectMapper.readTree(responseContent).get("id").asText();

            assertThat(jobId).isNotNull();
        }

        public static String getJobId() {
            return jobId;
        }

        // d2
        @Test
        public void testGetJobById() throws Exception {
            mockMvc.perform(get("/api/job/" + getJobId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.description").value("Java Developer"))
                    .andExpect(jsonPath("$.companyId").value("1")); // Assuming companyId is 1
        }
        // d3
        @Test
        public void testDeleteJob() throws Exception {
            mockMvc.perform(delete("/api/job/" + getJobId()))
                    .andExpect(status().isOk());

            // Verify the job is deleted by trying to get it and expecting a 404 status
            mockMvc.perform(get("/api/job/" + getJobId()))
                    .andExpect(status().isNotFound());
        }

        // d4
        @Test
        public void testJobNotFound() throws Exception {
            mockMvc.perform(get("/api/job/" + getJobId()))
                    .andExpect(status().isNotFound());
        }



    }




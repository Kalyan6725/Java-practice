package org.northernarc.testingassignment.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.northernarc.testingassignment.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
class SecurityTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void cleanDatabase() {
        mockMvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        customerRepository.deleteAll();
    }

    @Test
    void publicAuthRegisterEndpoint_allowsUnauthenticatedAccess() throws Exception {
        String email = "security+" + UUID.randomUUID() + "@example.com";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Security Customer",
                                  "email":"%s",
                                  "password":"secret123",
                                  "phone":"9876543210"
                                }
                                """.formatted(email)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.message").value("Registration successful"));
    }

    @Test
    void register_whenPayloadIsInvalid_returnsValidationErrors() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"",
                                  "email":"bad-email",
                                  "password":"123",
                                  "phone":"123"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.email").value("Email must be valid"))
                .andExpect(jsonPath("$.password").value("Password must be at least 6 characters"));
    }

    @Test
    void registerAndLogin_withValidCredentials_returnsToken() throws Exception {
        String email = "login+" + UUID.randomUUID() + "@example.com";
        String token = registerAndExtractToken(email);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"%s",
                                  "password":"secret123"
                                }
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    private String registerAndExtractToken(String email) throws Exception {
        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Test Customer",
                                  "email":"%s",
                                  "password":"secret123",
                                  "phone":"9876543210"
                                }
                                """.formatted(email)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode node = objectMapper.readTree(registerResult.getResponse().getContentAsString());
        return node.get("token").asText();
    }
}

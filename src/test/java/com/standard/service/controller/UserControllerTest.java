package com.standard.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.standard.service.dto.UserDto;
import com.standard.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUserAndGetById() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("Alice Smith")
                .email("alice.smith@example.com")
                .nationalId("SSN-111-222")
                .build();

        // 1. Test POST /api/users (Create User)
        String responseContent = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Alice Smith")))
                .andExpect(jsonPath("$.email", is("alice.smith@example.com")))
                .andExpect(jsonPath("$.nationalId", is("SSN-111-222")))
                .andReturn().getResponse().getContentAsString();

        UserDto createdUser = objectMapper.readValue(responseContent, UserDto.class);

        // 2. Test GET /api/users/{id} (Get User by ID)
        mockMvc.perform(get("/api/users/" + createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdUser.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Alice Smith")))
                .andExpect(jsonPath("$.email", is("alice.smith@example.com")))
                .andExpect(jsonPath("$.nationalId", is("SSN-111-222")));
    }

    @Test
    void testSearchUserByEmail() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("Bob Jones")
                .email("bob.jones@example.com")
                .nationalId("SSN-333-444")
                .build();

        // Create the user first
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        // Test GET /api/users/search/email (Search positive case)
        mockMvc.perform(get("/api/users/search/email")
                        .param("email", "bob.jones@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Bob Jones")))
                .andExpect(jsonPath("$.email", is("bob.jones@example.com")))
                .andExpect(jsonPath("$.nationalId", is("SSN-333-444")));

        // Test GET /api/users/search/email (Search negative case)
        mockMvc.perform(get("/api/users/search/email")
                        .param("email", "notfound@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchUserByNationalId() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("Charlie Brown")
                .email("charlie.brown@example.com")
                .nationalId("SSN-555-666")
                .build();

        // Create the user first
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        // Test GET /api/users/search/national-id (Search positive case)
        mockMvc.perform(get("/api/users/search/national-id")
                        .param("nationalId", "SSN-555-666"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Charlie Brown")))
                .andExpect(jsonPath("$.email", is("charlie.brown@example.com")))
                .andExpect(jsonPath("$.nationalId", is("SSN-555-666")));

        // Test GET /api/users/search/national-id (Search negative case)
        mockMvc.perform(get("/api/users/search/national-id")
                        .param("nationalId", "SSN-000-000"))
                .andExpect(status().isNotFound());
    }
}

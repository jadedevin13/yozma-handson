package org.devin.yozma.qa.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devin.yozma.qa.platform.dto.QuestionRequest;
import org.devin.yozma.qa.platform.model.QuestionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class QuestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPollQuestion_Success() throws Exception {
        // Given
        QuestionRequest request = new QuestionRequest();
        request.setType(QuestionType.POLL);
        request.setText("Favorite color?");
        request.setAnswers(List.of("Red", "Blue", "Green"));

        // When & Then
        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("Favorite color?"))
                .andExpect(jsonPath("$.type").value("POLL"))
                .andExpect(jsonPath("$.answers.length()").value(3));
    }

    @Test
    void createTriviaQuestion_Success() throws Exception {
        // Given
        QuestionRequest request = new QuestionRequest();
        request.setType(QuestionType.TRIVIA);
        request.setText("Programming languages?");
        request.setAnswers(List.of("Java", "Python", "London"));
        request.setCorrectAnswers(Set.of("Java", "Python"));

        // When & Then
        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.correctAnswers").isArray())
                .andExpect(jsonPath("$.correctAnswers.length()").value(2));
    }

    @Test
    void createTriviaQuestion_ValidationFail() throws Exception {
        // Given
        QuestionRequest request = new QuestionRequest();
        request.setType(QuestionType.TRIVIA);
        request.setText("Programming languages?");
        request.setAnswers(List.of("Java", "Python", "London"));
        // Missing correctAnswers

        // When & Then
        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]")
                        .value("Trivia questions must have at least one correct answer"));
    }

    @Test
    void voteOnQuestion_Success() throws Exception {
        // First create a question
        QuestionRequest request = new QuestionRequest();
        request.setType(QuestionType.POLL);
        request.setText("Favorite color?");
        request.setAnswers(List.of("Red", "Blue", "Green"));

        String createResult = mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long questionId = objectMapper.readTree(createResult).get("id").asLong();

        // Then vote on it
        mockMvc.perform(post("/api/questions/" + questionId + "/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votes.Red").value(1));
    }
}
package com.unibuc.gymapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.gymapp.dtos.ExerciseDto;
import com.unibuc.gymapp.services.ExerciseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = ExerciseController.class)
public class ExerciseControllerTest {
    @MockBean
    private ExerciseService exerciseService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser
    public void filterExercisesTest() throws Exception {
        String title = "test";
        ExerciseDto exerciseDto = ExerciseDto.builder()
                .title("test").build();
        when(exerciseService.filterExercises(title)).thenReturn(List.of(exerciseDto));
        MvcResult result = mockMvc.perform(get("/exercises/filter?title={title}", "test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(mapper.writeValueAsString(List.of(exerciseDto)), result.getResponse().getContentAsString());
    }
}

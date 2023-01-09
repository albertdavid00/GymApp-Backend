package com.unibuc.gymapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.gymapp.dtos.GymDto;
import com.unibuc.gymapp.services.GymService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
@WebMvcTest(controllers = GymController.class)
public class GymControllerTest {
    @MockBean
    private GymService gymService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private GymDto gymDto;

    @BeforeEach
    public void setup() {
        gymDto = GymDto.builder()
                .name("gym")
                .program("24/7")
                .location("Bucharest")
                .build();
    }

    @Test
    @WithMockUser
    public void GetGymTest() throws Exception {
        when(gymService.getGym(1L)).thenReturn(gymDto);
        MvcResult result = mockMvc.perform(get("/gyms/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(mapper.writeValueAsString(gymDto), result.getResponse().getContentAsString());
    }
    @Test
    @WithMockUser
    public void GetAllGymsTest() throws Exception {
        List<GymDto> gyms = List.of(gymDto);

        when(gymService.getAllGyms()).thenReturn(gyms);
        MvcResult result = mockMvc.perform(get("/gyms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(mapper.writeValueAsString(gyms), result.getResponse().getContentAsString());
    }
}

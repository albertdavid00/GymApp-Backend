package com.unibuc.gymapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.gymapp.dtos.UserDto;
import com.unibuc.gymapp.services.UserService;
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
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser
    public void getUsers() throws Exception {
        List<UserDto> users = List.of(UserDto.builder().build());
        when(userService.getUsers()).thenReturn(users);
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(mapper.writeValueAsString(users), result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    public void getUser() throws Exception {
        UserDto userDto = UserDto.builder().build();
        when(userService.getUser(1L)).thenReturn(userDto);
        MvcResult result = mockMvc.perform(get("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(mapper.writeValueAsString(userDto), result.getResponse().getContentAsString());

    }
}

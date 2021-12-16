package com.geoschmitt.bugtracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String ENDPOINT = "/auth";

    @Test
    public void shouldReturn200_auth() throws Exception {
        URI uri = new URI(ENDPOINT);
        String json = "{\"email\":\"dev@email.com\",\"password\":\"123456\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    public void shouldReturn400_auth() throws Exception {
        URI uri = new URI(ENDPOINT);
        String json = "{\"email\":\"invalid@email.com\",\"password\":\"123456\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));

    }

}

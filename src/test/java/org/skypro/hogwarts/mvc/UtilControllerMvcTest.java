package org.skypro.hogwarts.mvc;

import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.controller.UtilController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UtilController.class)
class UtilControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculateSum_ShouldReturnCorrectValue() throws Exception {
        mockMvc.perform(get("/util/sum-million"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(500000500000L));
    }

    @Test
    void calculateSumSlow_ShouldReturnCorrectValue() throws Exception {
        mockMvc.perform(get("/util/sum-million-slow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(500000500000L));
    }
}
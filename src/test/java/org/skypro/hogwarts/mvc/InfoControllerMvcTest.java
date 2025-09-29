package org.skypro.hogwarts.mvc;

import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.controller.InfoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InfoController.class)
@TestPropertySource(properties = "server.port=8080")
class InfoControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getServerPort_ShouldReturnPort() throws Exception {
        mockMvc.perform(get("/port"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(8080));
    }
}
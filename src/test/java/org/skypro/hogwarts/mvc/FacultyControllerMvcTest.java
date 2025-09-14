package org.skypro.hogwarts.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.controller.FacultyController;
import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    private final Faculty testFaculty = new Faculty("Гриффиндор", "красный");

    @Test
    void createFaculty_ShouldReturnCreatedFaculty() throws Exception {

        testFaculty.setId(1L);
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(testFaculty);


        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("красный"));
    }

    @Test
    void getFaculty_WhenFacultyExists_ShouldReturnFaculty() throws Exception {

        testFaculty.setId(1L);
        when(facultyService.getFacultyById(1L)).thenReturn(testFaculty);


        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("красный"));
    }

    @Test
    void getFaculty_WhenFacultyNotExists_ShouldReturnNotFound() throws Exception {

        when(facultyService.getFacultyById(999L)).thenReturn(null);


        mockMvc.perform(get("/faculty/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFaculty_WhenFacultyExists_ShouldReturnUpdatedFaculty() throws Exception {

        Faculty updatedFaculty = new Faculty("Слизерин", "зеленый");
        updatedFaculty.setId(1L);
        when(facultyService.updateFaculty(anyLong(), any(Faculty.class))).thenReturn(updatedFaculty);


        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Слизерин"))
                .andExpect(jsonPath("$.color").value("зеленый"));
    }

    @Test
    void deleteFaculty_ShouldReturnNoContent() throws Exception {

        doNothing().when(facultyService).deleteFaculty(1L);


        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getFacultiesByColor_ShouldReturnFacultiesList() throws Exception {

        when(facultyService.getFacultiesByColor("красный")).thenReturn(List.of(testFaculty));


        mockMvc.perform(get("/faculty/by-color")
                        .param("color", "красный"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("красный"));
    }

    @Test
    void searchFaculties_ShouldReturnMatchingFaculties() throws Exception {

        when(facultyService.findFacultiesByNameOrColor("Гриффиндор")).thenReturn(List.of(testFaculty));


        mockMvc.perform(get("/faculty/search")
                        .param("search", "Гриффиндор"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("красный"));
    }

    @Test
    void getAllFaculties_ShouldReturnAllFaculties() throws Exception {

        when(facultyService.getAllFaculties()).thenReturn(List.of(testFaculty));


        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("красный"));
    }
}
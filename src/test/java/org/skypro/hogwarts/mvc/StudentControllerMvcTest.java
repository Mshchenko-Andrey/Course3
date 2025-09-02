package org.skypro.hogwarts.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.controller.StudentController;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private final Student testStudent = new Student("Гарри Поттер", 17);

    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {

        testStudent.setId(1L);
        when(studentService.createStudent(any(Student.class))).thenReturn(testStudent);


        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void getStudent_WhenStudentExists_ShouldReturnStudent() throws Exception {

        testStudent.setId(1L);
        when(studentService.getStudentById(1L)).thenReturn(testStudent);


        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void getStudent_WhenStudentNotExists_ShouldReturnNotFound() throws Exception {

        when(studentService.getStudentById(999L)).thenReturn(null);


        mockMvc.perform(get("/student/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent_WhenStudentExists_ShouldReturnUpdatedStudent() throws Exception {

        Student updatedStudent = new Student("Гарри Джеймс Поттер", 18);
        updatedStudent.setId(1L);
        when(studentService.updateStudent(anyLong(), any(Student.class))).thenReturn(updatedStudent);


        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гарри Джеймс Поттер"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void deleteStudent_ShouldReturnNoContent() throws Exception {

        doNothing().when(studentService).deleteStudent(1L);


        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStudentsByAge_ShouldReturnStudentsList() throws Exception {

        when(studentService.getStudentsByAge(17)).thenReturn(List.of(testStudent));


        mockMvc.perform(get("/student/by-age")
                        .param("age", "17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() throws Exception {

        when(studentService.getAllStudents()).thenReturn(List.of(testStudent));


        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[0].age").value(17));
    }
}
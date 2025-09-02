package org.skypro.hogwarts.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StudentControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setName("Гарри Поттер");
        testStudent.setAge(17);
    }

    @Test
    void createStudent_ShouldReturnCreatedStudent() {

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "/student", testStudent, Student.class);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Гарри Поттер", response.getBody().getName());
        assertEquals(17, response.getBody().getAge());
    }

    @Test
    void getStudent_WhenStudentExists_ShouldReturnStudent() {
        // Given
        Student createdStudent = restTemplate.postForObject("/student", testStudent, Student.class);
        Long studentId = createdStudent.getId();


        ResponseEntity<Student> response = restTemplate.getForEntity(
                "/student/" + studentId, Student.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(studentId, response.getBody().getId());
        assertEquals("Гарри Поттер", response.getBody().getName());
    }

    @Test
    void getStudent_WhenStudentNotExists_ShouldReturnNotFound() {

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "/student/9999", Student.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateStudent_WhenStudentExists_ShouldReturnUpdatedStudent() {

        Student createdStudent = restTemplate.postForObject("/student", testStudent, Student.class);
        Long studentId = createdStudent.getId();

        Student updateData = new Student();
        updateData.setName("Гарри Джеймс Поттер");
        updateData.setAge(18);


        HttpEntity<Student> request = new HttpEntity<>(updateData);
        ResponseEntity<Student> response = restTemplate.exchange(
                "/student/" + studentId, HttpMethod.PUT, request, Student.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(studentId, response.getBody().getId());
        assertEquals("Гарри Джеймс Поттер", response.getBody().getName());
        assertEquals(18, response.getBody().getAge());
    }

    @Test
    void deleteStudent_WhenStudentExists_ShouldReturnNoContent() {

        Student createdStudent = restTemplate.postForObject("/student", testStudent, Student.class);
        Long studentId = createdStudent.getId();


        ResponseEntity<Void> response = restTemplate.exchange(
                "/student/" + studentId, HttpMethod.DELETE, null, Void.class);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getStudentsByAge_ShouldReturnStudentsList() {

        restTemplate.postForObject("/student", testStudent, Student.class);


        ResponseEntity<List> response = restTemplate.getForEntity(
                "/student/by-age?age=17", List.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() {

        restTemplate.postForObject("/student", testStudent, Student.class);


        ResponseEntity<List> response = restTemplate.getForEntity("/student", List.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}
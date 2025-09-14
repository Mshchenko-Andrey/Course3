package org.skypro.hogwarts.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.model.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FacultyControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        testFaculty = new Faculty();
        testFaculty.setName("Гриффиндор");
        testFaculty.setColor("красный");
    }

    @Test
    void createFaculty_ShouldReturnCreatedFaculty() {

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "/faculty", testFaculty, Faculty.class);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Гриффиндор", response.getBody().getName());
        assertEquals("красный", response.getBody().getColor());
    }

    @Test
    void getFaculty_WhenFacultyExists_ShouldReturnFaculty() {
        // Given
        Faculty createdFaculty = restTemplate.postForObject("/faculty", testFaculty, Faculty.class);
        Long facultyId = createdFaculty.getId();


        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/" + facultyId, Faculty.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(facultyId, response.getBody().getId());
        assertEquals("Гриффиндор", response.getBody().getName());
    }

    @Test
    void getFaculty_WhenFacultyNotExists_ShouldReturnNotFound() {

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/9999", Faculty.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateFaculty_WhenFacultyExists_ShouldReturnUpdatedFaculty() {

        Faculty createdFaculty = restTemplate.postForObject("/faculty", testFaculty, Faculty.class);
        Long facultyId = createdFaculty.getId();

        Faculty updateData = new Faculty();
        updateData.setName("Слизерин");
        updateData.setColor("зеленый");


        HttpEntity<Faculty> request = new HttpEntity<>(updateData);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty/" + facultyId, HttpMethod.PUT, request, Faculty.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(facultyId, response.getBody().getId());
        assertEquals("Слизерин", response.getBody().getName());
        assertEquals("зеленый", response.getBody().getColor());
    }

    @Test
    void deleteFaculty_WhenFacultyExists_ShouldReturnNoContent() {

        Faculty createdFaculty = restTemplate.postForObject("/faculty", testFaculty, Faculty.class);
        Long facultyId = createdFaculty.getId();


        ResponseEntity<Void> response = restTemplate.exchange(
                "/faculty/" + facultyId, HttpMethod.DELETE, null, Void.class);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getFacultiesByColor_ShouldReturnFacultiesList() {

        restTemplate.postForObject("/faculty", testFaculty, Faculty.class);


        ResponseEntity<List> response = restTemplate.getForEntity(
                "/faculty/by-color?color=красный", List.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void searchFaculties_ShouldReturnMatchingFaculties() {

        restTemplate.postForObject("/faculty", testFaculty, Faculty.class);


        ResponseEntity<List> response = restTemplate.getForEntity(
                "/faculty/search?search=Гриффиндор", List.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getAllFaculties_ShouldReturnAllFaculties() {

        restTemplate.postForObject("/faculty", testFaculty, Faculty.class);


        ResponseEntity<List> response = restTemplate.getForEntity("/faculty", List.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}
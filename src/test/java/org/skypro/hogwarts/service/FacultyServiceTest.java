package org.skypro.hogwarts.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.repository.FacultyRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;

    @Test
    void createFaculty_ShouldReturnCreatedFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "красный");
        Faculty savedFaculty = new Faculty("Гриффиндор", "красный");
        savedFaculty.setId(1L);

        when(facultyRepository.save(faculty)).thenReturn(savedFaculty);

        Faculty result = facultyService.createFaculty(faculty);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Гриффиндор", result.getName());
        assertEquals("красный", result.getColor());
        verify(facultyRepository, times(1)).save(faculty);
    }

    @Test
    void getFacultyById_WhenFacultyExists_ShouldReturnFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "красный");
        faculty.setId(1L);

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        Faculty result = facultyService.getFacultyById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Гриффиндор", result.getName());
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void getFacultyById_WhenFacultyNotExists_ShouldReturnNull() {
        when(facultyRepository.findById(999L)).thenReturn(Optional.empty());

        Faculty result = facultyService.getFacultyById(999L);

        assertNull(result);
        verify(facultyRepository, times(1)).findById(999L);
    }

    @Test
    void updateFaculty_WhenFacultyExists_ShouldReturnUpdatedFaculty() {
        Faculty existingFaculty = new Faculty("Гриффиндор", "красный");
        existingFaculty.setId(1L);
        Faculty updateData = new Faculty("Слизерин", "зеленый");
        updateData.setId(1L);

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(existingFaculty));
        when(facultyRepository.save(updateData)).thenReturn(updateData);

        Faculty result = facultyService.updateFaculty(1L, updateData);

        assertNotNull(result);
        assertEquals("Слизерин", result.getName());
        assertEquals("зеленый", result.getColor());
        verify(facultyRepository, times(1)).findById(1L);
        verify(facultyRepository, times(1)).save(updateData);
    }

    @Test
    void getAllFaculties_ShouldReturnAllFaculties() {
        List<Faculty> faculties = List.of(
                new Faculty("Гриффиндор", "красный"),
                new Faculty("Слизерин", "зеленый")
        );

        when(facultyRepository.findAll()).thenReturn(faculties);

        List<Faculty> result = facultyService.getAllFaculties();

        assertEquals(2, result.size());
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void getFacultiesByColor_ShouldReturnFilteredFaculties() {
        List<Faculty> faculties = List.of(new Faculty("Гриффиндор", "красный"));

        when(facultyRepository.findByColor("красный")).thenReturn(faculties);

        List<Faculty> result = facultyService.getFacultiesByColor("красный");

        assertEquals(1, result.size());
        verify(facultyRepository, times(1)).findByColor("красный");
    }

    @Test
    void getFacultyStudents_WhenFacultyExists_ShouldReturnStudents() {
        Faculty faculty = new Faculty("Гриффиндор", "красный");
        faculty.setId(1L);
        Student student1 = new Student("Гарри Поттер", 17);
        Student student2 = new Student("Гермиона Грейнджер", 17);
        faculty.setStudents(List.of(student1, student2));

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        List<Student> result = facultyService.getFacultyStudents(1L);

        assertEquals(2, result.size());
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void getLongestFacultyName_ShouldReturnLongestName() {
        List<Faculty> faculties = List.of(
                new Faculty("Гриф", "красный"),
                new Faculty("Слизеринский", "зеленый"),
                new Faculty("Ког", "синий")
        );

        when(facultyRepository.findAll()).thenReturn(faculties);

        String result = facultyService.getLongestFacultyName();

        assertEquals("Слизеринский", result);
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void getLongestFacultyName_WhenNoFaculties_ShouldReturnDefault() {
        when(facultyRepository.findAll()).thenReturn(List.of());

        String result = facultyService.getLongestFacultyName();

        assertEquals("Факультеты не найдены", result);
        verify(facultyRepository, times(1)).findAll();
    }
}
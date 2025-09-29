package org.skypro.hogwarts.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void createStudent_ShouldReturnCreatedStudent() {
        Student student = new Student("Гарри Поттер", 17);
        Student savedStudent = new Student("Гарри Поттер", 17);
        savedStudent.setId(1L);

        when(studentRepository.save(student)).thenReturn(savedStudent);

        Student result = studentService.createStudent(student);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Гарри Поттер", result.getName());
        assertEquals(17, result.getAge());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void getStudentById_WhenStudentExists_ShouldReturnStudent() {
        Student student = new Student("Гарри Поттер", 17);
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Гарри Поттер", result.getName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getStudentById_WhenStudentNotExists_ShouldReturnNull() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        Student result = studentService.getStudentById(999L);

        assertNull(result);
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    void updateStudent_WhenStudentExists_ShouldReturnUpdatedStudent() {
        Student existingStudent = new Student("Гарри Поттер", 17);
        existingStudent.setId(1L);
        Student updateData = new Student("Гарри Джеймс Поттер", 18);
        updateData.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(updateData)).thenReturn(updateData);

        Student result = studentService.updateStudent(1L, updateData);

        assertNotNull(result);
        assertEquals("Гарри Джеймс Поттер", result.getName());
        assertEquals(18, result.getAge());
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(updateData);
    }

    @Test
    void updateStudent_WhenStudentNotExists_ShouldReturnNull() {
        Student updateData = new Student("Гарри Поттер", 17);

        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        Student result = studentService.updateStudent(999L, updateData);

        assertNull(result);
        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_ShouldCallRepository() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() {
        List<Student> students = List.of(
                new Student("Гарри Поттер", 17),
                new Student("Гермиона Грейнджер", 17)
        );

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentsByAge_ShouldReturnFilteredStudents() {
        List<Student> students = List.of(new Student("Гарри Поттер", 17));

        when(studentRepository.findByAge(17)).thenReturn(students);

        List<Student> result = studentService.getStudentsByAge(17);

        assertEquals(1, result.size());
        verify(studentRepository, times(1)).findByAge(17);
    }

    @Test
    void getStudentFaculty_WhenStudentHasFaculty_ShouldReturnFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "красный");
        faculty.setId(1L);
        Student student = new Student("Гарри Поттер", 17);
        student.setId(1L);
        student.setFaculty(faculty);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Faculty result = studentService.getStudentFaculty(1L);

        assertNotNull(result);
        assertEquals("Гриффиндор", result.getName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getStudentNamesStartingWithA_ShouldReturnFilteredNames() {
        List<Student> students = List.of(
                new Student("Анна", 17),
                new Student("Борис", 18),
                new Student("Алексей", 19)
        );

        when(studentRepository.findAll()).thenReturn(students);

        List<String> result = studentService.getStudentNamesStartingWithA();

        assertEquals(2, result.size());
        assertTrue(result.contains("АННА"));
        assertTrue(result.contains("АЛЕКСЕЙ"));
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getAverageAgeWithStream_ShouldReturnCorrectAverage() {
        List<Student> students = List.of(
                new Student("Гарри", 17),
                new Student("Гермиона", 18),
                new Student("Рон", 19)
        );

        when(studentRepository.findAll()).thenReturn(students);

        Double result = studentService.getAverageAgeWithStream();

        assertEquals(18.0, result, 0.01);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void printStudentsParallel_ShouldPrintStudents() {
        List<Student> students = List.of(
                createStudent(1L, "Гарри Поттер"),
                createStudent(2L, "Гермиона Грейнджер"),
                createStudent(3L, "Рон Уизли"),
                createStudent(4L, "Драко Малфой"),
                createStudent(5L, "Невилл Лонгботтом"),
                createStudent(6L, "Луна Лавгуд")
        );

        when(studentRepository.findAll()).thenReturn(students);

        studentService.printStudentsParallel();

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void printStudentsSynchronized_ShouldPrintStudents() {
        List<Student> students = List.of(
                createStudent(1L, "Гарри Поттер"),
                createStudent(2L, "Гермиона Грейнджер"),
                createStudent(3L, "Рон Уизли"),
                createStudent(4L, "Драко Малфой"),
                createStudent(5L, "Невилл Лонгботтом"),
                createStudent(6L, "Луна Лавгуд")
        );

        when(studentRepository.findAll()).thenReturn(students);

        studentService.printStudentsSynchronized();

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void printStudentsParallel_WhenNotEnoughStudents_ShouldNotPrint() {
        List<Student> students = List.of(
                createStudent(1L, "Гарри Поттер"),
                createStudent(2L, "Гермиона Грейнджер")
        );

        when(studentRepository.findAll()).thenReturn(students);

        studentService.printStudentsParallel();

        verify(studentRepository, times(1)).findAll();
    }

    private Student createStudent(Long id, String name) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(17);
        return student;
    }
}
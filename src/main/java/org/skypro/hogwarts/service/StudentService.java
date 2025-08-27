package org.skypro.hogwarts.service;

import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, Student student) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            student.setId(id);
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> getStudentsByNameContaining(String namePart) {
        return studentRepository.findByNameContainingIgnoreCase(namePart);
    }

    public List<Student> getStudentsWithAgeLessThanId() {
        return studentRepository.findByAgeLessThan(0L);
    }

    public List<Student> getStudentsOrderedByAge() {
        return studentRepository.findAllByOrderByAgeAsc();
    }


    public Faculty getStudentFaculty(Long studentId) {
        Student student = getStudentById(studentId);
        return student != null ? student.getFaculty() : null;
    }
}
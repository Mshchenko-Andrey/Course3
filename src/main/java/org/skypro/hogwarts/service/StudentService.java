package org.skypro.hogwarts.service;

import org.springframework.stereotype.Service;
import org.skypro.hogwarts.model.Student;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long counter = 1L;

    public Student createStudent(Student student) {
        student.setId(counter++);
        students.put(student.getId(), student);
        return student;
    }

    public Student getStudentById(Long id) {
        return students.get(id);
    }

    public Student updateStudent(Long id, Student student) {
        if (!students.containsKey(id)) {
            return null;
        }
        student.setId(id);
        students.put(id, student);
        return student;
    }

    public Student deleteStudent(Long id) {
        return students.remove(id);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Collection<Student> getStudentsByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
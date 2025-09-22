package org.skypro.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        logger.info("StudentService initialized");
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Creating student: name={}, age={}", student.getName(), student.getAge());

        Student createdStudent = studentRepository.save(student);
        logger.info("Student created successfully with id: {}", createdStudent.getId());
        return createdStudent;
    }

    public Student getStudentById(Long id) {
        logger.info("Was invoked method for get student by id: {}", id);

        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            logger.debug("Student found: id={}, name={}", id, student.get().getName());
            return student.get();
        } else {
            logger.warn("Student not found with id: {}", id);
            return null;
        }
    }

    public Optional<Student> findStudentById(Long id) {
        logger.debug("Finding student by id: {}", id);
        return studentRepository.findById(id);
    }

    public Student updateStudent(Long id, Student student) {
        logger.info("Was invoked method for update student with id: {}", id);

        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            logger.debug("Updating student: id={}, new name={}, new age={}",
                    id, student.getName(), student.getAge());

            student.setId(id);
            Student updatedStudent = studentRepository.save(student);
            logger.info("Student updated successfully: id={}", id);
            return updatedStudent;
        }

        logger.error("Cannot update student: Student not found with id = {}", id);
        return null;
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student with id: {}", id);

        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            logger.info("Student deleted successfully: id={}", id);
        } else {
            logger.error("Cannot delete student: Student not found with id = {}", id);
        }
    }

    public List<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");

        List<Student> students = studentRepository.findAll();
        logger.debug("Retrieved {} students", students.size());
        return students;
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get students by age: {}", age);
        return studentRepository.findByAge(age);
    }

    public List<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for get students by age between: {} and {}", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> getStudentsByNameContaining(String namePart) {
        logger.info("Was invoked method for get students by name containing: {}", namePart);
        return studentRepository.findByNameContainingIgnoreCase(namePart);
    }

    public List<Student> getStudentsWithAgeLessThan(int age) {
        logger.info("Was invoked method for get students with age less than: {}", age);
        return studentRepository.findByAgeLessThan(age);
    }

    public List<Student> getStudentsOrderedByAge() {
        logger.info("Was invoked method for get students ordered by age");
        return studentRepository.findAllByOrderByAgeAsc();
    }

    public Faculty getStudentFaculty(Long studentId) {
        logger.info("Was invoked method for get student faculty by student id: {}", studentId);

        Student student = getStudentById(studentId);
        if (student != null && student.getFaculty() != null) {
            logger.debug("Faculty found for student {}: {}", studentId, student.getFaculty().getName());
            return student.getFaculty();
        }

        logger.warn("Faculty not found for student id: {}", studentId);
        return null;
    }

    public Integer getTotalStudentCount() {
        logger.info("Was invoked method for get total student count");

        Integer count = studentRepository.countAllStudents();
        logger.debug("Total student count: {}", count);
        return count;
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average student age");

        Double averageAge = studentRepository.findAverageAge();
        logger.debug("Average student age: {}", averageAge);
        return averageAge;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");

        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Retrieved {} last students", students.size());
        return students;
    }
}
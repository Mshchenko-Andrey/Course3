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
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        logger.info("Сервис студентов инициализирован");
    }

    public Student createStudent(Student student) {
        logger.info("Был вызван метод для создания студента");
        logger.debug("Создание студента: имя={}, возраст={}", student.getName(), student.getAge());

        Student createdStudent = studentRepository.save(student);
        logger.info("Студент успешно создан с id: {}", createdStudent.getId());
        return createdStudent;
    }

    public Student getStudentById(Long id) {
        logger.info("Был вызван метод для получения студента по id: {}", id);

        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            logger.debug("Студент найден: id={}, имя={}", id, student.get().getName());
            return student.get();
        } else {
            logger.warn("Студент не найден с id: {}", id);
            return null;
        }
    }

    public Optional<Student> findStudentById(Long id) {
        logger.debug("Поиск студента по id: {}", id);
        return studentRepository.findById(id);
    }

    public Student updateStudent(Long id, Student student) {
        logger.info("Был вызван метод для обновления студента с id: {}", id);

        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            logger.debug("Обновление студента: id={}, новое имя={}, новый возраст={}",
                    id, student.getName(), student.getAge());

            student.setId(id);
            Student updatedStudent = studentRepository.save(student);
            logger.info("Студент успешно обновлен: id={}", id);
            return updatedStudent;
        }

        logger.error("Невозможно обновить студента: Студент не найден с id = {}", id);
        return null;
    }

    public void deleteStudent(Long id) {
        logger.info("Был вызван метод для удаления студента с id: {}", id);

        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            logger.info("Студент успешно удален: id={}", id);
        } else {
            logger.error("Невозможно удалить студента: Студент не найден с id = {}", id);
        }
    }

    public List<Student> getAllStudents() {
        logger.info("Был вызван метод для получения всех студентов");

        List<Student> students = studentRepository.findAll();
        logger.debug("Получено {} студентов", students.size());
        return students;
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Был вызван метод для получения студентов по возрасту: {}", age);
        return studentRepository.findByAge(age);
    }

    public List<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Был вызван метод для получения студентов по возрасту между: {} и {}", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> getStudentsByNameContaining(String namePart) {
        logger.info("Был вызван метод для получения студентов по имени содержащему: {}", namePart);
        return studentRepository.findByNameContainingIgnoreCase(namePart);
    }

    public List<Student> getStudentsWithAgeLessThan(int age) {
        logger.info("Был вызван метод для получения студентов с возрастом меньше: {}", age);
        return studentRepository.findByAgeLessThan(age);
    }

    public List<Student> getStudentsOrderedByAge() {
        logger.info("Был вызван метод для получения студентов отсортированных по возрасту");
        return studentRepository.findAllByOrderByAgeAsc();
    }

    public Faculty getStudentFaculty(Long studentId) {
        logger.info("Был вызван метод для получения факультета студента по id: {}", studentId);

        Student student = getStudentById(studentId);
        if (student != null && student.getFaculty() != null) {
            logger.debug("Факультет найден для студента {}: {}", studentId, student.getFaculty().getName());
            return student.getFaculty();
        }

        logger.warn("Факультет не найден для студента с id: {}", studentId);
        return null;
    }

    public Integer getTotalStudentCount() {
        logger.info("Был вызван метод для получения общего количества студентов");

        Integer count = studentRepository.countAllStudents();
        logger.debug("Общее количество студентов: {}", count);
        return count;
    }

    public Double getAverageAge() {
        logger.info("Был вызван метод для получения среднего возраста студентов");

        Double averageAge = studentRepository.findAverageAge();
        logger.debug("Средний возраст студентов: {}", averageAge);
        return averageAge;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Был вызван метод для получения последних пяти студентов");

        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Получено {} последних студентов", students.size());
        return students;
    }

    public List<String> getStudentNamesStartingWithA() {
        logger.info("Был вызван метод для получения имен студентов, начинающихся на 'А'");

        List<String> names = studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.toUpperCase().startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());

        logger.debug("Найдено {} имен, начинающихся на 'А'", names.size());
        return names;
    }

    public Double getAverageAgeWithStream() {
        logger.info("Был вызван метод для получения среднего возраста студентов через Stream API");

        Double averageAge = studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);

        logger.debug("Средний возраст студентов через Stream: {}", averageAge);
        return averageAge;
    }
}
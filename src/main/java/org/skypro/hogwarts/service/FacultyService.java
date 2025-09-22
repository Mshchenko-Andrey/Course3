package org.skypro.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
        logger.info("Сервис факультетов инициализирован");
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Был вызван метод для создания факультета");
        logger.debug("Создание факультета: название={}, цвет={}", faculty.getName(), faculty.getColor());

        Faculty createdFaculty = facultyRepository.save(faculty);
        logger.info("Факультет успешно создан с id: {}", createdFaculty.getId());
        return createdFaculty;
    }

    public Faculty getFacultyById(Long id) {
        logger.info("Был вызван метод для получения факультета по id: {}", id);

        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isPresent()) {
            logger.debug("Факультет найден: id={}, название={}", id, faculty.get().getName());
            return faculty.get();
        } else {
            logger.warn("Факультет не найден с id: {}", id);
            return null;
        }
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.info("Был вызван метод для обновления факультета с id: {}", id);

        Optional<Faculty> existingFaculty = facultyRepository.findById(id);
        if (existingFaculty.isPresent()) {
            logger.debug("Обновление факультета: id={}, новое название={}, новый цвет={}",
                    id, faculty.getName(), faculty.getColor());

            faculty.setId(id);
            Faculty updatedFaculty = facultyRepository.save(faculty);
            logger.info("Факультет успешно обновлен: id={}", id);
            return updatedFaculty;
        }

        logger.error("Невозможно обновить факультет: Факультет не найден с id = {}", id);
        return null;
    }

    public void deleteFaculty(Long id) {
        logger.info("Был вызван метод для удаления факультета с id: {}", id);

        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            logger.info("Факультет успешно удален: id={}", id);
        } else {
            logger.error("Невозможно удалить факультет: Факультет не найден с id = {}", id);
        }
    }

    public List<Faculty> getAllFaculties() {
        logger.info("Был вызван метод для получения всех факультетов");

        List<Faculty> faculties = facultyRepository.findAll();
        logger.debug("Получено {} факультетов", faculties.size());
        return faculties;
    }

    public List<Faculty> getFacultiesByColor(String color) {
        logger.info("Был вызван метод для получения факультетов по цвету: {}", color);
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> findFacultiesByNameOrColor(String searchTerm) {
        logger.info("Был вызван метод для поиска факультетов по названию или цвету: {}", searchTerm);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(searchTerm, searchTerm);
    }

    public List<Student> getFacultyStudents(Long facultyId) {
        logger.info("Был вызван метод для получения студентов факультета по id: {}", facultyId);

        Faculty faculty = getFacultyById(facultyId);
        if (faculty != null) {
            List<Student> students = faculty.getStudents();
            logger.debug("Получено {} студентов для факультета {}", students.size(), facultyId);
            return students;
        }

        logger.warn("Невозможно получить студентов: Факультет не найден с id = {}", facultyId);
        return List.of();
    }
}
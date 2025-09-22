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
        logger.info("FacultyService initialized");
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        logger.debug("Creating faculty: name={}, color={}", faculty.getName(), faculty.getColor());

        Faculty createdFaculty = facultyRepository.save(faculty);
        logger.info("Faculty created successfully with id: {}", createdFaculty.getId());
        return createdFaculty;
    }

    public Faculty getFacultyById(Long id) {
        logger.info("Was invoked method for get faculty by id: {}", id);

        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isPresent()) {
            logger.debug("Faculty found: id={}, name={}", id, faculty.get().getName());
            return faculty.get();
        } else {
            logger.warn("Faculty not found with id: {}", id);
            return null;
        }
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.info("Was invoked method for update faculty with id: {}", id);

        Optional<Faculty> existingFaculty = facultyRepository.findById(id);
        if (existingFaculty.isPresent()) {
            logger.debug("Updating faculty: id={}, new name={}, new color={}",
                    id, faculty.getName(), faculty.getColor());

            faculty.setId(id);
            Faculty updatedFaculty = facultyRepository.save(faculty);
            logger.info("Faculty updated successfully: id={}", id);
            return updatedFaculty;
        }

        logger.error("Cannot update faculty: Faculty not found with id = {}", id);
        return null;
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty with id: {}", id);

        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            logger.info("Faculty deleted successfully: id={}", id);
        } else {
            logger.error("Cannot delete faculty: Faculty not found with id = {}", id);
        }
    }

    public List<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");

        List<Faculty> faculties = facultyRepository.findAll();
        logger.debug("Retrieved {} faculties", faculties.size());
        return faculties;
    }

    public List<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method for get faculties by color: {}", color);
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> findFacultiesByNameOrColor(String searchTerm) {
        logger.info("Was invoked method for find faculties by name or color: {}", searchTerm);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(searchTerm, searchTerm);
    }

    public List<Student> getFacultyStudents(Long facultyId) {
        logger.info("Was invoked method for get faculty students by faculty id: {}", facultyId);

        Faculty faculty = getFacultyById(facultyId);
        if (faculty != null) {
            List<Student> students = faculty.getStudents();
            logger.debug("Retrieved {} students for faculty {}", students.size(), facultyId);
            return students;
        }

        logger.warn("Cannot get students: Faculty not found with id = {}", facultyId);
        return List.of();
    }
}
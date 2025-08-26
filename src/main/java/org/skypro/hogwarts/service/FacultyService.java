package org.skypro.hogwarts.service;

import org.springframework.stereotype.Service;
import org.skypro.hogwarts.model.Faculty;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long counter = 1L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(counter++);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFacultyById(Long id) {
        return faculties.get(id);
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        if (!faculties.containsKey(id)) {
            return null;
        }
        faculty.setId(id);
        faculties.put(id, faculty);
        return faculty;
    }

    public Faculty deleteFaculty(Long id) {
        return faculties.remove(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}
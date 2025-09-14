package org.skypro.hogwarts.repository;

import org.skypro.hogwarts.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int minAge, int maxAge);
    List<Student> findByNameContainingIgnoreCase(String namePart);
    List<Student> findByAgeLessThan(int age);
    List<Student> findAllByOrderByAgeAsc();


    @Query("SELECT COUNT(s) FROM Student s")
    Integer countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double findAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC LIMIT 5")
    List<Student> findLastFiveStudents();
}
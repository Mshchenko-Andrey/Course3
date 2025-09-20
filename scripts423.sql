-- 1. Получение информации обо всех студентах с названиями факультетов
SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM students s
LEFT JOIN faculties f ON s.faculty_id = f.id
ORDER BY s.name;

-- 2. Получение студентов, у которых есть аватарки
SELECT
    s.name AS student_name,
    s.age AS student_age,
    a.file_path AS avatar_path
FROM students s
INNER JOIN avatars a ON s.id = a.student_id
ORDER BY s.name;
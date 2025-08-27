-- 1.Получить всех студентов, возраст которых находится между 10 и 20
SELECT * FROM students WHERE age BETWEEN 10 AND 20;

-- 2. Получить всех студентов, но отобразить только список их имен
SELECT name FROM students;

-- 3. Получить всех студентов, у которых в имени присутствует буква 'О'
SELECT * FROM students WHERE name ILIKE '%о%';

-- 4. Получить всех студентов, у которых возраст меньше идентификатора
SELECT * FROM students WHERE age < id;

-- 5. Получить всех студентов упорядоченных по возрасту
SELECT * FROM students ORDER BY age ASC;
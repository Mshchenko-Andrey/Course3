-- Ограничения для таблицы students
-- 1. Возраст студента не может быть меньше 16 лет
ALTER TABLE students
ADD CONSTRAINT age_check CHECK (age >= 16);

-- 2. Имена студентов должны быть уникальными и не равны нулю
ALTER TABLE students
ALTER COLUMN name SET NOT NULL;

ALTER TABLE students
ADD CONSTRAINT name_unique UNIQUE (name);

-- 3. При создании студента без возраста ему автоматически должно присваиваться 20 лет
ALTER TABLE students
ALTER COLUMN age SET DEFAULT 20;

-- Ограничения для таблицы faculties
-- 4. Пара "значение названия" - "цвет факультета" должна быть уникальной
ALTER TABLE faculties
ADD CONSTRAINT name_color_unique UNIQUE (name, color);
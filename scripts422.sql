-- Создание таблицы "машина"
CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price DECIMAL(12, 2) NOT NULL CHECK (price > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы "человек"
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL CHECK (age >= 18),
    has_license BOOLEAN DEFAULT FALSE,
    car_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Внешний ключ для связи с таблицей car
    CONSTRAINT fk_person_car
        FOREIGN KEY (car_id)
        REFERENCES car(id)
        ON DELETE SET NULL
);

-- Создание индексов для улучшения производительности
CREATE INDEX idx_person_car_id ON person(car_id);
CREATE INDEX idx_person_name ON person(name);
CREATE INDEX idx_car_brand_model ON car(brand, model);
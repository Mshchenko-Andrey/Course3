package org.skypro.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.skypro.hogwarts.model.Avatar;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${avatars.dir.path}")
    private String avatarsDirPath;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
        logger.info("Сервис аватарок инициализирован с директорией: {}", avatarsDirPath);
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Был вызван метод для загрузки аватарки для студента с id: {}", studentId);
        logger.debug("Детали файла: имя={}, размер={}, тип={}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        Student student = studentService.findStudentById(studentId)
                .orElseThrow(() -> {
                    logger.error("Невозможно загрузить аватарку: Студент не найден с id = {}", studentId);
                    return new RuntimeException("Студент не найден с id: " + studentId);
                });

        Path directoryPath = Path.of(avatarsDirPath);
        if (!Files.exists(directoryPath)) {
            logger.info("Создание директории для аватарок: {}", directoryPath);
            Files.createDirectories(directoryPath);
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = "avatar_" + studentId + fileExtension;
        Path filePath = directoryPath.resolve(fileName);

        logger.debug("Сохранение аватарки в файл: {}", filePath);
        Files.write(filePath, file.getBytes());

        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        Avatar savedAvatar = avatarRepository.save(avatar);
        logger.info("Аватарка успешно загружена для студента {}: id аватарки={}", studentId, savedAvatar.getId());
        return savedAvatar;
    }

    public Optional<Avatar> getAvatarFromDb(Long id) {
        logger.debug("Получение аватарки из базы данных по id: {}", id);
        return avatarRepository.findById(id);
    }

    public Optional<Avatar> getAvatarByStudentId(Long studentId) {
        logger.debug("Получение аватарки по id студента: {}", studentId);
        return avatarRepository.findByStudentId(studentId);
    }

    public byte[] getAvatarImageFromFile(Long studentId) throws IOException {
        logger.info("Был вызван метод для получения изображения аватарки из файла для студента с id: {}", studentId);

        Optional<Avatar> avatarOptional = avatarRepository.findByStudentId(studentId);
        if (avatarOptional.isPresent()) {
            Avatar avatar = avatarOptional.get();
            Path filePath = Path.of(avatar.getFilePath());
            if (Files.exists(filePath)) {
                logger.debug("Чтение файла аватарки: {}", filePath);
                return Files.readAllBytes(filePath);
            }
            logger.error("Файл аватарки не найден для студента с id: {}", studentId);
            throw new RuntimeException("Файл аватарки не найден для студента с id: " + studentId);
        }
        logger.error("Аватарка не найдена в базе данных для студента с id: {}", studentId);
        throw new RuntimeException("Аватарка не найдена в базе данных для студента с id: " + studentId);
    }


    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Был вызван метод для получения всех аватарок с пагинацией: страница={}, размер={}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Avatar> avatars = avatarRepository.findAll(pageable);
        logger.debug("Получено {} аватарок (страница {} из {})",
                avatars.getNumberOfElements(), page + 1, avatars.getTotalPages());
        return avatars;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ".dat";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
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
        logger.info("AvatarService initialized with avatars directory: {}", avatarsDirPath);
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar for student id: {}", studentId);
        logger.debug("File details: name={}, size={}, type={}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        Student student = studentService.findStudentById(studentId)
                .orElseThrow(() -> {
                    logger.error("Cannot upload avatar: Student not found with id = {}", studentId);
                    return new RuntimeException("Student not found with id: " + studentId);
                });

        Path directoryPath = Path.of(avatarsDirPath);
        if (!Files.exists(directoryPath)) {
            logger.info("Creating avatars directory: {}", directoryPath);
            Files.createDirectories(directoryPath);
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = "avatar_" + studentId + fileExtension;
        Path filePath = directoryPath.resolve(fileName);

        logger.debug("Saving avatar to file: {}", filePath);
        Files.write(filePath, file.getBytes());

        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        Avatar savedAvatar = avatarRepository.save(avatar);
        logger.info("Avatar uploaded successfully for student {}: avatar id={}", studentId, savedAvatar.getId());
        return savedAvatar;
    }

    public Optional<Avatar> getAvatarFromDb(Long id) {
        logger.debug("Getting avatar from database by id: {}", id);
        return avatarRepository.findById(id);
    }

    public Optional<Avatar> getAvatarByStudentId(Long studentId) {
        logger.debug("Getting avatar by student id: {}", studentId);
        return avatarRepository.findByStudentId(studentId);
    }

    public byte[] getAvatarImageFromFile(Long studentId) throws IOException {
        logger.info("Was invoked method for get avatar image from file for student id: {}", studentId);

        Optional<Avatar> avatarOptional = avatarRepository.findByStudentId(studentId);
        if (avatarOptional.isPresent()) {
            Avatar avatar = avatarOptional.get();
            Path filePath = Path.of(avatar.getFilePath());
            if (Files.exists(filePath)) {
                logger.debug("Reading avatar file: {}", filePath);
                return Files.readAllBytes(filePath);
            }
            logger.error("Avatar file not found for student id: {}", studentId);
            throw new RuntimeException("Avatar file not found for student id: " + studentId);
        }
        logger.error("Avatar not found in database for student id: {}", studentId);
        throw new RuntimeException("Avatar not found in database for student id: " + studentId);
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars with pagination: page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Avatar> avatars = avatarRepository.findAll(pageable);
        logger.debug("Retrieved {} avatars (page {} of {})",
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
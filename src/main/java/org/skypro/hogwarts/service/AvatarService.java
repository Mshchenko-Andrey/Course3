package org.skypro.hogwarts.service;

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
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${avatars.dir.path}")
    private String avatarsDirPath;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.findStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Path directoryPath = Path.of(avatarsDirPath);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = "avatar_" + studentId + fileExtension;
        Path filePath = directoryPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        return avatarRepository.save(avatar);
    }

    public Optional<Avatar> getAvatarFromDb(Long id) {
        return avatarRepository.findById(id);
    }

    public Optional<Avatar> getAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId);
    }

    public byte[] getAvatarImageFromFile(Long studentId) throws IOException {
        Optional<Avatar> avatarOptional = avatarRepository.findByStudentId(studentId);
        if (avatarOptional.isPresent()) {
            Avatar avatar = avatarOptional.get();
            Path filePath = Path.of(avatar.getFilePath());
            if (Files.exists(filePath)) {
                return Files.readAllBytes(filePath);
            }
            throw new RuntimeException("Avatar file not found for student id: " + studentId);
        }
        throw new RuntimeException("Avatar not found in database for student id: " + studentId);
    }


    public Page<Avatar> getAllAvatars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ".dat";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
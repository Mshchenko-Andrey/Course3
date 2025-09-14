package org.skypro.hogwarts.controller;

import org.skypro.hogwarts.model.Avatar;
import org.skypro.hogwarts.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    @Autowired
    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadAvatar(
            @PathVariable Long studentId,
            @RequestParam MultipartFile file) throws IOException {

        Avatar avatar = avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok(avatar.getId());
    }

    @GetMapping("/{id}/from-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long id) {
        Optional<Avatar> avatarOptional = avatarService.getAvatarFromDb(id);
        if (avatarOptional.isPresent()) {
            Avatar avatar = avatarOptional.get();
            HttpHeaders headers = prepareHeaders(avatar);
            return new ResponseEntity<>(avatar.getData(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{studentId}/from-file")
    public ResponseEntity<byte[]> getAvatarFromFile(@PathVariable Long studentId) throws IOException {
        byte[] imageData = avatarService.getAvatarImageFromFile(studentId);
        Optional<Avatar> avatarOptional = avatarService.getAvatarByStudentId(studentId);

        if (avatarOptional.isPresent()) {
            Avatar avatar = avatarOptional.get();
            HttpHeaders headers = prepareHeaders(avatar);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Avatar> getAvatarInfo(@PathVariable Long studentId) {
        Optional<Avatar> avatarOptional = avatarService.getAvatarByStudentId(studentId);
        return avatarOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    private HttpHeaders prepareHeaders(Avatar avatar) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());
        return headers;
    }
}
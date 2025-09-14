package org.skypro.hogwarts.model;

import jakarta.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "avatars")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;
    private long fileSize;
    private String mediaType;

    @Lob
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Avatar() {
    }

    public Avatar(String filePath, long fileSize, String mediaType, byte[] data, Student student) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.student = student;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return fileSize == avatar.fileSize &&
                id.equals(avatar.id) &&
                filePath.equals(avatar.filePath) &&
                mediaType.equals(avatar.mediaType) &&
                Arrays.equals(data, avatar.data) &&
                student.equals(avatar.student);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + filePath.hashCode();
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        result = 31 * result + mediaType.hashCode();
        result = 31 * result + Arrays.hashCode(data);
        result = 31 * result + student.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", dataSize=" + (data != null ? data.length : 0) +
                ", studentId=" + (student != null ? student.getId() : "null") +
                '}';
    }
}
package dev.overlax.agency.service.impl;

import dev.overlax.agency.service.ImageStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private final Path uploadDir;

    public ImageStorageServiceImpl(@Value("${app.upload-dir}") String dir) {
        this.uploadDir = Path.of(dir);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(uploadDir);
            log.info("The directory for images was created successfully, path: {}", uploadDir);
        } catch (IOException e) {
            log.error("Cannot create upload dir: {}", uploadDir);
            throw new IllegalStateException("Cannot create upload dir: " + uploadDir, e);
        }
    }

    @Override
    public String save(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            log.error("File is empty");
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = image.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalName);
        String filename = UUID.randomUUID() + "." + extension;
        Path target = uploadDir.resolve(filename);

        try {
            image.transferTo(target);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + filename, e);
        }

        return filename;
    }

    @Override
    public void delete(String imageName) {

    }
}

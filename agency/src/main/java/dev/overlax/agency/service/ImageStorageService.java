package dev.overlax.agency.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String save(MultipartFile image);

    void delete(String imageName);
}

package dev.vietis.nampd.employee.achievement.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    void init();

    String save(MultipartFile file);

    Resource load(String filename);

    boolean delete(String filename);
}

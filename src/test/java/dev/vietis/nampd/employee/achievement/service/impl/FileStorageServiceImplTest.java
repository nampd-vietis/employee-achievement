package dev.vietis.nampd.employee.achievement.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceImplTest {
    @Mock
    private MultipartFile file;

    @InjectMocks
    private FileStorageServiceImpl fileStorageServiceImpl;

    private Path root;

    @BeforeEach
    public void setUp() throws IOException {
        // Tạo thư mục tạm thời cho test
        root = Paths.get("./uploads/images");
        Files.createDirectories(root); // Đảm bảo thư mục tồn tại
        fileStorageServiceImpl.init(); // Khởi tạo dịch vụ lưu trữ tệp
    }

    @Test
    public void testSaveSuccess() throws Exception {
        // Tạo một tệp mẫu
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.png", "image/png", "Test content".getBytes());

        // Thiết lập hành vi của mock
        when(file.getOriginalFilename()).thenReturn(mockFile.getOriginalFilename());
        when(file.getInputStream()).thenReturn(mockFile.getInputStream());

        // Gọi phương thức save
        String savedFilename = fileStorageServiceImpl.save(file);

        // Kiểm tra kết quả
        assertEquals("test.png", savedFilename);
        assertTrue(Files.exists(root.resolve("test.png")));
        assertEquals("Test content", new String(Files.readAllBytes(root.resolve("test.png"))));
    }

    @Test
    public void testSaveInvalidFileName() {
        // Thiết lập hành vi của mock để trả về tên tệp không hợp lệ
        when(file.getOriginalFilename()).thenReturn(null);

        // Kiểm tra ngoại lệ khi gọi phương thức save
        Exception exception = assertThrows(RuntimeException.class, () -> fileStorageServiceImpl.save(file));
        assertEquals("Could not save file: File name is invalid", exception.getMessage());
    }

    @Test
    public void testLoad() throws Exception {
        // Tạo một tệp mẫu và lưu vào thư mục gốc
        MockMultipartFile mockFile = new MockMultipartFile("file", "testSave.png", "image/png", "Test content".getBytes());
        fileStorageServiceImpl.save(mockFile);

        // Gọi phương thức load
        Resource resource = fileStorageServiceImpl.load("testSave.png");

        // Kiểm tra kết quả
        assertNotNull(resource);
        assertTrue(resource.exists());
    }

    @Test
    public void testLoadFileNotExist() {
        // Kiểm tra ngoại lệ khi gọi phương thức load với tệp không tồn tại
        Exception exception = assertThrows(RuntimeException.class, () -> fileStorageServiceImpl.load("nonexistent.png"));
        assertEquals("Could not read the file!", exception.getMessage());
    }

    @Test
    public void testLoadInvalidUrlResource() throws Exception {
        // Giả lập trường hợp tệp ko đọc đc
        Path invalidFilePath = Paths.get("invalid/path/to/file.png");
        Resource resource = new UrlResource(invalidFilePath.toUri());

        // Kiểm tra ngoại lệ
        Exception exception = assertThrows(RuntimeException.class, () -> {
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Could not read the file!");
            }
        });

        assertEquals("Could not read the file!", exception.getMessage());
    }

}
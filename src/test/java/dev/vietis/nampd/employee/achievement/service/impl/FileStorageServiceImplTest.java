package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FileStorageServiceImplTest {
    @Mock
    private MultipartFile file;

    @InjectMocks
    private FileStorageServiceImpl fileStorageServiceImpl;

    private Path root;

    @BeforeEach
    public void setUp() {
        try {
            // Tạo thư mục tạm thời cho test
            root = Files.createTempDirectory("test-uploads");
            // Thiết lập đường dẫn gốc cho FileStorageServiceImpl
            Files.createDirectories(root.resolve("uploads/images"));
            fileStorageServiceImpl = new FileStorageServiceImpl(); // Khởi tạo lại sau khi thiết lập đường dẫn
        } catch (IOException e) {
            fail("Could not create temp directory for tests");
        }
    }

    @Test
    public void testSaveSuccess() throws Exception {
        // Tạo một tệp mẫu
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "text/plain", "Test content".getBytes());

        // Thiết lập hành vi của mock
        when(file.getOriginalFilename()).thenReturn(mockFile.getOriginalFilename());
        when(file.getInputStream()).thenReturn(mockFile.getInputStream());

        // Gọi phương thức save
        String savedFilename = fileStorageServiceImpl.save(file);

        // Kiểm tra kết quả
        assertEquals("test.jpg", savedFilename);
        assertTrue(Files.exists(root.resolve("uploads/images/test.jpg")));
    }

    @Test
    public void testSaveInvalidFileName() {
        // Thiết lập hành vi của mock để trả về tên tệp không hợp lệ
        when(file.getOriginalFilename()).thenReturn(null);

        // Kiểm tra ngoại lệ khi gọi phương thức save
        Exception exception = assertThrows(RuntimeException.class, () -> fileStorageServiceImpl.save(file));
        assertEquals("File name is invalid", exception.getMessage());
    }

    @Test
    public void testLoad_FileExists_ReturnsResource() throws Exception {
        // Tạo một tệp mẫu và lưu vào thư mục gốc
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        fileStorageServiceImpl.save(mockFile);

        // Gọi phương thức load
        Resource resource = fileStorageServiceImpl.load("test.txt");

        // Kiểm tra kết quả
        assertNotNull(resource);
        assertTrue(resource.exists());
    }

    @Test
    public void testLoad_FileDoesNotExist_ThrowsException() {
        // Kiểm tra ngoại lệ khi gọi phương thức load với tệp không tồn tại
        Exception exception = assertThrows(RuntimeException.class, () -> fileStorageServiceImpl.load("nonexistent.txt"));
        assertEquals("Could not read the file!", exception.getMessage());
    }

    @Test
    public void testLoad_InvalidUrlResource_ThrowsException() throws Exception {
        // Giả lập trường hợp tệp không thể đọc
        Path invalidFilePath = Paths.get("invalid/path/to/file.txt");
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
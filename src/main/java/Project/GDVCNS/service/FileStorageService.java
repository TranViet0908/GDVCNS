package Project.GDVCNS.service;

import Project.GDVCNS.entity.FileStorage;
import Project.GDVCNS.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FileStorageRepository fileStorageRepository;

    // Lấy đường dẫn upload từ file properties hoặc mặc định là "uploads"
    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir, FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu trữ file.", ex);
        }
    }

    public FileStorage storeFile(MultipartFile file) {
        // Chuẩn hóa tên file
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Kiểm tra tên file không hợp lệ
            if(originalFileName.contains("..")) {
                throw new RuntimeException("Tên file chứa đường dẫn không hợp lệ " + originalFileName);
            }

            // Tạo tên file mới để tránh trùng lặp (UUID)
            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i);
            }
            String generatedFileName = UUID.randomUUID().toString() + fileExtension;

            // Copy file vào thư mục đích (Thay thế nếu tồn tại)
            Path targetLocation = this.fileStorageLocation.resolve(generatedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Tạo đường dẫn URL tương đối (để client truy cập)
            // Ví dụ: /uploads/ten-file-moi.jpg
            String fileDownloadUri = "/uploads/" + generatedFileName;

            // Lưu thông tin vào Database
            FileStorage fileStorage = FileStorage.builder()
                    .id(UUID.randomUUID().toString())
                    .originalName(originalFileName)
                    .filePath(fileDownloadUri) // Lưu đường dẫn tương đối
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .createdAt(LocalDateTime.now())
                    .build();

            return fileStorageRepository.save(fileStorage);

        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu file " + originalFileName + ". Vui lòng thử lại!", ex);
        }
    }
}
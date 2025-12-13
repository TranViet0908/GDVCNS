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

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir, FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
        // Đường dẫn gốc: project_folder/uploads
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu trữ file gốc.", ex);
        }
    }

    // [CẬP NHẬT] Thêm tham số subDir để lưu vào thư mục con (VD: posts/slug-bai-viet)
    public FileStorage storeFile(MultipartFile file, String subDir) {
        // Chuẩn hóa tên file
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(originalFileName.contains("..")) {
                throw new RuntimeException("Tên file không hợp lệ " + originalFileName);
            }

            // Tạo tên file mới (UUID)
            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i);
            }
            String generatedFileName = UUID.randomUUID().toString() + fileExtension;

            // Xử lý thư mục đích: uploads + subDir
            Path targetDir = this.fileStorageLocation;
            if (subDir != null && !subDir.isEmpty()) {
                targetDir = this.fileStorageLocation.resolve(subDir);
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }
            }

            // Copy file
            Path targetLocation = targetDir.resolve(generatedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Tạo đường dẫn URL (Thay thế dấu \ thành / để chạy đúng trên Web)
            String urlSubDir = (subDir != null && !subDir.isEmpty()) ? subDir + "/" : "";
            // Kết quả: /uploads/posts/ten-bai-viet/file-moi.jpg
            String fileDownloadUri = "/uploads/" + urlSubDir + generatedFileName;

            // Fix lỗi đường dẫn Windows (nếu có)
            fileDownloadUri = fileDownloadUri.replace("\\", "/");

            // Lưu DB
            FileStorage fileStorage = FileStorage.builder()
                    .id(UUID.randomUUID().toString())
                    .originalName(originalFileName)
                    .filePath(fileDownloadUri)
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
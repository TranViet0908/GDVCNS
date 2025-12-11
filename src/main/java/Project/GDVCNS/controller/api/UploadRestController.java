package Project.GDVCNS.controller.api;

import Project.GDVCNS.entity.FileStorage;
import Project.GDVCNS.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadRestController {

    @Autowired
    private FileStorageService fileStorageService;

    // API này dành cho TinyMCE tự động upload khi kéo thả ảnh
    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            FileStorage storedFile = fileStorageService.storeFile(file);

            Map<String, String> response = new HashMap<>();
            // Trả về đường dẫn ảnh để TinyMCE hiển thị
            response.put("location", storedFile.getFilePath());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
package Project.GDVCNS.controller; // Hoặc package tương ứng của bạn

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Bắt lỗi khi file upload quá lớn
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        Map<String, String> response = new HashMap<>();
        // TinyMCE cần format lỗi (nếu custom handler) hoặc ta trả về message để hiển thị
        response.put("error", "File tải lên quá lớn! Vui lòng chọn ảnh nhỏ hơn 30MB.");

        // Trả về mã lỗi 413 (Payload Too Large) hoặc 400
        return ResponseEntity.status(413).body(response);
    }
}
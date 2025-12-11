package Project.GDVCNS.dto;

import Project.GDVCNS.enums.ContactStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String subject;
    private String message;
    private ContactStatus status;
    private LocalDateTime createdAt;
}
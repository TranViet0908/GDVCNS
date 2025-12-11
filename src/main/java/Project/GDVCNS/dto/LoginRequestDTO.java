package Project.GDVCNS.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username; // hoặc email
    private String password;
}
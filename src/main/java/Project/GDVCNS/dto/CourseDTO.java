package Project.GDVCNS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    private String slug;
    private String thumbnailUrl;
    private String summary;
    private String content;
    private String duration;
    private String targetAudience;
    private String trainingFormat;
    private Boolean isActive;
    private Long viewCount;

    private Long categoryId;
    private String categoryName;

    private LocalDateTime createdAt;
}
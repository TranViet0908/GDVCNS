package Project.GDVCNS.dto;

import Project.GDVCNS.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String slug;
    private String thumbnailUrl;
    private String summary;
    private String content;
    private PostStatus status;
    private Boolean isFeatured;
    private Long viewCount;
    private LocalDateTime publishedAt;

    // Mapping IDs
    private Long categoryId;
    private String categoryName;

    private Long courseId;
    private String courseName;

    private Long authorId;
    private String authorName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
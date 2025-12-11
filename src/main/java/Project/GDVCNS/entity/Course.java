package Project.GDVCNS.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String duration; // Ví dụ: "05 buổi"

    @Column(name = "target_audience")
    private String targetAudience; // Đối tượng tham gia

    @Column(name = "training_format")
    private String trainingFormat; // Hình thức: Online/Offline

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
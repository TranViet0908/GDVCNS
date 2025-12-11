package Project.GDVCNS.mapper;

import Project.GDVCNS.dto.PostDTO;
import Project.GDVCNS.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDTO toDTO(Post post) {
        if (post == null) return null;
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .thumbnailUrl(post.getThumbnailUrl())
                .summary(post.getSummary())
                .content(post.getContent())
                .status(post.getStatus())
                .isFeatured(post.getIsFeatured())
                .viewCount(post.getViewCount())
                .publishedAt(post.getPublishedAt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)
                .courseId(post.getCourse() != null ? post.getCourse().getId() : null)
                .courseName(post.getCourse() != null ? post.getCourse().getName() : null)
                .authorId(post.getAuthor() != null ? post.getAuthor().getId() : null)
                .authorName(post.getAuthor() != null ? post.getAuthor().getFullName() : null)
                .build();
    }

    public Post toEntity(PostDTO dto) {
        if (dto == null) return null;
        return Post.builder()
                .title(dto.getTitle())
                .slug(dto.getSlug())
                .thumbnailUrl(dto.getThumbnailUrl())
                .summary(dto.getSummary())
                .content(dto.getContent())
                .status(dto.getStatus())
                .isFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false)
                .build();
    }
}
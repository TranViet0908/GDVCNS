package Project.GDVCNS.mapper;

import Project.GDVCNS.dto.CourseDTO;
import Project.GDVCNS.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        if (course == null) return null;
        return CourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .slug(course.getSlug())
                .thumbnailUrl(course.getThumbnailUrl())
                .summary(course.getSummary())
                .content(course.getContent())
                .duration(course.getDuration())
                .targetAudience(course.getTargetAudience())
                .trainingFormat(course.getTrainingFormat())
                .isActive(course.getIsActive())
                .viewCount(course.getViewCount())
                .createdAt(course.getCreatedAt())
                .categoryId(course.getCategory() != null ? course.getCategory().getId() : null)
                .categoryName(course.getCategory() != null ? course.getCategory().getName() : null)
                .build();
    }

    public Course toEntity(CourseDTO dto) {
        if (dto == null) return null;
        return Course.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .thumbnailUrl(dto.getThumbnailUrl())
                .summary(dto.getSummary())
                .content(dto.getContent())
                .duration(dto.getDuration())
                .targetAudience(dto.getTargetAudience())
                .trainingFormat(dto.getTrainingFormat())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }
}
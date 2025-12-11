package Project.GDVCNS.repository;

import Project.GDVCNS.entity.Faq;
import org.springframework.data.domain.Sort; // Lưu ý import đúng cái này
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
    List<Faq> findByIsActiveTrueOrderByOrderIndexAsc();
    List<Faq> findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(String key1, String key2, Sort sort);

    @Query("SELECT f FROM Faq f WHERE f.isActive = true AND (LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword, '%'))) ORDER BY f.orderIndex ASC")
    List<Faq> searchActiveFaqs(@Param("keyword") String keyword);
}
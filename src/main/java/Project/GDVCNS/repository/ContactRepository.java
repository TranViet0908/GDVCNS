package Project.GDVCNS.repository;

import Project.GDVCNS.entity.Contact;
import Project.GDVCNS.enums.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByStatus(ContactStatus status, Pageable pageable);
    long countByStatus(ContactStatus status);
    List<Contact> findTop5ByOrderByCreatedAtDesc();

    // Hàm tìm kiếm tùy chỉnh: Search từ khóa + Filter Trạng thái
    @Query("SELECT c FROM Contact c WHERE " +
            "(:status IS NULL OR c.status = :status) AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "c.phone LIKE CONCAT('%', :keyword, '%'))")
    Page<Contact> searchContacts(@Param("keyword") String keyword,
                                 @Param("status") ContactStatus status,
                                 Pageable pageable);
}
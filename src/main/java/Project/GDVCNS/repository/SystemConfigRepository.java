package Project.GDVCNS.repository;

import Project.GDVCNS.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {
    // Vì ID là String (Key) nên findById chính là findByKey
}
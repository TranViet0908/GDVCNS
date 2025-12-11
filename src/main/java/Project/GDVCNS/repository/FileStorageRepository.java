package Project.GDVCNS.repository;

import Project.GDVCNS.entity.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, String> {
    Optional<FileStorage> findByOriginalName(String originalName);
}
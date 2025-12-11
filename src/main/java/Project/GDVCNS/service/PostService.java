package Project.GDVCNS.service;

import Project.GDVCNS.dto.PostDTO;
import Project.GDVCNS.entity.Category;
import Project.GDVCNS.entity.Post;
import Project.GDVCNS.entity.User;
import Project.GDVCNS.enums.PostStatus;
import Project.GDVCNS.mapper.PostMapper;
import Project.GDVCNS.repository.CategoryRepository;
import Project.GDVCNS.repository.PostRepository;
import Project.GDVCNS.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    // --- LẤY DANH SÁCH BÀI VIẾT (CÓ PHÂN QUYỀN VIEW) ---
    @Transactional(readOnly = true)
    public Page<PostDTO> searchPosts(String keyword, Long categoryId, PostStatus status, String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<Post> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(keyword)) {
                String likeKey = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), likeKey),
                        cb.like(cb.lower(root.get("summary")), likeKey)
                ));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            // Nếu muốn lọc theo type
            // if (StringUtils.hasText(type)) { predicates.add(cb.equal(root.get("type"), type)); }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return postRepository.findAll(spec, pageable).map(postMapper::toDTO);
    }

    // --- LẤY CHI TIẾT ĐỂ SỬA ---
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài viết"));

//        checkPermission(post);
        return postMapper.toDTO(post);
    }

    // --- TẠO BÀI VIẾT MỚI ---
    @Transactional
    public void createPost(PostDTO dto) {
        User currentUser = getCurrentUser();

        Post post = postMapper.toEntity(dto);
        post.setAuthor(currentUser); // Set tác giả là người đang login
        post.setCreatedAt(LocalDateTime.now());

        // Xử lý Slug
        post.setSlug(generateUniqueSlug(dto.getTitle()));

        // Xử lý Category
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            post.setCategory(category);
        }

        // Xử lý Status (Phân quyền duyệt)
        if (currentUser.getRole().name().equals("ADMIN")) {
            // Admin được quyền chọn status tùy ý (kể cả PUBLISHED)
            post.setStatus(dto.getStatus() != null ? dto.getStatus() : PostStatus.PUBLISHED);
        } else {
            // Editor chỉ được tạo DRAFT hoặc PENDING
            post.setStatus(PostStatus.PENDING);
        }

        if (post.getStatus() == PostStatus.PUBLISHED) {
            post.setPublishedAt(LocalDateTime.now());
        }

        postRepository.save(post);
    }

    // --- CẬP NHẬT BÀI VIẾT ---
    @Transactional
    public void updatePost(Long id, PostDTO dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài viết"));

        checkPermission(post); // Check quyền sở hữu

        User currentUser = getCurrentUser();

        // Cập nhật thông tin cơ bản
        post.setTitle(dto.getTitle());
        post.setSummary(dto.getSummary());
        post.setContent(dto.getContent());
        post.setThumbnailUrl(dto.getThumbnailUrl());
        post.setIsFeatured(dto.getIsFeatured());

        // Cập nhật Category
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            post.setCategory(category);
        }

        // Xử lý Status khi Update
        if (currentUser.getRole().name().equals("ADMIN")) {
            post.setStatus(dto.getStatus()); // Admin toàn quyền đổi status
        } else {
            // Editor: Nếu sửa bài, có thể giữ nguyên DRAFT/PENDING.
            // Nếu bài đang PUBLISHED mà Editor sửa -> Có thể chuyển về PENDING để duyệt lại (tùy logic cty)
            // Ở đây tôi để Editor chỉ được chọn DRAFT hoặc PENDING
            if (dto.getStatus() == PostStatus.PUBLISHED) {
                post.setStatus(PostStatus.PENDING); // Force về chờ duyệt nếu Editor cố tình chọn Publish
            } else {
                post.setStatus(dto.getStatus());
            }
        }

        // Cập nhật Slug nếu tiêu đề thay đổi (Optional, thường thì ít khi đổi slug để giữ SEO)
        // if (!post.getTitle().equals(dto.getTitle())) { ... }

        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // --- XÓA BÀI VIẾT ---
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài viết"));

        checkPermission(post); // Check quyền sở hữu
        postRepository.delete(post);
    }

    // === CÁC HÀM BỔ TRỢ ===

    // Kiểm tra quyền: Admin được làm hết. Editor chỉ làm trên bài mình.
    private void checkPermission(Post post) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole().name().equals("ADMIN")) {
            return; // Admin pass
        }
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Bạn không có quyền chỉnh sửa bài viết của người khác!");
        }
    }

    // Lấy User đang đăng nhập từ Security Context
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy User hiện tại"));
    }

    // Tạo Slug từ Title (VD: "Tin Mới" -> "tin-moi")
    private String generateUniqueSlug(String title) {
        String slug = toSlug(title);
        if (!postRepository.existsBySlug(slug)) {
            return slug;
        }
        // Nếu trùng, thêm số đếm: tin-moi-1, tin-moi-2
        int count = 1;
        while (postRepository.existsBySlug(slug + "-" + count)) {
            count++;
        }
        return slug + "-" + count;
    }

    private String toSlug(String input) {
        String nowhitespace = input.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase().replaceAll("[^a-z0-9-]", "");
    }

    // --- LẤY DANH SÁCH BÀI VIẾT (PUBLIC) ---
    // Hàm này hỗ trợ lọc theo Category Slug (VD: 'tin-tuc-su-kien' hoặc 'du-an-tieu-bieu')
    @Transactional(readOnly = true)
    public Page<PostDTO> getPostsByCategorySlug(String categorySlug, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());

        Specification<Post> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Chỉ lấy bài đã xuất bản
            predicates.add(cb.equal(root.get("status"), PostStatus.PUBLISHED));

            // 2. Lọc theo Category Slug (nếu có)
            if (categorySlug != null && !categorySlug.isEmpty()) {
                // Join bảng Category để check slug
                predicates.add(cb.equal(root.get("category").get("slug"), categorySlug));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return postRepository.findAll(spec, pageable).map(postMapper::toDTO);
    }

    // Thêm hàm lấy bài viết mới nhất (cho Home/Sidebar)
    public Page<PostDTO> getLatestPosts(int limit) {
        return getPostsByCategorySlug(null, 0, limit);
    }
}
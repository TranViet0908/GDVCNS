package Project.GDVCNS.service;

import Project.GDVCNS.dto.PostDTO;
import Project.GDVCNS.entity.Category;
import Project.GDVCNS.entity.Post;
import Project.GDVCNS.entity.User;
import Project.GDVCNS.enums.PostStatus;
import Project.GDVCNS.enums.UserRole;
import Project.GDVCNS.mapper.PostMapper;
import Project.GDVCNS.repository.CategoryRepository;
import Project.GDVCNS.repository.PostRepository;
import Project.GDVCNS.repository.UserRepository;
import Project.GDVCNS.utils.SlugUtils; // Dùng SlugUtils chuẩn đã fix
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    // =====================================================================
    // PHẦN 1: LOGIC QUẢN TRỊ (ADMIN/EDITOR) - CẦN LOGIN
    // =====================================================================

    // --- TẠO BÀI VIẾT MỚI ---
    @Transactional
    public void createPost(PostDTO dto) {
        User currentUser = getCurrentUser(); // <--- Đã fix: Tìm theo username hoặc email

        Post post = postMapper.toEntity(dto);
        post.setAuthor(currentUser);
        post.setCreatedAt(LocalDateTime.now());

        // Tạo Slug chuẩn (Dùng SlugUtils để không bị lỗi 'vit-nam')
        post.setSlug(generateUniqueSlug(dto.getTitle()));

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            post.setCategory(category);
        }

        // --- PHÂN QUYỀN TRẠNG THÁI ---
        // Admin: Được quyền chọn mọi trạng thái
        // Editor: Tạo mới auto là PENDING (Chờ duyệt)
        if (currentUser.getRole() == UserRole.ADMIN) {
            post.setStatus(dto.getStatus() != null ? dto.getStatus() : PostStatus.PUBLISHED);
        } else {
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

        checkPermission(post); // <--- Admin qua, Editor check chính chủ

        User currentUser = getCurrentUser();

        post.setTitle(dto.getTitle());
        post.setSummary(dto.getSummary());
        post.setContent(dto.getContent());
        post.setThumbnailUrl(dto.getThumbnailUrl());
        post.setIsFeatured(dto.getIsFeatured());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            post.setCategory(category);
        }

        // --- PHÂN QUYỀN TRẠNG THÁI KHI UPDATE ---
        if (currentUser.getRole() == UserRole.ADMIN) {
            post.setStatus(dto.getStatus());
        } else {
            // Editor: Không được tự ý Publish bài.
            // Nếu bài đang Published mà sửa -> Về Pending
            // Nếu bài đang Pending/Draft -> Giữ nguyên trạng thái gửi lên
            if (post.getStatus() == PostStatus.PUBLISHED || dto.getStatus() == PostStatus.PUBLISHED) {
                post.setStatus(PostStatus.PENDING);
            } else {
                post.setStatus(dto.getStatus());
            }
        }

        // Cập nhật Slug nếu tiêu đề đổi (Optional)
        // if(!post.getTitle().equals(dto.getTitle())) post.setSlug(generateUniqueSlug(dto.getTitle()));

        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // --- XÓA BÀI VIẾT ---
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài viết"));

        checkPermission(post);
        postRepository.delete(post);
    }

    // --- 1. LẤY USER HIỆN TẠI (ĐÃ FIX BUG NOT FOUND) ---
    private User getCurrentUser() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Logic: Tìm Username trước (vì admin login bằng username), nếu không có mới tìm email
        Optional<User> user = userRepository.findByUsername(loginId);
        if (user.isEmpty()) {
            user = userRepository.findByEmail(loginId);
        }

        return user.orElseThrow(() -> new EntityNotFoundException("Không tìm thấy User: " + loginId));
    }

    // --- 2. CHECK QUYỀN ---
    private void checkPermission(Post post) {
        User currentUser = getCurrentUser();
        // Admin: Full quyền
        if (currentUser.getRole() == UserRole.ADMIN) {
            return;
        }
        // Editor: Chỉ sửa bài mình
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Bạn chỉ được sửa bài viết của chính mình!");
        }
    }

    // Helper tạo slug
    private String generateUniqueSlug(String title) {
        // Gọi SlugUtils.makeSlug để xử lý tiếng Việt chuẩn (đã fix lỗi vit-nam)
        String slug = SlugUtils.makeSlug(title);

        if (!postRepository.existsBySlug(slug)) {
            return slug;
        }
        int count = 1;
        while (postRepository.existsBySlug(slug + "-" + count)) {
            count++;
        }
        return slug + "-" + count;
    }


    // =====================================================================
    // PHẦN 2: LOGIC PUBLIC (KHÔNG CẦN LOGIN - KHÔNG ẢNH HƯỞNG)
    // =====================================================================

    // Tìm kiếm trong Admin (Vẫn giữ nguyên)
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
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return postRepository.findAll(spec, pageable).map(postMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài viết"));
        return postMapper.toDTO(post);
    }

    // --- LẤY DANH SÁCH BÀI VIẾT (PUBLIC CONTROLLER GỌI HÀM NÀY) ---
    // Hàm này hoàn toàn độc lập với getCurrentUser, nên PublicController chạy bao mượt
    @Transactional(readOnly = true)
    public Page<PostDTO> getPostsByCategorySlug(String categorySlug, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());

        Specification<Post> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Chỉ lấy bài đã Published
            predicates.add(cb.equal(root.get("status"), PostStatus.PUBLISHED));

            if (categorySlug != null && !categorySlug.isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("slug"), categorySlug));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return postRepository.findAll(spec, pageable).map(postMapper::toDTO);
    }

    // Public Controller gọi hàm này cho trang chủ
    public Page<PostDTO> getLatestPosts(int limit) {
        return getPostsByCategorySlug(null, 0, limit);
    }
}
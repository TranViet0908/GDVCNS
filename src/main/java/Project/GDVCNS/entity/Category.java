package Project.GDVCNS.entity;

import Project.GDVCNS.enums.CategoryType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category { // Category thường ít khi cần createAt/updateAt nên có thể không kế thừa BaseEntity, tùy bạn. Ở đây tôi map theo DB của bạn là có ID tự tăng.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type;

    // Quan hệ cha-con (Self-referencing)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children;
}
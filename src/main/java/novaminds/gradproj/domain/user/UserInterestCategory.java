package novaminds.gradproj.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.BaseEntity;
import novaminds.gradproj.domain.Recipe.RecipeCategory;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_interest_categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "category"})
        })
public class UserInterestCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private RecipeCategory category;

    public static UserInterestCategory create(User user, RecipeCategory category) {
        return UserInterestCategory.builder()
                .user(user)
                .category(category)
                .build();
    }
}

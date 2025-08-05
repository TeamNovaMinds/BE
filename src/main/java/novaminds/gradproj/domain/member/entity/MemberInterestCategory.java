package novaminds.gradproj.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.global.BaseEntity;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member_interest_categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "category"})
        })
public class MemberInterestCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private RecipeCategory category;

    public static MemberInterestCategory create(Member member, RecipeCategory category) {
        return MemberInterestCategory.builder()
                .member(member)
                .category(category)
                .build();
    }
}

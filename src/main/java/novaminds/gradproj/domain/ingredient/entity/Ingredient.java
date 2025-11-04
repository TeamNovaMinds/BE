package novaminds.gradproj.domain.ingredient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.global.BaseEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Builder
@Entity
@Table(name = "ingredients", indexes = {
        @Index(name = "idx_ingredient_name_normalized", columnList = "ingredient_name_normalized")
})
public class Ingredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String ingredientName;

    @Embedded
    private ShelfLife shelfLife;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @ColumnDefault("'APPROVED'")
    private RegistrationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IngredientCategory ingredientCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //테이블 풀 스캔을 막기 위한 별도의 정규화된 컬럼.
    //미리 모든 띄어쓰기 없애고 소문자로 바꿔서 저장해두고 검색할 때 사용.
    @Column(name = "ingredient_name_normalized", length = 50)
    private String ingredientNameNormalized;

    @PrePersist
    @PreUpdate
    private void updateNormalizedName() {
        if (this.ingredientName != null) {
            this.ingredientNameNormalized = this.ingredientName.toLowerCase().replaceAll("\\s+", "");
        }
    }
}
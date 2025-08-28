package novaminds.gradproj.domain.refrigerator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.global.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refrigerator_skins")
public class RefrigeratorSkin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String skinName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private boolean isDefault = false;

    @OneToMany(mappedBy = "refrigeratorSkin", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("imageOrder ASC")
    @Builder.Default
    private List<RefrigeratorSkinImage> refrigeratorSkinImages = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addRefrigeratorSkinImage(RefrigeratorSkinImage image) {
        this.refrigeratorSkinImages.add(image);
        image.setRefrigeratorSkin(this);
    }

    public void removeRefrigeratorSkinImage(RefrigeratorSkinImage image) {
        this.refrigeratorSkinImages.remove(image);
        image.setRefrigeratorSkin(null);
    }
}

package novaminds.gradproj.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.global.ApplicationContextProvider;
import novaminds.gradproj.global.s3.service.S3Service;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipe_image")
public class RecipeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "image_order", nullable = false)
    private int imageOrder;

    @Column(name = "is_main", nullable = false)
    @Builder.Default
    private boolean isMain = false;

    @PreRemove
    public void deleteFileFromS3() {
        if (this.imageUrl != null && !this.imageUrl.isEmpty()) {
            S3Service s3Service = ApplicationContextProvider.getBean(S3Service.class);
            s3Service.deleteImageByUrl(this.imageUrl);
        }
    }
}

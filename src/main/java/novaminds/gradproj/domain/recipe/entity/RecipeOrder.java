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
@Table(name = "recipe_order")
public class RecipeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_order", nullable = false)
    private Integer order;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @PreRemove
    public void deleteFileFromS3() {
        if (this.imageUrl != null && !this.imageUrl.isEmpty()) {
            S3Service s3Service = ApplicationContextProvider.getBean(S3Service.class);
            s3Service.deleteImageByUrl(this.imageUrl);
        }
    }
}

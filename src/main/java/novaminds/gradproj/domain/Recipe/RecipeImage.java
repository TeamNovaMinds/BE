package novaminds.gradproj.domain.Recipe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void setAsMainImage() {
        this.isMain = true;
    }

    public void unsetAsMainImage() {
        this.isMain = false;
    }

    public void updateOrder(int newOrder) {
        this.imageOrder = newOrder;
    }
}

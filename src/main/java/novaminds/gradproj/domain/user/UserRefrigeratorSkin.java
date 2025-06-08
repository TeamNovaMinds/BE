package novaminds.gradproj.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.BaseEntity;
import novaminds.gradproj.domain.refrigerator.RefrigeratorSkin;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_refrigerator_skins",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "skin_id"})
        })
public class UserRefrigeratorSkin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id", nullable = false)
    private RefrigeratorSkin skin;

    @Column(name = "is_equipped", nullable = false)
    @Builder.Default
    private boolean isEquipped = false;

    public void equip() {
        this.isEquipped = true;
    }

    public void unEquip() {
        this.isEquipped = false;
    }
}

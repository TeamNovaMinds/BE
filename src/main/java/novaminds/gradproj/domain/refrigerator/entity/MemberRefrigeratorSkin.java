package novaminds.gradproj.domain.refrigerator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.global.BaseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member_refrigerator_skins",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "skin_id"})
        })
public class MemberRefrigeratorSkin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id", nullable = false)
    private RefrigeratorSkin skin;

    @Column(name = "equipped", nullable = false)
    @Builder.Default
    private boolean equipped = false;

    public void equip() {
        this.equipped = true;
    }

    public void unEquip() {
        this.equipped = false;
    }
}

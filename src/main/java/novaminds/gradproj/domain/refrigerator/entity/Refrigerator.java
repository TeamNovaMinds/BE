package novaminds.gradproj.domain.refrigerator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.global.BaseEntity;
import novaminds.gradproj.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "refrigerators",
        uniqueConstraints = @UniqueConstraint(columnNames = "member_id"))
public class Refrigerator extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "refrigerator", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StoredItem> storedItems = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
    }

    public void addstoredItems(StoredItem storedItem) {
        storedItems.add(storedItem);
    }

    public void removestoredItems(StoredItem storedItem) {
        storedItems.remove(storedItem);
    }
}

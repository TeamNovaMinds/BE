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
@Table(name = "refrigerators")
public class Refrigerator extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "refrigerator", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "refrigerator", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StoredItem> storedItems = new ArrayList<>();

    public void addMember(Member member) {
        if (!memberList.contains(member)) {
            memberList.add(member);
        }
    }

    public void removeMember(Member member) {
        memberList.remove(member);
    }

    public void addStoredItem(StoredItem storedItem) {
        storedItems.add(storedItem);
    }

    public void removeStoredItem(StoredItem storedItem) {
        storedItems.remove(storedItem);
    }
}

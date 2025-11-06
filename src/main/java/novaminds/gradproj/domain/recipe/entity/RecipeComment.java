package novaminds.gradproj.domain.recipe.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.global.BaseEntity;
import novaminds.gradproj.domain.member.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recipe_comments")
public class RecipeComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    //대댓글 기능을 위한 자기 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private RecipeComment parentComment;

    //대댓글 리스트
    @OneToMany(mappedBy = "parentComment")
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<RecipeComment> children = new ArrayList<>();

    /**
     * 댓글 내용을 수정
     *
     * @param content 수정할 댓글 내용
     */
    public void updateContent(String content) {
        this.content = content;
    }

}
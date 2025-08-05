package novaminds.gradproj.domain.member.repository;

import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.member.entity.MemberInterestCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberInterestCategoryRepository extends JpaRepository<MemberInterestCategory, Long> {

    List<MemberInterestCategory> findByMemberLoginId(String loginId);

    void deleteByMemberLoginId(String loginId);

    long countByCategory(RecipeCategory category);

    @Query("SELECT mic.category FROM MemberInterestCategory mic WHERE mic.member.loginId = :loginId")
    List<RecipeCategory> findCategoriesByMemberLoginId(@Param("loginId") String loginId);
}
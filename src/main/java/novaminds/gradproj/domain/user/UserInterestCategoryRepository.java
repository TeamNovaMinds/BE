package novaminds.gradproj.domain.user;

import novaminds.gradproj.domain.Recipe.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserInterestCategoryRepository extends JpaRepository<UserInterestCategory, Long> {

    List<UserInterestCategory> findByUserLoginId(String loginId);

    void deleteByUserLoginId(String loginId);

    long countByCategory(RecipeCategory category);

    @Query("SELECT uic.category FROM UserInterestCategory uic WHERE uic.user.loginId = :loginId")
    List<RecipeCategory> findCategoriesByUserLoginId(@Param("loginId") String loginId);
}
package novaminds.gradproj.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(String loginId);

    Optional<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.interestCategories where u.loginId = :loginId")
    Optional<User> findByLoginIdWithInterestCategories(@Param("loginId") String loginId);

    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}

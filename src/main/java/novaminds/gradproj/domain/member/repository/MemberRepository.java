package novaminds.gradproj.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import novaminds.gradproj.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsById(String loginId);

    Optional<Member> findByEmail(String email);

    @Query("select m from Member m left join fetch m.interestCategories where m.loginId = :loginId")
    Optional<Member> findByLoginIdWithInterestCategories(@Param("loginId") String loginId);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("SELECT CASE WHEN m.role = 'ADMIN' THEN true ELSE false END FROM Member m WHERE m.loginId = :loginId")
    boolean isAdminByLoginId(@Param("loginId") String loginId);
}

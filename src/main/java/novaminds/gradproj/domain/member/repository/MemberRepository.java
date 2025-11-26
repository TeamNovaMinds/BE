package novaminds.gradproj.domain.member.repository;

import java.time.LocalDateTime;
import novaminds.gradproj.domain.member.entity.Role;
import novaminds.gradproj.domain.member.repository.projection.AuthorInfoProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import novaminds.gradproj.domain.member.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByLoginIdAndRole(String loginId, Role role);

    @Query("SELECT m.loginId as loginId, m.nickname as nickname, m.profileImage as profileImage " +
            "FROM Member m " +
            "WHERE m.loginId IN :memberIds")
    List<AuthorInfoProjection> findAuthorInfoByLoginIds(@Param("memberIds") List<String> memberIds);

    @Query("select m.loginId from Member m where m.nickname = :nickname")
    Optional<String> findIdByNickname(@Param("nickname") String nickname);

    @Query("SELECT COUNT(m) + 1 FROM Member m WHERE m.point > :point")
    long countMembersWithHigherPoint(@Param("point") Integer point);

    @Query("SELECT m FROM Member m ORDER BY m.point DESC, m.createdAt ASC")
    List<Member> findTop8ByOrderByPointDescCreatedAtAsc(Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.point < :cursorPoint OR (m.point = :cursorPoint AND m.createdAt > :cursorCreatedAt) " +
            "ORDER BY m.point DESC, m.createdAt ASC")
    List<Member> findRankingWithCursor(
            @Param("cursorPoint") Integer cursorPoint,
            @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
            Pageable pageable
    );
}
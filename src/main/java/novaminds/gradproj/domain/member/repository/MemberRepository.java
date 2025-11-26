package novaminds.gradproj.domain.member.repository;

import novaminds.gradproj.domain.member.entity.Role;
import novaminds.gradproj.domain.member.repository.projection.AuthorInfoProjection;
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
}
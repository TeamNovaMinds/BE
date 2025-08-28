package novaminds.gradproj.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import novaminds.gradproj.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByLoginIdAndRole(String loginId, novaminds.gradproj.domain.member.entity.Role role);
}

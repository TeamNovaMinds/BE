package novaminds.gradproj.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import novaminds.gradproj.domain.member.entity.PasswordResetToken;
import novaminds.gradproj.domain.member.entity.Member;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByMember(Member member);

    boolean existsByToken(String token);
}
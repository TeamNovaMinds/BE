package novaminds.gradproj.domain.refrigerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;

import java.util.Optional;

public interface MemberRefrigeratorSkinRepository extends JpaRepository<MemberRefrigeratorSkin, Long>, MemberRefrigeratorSkinRepositoryCustom {
    boolean existsByMemberLoginIdAndSkinId(String loginId, Long skinId);

    Optional<MemberRefrigeratorSkin> findByMemberLoginIdAndEquippedTrue(String loginId);

    Optional<MemberRefrigeratorSkin> findByMemberLoginIdAndSkinId(String loginId, Long skinId);
}

package novaminds.gradproj.domain.refrigerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;

public interface MemberRefrigeratorSkinRepository extends JpaRepository<MemberRefrigeratorSkin, Long> {
    boolean existsByMemberLoginIdAndSkinId(String loginId, Long skinId);
}

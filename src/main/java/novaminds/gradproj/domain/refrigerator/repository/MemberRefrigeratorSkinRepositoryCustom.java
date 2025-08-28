package novaminds.gradproj.domain.refrigerator.repository;

import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;

import java.util.List;
import java.util.Map;

public interface MemberRefrigeratorSkinRepositoryCustom {
    List<MemberRefrigeratorSkin> findOwnedSkinsWithCursor(String memberId, Long cursorId, int pageSize);
    
    Map<Long, MemberRefrigeratorSkin> findByMemberLoginIdAndSkinIds(String memberId, List<Long> skinIds);
}
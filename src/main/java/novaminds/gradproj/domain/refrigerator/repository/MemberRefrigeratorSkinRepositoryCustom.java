package novaminds.gradproj.domain.refrigerator.repository;

import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;

import java.util.List;

public interface MemberRefrigeratorSkinRepositoryCustom {
    List<MemberRefrigeratorSkin> findOwnedSkinsWithCursor(String memberId, Long cursorId, int pageSize);
}
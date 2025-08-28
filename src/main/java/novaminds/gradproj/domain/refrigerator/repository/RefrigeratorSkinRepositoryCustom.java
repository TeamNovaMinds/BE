package novaminds.gradproj.domain.refrigerator.repository;

import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;

import java.util.List;

public interface RefrigeratorSkinRepositoryCustom {
    List<RefrigeratorSkin> findSkinsWithCursor(Long cursorId, int pageSize);
}
package novaminds.gradproj.domain.refrigerator.repository;

import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;

import java.util.List;

public interface StoredItemRepositoryCustom {
    
    List<StoredItem> findStoredItems(Long refrigeratorId, StorageType storageType, String keyword);
}
package novaminds.gradproj.domain.refrigerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;

import java.util.Optional;

public interface StoredItemRepository extends JpaRepository<StoredItem, Long>, StoredItemRepositoryCustom {

    Optional<StoredItem> findByRefrigeratorIdAndIngredientIdAndStorageType(Long refrigeratorId, Long ingredientId, StorageType storageType);
}

package novaminds.gradproj.domain.refrigerator.repository;

import novaminds.gradproj.domain.refrigerator.repository.projection.StorageTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoredItemRepository extends JpaRepository<StoredItem, Long>, StoredItemRepositoryCustom {

    Optional<StoredItem> findByRefrigeratorIdAndIngredientIdAndStorageType(Long refrigeratorId, Long ingredientId, StorageType storageType);

    @Query("""
      SELECT
          COALESCE(SUM(CASE WHEN s.storageType = 'REFRIGERATOR' THEN 1 ELSE 0 END), 0) AS refrigeratorCount,
          COALESCE(SUM(CASE WHEN s.storageType = 'FREEZER' THEN 1 ELSE 0 END), 0) AS freezerCount,
          COALESCE(SUM(CASE WHEN s.storageType = 'ROOM_TEMPERATURE' THEN 1 ELSE 0 END), 0) AS roomTempCount
      FROM StoredItem s
      WHERE s.refrigerator.id = :refrigeratorId
      """)
    StorageTypeCount countByStorageTypes(@Param("refrigeratorId") Long refrigeratorId);
}

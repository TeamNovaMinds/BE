package novaminds.gradproj.domain.refrigerator.repository;

import jakarta.persistence.LockModeType;
import novaminds.gradproj.domain.refrigerator.repository.projection.StorageTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoredItemRepository extends JpaRepository<StoredItem, Long>, StoredItemRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<StoredItem> findByRefrigeratorIdAndIngredientIdAndStorageType(Long refrigeratorId, Long ingredientId, StorageType storageType);

    @Query("""
      SELECT
          COALESCE(SUM(CASE WHEN s.storageType = novaminds.gradproj.domain.refrigerator.entity.StorageType.REFRIGERATOR THEN 1 ELSE 0 END), 0) AS refrigeratorCount,
          COALESCE(SUM(CASE WHEN s.storageType = novaminds.gradproj.domain.refrigerator.entity.StorageType.FREEZER THEN 1 ELSE 0 END), 0) AS freezerCount,
          COALESCE(SUM(CASE WHEN s.storageType = novaminds.gradproj.domain.refrigerator.entity.StorageType.ROOM_TEMPERATURE THEN 1 ELSE 0 END), 0) AS roomTempCount
      FROM StoredItem s
      WHERE s.refrigerator.id = :refrigeratorId
      """)
    StorageTypeCount countByStorageTypes(@Param("refrigeratorId") Long refrigeratorId);

    Optional<StoredItem> findByIdAndRefrigeratorId(Long id, Long refrigeratorId);
}

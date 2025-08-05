package novaminds.gradproj.domain.refrigerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.refrigerator.entity.StoredItem;

public interface StoredItemRepository extends JpaRepository<StoredItem, Long> {

}

package novaminds.gradproj.domain.refrigerator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredItemRepository extends JpaRepository<StoredItem, Long> {

}

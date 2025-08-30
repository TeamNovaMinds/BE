package novaminds.gradproj.domain.refrigerator.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static novaminds.gradproj.domain.refrigerator.entity.QStoredItem.storedItem;

@Repository
@RequiredArgsConstructor
public class StoredItemRepositoryImpl implements StoredItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoredItem> findStoredItems(Long refrigeratorId, StorageType storageType) {

        BooleanExpression storageTypeExp = createStorageTypeExp(storageType);

        return queryFactory
                .selectFrom(storedItem)
                .where(
                        storedItem.refrigerator.id.eq(refrigeratorId),
                        storageTypeExp
                )
                .fetch();
    }

    private BooleanExpression createStorageTypeExp(StorageType storageType) {
        return storageType != null ? storedItem.storageType.eq(storageType) : null;
    }
}
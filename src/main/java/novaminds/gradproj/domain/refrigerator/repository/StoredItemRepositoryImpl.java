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
    public List<StoredItem> findStoredItems(Long refrigeratorId, StorageType storageType, String keyword) {

        // 보관 방법에 따른 동적 조건 추가
        // null - 모든 재료 조회
        // 특정 값 - 해당 타입의 재료만 조회
        BooleanExpression storageTypeExp = createStorageTypeExp(storageType);

        // 재료 이름에 따라 검색
        // null - 모든 재료 조회
        BooleanExpression storedItemExp = createStoredItemExp(keyword);

        return queryFactory
                .selectFrom(storedItem)
                .leftJoin(storedItem.ingredient).fetchJoin()
                .where(
                        storedItem.refrigerator.id.eq(refrigeratorId),
                        storageTypeExp,
                        storedItemExp
                )
                .fetch();
    }

    private BooleanExpression createStorageTypeExp(StorageType storageType) {
        return storageType != null ? storedItem.storageType.eq(storageType) : null;
    }

    private BooleanExpression createStoredItemExp(String keyword) {
        if (keyword == null) {
            return null;
        }
        
        String normalizedKeyword = keyword.toLowerCase().replaceAll("\\s", "");
        return storedItem.ingredient.ingredientNameNormalized.startsWith(normalizedKeyword);
    }
}
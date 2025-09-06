package novaminds.gradproj.domain.refrigerator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorRequestDTO;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import novaminds.gradproj.global.BaseEntity;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "stored_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"refrigerator_id", "ingredient_id", "expiration_date", "storage_type"})
        })
public class StoredItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refrigerator_id", nullable = false)
    private Refrigerator refrigerator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "quantity", nullable = false, columnDefinition = "int default 0")
    @Builder.Default
    private Integer quantity = 0;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_type", nullable = false, length = 20)
    private StorageType storageType;

    @Version
    private Long version;

    public void updateFieldIfChanged(StoredItem storedItem, RefrigeratorRequestDTO.ModifyStoredItemRequest request) {
        updateIfDifferent(storedItem.getQuantity(), request.getQuantity(), storedItem::updateQuantity);
        updateIfDifferent(storedItem.getStorageType(), request.getStorageType(), storedItem::updateStorageType);
        updateIfDifferent(storedItem.getExpirationDate(), request.getExpirationDate(), storedItem::updateExpirationDate);
    }

    public void updateExpirationDate(LocalDate newExpirationDate) {
        this.expirationDate = newExpirationDate;
    }

    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
    }

    public void updateStorageType(StorageType newStorageType) {
        this.storageType = newStorageType;
    }

    private <T> void updateIfDifferent(T current, T requested, Consumer<T> updater) {
        if (requested != null && !Objects.equals(current, requested)) {
            updater.accept(requested);
        }
    }
}
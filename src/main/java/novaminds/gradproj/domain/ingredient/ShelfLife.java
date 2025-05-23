package novaminds.gradproj.domain.ingredient;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelfLife {

    @Column(name = "fridge_days")
    private int fridgeDays;

    @Column(name = "room_temp_days")
    private int roomTempDays;

    @Column(name = "freezer_days")
    private int freezerDays;
}

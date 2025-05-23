package novaminds.gradproj.domain.ingredient;

import jakarta.persistence.Embeddable;

@Embeddable
public class ShelfLife {
    private int fridgeDays;
    private int roomTempDays;
    private int freezerDays;
}

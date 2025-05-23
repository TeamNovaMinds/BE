package novaminds.gradproj.domain.Recipe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Difficulty {
    EASY("쉬움"),
    MEDIUM("보통"),
    HARD("어려움");

    private final String description;
}

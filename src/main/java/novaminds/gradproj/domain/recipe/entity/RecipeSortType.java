package novaminds.gradproj.domain.recipe.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeSortType {
    LATEST,
    LIKES;
}
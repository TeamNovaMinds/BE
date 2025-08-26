package novaminds.gradproj.global.s3.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3Directory {
    IMAGES("images"),
    RECIPES("recipes"),
    PROFILES("profiles"),
    INGREDIENTS("ingredients"),
    REFRIGERATOR("refrigerator");

    private final String path;
}
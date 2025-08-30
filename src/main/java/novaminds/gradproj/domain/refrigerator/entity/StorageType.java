package novaminds.gradproj.domain.refrigerator.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageType {
    ROOM_TEMPERATURE("실온"),
    REFRIGERATOR("냉장"),
    FREEZER("냉동");

    private final String storageName;
}

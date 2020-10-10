package com.moyada.reservoir.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public enum LevelType {
    SMALL(0, "中型水库"),
    MIDDLE(1, "大中型水库"),
    BIG(2, "大型水库"),
    ;

    private int code;
    private String desc;

    public static String get(Integer level) {
        if (level == null) {
            return "";
        }
        for (LevelType value : LevelType.values()) {
            if (value.code == level) {
                return value.desc;
            }
        }
        return "";
    }
}

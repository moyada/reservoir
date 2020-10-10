package com.moyada.reservoir.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public enum DataType {

    OK((byte) 0),
    DELETED((byte) 1),
    ;

    private byte value;
}

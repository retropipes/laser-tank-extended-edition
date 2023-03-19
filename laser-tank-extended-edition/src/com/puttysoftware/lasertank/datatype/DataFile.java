package com.puttysoftware.lasertank.datatype;

import java.util.Locale;

enum DataFile {
    SOLID,
    DIRECTION,
    FRAME,
    FRICTION,
    INDEX,
    MOVABLE,
    SHOOT,
    WEIGHT,
    MATERIAL,
    HEIGHT,
    LAYER,
    LETHAL,
    REFLECT,
    ACCEPT_TICK,
    NAVIGATE;

    public String getName() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }
}

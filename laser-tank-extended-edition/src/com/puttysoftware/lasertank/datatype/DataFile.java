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
    TRANSFORM,
    MATERIAL,
    HEIGHT,
    LAYER,
    LETHAL,
    REFLECT,
    ROTATE,
    ACCEPT_TICK;

    public String getName() {
	return this.toString().toLowerCase(Locale.ENGLISH);
    }
}

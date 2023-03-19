package com.puttysoftware.lasertank.datatype;

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
    NAVIGATE,
    TYPES;

    public String getName() {
        return Integer.toString(this.ordinal());
    }
}

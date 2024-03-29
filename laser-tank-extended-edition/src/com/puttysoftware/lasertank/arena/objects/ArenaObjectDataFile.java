package com.puttysoftware.lasertank.arena.objects;

enum ArenaObjectDataFile {
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
    CONTROL,
    HOSTILE,
    MOVES_TANKS,
    MOVES_HOSTILES,
    MOVES_BOXES,
    MOVES_MIRRORS,
    BOX,
    ROLL,
    MOVABLE_MIRROR,
    CLOAK,
    PAIR,
    ATTRIBUTE_RENDER,
    USES_TRIGGER,
    JUMP,
    LASER_PASSTHRU,
    COLOR,
    STUNNED,
    TIMER,
    POWERFUL;

    public String getName() {
	return Integer.toString(this.ordinal());
    }
}

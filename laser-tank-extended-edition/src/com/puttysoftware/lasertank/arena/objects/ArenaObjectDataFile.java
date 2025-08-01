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
    POWERFUL,
    ACCEPT_FIRE,
    ACCEPT_ICE,
    NEW_ID_FIRE,
    NEW_ID_ICE,
    LASER_ENTER_SOUND,
    RANGE_SOUND,
    REMOVES_PUSHED_OBJECTS,
    REACTS_TO_OBJECTS_PUSHED_INTO,
    WEAKEN,
    CAN_BREAK;

    public String getName() {
        return Integer.toString(this.ordinal());
    }
}

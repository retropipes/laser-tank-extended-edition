/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.direction;

public enum Direction {
    NONE,
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTH_WEST,
    NORTH_EAST,
    SOUTH_WEST,
    SOUTH_EAST,
    DOWN,
    UP,
    NORTH_DOWN,
    SOUTH_DOWN,
    WEST_DOWN,
    EAST_DOWN,
    NORTH_WEST_DOWN,
    NORTH_EAST_DOWN,
    SOUTH_WEST_DOWN,
    SOUTH_EAST_DOWN,
    NORTH_UP,
    SOUTH_UP,
    WEST_UP,
    EAST_UP,
    NORTH_WEST_UP,
    NORTH_EAST_UP,
    SOUTH_WEST_UP,
    SOUTH_EAST_UP,
    INTO,
    OUT,
    NORTH_INTO,
    SOUTH_INTO,
    WEST_INTO,
    EAST_INTO,
    NORTH_WEST_INTO,
    NORTH_EAST_INTO,
    SOUTH_WEST_INTO,
    SOUTH_EAST_INTO,
    DOWN_INTO,
    UP_INTO,
    NORTH_DOWN_INTO,
    SOUTH_DOWN_INTO,
    WEST_DOWN_INTO,
    EAST_DOWN_INTO,
    NORTH_WEST_DOWN_INTO,
    NORTH_EAST_DOWN_INTO,
    SOUTH_WEST_DOWN_INTO,
    SOUTH_EAST_DOWN_INTO,
    NORTH_UP_INTO,
    SOUTH_UP_INTO,
    WEST_UP_INTO,
    EAST_UP_INTO,
    NORTH_WEST_UP_INTO,
    NORTH_EAST_UP_INTO,
    SOUTH_WEST_UP_INTO,
    SOUTH_EAST_UP_INTO,
    NORTH_OUT,
    SOUTH_OUT,
    WEST_OUT,
    EAST_OUT,
    NORTH_WEST_OUT,
    NORTH_EAST_OUT,
    SOUTH_WEST_OUT,
    SOUTH_EAST_OUT,
    DOWN_OUT,
    UP_OUT,
    NORTH_DOWN_OUT,
    SOUTH_DOWN_OUT,
    WEST_DOWN_OUT,
    EAST_DOWN_OUT,
    NORTH_WEST_DOWN_OUT,
    NORTH_EAST_DOWN_OUT,
    SOUTH_WEST_DOWN_OUT,
    SOUTH_EAST_DOWN_OUT,
    NORTH_UP_OUT,
    SOUTH_UP_OUT,
    WEST_UP_OUT,
    EAST_UP_OUT,
    NORTH_WEST_UP_OUT,
    NORTH_EAST_UP_OUT,
    SOUTH_WEST_UP_OUT,
    SOUTH_EAST_UP_OUT,
    PREVIOUS,
    NEXT,
    NORTH_PREVIOUS,
    SOUTH_PREVIOUS,
    WEST_PREVIOUS,
    EAST_PREVIOUS,
    NORTH_WEST_PREVIOUS,
    NORTH_EAST_PREVIOUS,
    SOUTH_WEST_PREVIOUS,
    SOUTH_EAST_PREVIOUS,
    DOWN_PREVIOUS,
    UP_PREVIOUS,
    NORTH_DOWN_PREVIOUS,
    SOUTH_DOWN_PREVIOUS,
    WEST_DOWN_PREVIOUS,
    EAST_DOWN_PREVIOUS,
    NORTH_WEST_DOWN_PREVIOUS,
    NORTH_EAST_DOWN_PREVIOUS,
    SOUTH_WEST_DOWN_PREVIOUS,
    SOUTH_EAST_DOWN_PREVIOUS,
    NORTH_UP_PREVIOUS,
    SOUTH_UP_PREVIOUS,
    WEST_UP_PREVIOUS,
    EAST_UP_PREVIOUS,
    NORTH_WEST_UP_PREVIOUS,
    NORTH_EAST_UP_PREVIOUS,
    SOUTH_WEST_UP_PREVIOUS,
    SOUTH_EAST_UP_PREVIOUS,
    INTO_PREVIOUS,
    OUT_PREVIOUS,
    NORTH_INTO_PREVIOUS,
    SOUTH_INTO_PREVIOUS,
    WEST_INTO_PREVIOUS,
    EAST_INTO_PREVIOUS,
    NORTH_WEST_INTO_PREVIOUS,
    NORTH_EAST_INTO_PREVIOUS,
    SOUTH_WEST_INTO_PREVIOUS,
    SOUTH_EAST_INTO_PREVIOUS,
    DOWN_INTO_PREVIOUS,
    UP_INTO_PREVIOUS,
    NORTH_DOWN_INTO_PREVIOUS,
    SOUTH_DOWN_INTO_PREVIOUS,
    WEST_DOWN_INTO_PREVIOUS,
    EAST_DOWN_INTO_PREVIOUS,
    NORTH_WEST_DOWN_INTO_PREVIOUS,
    NORTH_EAST_DOWN_INTO_PREVIOUS,
    SOUTH_WEST_DOWN_INTO_PREVIOUS,
    SOUTH_EAST_DOWN_INTO_PREVIOUS,
    NORTH_UP_INTO_PREVIOUS,
    SOUTH_UP_INTO_PREVIOUS,
    WEST_UP_INTO_PREVIOUS,
    EAST_UP_INTO_PREVIOUS,
    NORTH_WEST_UP_INTO_PREVIOUS,
    NORTH_EAST_UP_INTO_PREVIOUS,
    SOUTH_WEST_UP_INTO_PREVIOUS,
    SOUTH_EAST_UP_INTO_PREVIOUS,
    NORTH_OUT_PREVIOUS,
    SOUTH_OUT_PREVIOUS,
    WEST_OUT_PREVIOUS,
    EAST_OUT_PREVIOUS,
    NORTH_WEST_OUT_PREVIOUS,
    NORTH_EAST_OUT_PREVIOUS,
    SOUTH_WEST_OUT_PREVIOUS,
    SOUTH_EAST_OUT_PREVIOUS,
    DOWN_OUT_PREVIOUS,
    UP_OUT_PREVIOUS,
    NORTH_DOWN_OUT_PREVIOUS,
    SOUTH_DOWN_OUT_PREVIOUS,
    WEST_DOWN_OUT_PREVIOUS,
    EAST_DOWN_OUT_PREVIOUS,
    NORTH_WEST_DOWN_OUT_PREVIOUS,
    NORTH_EAST_DOWN_OUT_PREVIOUS,
    SOUTH_WEST_DOWN_OUT_PREVIOUS,
    SOUTH_EAST_DOWN_OUT_PREVIOUS,
    NORTH_UP_OUT_PREVIOUS,
    SOUTH_UP_OUT_PREVIOUS,
    WEST_UP_OUT_PREVIOUS,
    EAST_UP_OUT_PREVIOUS,
    NORTH_WEST_UP_OUT_PREVIOUS,
    NORTH_EAST_UP_OUT_PREVIOUS,
    SOUTH_WEST_UP_OUT_PREVIOUS,
    SOUTH_EAST_UP_OUT_PREVIOUS,
    NORTH_NEXT,
    SOUTH_NEXT,
    WEST_NEXT,
    EAST_NEXT,
    NORTH_WEST_NEXT,
    NORTH_EAST_NEXT,
    SOUTH_WEST_NEXT,
    SOUTH_EAST_NEXT,
    DOWN_NEXT,
    UP_NEXT,
    NORTH_DOWN_NEXT,
    SOUTH_DOWN_NEXT,
    WEST_DOWN_NEXT,
    EAST_DOWN_NEXT,
    NORTH_WEST_DOWN_NEXT,
    NORTH_EAST_DOWN_NEXT,
    SOUTH_WEST_DOWN_NEXT,
    SOUTH_EAST_DOWN_NEXT,
    NORTH_UP_NEXT,
    SOUTH_UP_NEXT,
    WEST_UP_NEXT,
    EAST_UP_NEXT,
    NORTH_WEST_UP_NEXT,
    NORTH_EAST_UP_NEXT,
    SOUTH_WEST_UP_NEXT,
    SOUTH_EAST_UP_NEXT,
    INTO_NEXT,
    OUT_NEXT,
    NORTH_INTO_NEXT,
    SOUTH_INTO_NEXT,
    WEST_INTO_NEXT,
    EAST_INTO_NEXT,
    NORTH_WEST_INTO_NEXT,
    NORTH_EAST_INTO_NEXT,
    SOUTH_WEST_INTO_NEXT,
    SOUTH_EAST_INTO_NEXT,
    DOWN_INTO_NEXT,
    UP_INTO_NEXT,
    NORTH_DOWN_INTO_NEXT,
    SOUTH_DOWN_INTO_NEXT,
    WEST_DOWN_INTO_NEXT,
    EAST_DOWN_INTO_NEXT,
    NORTH_WEST_DOWN_INTO_NEXT,
    NORTH_EAST_DOWN_INTO_NEXT,
    SOUTH_WEST_DOWN_INTO_NEXT,
    SOUTH_EAST_DOWN_INTO_NEXT,
    NORTH_UP_INTO_NEXT,
    SOUTH_UP_INTO_NEXT,
    WEST_UP_INTO_NEXT,
    EAST_UP_INTO_NEXT,
    NORTH_WEST_UP_INTO_NEXT,
    NORTH_EAST_UP_INTO_NEXT,
    SOUTH_WEST_UP_INTO_NEXT,
    SOUTH_EAST_UP_INTO_NEXT,
    NORTH_OUT_NEXT,
    SOUTH_OUT_NEXT,
    WEST_OUT_NEXT,
    EAST_OUT_NEXT,
    NORTH_WEST_OUT_NEXT,
    NORTH_EAST_OUT_NEXT,
    SOUTH_WEST_OUT_NEXT,
    SOUTH_EAST_OUT_NEXT,
    DOWN_OUT_NEXT,
    UP_OUT_NEXT,
    NORTH_DOWN_OUT_NEXT,
    SOUTH_DOWN_OUT_NEXT,
    WEST_DOWN_OUT_NEXT,
    EAST_DOWN_OUT_NEXT,
    NORTH_WEST_DOWN_OUT_NEXT,
    NORTH_EAST_DOWN_OUT_NEXT,
    SOUTH_WEST_DOWN_OUT_NEXT,
    SOUTH_EAST_DOWN_OUT_NEXT,
    NORTH_UP_OUT_NEXT,
    SOUTH_UP_OUT_NEXT,
    WEST_UP_OUT_NEXT,
    EAST_UP_OUT_NEXT,
    NORTH_WEST_UP_OUT_NEXT,
    NORTH_EAST_UP_OUT_NEXT,
    SOUTH_WEST_UP_OUT_NEXT,
    SOUTH_EAST_UP_OUT_NEXT,
    PAST,
    FUTURE,
    NORTH_PAST,
    SOUTH_PAST,
    WEST_PAST,
    EAST_PAST,
    NORTH_WEST_PAST,
    NORTH_EAST_PAST,
    SOUTH_WEST_PAST,
    SOUTH_EAST_PAST,
    DOWN_PAST,
    UP_PAST,
    NORTH_DOWN_PAST,
    SOUTH_DOWN_PAST,
    WEST_DOWN_PAST,
    EAST_DOWN_PAST,
    NORTH_WEST_DOWN_PAST,
    NORTH_EAST_DOWN_PAST,
    SOUTH_WEST_DOWN_PAST,
    SOUTH_EAST_DOWN_PAST,
    NORTH_UP_PAST,
    SOUTH_UP_PAST,
    WEST_UP_PAST,
    EAST_UP_PAST,
    NORTH_WEST_UP_PAST,
    NORTH_EAST_UP_PAST,
    SOUTH_WEST_UP_PAST,
    SOUTH_EAST_UP_PAST,
    INTO_PAST,
    OUT_PAST,
    NORTH_INTO_PAST,
    SOUTH_INTO_PAST,
    WEST_INTO_PAST,
    EAST_INTO_PAST,
    NORTH_WEST_INTO_PAST,
    NORTH_EAST_INTO_PAST,
    SOUTH_WEST_INTO_PAST,
    SOUTH_EAST_INTO_PAST,
    DOWN_INTO_PAST,
    UP_INTO_PAST,
    NORTH_DOWN_INTO_PAST,
    SOUTH_DOWN_INTO_PAST,
    WEST_DOWN_INTO_PAST,
    EAST_DOWN_INTO_PAST,
    NORTH_WEST_DOWN_INTO_PAST,
    NORTH_EAST_DOWN_INTO_PAST,
    SOUTH_WEST_DOWN_INTO_PAST,
    SOUTH_EAST_DOWN_INTO_PAST,
    NORTH_UP_INTO_PAST,
    SOUTH_UP_INTO_PAST,
    WEST_UP_INTO_PAST,
    EAST_UP_INTO_PAST,
    NORTH_WEST_UP_INTO_PAST,
    NORTH_EAST_UP_INTO_PAST,
    SOUTH_WEST_UP_INTO_PAST,
    SOUTH_EAST_UP_INTO_PAST,
    NORTH_OUT_PAST,
    SOUTH_OUT_PAST,
    WEST_OUT_PAST,
    EAST_OUT_PAST,
    NORTH_WEST_OUT_PAST,
    NORTH_EAST_OUT_PAST,
    SOUTH_WEST_OUT_PAST,
    SOUTH_EAST_OUT_PAST,
    DOWN_OUT_PAST,
    UP_OUT_PAST,
    NORTH_DOWN_OUT_PAST,
    SOUTH_DOWN_OUT_PAST,
    WEST_DOWN_OUT_PAST,
    EAST_DOWN_OUT_PAST,
    NORTH_WEST_DOWN_OUT_PAST,
    NORTH_EAST_DOWN_OUT_PAST,
    SOUTH_WEST_DOWN_OUT_PAST,
    SOUTH_EAST_DOWN_OUT_PAST,
    NORTH_UP_OUT_PAST,
    SOUTH_UP_OUT_PAST,
    WEST_UP_OUT_PAST,
    EAST_UP_OUT_PAST,
    NORTH_WEST_UP_OUT_PAST,
    NORTH_EAST_UP_OUT_PAST,
    SOUTH_WEST_UP_OUT_PAST,
    SOUTH_EAST_UP_OUT_PAST,
    PREVIOUS_PAST,
    NEXT_PAST,
    NORTH_PREVIOUS_PAST,
    SOUTH_PREVIOUS_PAST,
    WEST_PREVIOUS_PAST,
    EAST_PREVIOUS_PAST,
    NORTH_WEST_PREVIOUS_PAST,
    NORTH_EAST_PREVIOUS_PAST,
    SOUTH_WEST_PREVIOUS_PAST,
    SOUTH_EAST_PREVIOUS_PAST,
    DOWN_PREVIOUS_PAST,
    UP_PREVIOUS_PAST,
    NORTH_DOWN_PREVIOUS_PAST,
    SOUTH_DOWN_PREVIOUS_PAST,
    WEST_DOWN_PREVIOUS_PAST,
    EAST_DOWN_PREVIOUS_PAST,
    NORTH_WEST_DOWN_PREVIOUS_PAST,
    NORTH_EAST_DOWN_PREVIOUS_PAST,
    SOUTH_WEST_DOWN_PREVIOUS_PAST,
    SOUTH_EAST_DOWN_PREVIOUS_PAST,
    NORTH_UP_PREVIOUS_PAST,
    SOUTH_UP_PREVIOUS_PAST,
    WEST_UP_PREVIOUS_PAST,
    EAST_UP_PREVIOUS_PAST,
    NORTH_WEST_UP_PREVIOUS_PAST,
    NORTH_EAST_UP_PREVIOUS_PAST,
    SOUTH_WEST_UP_PREVIOUS_PAST,
    SOUTH_EAST_UP_PREVIOUS_PAST,
    INTO_PREVIOUS_PAST,
    OUT_PREVIOUS_PAST,
    NORTH_INTO_PREVIOUS_PAST,
    SOUTH_INTO_PREVIOUS_PAST,
    WEST_INTO_PREVIOUS_PAST,
    EAST_INTO_PREVIOUS_PAST,
    NORTH_WEST_INTO_PREVIOUS_PAST,
    NORTH_EAST_INTO_PREVIOUS_PAST,
    SOUTH_WEST_INTO_PREVIOUS_PAST,
    SOUTH_EAST_INTO_PREVIOUS_PAST,
    DOWN_INTO_PREVIOUS_PAST,
    UP_INTO_PREVIOUS_PAST,
    NORTH_DOWN_INTO_PREVIOUS_PAST,
    SOUTH_DOWN_INTO_PREVIOUS_PAST,
    WEST_DOWN_INTO_PREVIOUS_PAST,
    EAST_DOWN_INTO_PREVIOUS_PAST,
    NORTH_WEST_DOWN_INTO_PREVIOUS_PAST,
    NORTH_EAST_DOWN_INTO_PREVIOUS_PAST,
    SOUTH_WEST_DOWN_INTO_PREVIOUS_PAST,
    SOUTH_EAST_DOWN_INTO_PREVIOUS_PAST,
    NORTH_UP_INTO_PREVIOUS_PAST,
    SOUTH_UP_INTO_PREVIOUS_PAST,
    WEST_UP_INTO_PREVIOUS_PAST,
    EAST_UP_INTO_PREVIOUS_PAST,
    NORTH_WEST_UP_INTO_PREVIOUS_PAST,
    NORTH_EAST_UP_INTO_PREVIOUS_PAST,
    SOUTH_WEST_UP_INTO_PREVIOUS_PAST,
    SOUTH_EAST_UP_INTO_PREVIOUS_PAST,
    NORTH_OUT_PREVIOUS_PAST,
    SOUTH_OUT_PREVIOUS_PAST,
    WEST_OUT_PREVIOUS_PAST,
    EAST_OUT_PREVIOUS_PAST,
    NORTH_WEST_OUT_PREVIOUS_PAST,
    NORTH_EAST_OUT_PREVIOUS_PAST,
    SOUTH_WEST_OUT_PREVIOUS_PAST,
    SOUTH_EAST_OUT_PREVIOUS_PAST,
    DOWN_OUT_PREVIOUS_PAST,
    UP_OUT_PREVIOUS_PAST,
    NORTH_DOWN_OUT_PREVIOUS_PAST,
    SOUTH_DOWN_OUT_PREVIOUS_PAST,
    WEST_DOWN_OUT_PREVIOUS_PAST,
    EAST_DOWN_OUT_PREVIOUS_PAST,
    NORTH_WEST_DOWN_OUT_PREVIOUS_PAST,
    NORTH_EAST_DOWN_OUT_PREVIOUS_PAST,
    SOUTH_WEST_DOWN_OUT_PREVIOUS_PAST,
    SOUTH_EAST_DOWN_OUT_PREVIOUS_PAST,
    NORTH_UP_OUT_PREVIOUS_PAST,
    SOUTH_UP_OUT_PREVIOUS_PAST,
    WEST_UP_OUT_PREVIOUS_PAST,
    EAST_UP_OUT_PREVIOUS_PAST,
    NORTH_WEST_UP_OUT_PREVIOUS_PAST,
    NORTH_EAST_UP_OUT_PREVIOUS_PAST,
    SOUTH_WEST_UP_OUT_PREVIOUS_PAST,
    SOUTH_EAST_UP_OUT_PREVIOUS_PAST,
    NORTH_NEXT_PAST,
    SOUTH_NEXT_PAST,
    WEST_NEXT_PAST,
    EAST_NEXT_PAST,
    NORTH_WEST_NEXT_PAST,
    NORTH_EAST_NEXT_PAST,
    SOUTH_WEST_NEXT_PAST,
    SOUTH_EAST_NEXT_PAST,
    DOWN_NEXT_PAST,
    UP_NEXT_PAST,
    NORTH_DOWN_NEXT_PAST,
    SOUTH_DOWN_NEXT_PAST,
    WEST_DOWN_NEXT_PAST,
    EAST_DOWN_NEXT_PAST,
    NORTH_WEST_DOWN_NEXT_PAST,
    NORTH_EAST_DOWN_NEXT_PAST,
    SOUTH_WEST_DOWN_NEXT_PAST,
    SOUTH_EAST_DOWN_NEXT_PAST,
    NORTH_UP_NEXT_PAST,
    SOUTH_UP_NEXT_PAST,
    WEST_UP_NEXT_PAST,
    EAST_UP_NEXT_PAST,
    NORTH_WEST_UP_NEXT_PAST,
    NORTH_EAST_UP_NEXT_PAST,
    SOUTH_WEST_UP_NEXT_PAST,
    SOUTH_EAST_UP_NEXT_PAST,
    INTO_NEXT_PAST,
    OUT_NEXT_PAST,
    NORTH_INTO_NEXT_PAST,
    SOUTH_INTO_NEXT_PAST,
    WEST_INTO_NEXT_PAST,
    EAST_INTO_NEXT_PAST,
    NORTH_WEST_INTO_NEXT_PAST,
    NORTH_EAST_INTO_NEXT_PAST,
    SOUTH_WEST_INTO_NEXT_PAST,
    SOUTH_EAST_INTO_NEXT_PAST,
    DOWN_INTO_NEXT_PAST,
    UP_INTO_NEXT_PAST,
    NORTH_DOWN_INTO_NEXT_PAST,
    SOUTH_DOWN_INTO_NEXT_PAST,
    WEST_DOWN_INTO_NEXT_PAST,
    EAST_DOWN_INTO_NEXT_PAST,
    NORTH_WEST_DOWN_INTO_NEXT_PAST,
    NORTH_EAST_DOWN_INTO_NEXT_PAST,
    SOUTH_WEST_DOWN_INTO_NEXT_PAST,
    SOUTH_EAST_DOWN_INTO_NEXT_PAST,
    NORTH_UP_INTO_NEXT_PAST,
    SOUTH_UP_INTO_NEXT_PAST,
    WEST_UP_INTO_NEXT_PAST,
    EAST_UP_INTO_NEXT_PAST,
    NORTH_WEST_UP_INTO_NEXT_PAST,
    NORTH_EAST_UP_INTO_NEXT_PAST,
    SOUTH_WEST_UP_INTO_NEXT_PAST,
    SOUTH_EAST_UP_INTO_NEXT_PAST,
    NORTH_OUT_NEXT_PAST,
    SOUTH_OUT_NEXT_PAST,
    WEST_OUT_NEXT_PAST,
    EAST_OUT_NEXT_PAST,
    NORTH_WEST_OUT_NEXT_PAST,
    NORTH_EAST_OUT_NEXT_PAST,
    SOUTH_WEST_OUT_NEXT_PAST,
    SOUTH_EAST_OUT_NEXT_PAST,
    DOWN_OUT_NEXT_PAST,
    UP_OUT_NEXT_PAST,
    NORTH_DOWN_OUT_NEXT_PAST,
    SOUTH_DOWN_OUT_NEXT_PAST,
    WEST_DOWN_OUT_NEXT_PAST,
    EAST_DOWN_OUT_NEXT_PAST,
    NORTH_WEST_DOWN_OUT_NEXT_PAST,
    NORTH_EAST_DOWN_OUT_NEXT_PAST,
    SOUTH_WEST_DOWN_OUT_NEXT_PAST,
    SOUTH_EAST_DOWN_OUT_NEXT_PAST,
    NORTH_UP_OUT_NEXT_PAST,
    SOUTH_UP_OUT_NEXT_PAST,
    WEST_UP_OUT_NEXT_PAST,
    EAST_UP_OUT_NEXT_PAST,
    NORTH_WEST_UP_OUT_NEXT_PAST,
    NORTH_EAST_UP_OUT_NEXT_PAST,
    SOUTH_WEST_UP_OUT_NEXT_PAST,
    SOUTH_EAST_UP_OUT_NEXT_PAST,
    NORTH_FUTURE,
    SOUTH_FUTURE,
    WEST_FUTURE,
    EAST_FUTURE,
    NORTH_WEST_FUTURE,
    NORTH_EAST_FUTURE,
    SOUTH_WEST_FUTURE,
    SOUTH_EAST_FUTURE,
    DOWN_FUTURE,
    UP_FUTURE,
    NORTH_DOWN_FUTURE,
    SOUTH_DOWN_FUTURE,
    WEST_DOWN_FUTURE,
    EAST_DOWN_FUTURE,
    NORTH_WEST_DOWN_FUTURE,
    NORTH_EAST_DOWN_FUTURE,
    SOUTH_WEST_DOWN_FUTURE,
    SOUTH_EAST_DOWN_FUTURE,
    NORTH_UP_FUTURE,
    SOUTH_UP_FUTURE,
    WEST_UP_FUTURE,
    EAST_UP_FUTURE,
    NORTH_WEST_UP_FUTURE,
    NORTH_EAST_UP_FUTURE,
    SOUTH_WEST_UP_FUTURE,
    SOUTH_EAST_UP_FUTURE,
    INTO_FUTURE,
    OUT_FUTURE,
    NORTH_INTO_FUTURE,
    SOUTH_INTO_FUTURE,
    WEST_INTO_FUTURE,
    EAST_INTO_FUTURE,
    NORTH_WEST_INTO_FUTURE,
    NORTH_EAST_INTO_FUTURE,
    SOUTH_WEST_INTO_FUTURE,
    SOUTH_EAST_INTO_FUTURE,
    DOWN_INTO_FUTURE,
    UP_INTO_FUTURE,
    NORTH_DOWN_INTO_FUTURE,
    SOUTH_DOWN_INTO_FUTURE,
    WEST_DOWN_INTO_FUTURE,
    EAST_DOWN_INTO_FUTURE,
    NORTH_WEST_DOWN_INTO_FUTURE,
    NORTH_EAST_DOWN_INTO_FUTURE,
    SOUTH_WEST_DOWN_INTO_FUTURE,
    SOUTH_EAST_DOWN_INTO_FUTURE,
    NORTH_UP_INTO_FUTURE,
    SOUTH_UP_INTO_FUTURE,
    WEST_UP_INTO_FUTURE,
    EAST_UP_INTO_FUTURE,
    NORTH_WEST_UP_INTO_FUTURE,
    NORTH_EAST_UP_INTO_FUTURE,
    SOUTH_WEST_UP_INTO_FUTURE,
    SOUTH_EAST_UP_INTO_FUTURE,
    NORTH_OUT_FUTURE,
    SOUTH_OUT_FUTURE,
    WEST_OUT_FUTURE,
    EAST_OUT_FUTURE,
    NORTH_WEST_OUT_FUTURE,
    NORTH_EAST_OUT_FUTURE,
    SOUTH_WEST_OUT_FUTURE,
    SOUTH_EAST_OUT_FUTURE,
    DOWN_OUT_FUTURE,
    UP_OUT_FUTURE,
    NORTH_DOWN_OUT_FUTURE,
    SOUTH_DOWN_OUT_FUTURE,
    WEST_DOWN_OUT_FUTURE,
    EAST_DOWN_OUT_FUTURE,
    NORTH_WEST_DOWN_OUT_FUTURE,
    NORTH_EAST_DOWN_OUT_FUTURE,
    SOUTH_WEST_DOWN_OUT_FUTURE,
    SOUTH_EAST_DOWN_OUT_FUTURE,
    NORTH_UP_OUT_FUTURE,
    SOUTH_UP_OUT_FUTURE,
    WEST_UP_OUT_FUTURE,
    EAST_UP_OUT_FUTURE,
    NORTH_WEST_UP_OUT_FUTURE,
    NORTH_EAST_UP_OUT_FUTURE,
    SOUTH_WEST_UP_OUT_FUTURE,
    SOUTH_EAST_UP_OUT_FUTURE,
    PREVIOUS_FUTURE,
    NEXT_FUTURE,
    NORTH_PREVIOUS_FUTURE,
    SOUTH_PREVIOUS_FUTURE,
    WEST_PREVIOUS_FUTURE,
    EAST_PREVIOUS_FUTURE,
    NORTH_WEST_PREVIOUS_FUTURE,
    NORTH_EAST_PREVIOUS_FUTURE,
    SOUTH_WEST_PREVIOUS_FUTURE,
    SOUTH_EAST_PREVIOUS_FUTURE,
    DOWN_PREVIOUS_FUTURE,
    UP_PREVIOUS_FUTURE,
    NORTH_DOWN_PREVIOUS_FUTURE,
    SOUTH_DOWN_PREVIOUS_FUTURE,
    WEST_DOWN_PREVIOUS_FUTURE,
    EAST_DOWN_PREVIOUS_FUTURE,
    NORTH_WEST_DOWN_PREVIOUS_FUTURE,
    NORTH_EAST_DOWN_PREVIOUS_FUTURE,
    SOUTH_WEST_DOWN_PREVIOUS_FUTURE,
    SOUTH_EAST_DOWN_PREVIOUS_FUTURE,
    NORTH_UP_PREVIOUS_FUTURE,
    SOUTH_UP_PREVIOUS_FUTURE,
    WEST_UP_PREVIOUS_FUTURE,
    EAST_UP_PREVIOUS_FUTURE,
    NORTH_WEST_UP_PREVIOUS_FUTURE,
    NORTH_EAST_UP_PREVIOUS_FUTURE,
    SOUTH_WEST_UP_PREVIOUS_FUTURE,
    SOUTH_EAST_UP_PREVIOUS_FUTURE,
    INTO_PREVIOUS_FUTURE,
    OUT_PREVIOUS_FUTURE,
    NORTH_INTO_PREVIOUS_FUTURE,
    SOUTH_INTO_PREVIOUS_FUTURE,
    WEST_INTO_PREVIOUS_FUTURE,
    EAST_INTO_PREVIOUS_FUTURE,
    NORTH_WEST_INTO_PREVIOUS_FUTURE,
    NORTH_EAST_INTO_PREVIOUS_FUTURE,
    SOUTH_WEST_INTO_PREVIOUS_FUTURE,
    SOUTH_EAST_INTO_PREVIOUS_FUTURE,
    DOWN_INTO_PREVIOUS_FUTURE,
    UP_INTO_PREVIOUS_FUTURE,
    NORTH_DOWN_INTO_PREVIOUS_FUTURE,
    SOUTH_DOWN_INTO_PREVIOUS_FUTURE,
    WEST_DOWN_INTO_PREVIOUS_FUTURE,
    EAST_DOWN_INTO_PREVIOUS_FUTURE,
    NORTH_WEST_DOWN_INTO_PREVIOUS_FUTURE,
    NORTH_EAST_DOWN_INTO_PREVIOUS_FUTURE,
    SOUTH_WEST_DOWN_INTO_PREVIOUS_FUTURE,
    SOUTH_EAST_DOWN_INTO_PREVIOUS_FUTURE,
    NORTH_UP_INTO_PREVIOUS_FUTURE,
    SOUTH_UP_INTO_PREVIOUS_FUTURE,
    WEST_UP_INTO_PREVIOUS_FUTURE,
    EAST_UP_INTO_PREVIOUS_FUTURE,
    NORTH_WEST_UP_INTO_PREVIOUS_FUTURE,
    NORTH_EAST_UP_INTO_PREVIOUS_FUTURE,
    SOUTH_WEST_UP_INTO_PREVIOUS_FUTURE,
    SOUTH_EAST_UP_INTO_PREVIOUS_FUTURE,
    NORTH_OUT_PREVIOUS_FUTURE,
    SOUTH_OUT_PREVIOUS_FUTURE,
    WEST_OUT_PREVIOUS_FUTURE,
    EAST_OUT_PREVIOUS_FUTURE,
    NORTH_WEST_OUT_PREVIOUS_FUTURE,
    NORTH_EAST_OUT_PREVIOUS_FUTURE,
    SOUTH_WEST_OUT_PREVIOUS_FUTURE,
    SOUTH_EAST_OUT_PREVIOUS_FUTURE,
    DOWN_OUT_PREVIOUS_FUTURE,
    UP_OUT_PREVIOUS_FUTURE,
    NORTH_DOWN_OUT_PREVIOUS_FUTURE,
    SOUTH_DOWN_OUT_PREVIOUS_FUTURE,
    WEST_DOWN_OUT_PREVIOUS_FUTURE,
    EAST_DOWN_OUT_PREVIOUS_FUTURE,
    NORTH_WEST_DOWN_OUT_PREVIOUS_FUTURE,
    NORTH_EAST_DOWN_OUT_PREVIOUS_FUTURE,
    SOUTH_WEST_DOWN_OUT_PREVIOUS_FUTURE,
    SOUTH_EAST_DOWN_OUT_PREVIOUS_FUTURE,
    NORTH_UP_OUT_PREVIOUS_FUTURE,
    SOUTH_UP_OUT_PREVIOUS_FUTURE,
    WEST_UP_OUT_PREVIOUS_FUTURE,
    EAST_UP_OUT_PREVIOUS_FUTURE,
    NORTH_WEST_UP_OUT_PREVIOUS_FUTURE,
    NORTH_EAST_UP_OUT_PREVIOUS_FUTURE,
    SOUTH_WEST_UP_OUT_PREVIOUS_FUTURE,
    SOUTH_EAST_UP_OUT_PREVIOUS_FUTURE,
    NORTH_NEXT_FUTURE,
    SOUTH_NEXT_FUTURE,
    WEST_NEXT_FUTURE,
    EAST_NEXT_FUTURE,
    NORTH_WEST_NEXT_FUTURE,
    NORTH_EAST_NEXT_FUTURE,
    SOUTH_WEST_NEXT_FUTURE,
    SOUTH_EAST_NEXT_FUTURE,
    DOWN_NEXT_FUTURE,
    UP_NEXT_FUTURE,
    NORTH_DOWN_NEXT_FUTURE,
    SOUTH_DOWN_NEXT_FUTURE,
    WEST_DOWN_NEXT_FUTURE,
    EAST_DOWN_NEXT_FUTURE,
    NORTH_WEST_DOWN_NEXT_FUTURE,
    NORTH_EAST_DOWN_NEXT_FUTURE,
    SOUTH_WEST_DOWN_NEXT_FUTURE,
    SOUTH_EAST_DOWN_NEXT_FUTURE,
    NORTH_UP_NEXT_FUTURE,
    SOUTH_UP_NEXT_FUTURE,
    WEST_UP_NEXT_FUTURE,
    EAST_UP_NEXT_FUTURE,
    NORTH_WEST_UP_NEXT_FUTURE,
    NORTH_EAST_UP_NEXT_FUTURE,
    SOUTH_WEST_UP_NEXT_FUTURE,
    SOUTH_EAST_UP_NEXT_FUTURE,
    INTO_NEXT_FUTURE,
    OUT_NEXT_FUTURE,
    NORTH_INTO_NEXT_FUTURE,
    SOUTH_INTO_NEXT_FUTURE,
    WEST_INTO_NEXT_FUTURE,
    EAST_INTO_NEXT_FUTURE,
    NORTH_WEST_INTO_NEXT_FUTURE,
    NORTH_EAST_INTO_NEXT_FUTURE,
    SOUTH_WEST_INTO_NEXT_FUTURE,
    SOUTH_EAST_INTO_NEXT_FUTURE,
    DOWN_INTO_NEXT_FUTURE,
    UP_INTO_NEXT_FUTURE,
    NORTH_DOWN_INTO_NEXT_FUTURE,
    SOUTH_DOWN_INTO_NEXT_FUTURE,
    WEST_DOWN_INTO_NEXT_FUTURE,
    EAST_DOWN_INTO_NEXT_FUTURE,
    NORTH_WEST_DOWN_INTO_NEXT_FUTURE,
    NORTH_EAST_DOWN_INTO_NEXT_FUTURE,
    SOUTH_WEST_DOWN_INTO_NEXT_FUTURE,
    SOUTH_EAST_DOWN_INTO_NEXT_FUTURE,
    NORTH_UP_INTO_NEXT_FUTURE,
    SOUTH_UP_INTO_NEXT_FUTURE,
    WEST_UP_INTO_NEXT_FUTURE,
    EAST_UP_INTO_NEXT_FUTURE,
    NORTH_WEST_UP_INTO_NEXT_FUTURE,
    NORTH_EAST_UP_INTO_NEXT_FUTURE,
    SOUTH_WEST_UP_INTO_NEXT_FUTURE,
    SOUTH_EAST_UP_INTO_NEXT_FUTURE,
    NORTH_OUT_NEXT_FUTURE,
    SOUTH_OUT_NEXT_FUTURE,
    WEST_OUT_NEXT_FUTURE,
    EAST_OUT_NEXT_FUTURE,
    NORTH_WEST_OUT_NEXT_FUTURE,
    NORTH_EAST_OUT_NEXT_FUTURE,
    SOUTH_WEST_OUT_NEXT_FUTURE,
    SOUTH_EAST_OUT_NEXT_FUTURE,
    DOWN_OUT_NEXT_FUTURE,
    UP_OUT_NEXT_FUTURE,
    NORTH_DOWN_OUT_NEXT_FUTURE,
    SOUTH_DOWN_OUT_NEXT_FUTURE,
    WEST_DOWN_OUT_NEXT_FUTURE,
    EAST_DOWN_OUT_NEXT_FUTURE,
    NORTH_WEST_DOWN_OUT_NEXT_FUTURE,
    NORTH_EAST_DOWN_OUT_NEXT_FUTURE,
    SOUTH_WEST_DOWN_OUT_NEXT_FUTURE,
    SOUTH_EAST_DOWN_OUT_NEXT_FUTURE,
    NORTH_UP_OUT_NEXT_FUTURE,
    SOUTH_UP_OUT_NEXT_FUTURE,
    WEST_UP_OUT_NEXT_FUTURE,
    EAST_UP_OUT_NEXT_FUTURE,
    NORTH_WEST_UP_OUT_NEXT_FUTURE,
    NORTH_EAST_UP_OUT_NEXT_FUTURE,
    SOUTH_WEST_UP_OUT_NEXT_FUTURE,
    SOUTH_EAST_UP_OUT_NEXT_FUTURE
}
/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.Direction;

public class DirectionHelper {
    public static String toStringValue(final Direction thing) {
	return Integer.toString(thing.ordinal());
    }

    public static Direction fromOrdinal(final int value) {
	return Direction.values()[value];
    }

    public static Direction fromStringValue(final String value) {
	return Direction.values()[Integer.parseInt(value)];
    }

    public static Direction invert(final Direction thing) {
	switch (thing) {
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.SOUTH;
	case NORTHEAST:
	    return Direction.SOUTHWEST;
	case EAST:
	    return Direction.WEST;
	case SOUTHEAST:
	    return Direction.NORTHWEST;
	case SOUTH:
	    return Direction.NORTH;
	case SOUTHWEST:
	    return Direction.NORTHEAST;
	case WEST:
	    return Direction.EAST;
	case NORTHWEST:
	    return Direction.SOUTHEAST;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	case BOTH:
	    return Direction.BOTH;
	default:
	    return Direction.NONE;
	}
    }

    public static Direction next(final Direction thing) {
	switch (thing) {
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.NORTHEAST;
	case NORTHEAST:
	    return Direction.EAST;
	case EAST:
	    return Direction.SOUTHEAST;
	case SOUTHEAST:
	    return Direction.SOUTH;
	case SOUTH:
	    return Direction.SOUTHWEST;
	case SOUTHWEST:
	    return Direction.WEST;
	case WEST:
	    return Direction.NORTHWEST;
	case NORTHWEST:
	    return Direction.NORTH;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	case BOTH:
	    return Direction.BOTH;
	default:
	    return Direction.NONE;
	}
    }

    public static Direction nextOrthogonal(final Direction thing) {
	switch (thing) {
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.EAST;
	case NORTHEAST:
	    return Direction.SOUTHEAST;
	case EAST:
	    return Direction.SOUTH;
	case SOUTHEAST:
	    return Direction.SOUTHWEST;
	case SOUTH:
	    return Direction.WEST;
	case SOUTHWEST:
	    return Direction.NORTHWEST;
	case WEST:
	    return Direction.NORTH;
	case NORTHWEST:
	    return Direction.NORTHEAST;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	case BOTH:
	    return Direction.BOTH;
	default:
	    return Direction.NONE;
	}
    }

    public static Direction previous(final Direction thing) {
	switch (thing) {
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.NORTHWEST;
	case NORTHEAST:
	    return Direction.NORTH;
	case EAST:
	    return Direction.NORTHEAST;
	case SOUTHEAST:
	    return Direction.EAST;
	case SOUTH:
	    return Direction.SOUTHEAST;
	case SOUTHWEST:
	    return Direction.SOUTH;
	case WEST:
	    return Direction.SOUTHWEST;
	case NORTHWEST:
	    return Direction.WEST;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	case BOTH:
	    return Direction.BOTH;
	default:
	    return Direction.NONE;
	}
    }

    public static Direction previousOrthogonal(final Direction thing) {
	switch (thing) {
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.WEST;
	case NORTHEAST:
	    return Direction.NORTHWEST;
	case EAST:
	    return Direction.NORTH;
	case SOUTHEAST:
	    return Direction.NORTHEAST;
	case SOUTH:
	    return Direction.EAST;
	case SOUTHWEST:
	    return Direction.SOUTHEAST;
	case WEST:
	    return Direction.SOUTH;
	case NORTHWEST:
	    return Direction.SOUTHWEST;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	case BOTH:
	    return Direction.BOTH;
	default:
	    return Direction.NONE;
	}
    }

    public static Direction resolveRelative(final int dX, final int dY) {
	final int dirX = (int) Math.signum(dX);
	final int dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	} else if (dirX == 0 && dirY == -1) {
	    return Direction.NORTH;
	} else if (dirX == 0 && dirY == 1) {
	    return Direction.SOUTH;
	} else if (dirX == -1 && dirY == 0) {
	    return Direction.WEST;
	} else if (dirX == 1 && dirY == 0) {
	    return Direction.EAST;
	} else if (dirX == 1 && dirY == 1) {
	    return Direction.SOUTHEAST;
	} else if (dirX == -1 && dirY == 1) {
	    return Direction.SOUTHWEST;
	} else if (dirX == -1 && dirY == -1) {
	    return Direction.NORTHWEST;
	} else if (dirX == 1 && dirY == -1) {
	    return Direction.NORTHEAST;
	} else {
	    return Direction.NONE;
	}
    }

    public static Direction resolveRelativeHV(final int dX, final int dY) {
	final int dirX = (int) Math.signum(dX);
	final int dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	} else if (dirX == 0 && dirY == -1) {
	    return Direction.VERTICAL;
	} else if (dirX == 0 && dirY == 1) {
	    return Direction.VERTICAL;
	} else if (dirX == -1 && dirY == 0) {
	    return Direction.HORIZONTAL;
	} else if (dirX == 1 && dirY == 0) {
	    return Direction.HORIZONTAL;
	} else if (dirX == 1 && dirY == 1) {
	    return Direction.SOUTHEAST;
	} else if (dirX == -1 && dirY == 1) {
	    return Direction.SOUTHWEST;
	} else if (dirX == -1 && dirY == -1) {
	    return Direction.NORTHWEST;
	} else if (dirX == 1 && dirY == -1) {
	    return Direction.NORTHEAST;
	} else {
	    return Direction.NONE;
	}
    }

    public static Direction resolveRelativeInvert(final int dX, final int dY) {
	final int dirX = (int) Math.signum(dX);
	final int dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	} else if (dirX == 0 && dirY == -1) {
	    return Direction.SOUTH;
	} else if (dirX == 0 && dirY == 1) {
	    return Direction.NORTH;
	} else if (dirX == -1 && dirY == 0) {
	    return Direction.EAST;
	} else if (dirX == 1 && dirY == 0) {
	    return Direction.WEST;
	} else if (dirX == 1 && dirY == 1) {
	    return Direction.NORTHWEST;
	} else if (dirX == -1 && dirY == 1) {
	    return Direction.NORTHEAST;
	} else if (dirX == -1 && dirY == -1) {
	    return Direction.SOUTHEAST;
	} else if (dirX == 1 && dirY == -1) {
	    return Direction.SOUTHWEST;
	} else {
	    return Direction.NONE;
	}
    }

    public static int[] unresolveRelative(final Direction dir) {
	int[] res = new int[2];
	if (dir == Direction.NONE) {
	    res[0] = 0;
	    res[1] = 0;
	} else if (dir == Direction.NORTH) {
	    res[0] = 0;
	    res[1] = -1;
	} else if (dir == Direction.SOUTH) {
	    res[0] = 0;
	    res[1] = 1;
	} else if (dir == Direction.WEST) {
	    res[0] = -1;
	    res[1] = 0;
	} else if (dir == Direction.EAST) {
	    res[0] = 1;
	    res[1] = 0;
	} else if (dir == Direction.SOUTHEAST) {
	    res[0] = 1;
	    res[1] = 1;
	} else if (dir == Direction.SOUTHWEST) {
	    res[0] = -1;
	    res[1] = 1;
	} else if (dir == Direction.NORTHWEST) {
	    res[0] = -1;
	    res[1] = -1;
	} else if (dir == Direction.NORTHEAST) {
	    res[0] = 1;
	    res[1] = -1;
	} else {
	    res = null;
	}
	return res;
    }

    private DirectionHelper() {
	// Do nothing
    }
}

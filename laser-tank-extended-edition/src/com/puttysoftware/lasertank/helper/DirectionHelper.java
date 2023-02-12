/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

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
	return switch (thing) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.SOUTH;
	case NORTHEAST -> Direction.SOUTHWEST;
	case EAST -> Direction.WEST;
	case SOUTHEAST -> Direction.NORTHWEST;
	case SOUTH -> Direction.NORTH;
	case SOUTHWEST -> Direction.NORTHEAST;
	case WEST -> Direction.EAST;
	case NORTHWEST -> Direction.SOUTHEAST;
	case HORIZONTAL -> Direction.VERTICAL;
	case VERTICAL -> Direction.HORIZONTAL;
	case BOTH -> Direction.BOTH;
	default -> Direction.NONE;
	};
    }

    public static Direction next(final Direction thing) {
	return switch (thing) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.NORTHEAST;
	case NORTHEAST -> Direction.EAST;
	case EAST -> Direction.SOUTHEAST;
	case SOUTHEAST -> Direction.SOUTH;
	case SOUTH -> Direction.SOUTHWEST;
	case SOUTHWEST -> Direction.WEST;
	case WEST -> Direction.NORTHWEST;
	case NORTHWEST -> Direction.NORTH;
	case HORIZONTAL -> Direction.VERTICAL;
	case VERTICAL -> Direction.HORIZONTAL;
	case BOTH -> Direction.BOTH;
	default -> Direction.NONE;
	};
    }

    public static Direction nextOrthogonal(final Direction thing) {
	return switch (thing) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.EAST;
	case NORTHEAST -> Direction.SOUTHEAST;
	case EAST -> Direction.SOUTH;
	case SOUTHEAST -> Direction.SOUTHWEST;
	case SOUTH -> Direction.WEST;
	case SOUTHWEST -> Direction.NORTHWEST;
	case WEST -> Direction.NORTH;
	case NORTHWEST -> Direction.NORTHEAST;
	case HORIZONTAL -> Direction.VERTICAL;
	case VERTICAL -> Direction.HORIZONTAL;
	case BOTH -> Direction.BOTH;
	default -> Direction.NONE;
	};
    }

    public static Direction previous(final Direction thing) {
	return switch (thing) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.NORTHWEST;
	case NORTHEAST -> Direction.NORTH;
	case EAST -> Direction.NORTHEAST;
	case SOUTHEAST -> Direction.EAST;
	case SOUTH -> Direction.SOUTHEAST;
	case SOUTHWEST -> Direction.SOUTH;
	case WEST -> Direction.SOUTHWEST;
	case NORTHWEST -> Direction.WEST;
	case HORIZONTAL -> Direction.VERTICAL;
	case VERTICAL -> Direction.HORIZONTAL;
	case BOTH -> Direction.BOTH;
	default -> Direction.NONE;
	};
    }

    public static Direction previousOrthogonal(final Direction thing) {
	return switch (thing) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.WEST;
	case NORTHEAST -> Direction.NORTHWEST;
	case EAST -> Direction.NORTH;
	case SOUTHEAST -> Direction.NORTHEAST;
	case SOUTH -> Direction.EAST;
	case SOUTHWEST -> Direction.SOUTHEAST;
	case WEST -> Direction.SOUTH;
	case NORTHWEST -> Direction.SOUTHWEST;
	case HORIZONTAL -> Direction.VERTICAL;
	case VERTICAL -> Direction.HORIZONTAL;
	case BOTH -> Direction.BOTH;
	default -> Direction.NONE;
	};
    }

    public static Direction resolveRelative(final int dX, final int dY) {
	final var dirX = (int) Math.signum(dX);
	final var dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	}
	if (dirX == 0 && dirY == -1) {
	    return Direction.NORTH;
	}
	if (dirX == 0 && dirY == 1) {
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
	final var dirX = (int) Math.signum(dX);
	final var dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	}
	if ((dirX == 0 && (dirY == -1 || dirY == 1))) {
	    return Direction.VERTICAL;
	}
	if ((dirX == -1 && dirY == 0) || (dirX == 1 && dirY == 0)) {
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
	final var dirX = (int) Math.signum(dX);
	final var dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	}
	if (dirX == 0 && dirY == -1) {
	    return Direction.SOUTH;
	}
	if (dirX == 0 && dirY == 1) {
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
	var res = new int[2];
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

package com.puttysoftware.lasertank.datatype;

import com.puttysoftware.lasertank.index.GameAction;

final class LaserTankPlaybackEntry {
    private final GameAction action;
    private final int dirX;
    private final int dirY;
    private final int dirZ;
    private final int dirW;

    LaserTankPlaybackEntry(final GameAction action1, final int dirX1, final int dirY1, final int dirZ1,
            final int dirW1) {
        this.action = action1;
        this.dirX = dirX1;
        this.dirY = dirY1;
        this.dirZ = dirZ1;
        this.dirW = dirW1;
    }

    LaserTankPlaybackEntry(final LaserTankPlaybackEntry source) {
        this.action = source.action;
        this.dirX = source.dirX;
        this.dirY = source.dirY;
        this.dirZ = source.dirZ;
        this.dirW = source.dirW;
    }

    GameAction getAction() {
        return this.action;
    }

    int getDirW() {
        return this.dirW;
    }

    int getDirX() {
        return this.dirX;
    }

    int getDirY() {
        return this.dirY;
    }

    int getDirZ() {
        return this.dirZ;
    }
}

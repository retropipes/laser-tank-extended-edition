package com.puttysoftware.lasertank.datatype;

import com.puttysoftware.lasertank.index.GameAction;

final class LaserTankPlaybackEntry {
    private final GameAction action;
    private final int dirX;
    private final int dirY;
    private final int dirZ;
    private final int dirW;

    LaserTankPlaybackEntry(GameAction action1, int dirX1, int dirY1, int dirZ1, int dirW1) {
	super();
	this.action = action1;
	this.dirX = dirX1;
	this.dirY = dirY1;
	this.dirZ = dirZ1;
	this.dirW = dirW1;
    }

    LaserTankPlaybackEntry(LaserTankPlaybackEntry source) {
	super();
	this.action = source.action;
	this.dirX = source.dirX;
	this.dirY = source.dirY;
	this.dirZ = source.dirZ;
	this.dirW = source.dirW;
    }

    final GameAction getAction() {
	return this.action;
    }

    final int getDirW() {
	return this.dirW;
    }

    final int getDirX() {
	return this.dirX;
    }

    final int getDirY() {
	return this.dirY;
    }

    final int getDirZ() {
	return this.dirZ;
    }
}

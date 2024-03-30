package com.puttysoftware.lasertank.game;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

class UpdatePushedPositionTask implements Runnable {
    private final int x;
    private final int y;
    private final int pushX;
    private final int pushY;
    private final ArenaObject o;
    private final int x2;
    private final int y2;
    private final ArenaObject other;
    private final LaserType laserType;
    private final int forceUnits;

    UpdatePushedPositionTask(final int nx, final int ny, final int npushX, final int npushY, final ArenaObject no,
	    final int nx2, final int ny2, final ArenaObject nother, final LaserType nlaserType, final int nforceUnits) {
	this.x = nx;
	this.y = ny;
	this.pushX = npushX;
	this.pushY = npushY;
	this.o = no;
	this.x2 = nx2;
	this.y2 = ny2;
	this.other = nother;
	this.laserType = nlaserType;
	this.forceUnits = nforceUnits;
    }

    @Override
    public void run() {
	try {
	    this.other.laserEnteredAction(this.x2, this.y2, Game.plMgr.getPlayerLocationZ(), this.pushX, this.pushY,
		    this.laserType, this.forceUnits);
	    Game.waitForMLOLoop();
	    Game.updatePushedPosition(this.x, this.y, this.x + this.pushX, this.y + this.pushY, this.o);
	    Game.waitForMLOLoop();
	} catch (final Throwable t) {
	    AppTaskManager.error(t);
	}
    }
}

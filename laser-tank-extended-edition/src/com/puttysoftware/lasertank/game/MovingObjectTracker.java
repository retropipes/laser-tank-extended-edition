/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import com.puttysoftware.lasertank.Application;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractJumpObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Layer;

final class MovingObjectTracker {
    private static boolean checkSolid(final AbstractArenaObject next) {
	final boolean nextSolid = next.isConditionallySolid();
	if (nextSolid) {
	    if (next.isOfType(GameType.CHARACTER)) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return true;
	}
    }

    // Fields
    private boolean objectMoving;
    private int objCumX, objCumY, objIncX, objIncY;
    private int objMultX, objMultY;
    private AbstractArenaObject belowUpper;
    private AbstractArenaObject belowLower;
    private AbstractMovableObject movingObj;
    private boolean objectCheck;
    private boolean objectNewlyActivated;
    private boolean jumpOnMover;

    // Constructors
    public MovingObjectTracker() {
	this.resetTracker();
    }

    void activateObject(final int zx, final int zy, final int pushX, final int pushY, final AbstractMovableObject gmo) {
	final Game gm = LaserTankEE.getApplication().getGameManager();
	final PlayerLocationManager plMgr = gm.getPlayerManager();
	final int pz = plMgr.getPlayerLocationZ();
	this.objIncX = pushX - zx;
	this.objIncY = pushY - zy;
	if (gmo instanceof AbstractJumpObject) {
	    if (pushX - zx == 0) {
		if (pushY > zy) {
		    this.objIncX = -1;
		} else {
		    this.objIncX = 1;
		}
	    }
	    if (pushY - zy == 0) {
		if (pushX > zx) {
		    this.objIncY = 1;
		} else {
		    this.objIncY = -1;
		}
	    }
	}
	this.objCumX = zx;
	this.objCumY = zy;
	this.movingObj = gmo;
	this.belowUpper = LaserTankEE.getApplication().getArenaManager().getArena().getCell(
		this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		Layer.UPPER_GROUND.ordinal());
	this.belowLower = LaserTankEE.getApplication().getArenaManager().getArena().getCell(
		this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		Layer.LOWER_GROUND.ordinal());
	this.objectMoving = true;
	this.objectCheck = true;
	this.objectNewlyActivated = true;
    }

    private void doJumpObjectOnce(final AbstractJumpObject jumper) {
	final Application app = LaserTankEE.getApplication();
	final Arena m = app.getArenaManager().getArena();
	final Game gm = app.getGameManager();
	final int pz = gm.getPlayerManager().getPlayerLocationZ();
	try {
	    this.jumpOnMover = false;
	    this.objMultX = jumper.getActualJumpCols();
	    this.objMultY = jumper.getActualJumpRows();
	    if (gm.isDelayedDecayActive() && gm.isRemoteDecayActive()) {
		gm.doRemoteDelayedDecay(jumper);
	    }
	    final AbstractArenaObject oldSave = jumper.getSavedObject();
	    final AbstractArenaObject saved = m.getCell(this.objCumX + this.objIncX * this.objMultX,
		    this.objCumY + this.objIncY * this.objMultY, pz, jumper.getLayer());
	    this.belowUpper = LaserTankEE.getApplication().getArenaManager().getArena().getCell(
		    this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		    Layer.UPPER_GROUND.ordinal());
	    this.belowLower = LaserTankEE.getApplication().getArenaManager().getArena().getCell(
		    this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		    Layer.LOWER_GROUND.ordinal());
	    if (MovingObjectTracker.checkSolid(saved) && this.objMultX != 0 && this.objMultY != 0) {
		jumper.jumpSound(true);
		oldSave.pushOutAction(jumper, this.objCumX, this.objCumY, pz);
		m.setCell(oldSave, this.objCumX, this.objCumY, pz, jumper.getLayer());
		jumper.setSavedObject(saved);
		m.setCell(jumper, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz, jumper.getLayer());
		boolean stopObj = this.belowLower.pushIntoAction(this.movingObj,
			this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz);
		final boolean temp1 = this.belowUpper.pushIntoAction(this.movingObj,
			this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz);
		if (!temp1) {
		    stopObj = false;
		}
		final boolean temp2 = saved.pushIntoAction(this.movingObj, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz);
		if (!temp2) {
		    stopObj = false;
		}
		this.objectMoving = stopObj;
		this.objectCheck = stopObj;
		final int oldObjIncX = this.objIncX;
		final int oldObjIncY = this.objIncY;
		if (this.belowUpper == null || this.belowLower == null) {
		    this.objectCheck = false;
		} else {
		    if (jumper.isOfType(GameType.ICY)) {
			// Handle icy objects
			this.objectCheck = true;
		    } else if (this.belowUpper.isOfType(GameType.ANTI_MOVER) && jumper.isOfType(GameType.ANTI)) {
			// Handle anti-tank on anti-tank mover
			final Direction dir = this.belowUpper.getDirection();
			final int[] unres = DirectionHelper.unresolveRelative(dir);
			this.objIncX = unres[0];
			this.objIncY = unres[1];
			this.objectCheck = true;
		    } else if (this.belowUpper.isOfType(GameType.BOX_MOVER) && jumper.isOfType(GameType.BOX)) {
			// Handle box on box mover
			this.jumpOnMover = true;
			final Direction dir = this.belowUpper.getDirection();
			final int[] unres = DirectionHelper.unresolveRelative(dir);
			this.objIncX = unres[0];
			this.objIncY = unres[1];
			this.objectCheck = true;
		    } else if (this.belowUpper.isOfType(GameType.MIRROR_MOVER)
			    && this.movingObj.isOfType(GameType.MOVABLE_MIRROR)) {
			// Handle mirror on mirror mover
			final Direction dir = this.belowUpper.getDirection();
			final int[] unres = DirectionHelper.unresolveRelative(dir);
			this.objIncX = unres[0];
			this.objIncY = unres[1];
			this.objectCheck = true;
		    } else {
			this.objectCheck = !this.belowLower.hasFriction() || !this.belowUpper.hasFriction();
		    }
		}
		if (this.objIncX != oldObjIncX || this.objIncY != oldObjIncY) {
		    if (this.jumpOnMover) {
			this.objCumX += oldObjIncX;
			this.objCumY += oldObjIncY;
		    } else {
			this.objCumX += oldObjIncX * this.objMultX;
			this.objCumY += oldObjIncY * this.objMultY;
		    }
		} else {
		    if (this.jumpOnMover) {
			this.objCumX += this.objIncX;
			this.objCumY += this.objIncY;
		    } else {
			this.objCumX += this.objIncX * this.objMultX;
			this.objCumY += this.objIncY * this.objMultY;
		    }
		}
		app.getArenaManager().setDirty(true);
	    } else {
		// Movement failed
		jumper.jumpSound(false);
		oldSave.pushIntoAction(jumper, this.objCumX, this.objCumY, pz);
		jumper.pushCollideAction(this.movingObj, this.objCumX, this.objCumY, pz);
		saved.pushCollideAction(jumper, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz);
		this.objectMoving = false;
		this.objectCheck = false;
	    }
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    jumper.jumpSound(false);
	    this.objectMoving = false;
	    this.objectCheck = false;
	}
	gm.redrawArena();
    }

    private void doNormalObjectOnce() {
	final Application app = LaserTankEE.getApplication();
	final Arena m = app.getArenaManager().getArena();
	final Game gm = app.getGameManager();
	final int pz = gm.getPlayerManager().getPlayerLocationZ();
	try {
	    if (gm.isDelayedDecayActive() && gm.isRemoteDecayActive()) {
		gm.doRemoteDelayedDecay(this.movingObj);
	    }
	    final AbstractArenaObject oldSave = this.movingObj.getSavedObject();
	    final AbstractArenaObject saved = m.getCell(this.objCumX + this.objIncX * this.objMultX,
		    this.objCumY + this.objIncY * this.objMultY, pz, this.movingObj.getLayer());
	    this.belowUpper = LaserTankEE.getApplication().getArenaManager().getArena().getCell(
		    this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		    Layer.UPPER_GROUND.ordinal());
	    this.belowLower = LaserTankEE.getApplication().getArenaManager().getArena().getCell(
		    this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		    Layer.LOWER_GROUND.ordinal());
	    if (MovingObjectTracker.checkSolid(saved)) {
		this.belowLower.pushOutAction(this.movingObj, this.objCumX, this.objCumY, pz);
		this.belowUpper.pushOutAction(this.movingObj, this.objCumX, this.objCumY, pz);
		oldSave.pushOutAction(this.movingObj, this.objCumX, this.objCumY, pz);
		m.setCell(oldSave, this.objCumX, this.objCumY, pz, this.movingObj.getLayer());
		this.movingObj.setSavedObject(saved);
		m.setCell(this.movingObj, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz, this.movingObj.getLayer());
		boolean stopObj = this.belowLower.pushIntoAction(this.movingObj,
			this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz);
		final boolean temp1 = this.belowUpper.pushIntoAction(this.movingObj,
			this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz);
		if (!temp1) {
		    stopObj = false;
		}
		final boolean temp2 = saved.pushIntoAction(this.movingObj, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz);
		if (!temp2) {
		    stopObj = false;
		}
		this.objectMoving = stopObj;
		this.objectCheck = stopObj;
		final int oldObjIncX = this.objIncX;
		final int oldObjIncY = this.objIncY;
		if (this.belowUpper == null || this.belowLower == null) {
		    this.objectCheck = false;
		} else {
		    if (this.movingObj.isOfType(GameType.ICY)) {
			// Handle icy objects
			this.objectCheck = true;
		    } else if (this.belowUpper.isOfType(GameType.ANTI_MOVER)
			    && this.movingObj.isOfType(GameType.ANTI)) {
			// Handle anti-tank on anti-tank mover
			final Direction dir = this.belowUpper.getDirection();
			final int[] unres = DirectionHelper.unresolveRelative(dir);
			this.objIncX = unres[0];
			this.objIncY = unres[1];
			this.objectCheck = true;
		    } else if (this.belowUpper.isOfType(GameType.BOX_MOVER) && this.movingObj.isOfType(GameType.BOX)) {
			// Handle box on box mover
			final Direction dir = this.belowUpper.getDirection();
			final int[] unres = DirectionHelper.unresolveRelative(dir);
			this.objIncX = unres[0];
			this.objIncY = unres[1];
			this.objectCheck = true;
		    } else if (this.belowUpper.isOfType(GameType.MIRROR_MOVER)
			    && this.movingObj.isOfType(GameType.MOVABLE_MIRROR)) {
			// Handle mirror on mirror mover
			final Direction dir = this.belowUpper.getDirection();
			final int[] unres = DirectionHelper.unresolveRelative(dir);
			this.objIncX = unres[0];
			this.objIncY = unres[1];
			this.objectCheck = true;
		    } else {
			this.objectCheck = !this.belowLower.hasFriction() || !this.belowUpper.hasFriction();
		    }
		}
		if (this.objIncX != oldObjIncX || this.objIncY != oldObjIncY) {
		    this.objCumX += oldObjIncX;
		    this.objCumY += oldObjIncY;
		} else {
		    this.objCumX += this.objIncX;
		    this.objCumY += this.objIncY;
		}
		app.getArenaManager().setDirty(true);
	    } else {
		// Movement failed
		this.belowLower.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		this.belowUpper.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		oldSave.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		this.movingObj.pushCollideAction(this.movingObj, this.objCumX, this.objCumY, pz);
		saved.pushCollideAction(this.movingObj, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz);
		this.objectMoving = false;
		this.objectCheck = false;
	    }
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    this.objectMoving = false;
	    this.objectCheck = false;
	}
	gm.redrawArena();
    }

    private void doObjectOnce() {
	if (this.movingObj instanceof AbstractJumpObject) {
	    this.doJumpObjectOnce((AbstractJumpObject) this.movingObj);
	} else {
	    this.doNormalObjectOnce();
	}
    }

    void haltMovingObject() {
	this.objectMoving = false;
	this.objectCheck = false;
    }

    boolean isChecking() {
	return this.objectCheck;
    }

    boolean isTracking() {
	return this.objectMoving;
    }

    void resetTracker() {
	this.objectCheck = true;
	this.objectNewlyActivated = false;
	this.jumpOnMover = false;
	this.objMultX = 1;
	this.objMultY = 1;
    }

    void trackPart1() {
	if (this.objectMoving && this.objectCheck) {
	    this.doObjectOnce();
	}
    }

    void trackPart2() {
	try {
	    final Game gm = LaserTankEE.getApplication().getGameManager();
	    final PlayerLocationManager plMgr = gm.getPlayerManager();
	    final int pz = plMgr.getPlayerLocationZ();
	    if (this.objectMoving) {
		// Make objects pushed into ice move 2 squares first time
		if (this.objectCheck && this.objectNewlyActivated) {
		    if (!this.belowLower.hasFriction() || !this.belowUpper.hasFriction()) {
			this.doObjectOnce();
			this.objectCheck = !this.belowLower.hasFriction() || !this.belowUpper.hasFriction();
		    }
		}
	    } else {
		this.objectCheck = false;
		// Check for moving object stopped on thin ice
		if (this.movingObj != null) {
		    if (gm.isDelayedDecayActive() && gm.isRemoteDecayActive()) {
			gm.doRemoteDelayedDecay(this.movingObj);
			this.belowUpper.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
			this.belowLower.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		    }
		}
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Stop object
	    this.objectMoving = false;
	    this.objectCheck = false;
	}
    }

    void trackPart3() {
	if (!this.objectCheck) {
	    this.objectMoving = false;
	}
    }

    void trackPart4() {
	this.objectNewlyActivated = false;
    }
}

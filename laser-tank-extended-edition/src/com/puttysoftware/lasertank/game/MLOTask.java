/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaLocks;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;
import com.puttysoftware.lasertank.utility.AlreadyDeadException;

final class MLOTask implements Runnable {
    static void activateAutomaticMovement() {
	Game.scheduleAutoMove();
    }

    private static boolean checkSolid(final ArenaObject next) {
	// Check cheats
	if (Game.getCheatStatus(Game.CHEAT_GHOSTLY)) {
	    return true;
	}
	return !next.isConditionallySolid();
    }

    static boolean checkSolid(final int zx, final int zy) {
	final var next = ArenaManager.getArena().getCell(zx, zy, Game.getPlayerLocationZ(),
		Layer.LOWER_OBJECTS.ordinal());
	// Check cheats
	if (Game.getCheatStatus(Game.CHEAT_GHOSTLY)) {
	    return true;
	}
	return !next.isConditionallySolid();
    }

    private static void freezeTank() {
	final var tank = Game.getTank();
	final var dir = tank.getDirection();
	final var px = Game.getPlayerLocationX();
	final var py = Game.getPlayerLocationY();
	final var pz = Game.getPlayerLocationZ();
	final var ft = new ArenaObject(GameObjectID.FROZEN_TANK, dir, tank.getNumber());
	ft.setSavedObject(tank.getSavedObject());
	Game.morph(ft, px, py, pz, ft.layer());
	Game.updateTank();
    }

    private static void meltTank() {
	final var tank = Game.getTank();
	final var dir = tank.getDirection();
	final var px = Game.getPlayerLocationX();
	final var py = Game.getPlayerLocationY();
	final var pz = Game.getPlayerLocationZ();
	final var mt = new ArenaObject(GameObjectID.MOLTEN_TANK, dir, tank.getNumber());
	mt.setSavedObject(tank.getSavedObject());
	Game.morph(mt, px, py, pz, mt.layer());
	Game.updateTank();
    }

    private static int normalizeColumn(final int column, final int columns) {
	var fC = column;
	if (fC < 0) {
	    fC += columns;
	    while (fC < 0) {
		fC += columns;
	    }
	} else if (fC > columns - 1) {
	    fC -= columns;
	    while (fC > columns - 1) {
		fC -= columns;
	    }
	}
	return fC;
    }

    private static int normalizeRow(final int row, final int rows) {
	var fR = row;
	if (fR < 0) {
	    fR += rows;
	    while (fR < 0) {
		fR += rows;
	    }
	} else if (fR > rows - 1) {
	    fR -= rows;
	    while (fR > rows - 1) {
		fR -= rows;
	    }
	}
	return fR;
    }

    // Fields
    private int sx, sy;
    private boolean mover;
    private boolean move;
    private boolean proceed;
    private boolean abort;
    private boolean frozen;
    private boolean magnet;
    private boolean loopCheck;
    private final ArrayList<MovingLaserTracker> laserTrackers;
    private final ArrayList<MovingObjectTracker> objectTrackers;

    // Constructors
    public MLOTask() {
	this.abort = false;
	this.laserTrackers = new ArrayList<>();
	this.objectTrackers = new ArrayList<>();
	this.frozen = false;
	this.magnet = false;
    }

    void abortLoop() {
	this.abort = true;
    }

    void activateFrozenMovement(final int zx, final int zy) {
	// Moving under the influence of a Frost Field
	this.frozen = true;
	MLOTask.freezeTank();
	Game.updateScore(1, 0, 0);
	this.sx = zx;
	this.sy = zy;
	Game.updateUndo(false, false, false, false, false, false, false, false, false, false);
	this.move = true;
	if (!Game.isReplaying()) {
	    Game.updateReplay(GameAction.MOVE, zx, zy);
	}
    }

    void activateLasers(final int zx, final int zy, final int zox, final int zoy, final LaserType zlt,
	    final ArenaObject zshooter) {
	final var tracker = new MovingLaserTracker();
	tracker.activateLasers(zx, zy, zox, zoy, zlt, zshooter);
	this.laserTrackers.add(tracker);
    }

    void activateMoltenMovement(final int zx, final int zy) {
	// Melt the tank - stepped into a Melt Field
	MLOTask.meltTank();
	Game.updateScore(1, 0, 0);
	this.sx = zx;
	this.sy = zy;
	Game.updateUndo(false, false, false, false, false, false, false, false, false, false);
	this.move = true;
	if (!Game.isReplaying()) {
	    Game.updateReplay(GameAction.MOVE, zx, zy);
	}
    }

    void activateMovement(final int zx, final int zy) {
	if (zx == 2 || zy == 2 || zx == -2 || zy == -2) {
	    // Boosting
	    Sounds.play(Sound.BOOST);
	    Game.updateScore(0, 0, 1);
	    this.sx = zx;
	    this.sy = zy;
	    this.magnet = false;
	    Game.updateUndo(false, false, false, true, false, false, false, false, false, false);
	    if (!Game.isReplaying()) {
		Game.updateReplay(GameAction.USE_TOOL, zx, zy);
	    }
	} else if (zx == 3 || zy == 3 || zx == -3 || zy == -3) {
	    // Using a Magnet
	    Game.updateScore(0, 0, 1);
	    final var a = ArenaManager.getArena();
	    final var px = Game.getPlayerLocationX();
	    final var py = Game.getPlayerLocationY();
	    final var pz = Game.getPlayerLocationZ();
	    if (zx == 3) {
		this.sx = a.checkForMagnetic(pz, px, py, Direction.EAST);
		this.sy = 0;
	    } else if (zx == -3) {
		this.sx = -a.checkForMagnetic(pz, px, py, Direction.WEST);
		this.sy = 0;
	    }
	    if (zy == 3) {
		this.sx = 0;
		this.sy = a.checkForMagnetic(pz, px, py, Direction.SOUTH);
	    } else if (zy == -3) {
		this.sx = 0;
		this.sy = -a.checkForMagnetic(pz, px, py, Direction.NORTH);
	    }
	    this.magnet = true;
	    if (this.sx == 0 && this.sy == 0) {
		// Failure
		Sounds.play(Sound.BUMP_HEAD);
	    } else {
		// Success
		Sounds.play(Sound.MAGNET);
	    }
	    Game.updateUndo(false, false, false, false, true, false, false, false, false, false);
	    if (!Game.isReplaying()) {
		Game.updateReplay(GameAction.USE_TOOL, zx, zy);
	    }
	} else {
	    // Moving normally
	    Sounds.play(Sound.MOVE);
	    Game.updateScore(1, 0, 0);
	    this.sx = zx;
	    this.sy = zy;
	    this.magnet = false;
	    Game.updateUndo(false, false, false, false, false, false, false, false, false, false);
	    if (!Game.isReplaying()) {
		Game.updateReplay(GameAction.MOVE, zx, zy);
	    }
	}
	this.move = true;
	this.loopCheck = true;
    }

    void activateObjects(final int zx, final int zy, final int pushX, final int pushY, final ArenaObject gmo) {
	final var tracker = new MovingObjectTracker();
	tracker.activateObject(zx, zy, pushX, pushY, gmo);
	this.objectTrackers.add(tracker);
    }

    private boolean areLaserTrackersChecking() {
	var result = false;
	for (final MovingLaserTracker tracker : this.laserTrackers) {
	    if (tracker.isChecking()) {
		result = true;
	    }
	}
	return result;
    }

    private boolean areObjectTrackersChecking() {
	var result = false;
	for (final MovingObjectTracker tracker : this.objectTrackers) {
	    if (tracker.isChecking()) {
		result = true;
	    }
	}
	return result;
    }

    private boolean areObjectTrackersTracking() {
	var result = false;
	for (final MovingObjectTracker tracker : this.objectTrackers) {
	    if (tracker.isTracking()) {
		result = true;
	    }
	}
	return result;
    }

    private boolean canMoveThere() {
	final var px = Game.getPlayerLocationX();
	final var py = Game.getPlayerLocationY();
	final var pz = Game.getPlayerLocationZ();
	final var pw = Layer.UPPER_OBJECTS.ordinal();
	final var m = ArenaManager.getArena();
	ArenaObject lgo = null;
	ArenaObject ugo = null;
	ArenaObject loo = null;
	ArenaObject uoo = null;
	try {
	    try {
		lgo = m.getCell(px + this.sx, py + this.sy, pz, Layer.LOWER_GROUND.ordinal());
		ugo = m.getCell(px + this.sx, py + this.sy, pz, Layer.UPPER_GROUND.ordinal());
		loo = m.getCell(px + this.sx, py + this.sy, pz, Layer.LOWER_OBJECTS.ordinal());
		uoo = m.getCell(px + this.sx, py + this.sy, pz, pw);
	    } catch (final ArrayIndexOutOfBoundsException ae) {
		lgo = new ArenaObject(GameObjectID.WALL);
		ugo = new ArenaObject(GameObjectID.WALL);
		loo = new ArenaObject(GameObjectID.WALL);
		uoo = new ArenaObject(GameObjectID.WALL);
	    }
	} catch (final NullPointerException np) {
	    this.proceed = false;
	    lgo = new ArenaObject(GameObjectID.WALL);
	    ugo = new ArenaObject(GameObjectID.WALL);
	    loo = new ArenaObject(GameObjectID.WALL);
	    uoo = new ArenaObject(GameObjectID.WALL);
	}
	return MLOTask.checkSolid(lgo) && MLOTask.checkSolid(ugo) && MLOTask.checkSolid(loo) && MLOTask.checkSolid(uoo);
    }

    private boolean checkLoopCondition(final boolean zproceed) {
	final var px = Game.getPlayerLocationX();
	final var py = Game.getPlayerLocationY();
	final var pz = Game.getPlayerLocationZ();
	final var m = ArenaManager.getArena();
	ArenaObject lgo = null;
	ArenaObject ugo = null;
	try {
	    try {
		lgo = m.getCell(px, py, pz, Layer.LOWER_GROUND.ordinal());
		ugo = m.getCell(px, py, pz, Layer.UPPER_GROUND.ordinal());
	    } catch (final ArrayIndexOutOfBoundsException ae) {
		lgo = new ArenaObject(GameObjectID.WALL);
		ugo = new ArenaObject(GameObjectID.WALL);
	    }
	} catch (final NullPointerException np) {
	    this.proceed = false;
	    lgo = new ArenaObject(GameObjectID.WALL);
	    ugo = new ArenaObject(GameObjectID.WALL);
	}
	return zproceed && (!lgo.hasFriction() || !ugo.hasFriction() || this.mover || this.frozen)
		&& this.canMoveThere();
    }

    private void cullTrackers() {
	final var tempArray1 = this.objectTrackers.toArray(new MovingObjectTracker[this.objectTrackers.size()]);
	this.objectTrackers.clear();
	for (final MovingObjectTracker tracker : tempArray1) {
	    if (tracker != null && tracker.isTracking()) {
		this.objectTrackers.add(tracker);
	    }
	}
	final var tempArray2 = this.laserTrackers.toArray(new MovingLaserTracker[this.laserTrackers.size()]);
	this.laserTrackers.clear();
	for (final MovingLaserTracker tracker : tempArray2) {
	    if (tracker != null && tracker.isTracking()) {
		this.laserTrackers.add(tracker);
	    }
	}
    }

    private void defrostTank() {
	if (this.frozen) {
	    this.frozen = false;
	    final var tank = Game.getTank();
	    final var dir = tank.getDirection();
	    final var px = Game.getPlayerLocationX();
	    final var py = Game.getPlayerLocationY();
	    final var pz = Game.getPlayerLocationZ();
	    final var t = new ArenaObject(GameObjectID.TANK, dir, tank.getNumber());
	    t.setSavedObject(tank.getSavedObject());
	    Game.morph(t, px, py, pz, t.layer());
	    Game.updateTank();
	    Sounds.play(Sound.DEFROST);
	}
    }

    private void doMovementLasersObjects() {
	synchronized (ArenaLocks.LOCK_OBJECT) {
	    final var pz = Game.getPlayerLocationZ();
	    this.loopCheck = true;
	    var objs = new ArenaObject[4];
	    objs[Layer.LOWER_GROUND.ordinal()] = new ArenaObject(GameObjectID.WALL);
	    objs[Layer.UPPER_GROUND.ordinal()] = new ArenaObject(GameObjectID.WALL);
	    objs[Layer.LOWER_OBJECTS.ordinal()] = new ArenaObject(GameObjectID.WALL);
	    objs[Layer.UPPER_OBJECTS.ordinal()] = new ArenaObject(GameObjectID.WALL);
	    do {
		try {
		    if (this.move && this.loopCheck) {
			objs = this.doMovementOnce();
		    }
		    // Abort check 1
		    if (this.abort) {
			break;
		    }
		    for (final MovingLaserTracker tracker : this.laserTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart1(this.areObjectTrackersTracking());
			}
		    }
		    // Abort check 2
		    if (this.abort) {
			break;
		    }
		    for (final MovingObjectTracker tracker : this.objectTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart1();
			}
		    }
		    // Abort check 3
		    if (this.abort) {
			break;
		    }
		    var actionType = GameAction.NONE;
		    if (this.move && !this.magnet && Math.abs(this.sx) <= 1 && Math.abs(this.sy) <= 1) {
			actionType = GameAction.MOVE;
		    } else {
			actionType = GameAction.USE_TOOL;
		    }
		    for (final MovingLaserTracker tracker : this.laserTrackers) {
			if (tracker.isTracking()) {
			    this.mover = tracker.trackPart2(this.sx, this.sy, this.mover);
			}
		    }
		    for (final MovingObjectTracker tracker : this.objectTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart2();
			}
		    }
		    if (this.move) {
			this.loopCheck = this.checkLoopCondition(this.proceed);
			if (this.mover && !this.canMoveThere()) {
			    MLOTask.activateAutomaticMovement();
			}
			if (objs[Layer.LOWER_OBJECTS.ordinal()].solvesOnMove()) {
			    this.abort = true;
			    if (this.move) {
				ArenaManager.setDirty(true);
				this.defrostTank();
				Game.moveLoopDone();
				this.move = false;
			    }
			    for (final MovingLaserTracker tracker : this.laserTrackers) {
				if (tracker.isTracking()) {
				    tracker.clearLastLaser();
				}
			    }
			    Game.solvedLevel(true);
			    return;
			}
		    } else {
			this.loopCheck = false;
		    }
		    if (this.move && !this.loopCheck) {
			ArenaManager.setDirty(true);
			this.defrostTank();
			Game.moveLoopDone();
			this.move = false;
		    }
		    for (final MovingObjectTracker tracker : this.objectTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart3();
			}
		    }
		    // Check auto-move flag
		    if (Game.isAutoMoveScheduled() && this.canMoveThere()) {
			Game.unscheduleAutoMove();
			this.move = true;
			this.loopCheck = true;
		    }
		    ArenaManager.getArena().tickTimers(pz, actionType);
		    final var px = Game.getPlayerLocationX();
		    final var py = Game.getPlayerLocationY();
		    ArenaManager.getArena().checkForEnemies(pz, px, py, Game.getTank());
		    // Delay
		    try {
			Thread.sleep(Settings.getActionSpeed());
		    } catch (final InterruptedException ie) {
			// Ignore
		    }
		    for (final MovingObjectTracker tracker : this.objectTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart4();
			}
		    }
		    this.cullTrackers();
		} catch (final ConcurrentModificationException cme) {
		    // Ignore
		}
	    } while (!this.abort
		    && (this.loopCheck || this.areLaserTrackersChecking() || this.areObjectTrackersChecking()));
	    // Check cheats
	    if (objs[Layer.LOWER_GROUND.ordinal()].killsOnMove() && !Game.getCheatStatus(Game.CHEAT_SWIMMING)) {
		Game.gameOver();
	    }
	}
    }

    private ArenaObject[] doMovementOnce() {
	var px = Game.getPlayerLocationX();
	var py = Game.getPlayerLocationY();
	final var pz = Game.getPlayerLocationZ();
	final var pw = Layer.UPPER_OBJECTS.ordinal();
	final var m = ArenaManager.getArena();
	this.proceed = true;
	this.mover = false;
	ArenaObject lgo = null;
	ArenaObject ugo = null;
	ArenaObject loo = null;
	ArenaObject uoo = null;
	try {
	    try {
		lgo = m.getCell(px + this.sx, py + this.sy, pz, Layer.LOWER_GROUND.ordinal());
		ugo = m.getCell(px + this.sx, py + this.sy, pz, Layer.UPPER_GROUND.ordinal());
		loo = m.getCell(px + this.sx, py + this.sy, pz, Layer.LOWER_OBJECTS.ordinal());
		uoo = m.getCell(px + this.sx, py + this.sy, pz, pw);
	    } catch (final ArrayIndexOutOfBoundsException ae) {
		lgo = new ArenaObject(GameObjectID.WALL);
		ugo = new ArenaObject(GameObjectID.WALL);
		loo = new ArenaObject(GameObjectID.WALL);
		uoo = new ArenaObject(GameObjectID.WALL);
	    }
	} catch (final NullPointerException np) {
	    this.proceed = false;
	    lgo = new ArenaObject(GameObjectID.WALL);
	    ugo = new ArenaObject(GameObjectID.WALL);
	    loo = new ArenaObject(GameObjectID.WALL);
	    uoo = new ArenaObject(GameObjectID.WALL);
	}
	if (this.proceed) {
	    Game.savePlayerLocation();
	    try {
		if (this.canMoveThere()) {
		    if (Game.isDelayedDecayActive()) {
			Game.doDelayedDecay();
		    }
		    // Preserve other objects
		    if (m.getCell(px, py, pz, pw) != null) {
			Game.getTank().setSavedObject(m.getCell(px, py, pz, pw));
		    }
		    m.setCell(Game.getTank().getSavedObject(), px, py, pz, pw);
		    Game.offsetPlayerLocationX(this.sx);
		    Game.offsetPlayerLocationY(this.sy);
		    px = MLOTask.normalizeColumn(px + this.sx, Arena.getMinColumns());
		    py = MLOTask.normalizeRow(py + this.sy, Arena.getMinRows());
		    Game.getTank().setSavedObject(m.getCell(px, py, pz, pw));
		    m.setCell(Game.getTank(), px, py, pz, pw);
		    lgo.postMoveAction(px, py, pz);
		    ugo.postMoveAction(px, py, pz);
		    loo.postMoveAction(px, py, pz);
		    uoo.postMoveAction(px, py, pz);
		    if (ugo.movesTanks(ugo.getDirection())) {
			final var dir = ugo.getDirection();
			final var unres = DirectionHelper.unresolveRelative(dir);
			this.sx = unres[0];
			this.sy = unres[1];
			this.mover = true;
		    } else {
			this.mover = false;
		    }
		} else {
		    // Move failed - object is solid in that direction
		    if (Game.isDelayedDecayActive()) {
			Game.doDelayedDecay();
		    }
		    if (lgo == null) {
			lgo = new ArenaObject(GameObjectID.GROUND);
		    }
		    lgo.moveFailedAction(Game.getPlayerLocationX() + this.sx, Game.getPlayerLocationY() + this.sy,
			    Game.getPlayerLocationZ());
		    if (ugo == null) {
			ugo = new ArenaObject(GameObjectID.GROUND);
		    }
		    ugo.moveFailedAction(Game.getPlayerLocationX() + this.sx, Game.getPlayerLocationY() + this.sy,
			    Game.getPlayerLocationZ());
		    if (loo == null) {
			loo = new ArenaObject(GameObjectID.GROUND);
		    }
		    loo.moveFailedAction(Game.getPlayerLocationX() + this.sx, Game.getPlayerLocationY() + this.sy,
			    Game.getPlayerLocationZ());
		    if (uoo == null) {
			uoo = new ArenaObject(GameObjectID.GROUND);
		    }
		    uoo.moveFailedAction(Game.getPlayerLocationX() + this.sx, Game.getPlayerLocationY() + this.sy,
			    Game.getPlayerLocationZ());
		    if (Game.getTank().getSavedObject().movesTanks(Game.getTank().getSavedObject().getDirection())) {
			final var dir = Game.getTank().getSavedObject().getDirection();
			final var unres = DirectionHelper.unresolveRelative(dir);
			this.sx = unres[0];
			this.sy = unres[1];
			this.mover = true;
		    } else {
			this.mover = false;
		    }
		    this.proceed = false;
		}
	    } catch (final ArrayIndexOutOfBoundsException ae) {
		Game.restorePlayerLocation();
		m.setCell(Game.getTank(), Game.getPlayerLocationX(), Game.getPlayerLocationY(),
			Game.getPlayerLocationZ(), pw);
		// Move failed - attempted to go outside the arena
		if (lgo == null) {
		    lgo = new ArenaObject(GameObjectID.GROUND);
		}
		lgo.moveFailedAction(Game.getPlayerLocationX() + this.sx, Game.getPlayerLocationY() + this.sy,
			Game.getPlayerLocationZ());
		if (ugo == null) {
		    ugo = new ArenaObject(GameObjectID.GROUND);
		}
		ugo.moveFailedAction(Game.getPlayerLocationX() + this.sx, Game.getPlayerLocationY() + this.sy,
			Game.getPlayerLocationZ());
		if (loo == null) {
		    loo = new ArenaObject(GameObjectID.GROUND);
		}
		loo.moveFailedAction(Game.getPlayerLocationX() + this.sx, Game.getPlayerLocationY() + this.sy,
			Game.getPlayerLocationZ());
		if (uoo == null) {
		    uoo = new ArenaObject(GameObjectID.GROUND);
		}
		uoo.moveFailedAction(Game.getPlayerLocationX() + this.sx, Game.getPlayerLocationY() + this.sy,
			Game.getPlayerLocationZ());
		this.proceed = false;
	    }
	} else {
	    // Move failed - pre-move check failed
	    lgo.moveFailedAction(px + this.sx, py + this.sy, pz);
	    ugo.moveFailedAction(px + this.sx, py + this.sy, pz);
	    loo.moveFailedAction(px + this.sx, py + this.sy, pz);
	    uoo.moveFailedAction(px + this.sx, py + this.sy, pz);
	    this.proceed = false;
	}
	Game.redrawArena();
	return new ArenaObject[] { lgo, ugo, loo, uoo };
    }

    void haltMovingObjects() {
	for (final MovingObjectTracker tracker : this.objectTrackers) {
	    if (tracker.isTracking()) {
		tracker.haltMovingObject();
	    }
	}
    }

    @Override
    public void run() {
	try {
	    Game.clearDead();
	    this.doMovementLasersObjects();
	    // Check auto-move flag
	    if (Game.isAutoMoveScheduled() && this.canMoveThere()) {
		Game.unscheduleAutoMove();
		this.doMovementLasersObjects();
	    }
	} catch (final AlreadyDeadException ade) {
	    // Ignore
	} catch (final Throwable t) {
	    AppTaskManager.error(t);
	}
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import java.awt.Color;
import java.io.IOException;

import com.puttysoftware.lasertank.arena.ArenaData;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.fileio.DataIOWriter;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.GameColorHelper;
import com.puttysoftware.lasertank.helper.LaserTypeHelper;
import com.puttysoftware.lasertank.helper.RangeTypeHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameColor;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;
import com.puttysoftware.lasertank.locale.Strings;

public class ArenaObject {
    static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;
    private final static boolean[] tunnelsFull = new boolean[GameColorHelper.COUNT];
    private static final int STUNNED_START = 10;

    public static void checkTunnels() {
	for (var x = 0; x < GameColorHelper.COUNT; x++) {
	    ArenaObject.checkTunnelsOfColor(GameColorHelper.fromOrdinal(x));
	}
    }

    private static void checkTunnelsOfColor(final GameColor color) {
	final var tx = Game.getPlayerLocationX();
	final var ty = Game.getPlayerLocationY();
	final var pgrmdest = ArenaManager.getArena().circularScanTunnel(0, 0, 0, ArenaData.TUNNEL_SCAN_RADIUS, tx, ty,
		ArenaObject.getTunnelOfColor(color), false);
	ArenaObject.tunnelsFull[color.ordinal()] = pgrmdest != null;
    }

    public static final int getImbuedForce(final Material materialID) {
	return ArenaObjectData.getImbuedForce(materialID);
    }

    private static ArenaObject getTunnelOfColor(final GameColor color) {
	var o = new ArenaObject(GameObjectID.TUNNEL);
	o.setColor(color);
	return o;
    }

    public static boolean tunnelsFull(final GameColor color) {
	return ArenaObject.tunnelsFull[color.ordinal()];
    }

    // Fixed Properties
    private final GameObjectID gameObjectID;
    // Cached Properties
    private int stunnedLeft;
    private int timerValue;
    private boolean timerActive;
    private int frameNumber;
    private Direction direction;
    private GameColor color;
    private ArenaObject savedObject;
    private ArenaObject pairedWith;
    private int index;
    private boolean acceptFire;
    private boolean acceptIce;
    private int blockHeight;
    private boolean canBreak;
    private boolean canCloak;
    private boolean canControl;
    private boolean canJump;
    private boolean canLasersPassThrough;
    private boolean canMove;
    private boolean canRoll;
    private boolean canShoot;
    private boolean hasFriction;
    private boolean isAnimated;
    private boolean isBox;
    private boolean isHostile;
    private boolean isPowerful;
    private boolean isPushable;
    private boolean isSolid;
    private boolean isStunned;
    private Sound laserEnteredSound;
    private int layer;
    private Material material;
    private Sound rangeSound;
    private boolean reactsToObjectsPushedInto;
    private boolean removesPushedObjects;
    private boolean solvesOnMove;
    // Dynamic Properties
    private boolean imageEnabled;
    private boolean waitingOnTunnel;
    private boolean pairTriggered;
    private int pairX;
    private int pairY;
    private int jumpRows;
    private int jumpCols;
    private boolean jumpShot;
    private boolean flip;
    private int dir1X;
    private int dir1Y;
    private int dir2X;
    private int dir2Y;
    private ArenaObject previousState;
    private boolean autoMove;
    private boolean shotUnlocked;

    // Constructors
    public ArenaObject() {
	this(GameObjectID.NOTHING);
    }

    public ArenaObject(final GameObjectID goid) {
	this.gameObjectID = goid;
	this.cachePropertyValues(goid);
	this.initDynamicPropertyValues();
    }

    public ArenaObject(final ArenaObject source) {
	// Copy ID
	this.gameObjectID = source.gameObjectID;
	// Copy caches
	this.acceptFire = source.acceptFire;
	this.acceptIce = source.acceptIce;
	this.blockHeight = source.blockHeight;
	this.canBreak = source.canBreak;
	this.canCloak = source.canCloak;
	this.canControl = source.canControl;
	this.canJump = source.canJump;
	this.canLasersPassThrough = source.canLasersPassThrough;
	this.canMove = source.canMove;
	this.canRoll = source.canRoll;
	this.canShoot = source.canShoot;
	this.hasFriction = source.hasFriction;
	this.isAnimated = source.isAnimated;
	this.isBox = source.isBox;
	this.isHostile = source.isHostile;
	this.isPowerful = source.isPowerful;
	this.isPushable = source.isPushable;
	this.isSolid = source.isSolid;
	this.isStunned = source.isStunned;
	this.laserEnteredSound = source.laserEnteredSound;
	this.layer = source.layer;
	this.material = source.material;
	this.rangeSound = source.rangeSound;
	this.reactsToObjectsPushedInto = source.reactsToObjectsPushedInto;
	this.removesPushedObjects = source.removesPushedObjects;
	this.solvesOnMove = source.solvesOnMove;
	// Copy everything else
	this.stunnedLeft = source.stunnedLeft;
	this.index = source.index;
	this.timerActive = source.timerActive;
	this.timerValue = source.timerValue;
	this.frameNumber = source.frameNumber;
	this.direction = source.direction;
	this.color = source.color;
	this.imageEnabled = source.imageEnabled;
	this.waitingOnTunnel = source.waitingOnTunnel;
	this.pairTriggered = source.pairTriggered;
	this.pairX = source.pairX;
	this.pairY = source.pairY;
	this.jumpRows = source.jumpRows;
	this.jumpCols = source.jumpCols;
	this.jumpShot = source.jumpShot;
	this.flip = source.flip;
	this.dir1X = source.dir1X;
	this.dir1Y = source.dir1Y;
	this.dir2X = source.dir2X;
	this.dir2Y = source.dir2Y;
	if (source.savedObject != null) {
	    this.savedObject = new ArenaObject(source.savedObject);
	}
	final var pairID = ArenaObjectData.getPairedObjectID(source.getID());
	if (pairID != null) {
	    this.pairedWith = new ArenaObject(pairID);
	}
    }

    public ArenaObject(final GameObjectID goid, final Direction dir, final int newIndex) {
	this.gameObjectID = goid;
	this.cachePropertyValues(goid);
	this.initDynamicPropertyValues();
	this.index = newIndex;
	this.direction = dir;
    }

    private final void cachePropertyValues(final GameObjectID goid) {
	this.acceptFire = ArenaObjectData.acceptFire(goid);
	this.acceptIce = ArenaObjectData.acceptIce(goid);
	this.blockHeight = ArenaObjectData.getBlockHeight(goid);
	this.canBreak = ArenaObjectData.canBreak(goid);
	this.canCloak = ArenaObjectData.canCloak(goid);
	this.canControl = ArenaObjectData.canControl(goid);
	this.canJump = ArenaObjectData.canJump(goid);
	this.canLasersPassThrough = ArenaObjectData.canLasersPassThrough(goid);
	this.canMove = ArenaObjectData.canMove(goid);
	this.canRoll = ArenaObjectData.canRoll(goid);
	this.canShoot = ArenaObjectData.canShoot(goid);
	this.hasFriction = ArenaObjectData.hasFriction(goid);
	this.isAnimated = ArenaObjectData.isAnimated(goid);
	this.isBox = ArenaObjectData.isBox(goid);
	this.isHostile = ArenaObjectData.isHostile(goid);
	this.isPowerful = ArenaObjectData.isPowerful(goid);
	this.isPushable = ArenaObjectData.isPushable(goid);
	this.isSolid = ArenaObjectData.isSolid(goid);
	this.isStunned = ArenaObjectData.isStunned(goid);
	this.laserEnteredSound = ArenaObjectData.laserEnterSound(goid);
	this.layer = ArenaObjectData.getLayer(goid);
	this.material = ArenaObjectData.getMaterial(goid);
	this.rangeSound = ArenaObjectData.rangeSound(goid);
	this.reactsToObjectsPushedInto = ArenaObjectData.reactsToObjectsPushedInto(goid);
	this.removesPushedObjects = ArenaObjectData.removesPushedObjects(goid);
	this.solvesOnMove = ArenaObjectData.solvesOnMove(goid);
	this.index = this.getInitialIndex();
	this.timerActive = this.getInitialTimerActive();
	this.timerValue = this.getInitialTimerValue();
	this.direction = this.getInitialDirection();
	this.color = this.getInitialColor();
	this.frameNumber = this.isAnimated() ? 1 : 0;
	if (this.isStunned()) {
	    this.stunnedLeft = ArenaObject.STUNNED_START;
	} else {
	    this.stunnedLeft = 0;
	}
	if (this.canMove() || this.canControl()) {
	    this.savedObject = new ArenaObject(GameObjectID.PLACEHOLDER);
	}
	final var pairID = ArenaObjectData.getPairedObjectID(goid);
	if (pairID != null) {
	    this.pairedWith = new ArenaObject(pairID);
	}
    }

    private final void initDynamicPropertyValues() {
	this.imageEnabled = true;
	this.waitingOnTunnel = false;
	this.pairTriggered = false;
	this.pairX = -1;
	this.pairY = -1;
	this.jumpRows = 0;
	this.jumpCols = 0;
	this.jumpShot = false;
	this.flip = false;
	this.dir1X = 0;
	this.dir1Y = 0;
	this.dir2X = 0;
	this.dir2Y = 0;
	this.previousState = null;
	this.autoMove = false;
	this.shotUnlocked = false;
    }

    /* BEGIN: Cache Fetch */
    public final boolean acceptFire() {
	return this.acceptFire;
    }

    public final boolean acceptIce() {
	return this.acceptIce;
    }

    public final int blockHeight() {
	return this.blockHeight;
    }

    public final boolean canBreak() {
	return this.canBreak;
    }

    public final boolean canCloak() {
	return this.canCloak;
    }

    public final boolean canControl() {
	return this.canControl;
    }

    public final boolean canJump() {
	return this.canJump;
    }

    public final boolean canLasersPassThrough() {
	return this.canLasersPassThrough;
    }

    public final boolean canMove() {
	return this.canMove;
    }

    public final boolean canRoll() {
	return this.canRoll;
    }

    public final boolean canShoot() {
	return this.canShoot;
    }

    public final boolean hasFriction() {
	return this.hasFriction;
    }

    public final boolean isAnimated() {
	return this.isAnimated;
    }

    public final boolean isBox() {
	return this.isBox;
    }

    public final boolean isHostile() {
	return this.isHostile;
    }

    public final boolean isPowerful() {
	return this.isPowerful;
    }

    public final boolean isPushable() {
	return this.isPushable;
    }

    public final boolean isSolid() {
	return this.isSolid;
    }

    public final boolean isStunned() {
	return this.isStunned;
    }

    public final Sound laserEnteredSound() {
	return this.laserEnteredSound;
    }

    public final int layer() {
	return this.layer;
    }

    public final Material material() {
	return this.material;
    }

    public final Sound rangeSound() {
	return this.rangeSound;
    }

    public final boolean reactsToObjectsPushedInto() {
	return this.reactsToObjectsPushedInto;
    }

    public final boolean removesPushedObjects() {
	return this.removesPushedObjects;
    }

    public final boolean solvesOnMove() {
	return this.solvesOnMove;
    }
    /* END: Cache Fetch */

    public final boolean acceptTick(final GameAction actionType) {
	return ArenaObjectData.acceptTick(this.getID(), actionType);
    }

    public final void activateTimer(final int ticks) {
	this.timerActive = true;
	this.timerValue = ticks;
    }

    public final ArenaObject attributeRenderHook() {
	final var renderAs = ArenaObjectData.attributeRenderHook(this.getID());
	return renderAs != null ? new ArenaObject(renderAs) : null;
    }

    /**
     *
     * @param materialID
     * @return
     */
    public final ArenaObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case ICE -> {
	    if (this.acceptIce()) {
		final var newID = ArenaObjectData.getNewObjectIDOnAcceptingIce(this.getID());
		if (this.getID() != newID) {
		    final var i = new ArenaObject(newID);
		    i.setPreviousState(this);
		    yield i;
		}
	    }
	    yield this;
	}
	case FIRE -> {
	    if (this.acceptFire()) {
		final var newID = ArenaObjectData.getNewObjectIDOnAcceptingFire(this.getID());
		if (this.getID() != newID) {
		    final var f = new ArenaObject(newID);
		    f.setPreviousState(this);
		    yield f;
		}
	    }
	    yield this;
	}
	default -> this;
	};
    }

    public final boolean defersSetProperties() {
	return this.hasDirection() || this.canJump();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public final void editorPlaceHook(final int x, final int y, final int z) {
	if (this.canControl()) {
	    Editor.get().setPlayerLocation();
	} else if (this.pairedWith != null && this.usesTrigger()) {
	    final var loc = ArenaManager.getArena().findObject(z, this.getPairedWith());
	    if (loc != null) {
		this.setPairX(loc[0]);
		this.setPairY(loc[1]);
		this.setPairTriggered(false);
	    }
	    if (this.usesTrigger()) {
		ArenaManager.getArena().fullScanPairCleanup(x, y, z, this);
	    }
	    Editor.get().redrawEditor();
	}
    }

    public final ArenaObject editorPropertiesHook() {
	if (this.hasDirection()) {
	    this.toggleDirection();
	    return this;
	}
	if (this.canJump()) {
	    Editor.get();
	    Editor.editJumpBox(this);
	    return this;
	}
	return null;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof final ArenaObject other) || this.direction != other.direction
		|| this.color != other.color) {
	    return false;
	}
	return true;
    }

    public final GameColor getColor() {
	return this.color;
    }

    public int getColumnsToJump() {
	if (this.flip) {
	    if (this.dir2Y == 0) {
		return this.jumpRows * this.dir1Y;
	    }
	    return this.jumpRows * this.dir2Y;
	}
	if (this.dir2X == 0) {
	    return this.jumpCols * this.dir1X;
	}
	return this.jumpCols * this.dir2X;
    }

    public int getCustomFormat() {
	return 0;
    }

    /**
     * @param propID
     */
    public int getCustomProperty(final int propID) {
	return ArenaObject.DEFAULT_CUSTOM_VALUE;
    }

    public String getCustomText() {
	if (this.canControl()) {
	    return Integer.toString(this.index);
	}
	if (this.canJump()) {
	    return Strings.loadObjectCustomText(this.getID(), this.jumpRows, this.jumpCols);
	}
	return null;
    }

    public Color getCustomTextColor() {
	if (this.canControl()) {
	    return Color.white;
	}
	return null;
    }

    public final Direction getDirection() {
	return this.direction;
    }

    public final int getFrameNumber() {
	return this.frameNumber;
    }

    public GameObjectID getID() {
	return this.gameObjectID;
    }

    public final String getImageName() {
	if (this.hasDirection() && this.isAnimated()) {
	    return ArenaObjectImageResolver.getImageName(this.getID(), this.direction, this.frameNumber);
	}
	if (!this.hasDirection() && this.isAnimated()) {
	    return ArenaObjectImageResolver.getImageName(this.getID(), this.frameNumber);
	}
	if (this.hasDirection() && !this.isAnimated()) {
	    return ArenaObjectImageResolver.getImageName(this.getID(), this.direction);
	}
	return ArenaObjectImageResolver.getImageName(this.getID());
    }

    private final GameColor getInitialColor() {
	return ArenaObjectData.getValidColors(this.getID())[0];
    }

    private final Direction getInitialDirection() {
	return ArenaObjectData.getValidDirections(this.getID())[0];
    }

    private final int getInitialIndex() {
	return ArenaObjectData.getValidIndexes(this.getID())[0];
    }

    private final boolean getInitialTimerActive() {
	return ArenaObjectData.isTimerActive(this.getID());
    }

    private final int getInitialTimerValue() {
	return ArenaObjectData.initialTimerValue(this.getID());
    }

    public final int getJumpCols() {
	return this.jumpCols;
    }

    public final int getJumpRows() {
	return this.jumpRows;
    }

    private final int getLastFrameNumber() {
	return ArenaObjectData.getLastFrameNumber(this.getID());
    }

    public final int getMinimumReactionForce() {
	return ArenaObjectData.getMinimumReactionForce(this.material());
    }

    public final int getNumber() {
	return this.index;
    }

    public final ArenaObject getPairedWith() {
	return this.pairedWith;
    }

    public int getPairX() {
	return this.pairX;
    }

    public int getPairY() {
	return this.pairY;
    }

    public final ArenaObject getPreviousState() {
	return this.previousState;
    }

    public int getRowsToJump() {
	if (this.flip) {
	    if (this.dir2X == 0) {
		return this.jumpCols * this.dir1X;
	    }
	    return this.jumpCols * this.dir2X;
	}
	if (this.dir2Y == 0) {
	    return this.jumpRows * this.dir1Y;
	}
	return this.jumpRows * this.dir2Y;
    }

    public final ArenaObject getSavedObject() {
	return this.savedObject;
    }

    private final boolean hasDirection() {
	return this.direction != Direction.NONE;
    }

    @Override
    public int hashCode() {
	final var prime = 31;
	var result = 1;
	result = prime * result + (this.timerActive ? 1231 : 1237);
	result = prime * result + this.timerValue;
	result = prime * result + this.direction.hashCode();
	return prime * result + this.color.ordinal();
    }

    public final boolean hasPreviousState() {
	return this.previousState != null;
    }

    public boolean hasSamePair(final ArenaObject other) {
	if (this == other) {
	    return true;
	}
	if (this.pairedWith == null) {
	    if (other.pairedWith != null) {
		return false;
	    }
	} else if (!this.pairedWith.getClass().equals(other.pairedWith.getClass())) {
	    return false;
	}
	return true;
    }

    public final boolean hitReflectiveSide(final Direction dir) {
	if (!ArenaObjectData.isReflective(this.getID(), dir)) {
	    return false;
	}
	return ArenaObjectData.hitReflectiveSide(dir);
    }

    public boolean isConditionallySolid() {
	return this.isSolid();
    }

    public boolean isEnabled() {
	return this.imageEnabled;
    }

    public final boolean isMovableMirror(final Direction dir) {
	return ArenaObjectData.isMovableMirror(this.getID(), dir);
    }

    public boolean isPairedWith(final ArenaObject other) {
	if (this.pairedWith == null) {
	    if (other != null) {
		return false;
	    }
	} else if (!this.pairedWith.getClass().equals(other.getClass())) {
	    return false;
	}
	return true;
    }

    public boolean isPairTriggered() {
	return this.pairTriggered;
    }

    public final boolean isTunnel() {
	return this.getID() == GameObjectID.TUNNEL;
    }

    public final void jumpSound(final boolean success) {
	if (!success || this.jumpRows == 0 && this.jumpCols == 0) {
	    Sounds.play(Sound.LASER_DIE);
	} else {
	    Sounds.play(Sound.JUMPING);
	}
    }

    public void kill(final int locX, final int locY) {
	if (this.isHostile() && this.shotUnlocked) {
	    Game.setLaserType(LaserType.RED);
	    Game.fireLaser(locX, locY, this);
	    this.shotUnlocked = false;
	}
    }

    public final boolean killsOnMove() {
	return ArenaObjectData.killsOnMove(this.getID(), this.index);
    }

    public void laserDoneAction() {
	if (this.isHostile()) {
	    this.shotUnlocked = true;
	}
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param laserType
     * @param forceUnits
     * @return
     */
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	if (forceUnits >= this.getMinimumReactionForce()) {
	    if (this.canMove()) {
		try {
		    final var mof = ArenaManager.getArena().getCell(locX + dirX, locY + dirY, locZ, this.layer());
		    final var mor = ArenaManager.getArena().getCell(locX - dirX, locY - dirY, locZ, this.layer());
		    if (this.material() == Material.MAGNETIC) {
			if (laserType == LaserType.BLUE && mof != null && (mof.canControl() || !mof.isSolid())) {
			    Game.updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
			} else if (mor != null && (mor.canControl() || !mor.isSolid())) {
			    Game.updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
			} else {
			    // Object doesn't react to this type of laser
			    return Direction.NONE;
			}
		    } else if (laserType == LaserType.BLUE && mor != null && (mor.canControl() || !mor.isSolid())) {
			Game.updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
		    } else if (mof != null && (mof.canControl() || !mof.isSolid())) {
			Game.updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
		    } else {
			// Object doesn't react to this type of laser
			return Direction.NONE;
		    }
		    Sounds.play(this.laserEnteredSound());
		} catch (final ArrayIndexOutOfBoundsException aioobe) {
		}
		return Direction.NONE;
	    }
	    if (this.canJump()) {
		final var px = Game.getPlayerLocationX();
		final var py = Game.getPlayerLocationY();
		if (forceUnits > this.getMinimumReactionForce() && this.jumpRows == 0 && this.jumpCols == 0) {
		    this.pushCrushAction(locX, locY, locZ);
		    return Direction.NONE;
		}
		if (!this.jumpShot) {
		    this.jumpShot = true;
		    this.dir1X = (int) Math.signum(px - locX);
		    this.dir1Y = (int) Math.signum(py - locY);
		    Sounds.play(Sound.PREPARE);
		    return Direction.NONE;
		}
		this.jumpShot = false;
		this.dir2X = (int) Math.signum(px - locX);
		this.dir2Y = (int) Math.signum(py - locY);
		if (this.dir1X != 0 && this.dir2X != 0 || this.dir1Y != 0 && this.dir2Y != 0) {
		    Sounds.play(Sound.LASER_DIE);
		    return Direction.NONE;
		}
		if (this.dir1X == 0 && this.dir2X == 1 && this.dir1Y == -1 && this.dir2Y == 0
			|| this.dir1X == 0 && this.dir2X == -1 && this.dir1Y == 1 && this.dir2Y == 0
			|| this.dir1X == 1 && this.dir2X == 0 && this.dir1Y == 0 && this.dir2Y == -1
			|| this.dir1X == -1 && this.dir2X == 0 && this.dir1Y == 0 && this.dir2Y == 1) {
		    this.flip = true;
		} else {
		    this.flip = false;
		}
	    } else if (this.isHostile()) {
		final var baseDir = this.getDirection();
		if (laserType == LaserType.MISSILE || laserType == LaserType.POWER) {
		    // Kill
		    final var dat = new ArenaObject(GameObjectID.DEAD_ANTI_TANK);
		    dat.setSavedObject(this.getSavedObject());
		    dat.setDirection(baseDir);
		    Game.morph(dat, locX, locY, locZ, this.layer());
		    Sounds.play(Sound.ANTI_DIE);
		    return Direction.NONE;
		}
		if (laserType == LaserType.STUNNER) {
		    // Stun
		    final var sat = new ArenaObject(GameObjectID.STUNNED_ANTI_TANK);
		    sat.setSavedObject(this.getSavedObject());
		    sat.setDirection(baseDir);
		    Game.morph(sat, locX, locY, locZ, this.layer());
		    Sounds.play(Sound.STUN);
		    return Direction.NONE;
		}
		final var sourceDir = DirectionHelper.resolveRelativeInvert(dirX, dirY);
		if (sourceDir == baseDir) {
		    // Kill
		    final var dat = new ArenaObject(GameObjectID.DEAD_ANTI_TANK);
		    dat.setSavedObject(this.getSavedObject());
		    dat.setDirection(baseDir);
		    Game.morph(dat, locX, locY, locZ, this.layer());
		    Sounds.play(Sound.ANTI_DIE);
		    return Direction.NONE;
		}
	    } else if (this.isSolid()) {
		if (this.canBreak()) {
		    // Delete self
		    Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.layer());
		    if (laserType == LaserType.POWER) {
			// Laser keeps going
			return DirectionHelper.resolveRelative(dirX, dirY);
		    }
		    // Laser stops
		    return Direction.NONE;
		}
		return this.laserEnteredActionHook(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	    final var dir = DirectionHelper.resolveRelative(dirX, dirY);
	    if (this.isSolid()) {
		if (forceUnits > this.getMinimumReactionForce() && this.canMove()) {
		    try {
			final var nextObj = ArenaManager.getArena().getCell(locX + dirX, locY + dirY, locZ,
				this.layer());
			final var nextObj2 = ArenaManager.getArena().getCell(locX + dirX * 2, locY + dirY * 2, locZ,
				this.layer());
			if (nextObj.canMove()
				&& (nextObj2 != null && !nextObj2.isConditionallySolid() || forceUnits > 2)) {
			    Game.updatePushedPositionLater(locX, locY, dirX, dirY, this, locX + dirX, locY + dirY,
				    nextObj, laserType, forceUnits - Math.max(1, this.getMinimumReactionForce()));
			} else {
			    // Object crushed by impact
			    this.pushCrushAction(locX, locY, locZ);
			}
		    } catch (final ArrayIndexOutOfBoundsException aioob) {
			// Object crushed by impact
			this.pushCrushAction(locX, locY, locZ);
		    }
		} else {
		    final var adj = ArenaManager.getArena().getCell(locX - dirX, locY - dirY, locZ, this.layer());
		    if (adj != null && !adj.rangeAction(locX - 2 * dirX, locY - 2 * dirY, locZ, dirX, dirY,
			    LaserTypeHelper.rangeType(laserType), 1)) {
			Sounds.play(Sound.LASER_DIE);
		    }
		}
		// Laser stops
		return Direction.NONE;
	    }
	    if (ArenaObjectData.isReflective(this.getID(), dir) && ArenaObjectData.hitReflectiveSide(dir)) {
		// Laser reflected
		return this.direction;
	    }
	    return DirectionHelper.resolveRelative(dirX, dirY);
	}
	// Not enough force
	return Direction.NONE;
    }

    /**
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param laserType
     * @param forceUnits
     */
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	// Do nothing
	return Direction.NONE;
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param laserType
     * @return
     */
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType) {
	final var dir = DirectionHelper.resolveRelative(dirX, dirY);
	if (!ArenaObjectData.isReflective(this.getID(), dir) || !ArenaObjectData.hitReflectiveSide(dir)) {
	    return DirectionHelper.resolveRelative(dirX, dirY);
	}
	// Finish reflecting laser
	Sounds.play(Sound.REFLECT);
	final var oldlaser = DirectionHelper.resolveRelativeInvert(locX, locY);
	final var currdir = this.getDirection();
	if (oldlaser == Direction.NORTH) {
	    if (currdir == Direction.NORTHWEST) {
		return Direction.WEST;
	    }
	    if (currdir == Direction.NORTHEAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.SOUTH) {
	    if (currdir == Direction.SOUTHWEST) {
		return Direction.WEST;
	    }
	    if (currdir == Direction.SOUTHEAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.WEST) {
	    if (currdir == Direction.SOUTHWEST) {
		return Direction.SOUTH;
	    }
	    if (currdir == Direction.NORTHWEST) {
		return Direction.NORTH;
	    }
	} else if (oldlaser == Direction.EAST) {
	    if (currdir == Direction.SOUTHEAST) {
		return Direction.SOUTH;
	    }
	    if (currdir == Direction.NORTHEAST) {
		return Direction.NORTH;
	    }
	}
	return Direction.NONE;
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     */
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	Sounds.play(Sound.BUMP_HEAD);
    }

    public final boolean movesBoxes(final Direction dir) {
	return ArenaObjectData.movesBoxes(this.getID(), dir);
    }

    public final boolean movesHostiles(final Direction dir) {
	return ArenaObjectData.movesHostiles(this.getID(), dir);
    }

    public final boolean movesMirrors(final Direction dir) {
	return ArenaObjectData.movesMirrors(this.getID(), dir);
    }

    public final boolean movesTanks(final Direction dir) {
	return ArenaObjectData.movesTanks(this.getID(), dir);
    }

    public final void nextFrame() {
	if (this.isAnimated()) {
	    this.frameNumber++;
	    if (this.frameNumber > this.getLastFrameNumber()) {
		this.frameNumber = 1;
	    }
	}
    }

    public final void nextIndex() {
	final var validIndexes = ArenaObjectData.getValidIndexes(this.getID());
	if (validIndexes.length > 0) {
	    final var lastIndex = validIndexes[validIndexes.length - 1];
	    if (lastIndex != 0 && this.index < lastIndex) {
		this.index++;
	    }
	}
    }

    // Scripting
    public final void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	final var n = ArenaObjectData.navigatesToOnMove(this.getID());
	if (n != 0) {
	    Game.updatePositionAbsoluteNoEvents(n);
	}
	if (this.isTunnel()) {
	    final var tx = Game.getPlayerLocationX();
	    final var ty = Game.getPlayerLocationY();
	    final var pgrmdest = ArenaManager.getArena().circularScanTunnel(dirX, dirY, dirZ,
		    ArenaData.TUNNEL_SCAN_RADIUS, tx, ty, ArenaObject.getTunnelOfColor(this.getColor()), true);
	    if (pgrmdest != null) {
		Game.updatePositionAbsoluteNoEvents(pgrmdest[0], pgrmdest[1], pgrmdest[2]);
		Sounds.play(Sound.WARP_TANK);
	    }
	}
	this.postMoveActionHook(dirX, dirY, dirZ);
    }

    /**
     * @param dirX
     * @param dirY
     * @param dirZ
     */
    protected void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     */
    public void pushCollideAction(final ArenaObject pushed, final int x, final int y, final int z) {
	// Do nothing
    }

    protected void pushCrushAction(final int x, final int y, final int z) {
	// Object crushed
	Sounds.play(Sound.CRUSH);
	Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, this.layer());
    }

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
	if (this.removesPushedObjects()) {
	    // Remove pushed object
	    Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, pushed.layer());
	    Sounds.play(Sound.SINK);
	}
	if (this.reactsToObjectsPushedInto()) {
	    // Transform based on material of pushed object
	    if (pushed.isBox()) {
		if (this.blockHeight() > -1) {
		    // Weaken if depth is more than 1
		    Game.morph(this.weakensTo(), x, y, z, this.layer());
		} else {
		    if (pushed.material() == Material.ICE) {
			Game.morph(new ArenaObject(GameObjectID.ICE_BRIDGE), x, y, z, this.layer());
		    } else if (pushed.material() == Material.FIRE) {
			Game.morph(new ArenaObject(GameObjectID.LAVA_BRIDGE), x, y, z, this.layer());
		    } else {
			Game.morph(new ArenaObject(GameObjectID.BRIDGE), x, y, z, this.layer());
		    }
		}
	    }
	}
	if (this.isTunnel()) {
	    final var tx = Game.getPlayerLocationX();
	    final var ty = Game.getPlayerLocationY();
	    final var objColor = this.getColor();
	    final var pgrmdest = ArenaManager.getArena().circularScanTunnel(x, y, z, ArenaData.TUNNEL_SCAN_RADIUS, tx,
		    ty, ArenaObject.getTunnelOfColor(this.getColor()), false);
	    if (pgrmdest != null) {
		ArenaObject.tunnelsFull[objColor.ordinal()] = false;
		Game.updatePushedIntoPositionAbsolute(pgrmdest[0], pgrmdest[1], pgrmdest[2], x, y, z, pushed, this);
		Sounds.play(Sound.WARP_OBJECT);
	    } else {
		ArenaObject.tunnelsFull[objColor.ordinal()] = true;
		pushed.setWaitingOnTunnel(true);
	    }
	    return false;
	}
	if (this.getPairedWith() != null && this.usesTrigger() && pushed.material() == this.material()) {
	    Sounds.play(Sound.BUTTON);
	    if (!this.isPairTriggered()) {
		// Check to open door at location
		this.setPairTriggered(true);
		ArenaManager.getArena().fullScanPairOpen(z, this);
	    }
	}
	// Do nothing
	return true;
    }

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     */
    public void pushOutAction(final ArenaObject pushed, final int x, final int y, final int z) {
	if (this.getPairedWith() != null && this.usesTrigger() && pushed.material() == this.material()
		&& this.isPairTriggered()) {
	    // Check to close door at location
	    this.setPairTriggered(false);
	    ArenaManager.getArena().fullScanPairClose(z, this);
	}
	// Do nothing
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param rangeType
     * @param forceUnits
     * @return
     */
    public boolean rangeAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	if (forceUnits >= this.getMinimumReactionForce()) {
	    if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.material() == Material.WOODEN
		    && this.changesToOnExposure(Material.FIRE) != null) {
		// Burn wooden object
		Sounds.play(Sound.BURN);
		Game.morph(this.changesToOnExposure(Material.FIRE), locX + dirX, locY + dirY, locZ, this.layer());
	    }
	    if (RangeTypeHelper.material(rangeType) == Material.ICE && (this.material() == Material.METALLIC
		    || this.material() == Material.WOODEN || this.material() == Material.PLASTIC)
		    && this.changesToOnExposure(Material.ICE) != null) {
		// Freeze metal, wooden, or plastic object
		Sounds.play(Sound.FREEZE);
		Game.morph(this.changesToOnExposure(Material.ICE), locX + dirX, locY + dirY, locZ, this.layer());
	    }
	    if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.material() == Material.ICE
		    && this.changesToOnExposure(Material.FIRE) != null) {
		// Melt icy object
		Sounds.play(Sound.DEFROST);
		Game.morph(this.changesToOnExposure(Material.FIRE), locX + dirX, locY + dirY, locZ, this.layer());
	    }
	    if (RangeTypeHelper.material(rangeType) == Material.ICE && this.material() == Material.FIRE
		    && this.changesToOnExposure(Material.ICE) != null) {
		// Cool hot object
		Sounds.play(Sound.COOL_OFF);
		Game.morph(this.changesToOnExposure(Material.ICE), locX + dirX, locY + dirY, locZ, this.layer());
	    }
	    if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.material() == Material.METALLIC
		    && this.changesToOnExposure(Material.FIRE) != null) {
		// Melt metal object
		Sounds.play(Sound.MELT);
		Game.morph(this.changesToOnExposure(Material.FIRE), locX + dirX, locY + dirY, locZ, this.layer());
	    }
	    var snd = this.rangeSound();
	    if (snd != Sound._NONE) {
		Sounds.play(snd);
	    }
	    return this.rangeActionHook(locX, locY, locZ, dirX, dirY, rangeType, forceUnits);
	}
	return false;
    }

    /**
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param rangeType
     * @param forceUnits
     */
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	// Do nothing
	return true;
    }

    /**
     * @param ident
     */
    public final ArenaObject readArenaObjectG2(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	final var cc = this.getCustomFormat();
	if (cc == ArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    reader.readInt();
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    return this.readArenaObjectHookG2(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    /**
     * @param ident
     */
    public final ArenaObject readArenaObjectG3(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	final var cc = this.getCustomFormat();
	if (cc == ArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    // Discard material
	    reader.readInt();
	    return this.readArenaObjectHookG3(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	// Discard material
	reader.readInt();
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    /**
     * @param ident
     */
    public final ArenaObject readArenaObjectG4(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	final var cc = this.getCustomFormat();
	if (cc == ArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    return this.readArenaObjectHookG4(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    /**
     * @param ident
     */
    public final ArenaObject readArenaObjectG5(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	final var cc = this.getCustomFormat();
	if (cc == ArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    return this.readArenaObjectHookG5(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    /**
     * @param ident
     */
    public final ArenaObject readArenaObjectG6(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	final var cc = this.getCustomFormat();
	if (cc == ArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    return this.readArenaObjectHookG6(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected ArenaObject readArenaObjectHookG2(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected ArenaObject readArenaObjectHookG3(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected ArenaObject readArenaObjectHookG4(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected ArenaObject readArenaObjectHookG5(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected ArenaObject readArenaObjectHookG6(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    public final void setColor(final GameColor col) {
	this.color = col;
    }

    /**
     * @param propID
     * @param value
     */
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    public final void setDirection(final Direction dir) {
	this.direction = dir;
    }

    public void setEnabled(final boolean value) {
	this.imageEnabled = value;
    }

    public final void setIndex(final int ni) {
	this.index = ni;
    }

    public final void setJumpCols(final int njc) {
	this.jumpCols = njc;
    }

    public final void setJumpRows(final int njr) {
	this.jumpRows = njr;
    }

    public void setPairTriggered(final boolean isPairTriggered) {
	this.pairTriggered = isPairTriggered;
    }

    public void setPairX(final int newPairX) {
	this.pairX = newPairX;
    }

    public void setPairY(final int newPairY) {
	this.pairY = newPairY;
    }

    public final void setPreviousState(final ArenaObject obj) {
	this.previousState = obj;
    }

    public final void setSavedObject(final ArenaObject obj) {
	this.savedObject = obj;
    }

    public final void setWaitingOnTunnel(final boolean value) {
	this.waitingOnTunnel = value;
    }

    public final void tickTimer(final int dirX, final int dirY, final GameAction actionType) {
	if (this.timerActive && this.acceptTick(actionType)) {
	    this.timerValue--;
	    if (this.timerValue == 0) {
		this.timerActive = false;
		this.timerExpiredAction(dirX, dirY);
	    }
	}
    }

    /**
     *
     * @param dirX
     * @param dirY
     */
    public void timerExpiredAction(final int dirX, final int dirY) {
	if (this.canControl()) {
	    if (this.isPowerful()) {
		Sounds.play(Sound.RETURN);
		Game.setNormalTank();
	    } else {
		if (this.getSavedObject().canMove()) {
		    this.getSavedObject().timerExpiredAction(dirX, dirY);
		}
		this.activateTimer(1);
	    }
	} else if (this.isHostile()) {
	    final var moveDir = this.getSavedObject().getDirection();
	    if (this.getSavedObject().movesHostiles(moveDir)) {
		final var unres = DirectionHelper.unresolveRelative(moveDir);
		if (Game.canObjectMove(dirX, dirY, unres[0], unres[1])) {
		    if (this.autoMove) {
			this.autoMove = false;
			Game.updatePushedPosition(dirX, dirY, dirX + unres[0], dirY + unres[1], this);
		    }
		} else {
		    this.autoMove = true;
		}
	    }
	    this.activateTimer(1);
	} else if (this.isStunned()) {
	    this.stunnedLeft--;
	    if (this.stunnedLeft == 1) {
		Sounds.play(Sound.RETURN);
		this.activateTimer(1);
	    } else if (this.stunnedLeft == 0) {
		final var z = Game.getPlayerLocationZ();
		final var at = new ArenaObject(GameObjectID.ANTI_TANK);
		at.setSavedObject(this.getSavedObject());
		at.setDirection(this.getDirection());
		Game.morph(at, dirX, dirY, z, this.layer());
	    } else {
		Sounds.play(Sound.STUNNED);
		this.activateTimer(1);
	    }
	}
	// Do nothing
    }

    public final void toggleDirection() {
	this.direction = DirectionHelper.nextOrthogonal(this.direction);
    }

    public final void toggleDirectionInvert() {
	this.direction = DirectionHelper.previousOrthogonal(this.direction);
    }

    public final boolean usesTrigger() {
	return ArenaObjectData.usesTrigger(this.getID());
    }

    public final boolean waitingOnTunnel() {
	return this.waitingOnTunnel;
    }

    public final ArenaObject weakensTo() {
	final var newID = ArenaObjectData.weakensTo(this.getID());
	if (this.getID() != newID) {
	    return new ArenaObject(newID);
	}
	return this;
    }

    public final void writeArenaObject(final DataIOWriter writer) throws IOException {
	writer.writeString(this.getID().toString());
	final var cc = this.getCustomFormat();
	if (cc == ArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color.ordinal());
	    this.writeArenaObjectHook(writer);
	} else {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color.ordinal());
	    for (var x = 0; x < cc; x++) {
		final var cx = this.getCustomProperty(x + 1);
		writer.writeInt(cx);
	    }
	}
    }

    /**
     *
     * @param writer
     * @throws IOException
     */
    protected void writeArenaObjectHook(final DataIOWriter writer) throws IOException {
	// Do nothing - but let subclasses override
    }
}

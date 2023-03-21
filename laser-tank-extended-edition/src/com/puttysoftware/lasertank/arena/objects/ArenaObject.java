/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
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
	private final static int TUNNEL_SCAN_RADIUS = 24;

	public static final int getImbuedForce(final Material materialID) {
		return ArenaObjectData.getImbuedForce(materialID);
	}

	public static void checkTunnels() {
		for (var x = 0; x < GameColorHelper.COUNT; x++) {
			checkTunnelsOfColor(GameColorHelper.fromOrdinal(x));
		}
	}

	private static void checkTunnelsOfColor(final GameColor color) {
		final var tx = LaserTankEE.getGame().getPlayerManager().getPlayerLocationX();
		final var ty = LaserTankEE.getGame().getPlayerManager().getPlayerLocationY();
		final var pgrmdest = LaserTankEE.getArenaManager().getArena().circularScanTunnel(0, 0, 0,
				TUNNEL_SCAN_RADIUS,
				tx, ty, getTunnelOfColor(color), false);
		tunnelsFull[color.ordinal()] = (pgrmdest != null);
	}

	private static ArenaObject getTunnelOfColor(final GameColor color) {
		return new ArenaObject(GameObjectID.TUNNEL, color);
	}

	public static boolean tunnelsFull(final GameColor color) {
		return tunnelsFull[color.ordinal()];
	}

	// Properties
	private int timerValue;
	private boolean timerActive;
	private int frameNumber;
	private Direction direction;
	private GameColor color;
	private int index;
	private boolean imageEnabled;
	private ArenaObject savedObject;
	private ArenaObject previousState;
	private GameObjectID gameObjectID;
	private boolean waitingOnTunnel;
	private boolean pairTriggered;
	private int pairX;
	private int pairY;
	private ArenaObject pairedWith;
	private boolean flip;
	private int dir1X;
	private int dir1Y;
	private int dir2X;
	private int dir2Y;
	private int jumpRows;
	private int jumpCols;
	private boolean jumpShot;
	private boolean autoMove;
	private boolean shotUnlocked;

	// Constructors
	public ArenaObject() {
		if (this.canControl()) {
			this.activateTimer(1);
			this.index = 1;
		} else if (this.isHostile()) {
			this.activateTimer(1);
			this.index = 0;
		} else {
			this.timerValue = 0;
			this.timerActive = false;
			this.index = 0;
		}
		this.frameNumber = this.isAnimated() ? 1 : 0;
		this.direction = this.getInitialDirection();
		this.color = GameColor.NONE;
		this.imageEnabled = true;
		this.waitingOnTunnel = false;
		if (this.canMove() || this.canControl()) {
			this.savedObject = new ArenaObject(GameObjectID.PLACEHOLDER);
		}
		this.pairTriggered = false;
		this.pairX = -1;
		this.pairY = -1;
		var pairID = ArenaObjectData.getPairedObjectID(this.getID());
		if (pairID != null) {
			this.pairedWith = new ArenaObject(pairID);
		}
		this.jumpRows = 0;
		this.jumpCols = 0;
		this.jumpShot = false;
		this.flip = false;
		this.dir1X = 0;
		this.dir1Y = 0;
		this.dir2X = 0;
		this.dir2Y = 0;
	}

	public ArenaObject(final GameObjectID goid) {
		if (this.canControl()) {
			this.activateTimer(1);
			this.index = 1;
		} else if (this.isHostile()) {
			this.activateTimer(1);
			this.index = 0;
		} else {
			this.timerValue = 0;
			this.timerActive = false;
			this.index = 0;
		}
		this.frameNumber = this.isAnimated() ? 1 : 0;
		this.direction = this.getInitialDirection();
		this.color = GameColor.NONE;
		this.imageEnabled = true;
		this.gameObjectID = goid;
		this.waitingOnTunnel = false;
		if (this.canMove() || this.canControl()) {
			this.savedObject = new ArenaObject(GameObjectID.PLACEHOLDER);
		}
		this.pairTriggered = false;
		this.pairX = -1;
		this.pairY = -1;
		var pairID = ArenaObjectData.getPairedObjectID(this.getID());
		if (pairID != null) {
			this.pairedWith = new ArenaObject(pairID);
		}
		this.jumpRows = 0;
		this.jumpCols = 0;
		this.jumpShot = false;
		this.flip = false;
		this.dir1X = 0;
		this.dir1Y = 0;
		this.dir2X = 0;
		this.dir2Y = 0;
	}

	public ArenaObject(final GameObjectID goid, final Direction dir) {
		if (this.canControl()) {
			this.activateTimer(1);
			this.index = 1;
		} else if (this.isHostile()) {
			this.activateTimer(1);
			this.index = 0;
		} else {
			this.timerValue = 0;
			this.timerActive = false;
			this.index = 0;
		}
		this.frameNumber = this.isAnimated() ? 1 : 0;
		this.direction = dir;
		this.color = GameColor.NONE;
		this.imageEnabled = true;
		this.gameObjectID = goid;
		this.waitingOnTunnel = false;
		if (this.canMove() || this.canControl()) {
			this.savedObject = new ArenaObject(GameObjectID.PLACEHOLDER);
		}
		this.pairTriggered = false;
		this.pairX = -1;
		this.pairY = -1;
		var pairID = ArenaObjectData.getPairedObjectID(this.getID());
		if (pairID != null) {
			this.pairedWith = new ArenaObject(pairID);
		}
		this.jumpRows = 0;
		this.jumpCols = 0;
		this.jumpShot = false;
		this.flip = false;
		this.dir1X = 0;
		this.dir1Y = 0;
		this.dir2X = 0;
		this.dir2Y = 0;
	}

	public ArenaObject(final GameObjectID goid, final int newIndex) {
		if (this.canControl()) {
			this.activateTimer(1);
			this.index = newIndex;
		} else if (this.isHostile()) {
			this.activateTimer(1);
			this.index = newIndex;
		} else {
			this.timerValue = 0;
			this.timerActive = false;
			this.index = newIndex;
		}
		this.frameNumber = this.isAnimated() ? 1 : 0;
		this.direction = this.getInitialDirection();
		this.color = GameColor.NONE;
		this.imageEnabled = true;
		this.gameObjectID = goid;
		this.waitingOnTunnel = false;
		if (this.canMove() || this.canControl()) {
			this.savedObject = new ArenaObject(GameObjectID.PLACEHOLDER);
		}
		this.pairTriggered = false;
		this.pairX = -1;
		this.pairY = -1;
		var pairID = ArenaObjectData.getPairedObjectID(this.getID());
		if (pairID != null) {
			this.pairedWith = new ArenaObject(pairID);
		}
		this.jumpRows = 0;
		this.jumpCols = 0;
		this.jumpShot = false;
		this.flip = false;
		this.dir1X = 0;
		this.dir1Y = 0;
		this.dir2X = 0;
		this.dir2Y = 0;
	}

	public ArenaObject(final GameObjectID goid, final Direction dir, final int newIndex) {
		if (this.canControl()) {
			this.activateTimer(1);
			this.index = newIndex;
		} else if (this.isHostile()) {
			this.activateTimer(1);
			this.index = newIndex;
		} else {
			this.timerValue = 0;
			this.timerActive = false;
			this.index = newIndex;
		}
		this.frameNumber = this.isAnimated() ? 1 : 0;
		this.direction = dir;
		this.color = GameColor.NONE;
		this.imageEnabled = true;
		this.gameObjectID = goid;
		this.waitingOnTunnel = false;
		if (this.canMove() || this.canControl()) {
			this.savedObject = new ArenaObject(GameObjectID.PLACEHOLDER);
		}
		this.pairTriggered = false;
		this.pairX = -1;
		this.pairY = -1;
		var pairID = ArenaObjectData.getPairedObjectID(this.getID());
		if (pairID != null) {
			this.pairedWith = new ArenaObject(pairID);
		}
		this.jumpRows = 0;
		this.jumpCols = 0;
		this.jumpShot = false;
		this.flip = false;
		this.dir1X = 0;
		this.dir1Y = 0;
		this.dir2X = 0;
		this.dir2Y = 0;
	}

	public ArenaObject(final GameObjectID goid, final GameColor newColor) {
		if (this.canControl()) {
			this.activateTimer(1);
			this.index = 1;
		} else if (this.isHostile()) {
			this.activateTimer(1);
			this.index = 0;
		} else {
			this.timerValue = 0;
			this.timerActive = false;
			this.index = 0;
		}
		this.frameNumber = this.isAnimated() ? 1 : 0;
		this.direction = this.getInitialDirection();
		this.color = newColor;
		this.imageEnabled = true;
		this.gameObjectID = goid;
		this.waitingOnTunnel = false;
		if (this.canMove() || this.canControl()) {
			this.savedObject = new ArenaObject(GameObjectID.PLACEHOLDER);
		}
		this.pairTriggered = false;
		this.pairX = -1;
		this.pairY = -1;
		var pairID = ArenaObjectData.getPairedObjectID(this.getID());
		if (pairID != null) {
			this.pairedWith = new ArenaObject(pairID);
		}
		this.jumpRows = 0;
		this.jumpCols = 0;
		this.jumpShot = false;
		this.flip = false;
		this.dir1X = 0;
		this.dir1Y = 0;
		this.dir2X = 0;
		this.dir2Y = 0;
	}

	public ArenaObject(final GameObjectID goid, final GameColor newColor, final int newIndex) {
		if (this.canControl()) {
			this.activateTimer(1);
			this.index = newIndex;
		} else if (this.isHostile()) {
			this.activateTimer(1);
			this.index = newIndex;
		} else {
			this.timerValue = 0;
			this.timerActive = false;
			this.index = newIndex;
		}
		this.frameNumber = this.isAnimated() ? 1 : 0;
		this.direction = this.getInitialDirection();
		this.color = newColor;
		this.imageEnabled = true;
		this.gameObjectID = goid;
		this.waitingOnTunnel = false;
		if (this.canMove() || this.canControl()) {
			this.savedObject = new ArenaObject(GameObjectID.PLACEHOLDER);
		}
		this.pairTriggered = false;
		this.pairX = -1;
		this.pairY = -1;
		var pairID = ArenaObjectData.getPairedObjectID(this.getID());
		if (pairID != null) {
			this.pairedWith = new ArenaObject(pairID);
		}
		this.jumpRows = 0;
		this.jumpCols = 0;
		this.jumpShot = false;
		this.flip = false;
		this.dir1X = 0;
		this.dir1Y = 0;
		this.dir2X = 0;
		this.dir2Y = 0;
	}

	public final boolean acceptTick(final GameAction actionType) {
		return ArenaObjectData.acceptTick(this.getID(), actionType);
	}

	public final void activateTimer(final int ticks) {
		this.timerActive = true;
		this.timerValue = ticks;
	}

	public final ArenaObject attributeRenderHook() {
		var renderAs = ArenaObjectData.attributeRenderHook(this.getID());
		return renderAs != null ? new ArenaObject(renderAs) : null;
	}

	public final boolean canCloak() {
		return ArenaObjectData.canCloak(this.getID());
	}

	public final boolean canControl() {
		return ArenaObjectData.canControl(this.getID());
	}

	public final boolean canJump() {
		return ArenaObjectData.canJump(this.getID());
	}

	public final boolean canMove() {
		return ArenaObjectData.canMove(this.getID());
	}

	public final boolean canRoll() {
		return ArenaObjectData.canRoll(this.getID());
	}

	public final boolean canShoot() {
		return ArenaObjectData.canShoot(this.getID());
	}

	/**
	 *
	 * @param materialID
	 * @return
	 */
	public ArenaObject changesToOnExposure(final Material materialID) {
		return this;
	}

	// Methods
	@Override
	public ArenaObject clone() {
		try {
			final ArenaObject copy = this.getClass().getConstructor().newInstance();
			copy.timerValue = this.timerValue;
			copy.timerActive = this.timerActive;
			copy.frameNumber = this.frameNumber;
			copy.direction = this.direction;
			copy.color = this.color;
			return copy;
		} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			LaserTankEE.logError(e);
			return null;
		}
	}

	public boolean defersSetProperties() {
		return false;
	}

	public boolean doLasersPassThrough() {
		return !this.isSolid();
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void editorPlaceHook(final int x, final int y, final int z) {
		if (this.pairedWith != null && this.usesTrigger()) {
			final var loc = LaserTankEE.getArenaManager().getArena().findObject(z, this.getPairedWith());
			if (loc != null) {
				this.setPairX(loc[0]);
				this.setPairY(loc[1]);
				this.setPairTriggered(false);
			}
			if (this.usesTrigger()) {
				LaserTankEE.getArenaManager().getArena().fullScanPairCleanup(x, y, z, this);
			}
			LaserTankEE.getEditor().redrawEditor();
		}
		// Do nothing
	}

	public ArenaObject editorPropertiesHook() {
		if (this.hasDirection()) {
			this.toggleDirection();
			return this;
		} else if (this.canJump()) {
			LaserTankEE.getEditor().editJumpBox(this);
			return this;
		}
		return null;
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void editorRemoveHook(final int x, final int y, final int z) {
		// Do nothing
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof final ArenaObject other)) {
			return false;
		}
		if (this.direction != other.direction) {
			return false;
		}
		if (this.color != other.color) {
			return false;
		}
		return true;
	}

	public final int getBlockHeight() {
		return ArenaObjectData.getBlockHeight(this.getID());
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

	public int getCustomProperty(final int propID) {
		return ArenaObject.DEFAULT_CUSTOM_VALUE;
	}

	public String getCustomText() {
		if (this.canControl()) {
			return Integer.toString(this.index);
		} else if (this.canJump()) {
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

	private final int getLastFrameNumber() {
		return ArenaObjectData.getLastFrameNumber(this.getID());
	}

	private final String getIdentifier() {
		return this.getImageName();
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
		} else {
			return ArenaObjectImageResolver.getImageName(this.getID());
		}
	}

	private final Direction getInitialDirection() {
		return ArenaObjectData.getValidDirections(this.getID())[0];
	}

	public final int getJumpCols() {
		return this.jumpCols;
	}

	public final int getJumpRows() {
		return this.jumpRows;
	}

	public final int getLayer() {
		return ArenaObjectData.getLayer(getID());
	}

	public final Material getMaterial() {
		return ArenaObjectData.getMaterial(getID());
	}

	public final int getMinimumReactionForce() {
		return ArenaObjectData.getMinimumReactionForce(this.getMaterial());
	}

	public final int getNumber() {
		return this.index;
	}

	public int getPairX() {
		return this.pairX;
	}

	public int getPairY() {
		return this.pairY;
	}

	public final ArenaObject getPairedWith() {
		return this.pairedWith;
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

	public GameObjectID getID() {
		return this.gameObjectID;
	}

	private final boolean hasDirection() {
		return this.direction != Direction.NONE;
	}

	public final boolean hasFriction() {
		return ArenaObjectData.hasFriction(this.getID());
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

	private final boolean isAnimated() {
		return ArenaObjectData.isAnimated(this.getID());
	}

	public final boolean isBox() {
		return ArenaObjectData.isBox(this.getID());
	}

	public boolean isConditionallySolid() {
		return this.isSolid();
	}

	public boolean isEnabled() {
		return this.imageEnabled;
	}

	public final boolean isHostile() {
		return ArenaObjectData.isHostile(this.getID());
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

	public final boolean isMovableMirror(final Direction dir) {
		return ArenaObjectData.isMovableMirror(this.getID(), dir);
	}

	public boolean isPairTriggered() {
		return this.pairTriggered;
	}

	public final boolean isPushable() {
		return ArenaObjectData.isPushable(this.getID());
	}

	public final boolean isSolid() {
		return ArenaObjectData.isSolid(this.getID());
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
			LaserTankEE.getGame().setLaserType(LaserType.RED);
			LaserTankEE.getGame().fireLaser(locX, locY, this);
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
		// Do nothing
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
					final var mof = LaserTankEE.getArenaManager().getArena().getCell(locX + dirX, locY + dirY, locZ,
							this.getLayer());
					final var mor = LaserTankEE.getArenaManager().getArena().getCell(locX - dirX, locY - dirY, locZ,
							this.getLayer());
					if (this.getMaterial() == Material.MAGNETIC) {
						if (laserType == LaserType.BLUE && mof != null
								&& (mof.canControl() || !mof.isSolid())) {
							LaserTankEE.getGame().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
						} else if (mor != null && (mor.canControl() || !mor.isSolid())) {
							LaserTankEE.getGame().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
						} else {
							// Object doesn't react to this type of laser
							return Direction.NONE;
						}
					} else {
						if (laserType == LaserType.BLUE && mor != null
								&& (mor.canControl() || !mor.isSolid())) {
							LaserTankEE.getGame().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
						} else if (mof != null && (mof.canControl() || !mof.isSolid())) {
							LaserTankEE.getGame().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
						} else {
							// Object doesn't react to this type of laser
							return Direction.NONE;
						}
					}
					Sounds.play(this.laserEnteredSound());
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					// Object can't go that way
					return Direction.NONE;
				}
				return Direction.NONE;
			} else if (this.canJump()) {
				final var px = LaserTankEE.getGame().getPlayerManager().getPlayerLocationX();
				final var py = LaserTankEE.getGame().getPlayerManager().getPlayerLocationY();
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
				} else {
					if (this.dir1X == 0 && this.dir2X == 1 && this.dir1Y == -1 && this.dir2Y == 0
							|| this.dir1X == 0 && this.dir2X == -1 && this.dir1Y == 1 && this.dir2Y == 0
							|| this.dir1X == 1 && this.dir2X == 0 && this.dir1Y == 0 && this.dir2Y == -1
							|| this.dir1X == -1 && this.dir2X == 0 && this.dir1Y == 0 && this.dir2Y == 1) {
						this.flip = true;
					} else {
						this.flip = false;
					}
				}
			} else if (this.isHostile()) {
				final var baseDir = this.getDirection();
				if (laserType == LaserType.MISSILE || laserType == LaserType.POWER) {
					// Kill
					final var gm = LaserTankEE.getGame();
					final var dat = new ArenaObject(GameObjectID.DEAD_ANTI_TANK);
					dat.setSavedObject(this.getSavedObject());
					dat.setDirection(baseDir);
					gm.morph(dat, locX, locY, locZ, this.getLayer());
					Sounds.play(Sound.ANTI_DIE);
					return Direction.NONE;
				}
				if (laserType == LaserType.STUNNER) {
					// Stun
					final var gm = LaserTankEE.getGame();
					final var sat = new StunnedAntiTank();
					sat.setSavedObject(this.getSavedObject());
					sat.setDirection(baseDir);
					gm.morph(sat, locX, locY, locZ, this.getLayer());
					Sounds.play(Sound.STUN);
					return Direction.NONE;
				}
				final var sourceDir = DirectionHelper.resolveRelativeInvert(dirX, dirY);
				if (sourceDir == baseDir) {
					// Kill
					final var gm = LaserTankEE.getGame();
					final var dat = new ArenaObject(GameObjectID.DEAD_ANTI_TANK);
					dat.setSavedObject(this.getSavedObject());
					dat.setDirection(baseDir);
					gm.morph(dat, locX, locY, locZ, this.getLayer());
					Sounds.play(Sound.ANTI_DIE);
					return Direction.NONE;
				}
			} else if (this.isSolid()) {
				return this.laserEnteredActionHook(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
			}
			final var dir = DirectionHelper.resolveRelative(dirX, dirY);
			if (this.isSolid()) {
				if (forceUnits > this.getMinimumReactionForce() && this.canMove()) {
					try {
						final var nextObj = LaserTankEE.getArenaManager().getArena().getCell(
								locX + dirX,
								locY + dirY, locZ, this.getLayer());
						final var nextObj2 = LaserTankEE.getArenaManager().getArena()
								.getCell(locX + dirX * 2, locY + dirY * 2, locZ, this.getLayer());
						if (this instanceof final ArenaObject gmo && nextObj instanceof ArenaObject gmo2
								&& nextObj.canMove()
								&& (nextObj2 != null && !nextObj2.isConditionallySolid() || forceUnits > 2)) {
							LaserTankEE.getGame().updatePushedPositionLater(locX, locY, dirX,
									dirY,
									gmo, locX + dirX, locY + dirY, gmo2, laserType,
									forceUnits - Math.max(1, this.getMinimumReactionForce()));
						} else {
							// Object crushed by impact
							this.pushCrushAction(locX, locY, locZ);
						}
					} catch (final ArrayIndexOutOfBoundsException aioob) {
						// Object crushed by impact
						this.pushCrushAction(locX, locY, locZ);
					}
				} else {
					final var adj = LaserTankEE.getArenaManager().getArena().getCell(locX - dirX,
							locY - dirY, locZ, this.getLayer());
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

	public Direction laserEnteredActionHook(int locX, int locY, int locZ, int dirX, int dirY,
			LaserType laserType, int forceUnits) {
		// Do nothing
		return Direction.NONE;
	}

	public Sound laserEnteredSound() {
		if (this.isHostile()) {
			return Sound.PUSH_ANTI_TANK;
		}
		return null;
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

	public final void nextIndex() {
		var validIndexes = ArenaObjectData.getValidIndexes(getID());
		if (validIndexes.length > 0) {
			var lastIndex = validIndexes[validIndexes.length - 1];
			if (lastIndex != 0 && this.index < lastIndex) {
				this.index++;
			}
		}
	}

	public final void nextFrame() {
		if (this.isAnimated()) {
			this.frameNumber++;
			if (this.frameNumber > this.getLastFrameNumber()) {
				this.frameNumber = 1;
			}
		}
	}

	// Scripting
	public final void postMoveAction(final int dirX, final int dirY, int dirZ) {
		var n = ArenaObjectData.navigatesToOnMove(this.getID());
		if (n != 0) {
			LaserTankEE.getGame().updatePositionAbsoluteNoEvents(n);
		}
		if (this.isTunnel()) {
			final var tx = LaserTankEE.getGame().getPlayerManager().getPlayerLocationX();
			final var ty = LaserTankEE.getGame().getPlayerManager().getPlayerLocationY();
			final var pgrmdest = LaserTankEE.getArenaManager().getArena().circularScanTunnel(dirX, dirY, dirZ,
					TUNNEL_SCAN_RADIUS, tx, ty, getTunnelOfColor(this.getColor()), true);
			if (pgrmdest != null) {
				LaserTankEE.getGame().updatePositionAbsoluteNoEvents(pgrmdest[0], pgrmdest[1], pgrmdest[2]);
				Sounds.play(Sound.WARP_TANK);
			}
		}
		this.postMoveActionHook(dirX, dirY, dirZ);
	}

	protected void postMoveActionHook(final int dirX, final int dirY, int dirZ) {
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
		LaserTankEE.getGame().morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, this.getLayer());
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
		if (this.isTunnel()) {
			final var tx = LaserTankEE.getGame().getPlayerManager().getPlayerLocationX();
			final var ty = LaserTankEE.getGame().getPlayerManager().getPlayerLocationY();
			final var color = this.getColor();
			final var pgrmdest = LaserTankEE.getArenaManager().getArena().circularScanTunnel(x, y, z,
					TUNNEL_SCAN_RADIUS,
					tx, ty, getTunnelOfColor(this.getColor()), false);
			if (pgrmdest != null) {
				tunnelsFull[color.ordinal()] = false;
				LaserTankEE.getGame().updatePushedIntoPositionAbsolute(pgrmdest[0], pgrmdest[1], pgrmdest[2], x, y, z,
						pushed, this);
				Sounds.play(Sound.WARP_OBJECT);
			} else {
				tunnelsFull[color.ordinal()] = true;
				pushed.setWaitingOnTunnel(true);
			}
			return false;
		} else if (this.getPairedWith() != null && this.usesTrigger()) {
			if (pushed.getMaterial() == this.getMaterial()) {
				Sounds.play(Sound.BUTTON);
				if (!this.isPairTriggered()) {
					// Check to open door at location
					this.setPairTriggered(true);
					LaserTankEE.getArenaManager().getArena().fullScanPairOpen(z, this);
				}
			}
			return true;
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
		if (this.getPairedWith() != null && this.usesTrigger()) {
			if ((pushed.getMaterial() == this.getMaterial()) && this.isPairTriggered()) {
				// Check to close door at location
				this.setPairTriggered(false);
				LaserTankEE.getArenaManager().getArena().fullScanPairClose(z, this);
			}
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
			if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.getMaterial() == Material.WOODEN
					&& this.changesToOnExposure(Material.FIRE) != null) {
				// Burn wooden object
				Sounds.play(Sound.BURN);
				LaserTankEE.getGame().morph(this.changesToOnExposure(Material.FIRE),
						locX + dirX,
						locY + dirY, locZ, this.getLayer());
				return true;
			}
			if (RangeTypeHelper.material(rangeType) == Material.ICE && (this.getMaterial() == Material.METALLIC
					|| this.getMaterial() == Material.WOODEN || this.getMaterial() == Material.PLASTIC)
					&& this.changesToOnExposure(Material.ICE) != null) {
				// Freeze metal, wooden, or plastic object
				Sounds.play(Sound.FREEZE);
				LaserTankEE.getGame().morph(this.changesToOnExposure(Material.ICE), locX + dirX,
						locY + dirY, locZ, this.getLayer());
				return true;
			}
			if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.getMaterial() == Material.ICE
					&& this.changesToOnExposure(Material.FIRE) != null) {
				// Melt icy object
				Sounds.play(Sound.DEFROST);
				LaserTankEE.getGame().morph(this.changesToOnExposure(Material.FIRE),
						locX + dirX,
						locY + dirY, locZ, this.getLayer());
				return true;
			} else if (RangeTypeHelper.material(rangeType) == Material.ICE && this.getMaterial() == Material.FIRE
					&& this.changesToOnExposure(Material.ICE) != null) {
				// Cool hot object
				Sounds.play(Sound.COOL_OFF);
				LaserTankEE.getGame().morph(this.changesToOnExposure(Material.ICE), locX + dirX,
						locY + dirY, locZ, this.getLayer());
				return true;
			} else if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.getMaterial() == Material.METALLIC
					&& this.changesToOnExposure(Material.FIRE) != null) {
				// Melt metal object
				Sounds.play(Sound.MELT);
				LaserTankEE.getGame().morph(this.changesToOnExposure(Material.FIRE),
						locX + dirX,
						locY + dirY, locZ, this.getLayer());
				return true;
			}
		}
		return false;
	}

	public boolean rangeActionHook(int locX, int locY, int locZ, int dirX, int dirY, RangeType rangeType,
			int forceUnits) {
		// Do nothing
		return false;
	}

	public final ArenaObject readArenaObjectG2(final DataIOReader reader, final String ident,
			final GameFormat formatVersion) throws IOException {
		if (!ident.equals(this.getIdentifier())) {
			return null;
		}
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

	public final ArenaObject readArenaObjectG3(final DataIOReader reader, final String ident,
			final GameFormat formatVersion) throws IOException {
		if (!ident.equals(this.getIdentifier())) {
			return null;
		}
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

	public final ArenaObject readArenaObjectG4(final DataIOReader reader, final String ident,
			final GameFormat formatVersion) throws IOException {
		if (!ident.equals(this.getIdentifier())) {
			return null;
		}
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

	public final ArenaObject readArenaObjectG5(final DataIOReader reader, final String ident,
			final GameFormat formatVersion) throws IOException {
		if (!ident.equals(this.getIdentifier())) {
			return null;
		}
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

	public final ArenaObject readArenaObjectG6(final DataIOReader reader, final String ident,
			final GameFormat formatVersion) throws IOException {
		if (!ident.equals(this.getIdentifier())) {
			return null;
		}
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

	public void setCustomProperty(final int propID, final int value) {
		// Do nothing
	}

	public final void setDirection(final Direction dir) {
		this.direction = dir;
	}

	public void setEnabled(final boolean value) {
		this.imageEnabled = value;
	}

	public final void setJumpCols(final int njc) {
		this.jumpCols = njc;
	}

	public final void setJumpRows(final int njr) {
		this.jumpRows = njr;
	}

	public void setPairX(final int newPairX) {
		this.pairX = newPairX;
	}

	public void setPairY(final int newPairY) {
		this.pairY = newPairY;
	}

	public void setPairTriggered(final boolean isPairTriggered) {
		this.pairTriggered = isPairTriggered;
	}

	protected void useIndex() {
		this.index = 1;
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

	public final boolean solvesOnMove() {
		return ArenaObjectData.solvesOnMove(getID());
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
			if (this.getSavedObject().canMove()) {
				this.getSavedObject().timerExpiredAction(dirX, dirY);
			}
			this.activateTimer(1);
		} else if (this.isHostile()) {
			final var moveDir = this.getSavedObject().getDirection();
			if (this.getSavedObject().movesHostiles(moveDir)) {
				final var unres = DirectionHelper.unresolveRelative(moveDir);
				if (Game.canObjectMove(dirX, dirY, unres[0], unres[1])) {
					if (this.autoMove) {
						this.autoMove = false;
						LaserTankEE.getGame().updatePushedPosition(dirX, dirY, dirX + unres[0],
								dirY + unres[1], this);
					}
				} else {
					this.autoMove = true;
				}
			}
			this.activateTimer(1);
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

	public final void writeArenaObject(final DataIOWriter writer) throws IOException {
		writer.writeString(this.getIdentifier());
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

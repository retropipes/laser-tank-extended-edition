/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import com.puttysoftware.lasertank.arena.Arena;
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
import com.puttysoftware.lasertank.utility.TankInventory;

final class MovingLaserTracker {
	private static boolean canMoveThere(final int sx, final int sy) {
		final var px = Game.getPlayerLocationX();
		final var py = Game.getPlayerLocationY();
		final var pz = Game.getPlayerLocationZ();
		final var m = ArenaManager.getArena();
		var zproceed = true;
		ArenaObject zo = null;
		try {
			try {
				zo = m.getCell(px + sx, py + sy, pz, Layer.LOWER_OBJECTS.ordinal());
			} catch (final ArrayIndexOutOfBoundsException ae) {
				zo = new ArenaObject(GameObjectID.WALL);
			}
		} catch (final NullPointerException np) {
			zproceed = false;
			zo = new ArenaObject(GameObjectID.WALL);
		}
		if (zproceed) {
			try {
				if (MovingLaserTracker.checkSolid(zo)) {
					return true;
				}
			} catch (final ArrayIndexOutOfBoundsException ae) {
				// Ignore
			}
		}
		return false;
	}

	private static boolean checkSolid(final ArenaObject next) {
		// Check cheats
		if (Game.getCheatStatus(Game.CHEAT_GHOSTLY)) {
			return true;
		}
		final var nextSolid = next.isConditionallySolid();
		if (!nextSolid || next.canControl()) {
			return true;
		}
		return false;
	}

	private static ArenaObject createLaserForType(final LaserType type) {
		return switch (type) {
			case GREEN -> new ArenaObject(GameObjectID.GREEN_LASER);
			case BLUE -> new ArenaObject(GameObjectID.BLUE_LASER);
			case RED -> new ArenaObject(GameObjectID.RED_LASER);
			case MISSILE -> new ArenaObject(GameObjectID.MISSILE);
			case STUNNER -> new ArenaObject(GameObjectID.STUNNER);
			case POWER -> new ArenaObject(GameObjectID.POWER_LASER);
			case VIOLET -> new ArenaObject(GameObjectID.VIOLET_LASER);
			case HEAT -> new ArenaObject(GameObjectID.HEAT_LASER);
			case ICE -> new ArenaObject(GameObjectID.ICE_LASER);
			case LIGHT -> new ArenaObject(GameObjectID.LIGHT_LASER);
			case SHADOW -> new ArenaObject(GameObjectID.SHADOW_LASER);
			default -> null;
		};
	}

	private Sound createSoundForLaserType() {
		return switch (this.lt) {
			case GREEN -> Sound.FIRE_LASER;
			case BLUE -> Sound.BLUE_LASER;
			case RED -> Sound.ANTI_FIRE;
			case MISSILE -> Sound.MISSILE;
			case STUNNER -> Sound.STUNNER;
			case POWER -> Sound.POWER_LASER;
			case VIOLET -> Sound.POWER_TURRET;
			case HEAT -> Sound.FIRE_LASER;
			case ICE -> Sound.FIRE_LASER;
			case LIGHT -> Sound.FIRE_LASER;
			case SHADOW -> Sound.FIRE_LASER;
			default -> null;
		};
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
	private ArenaObject shooter;
	private int ox, oy;
	private LaserType lt;
	private boolean res;
	private boolean laser;
	private int cumX, cumY, incX, incY;
	private ArenaObject l;

	// Constructors
	public MovingLaserTracker() {
		this.lt = LaserType.NONE;
	}

	void activateLasers(final int zx, final int zy, final int zox, final int zoy, final LaserType zlt,
			final ArenaObject zshooter) {
		this.shooter = zshooter;
		this.ox = zox;
		this.oy = zoy;
		this.lt = zlt;
		this.cumX = zx;
		this.cumY = zy;
		this.incX = zx;
		this.incY = zy;
		if (this.lt == LaserType.GREEN) {
			if (this.shooter.getID() == GameObjectID.POWER_TANK) {
				this.lt = LaserType.POWER;
			}
			ArenaManager.setDirty(true);
			Game.updateUndo(true, false, false, false, false, false, false, false, false, false);
			Game.updateScore(0, 1, 0);
			if (!Game.isReplaying()) {
				Game.updateReplay(GameAction.SHOOT, 0, 0);
			}
			this.laser = true;
			this.res = true;
		} else if (this.lt == LaserType.RED) {
			if (!Game.getCheatStatus(Game.CHEAT_INVINCIBLE)) {
				if (this.shooter.getID() == GameObjectID.POWER_TURRET) {
					this.lt = LaserType.VIOLET;
				}
				this.laser = true;
				this.res = true;
			}
		} else if (this.lt == LaserType.MISSILE) {
			ArenaManager.setDirty(true);
			Game.updateUndo(false, true, false, false, false, false, false, false, false, false);
			TankInventory.fireMissile();
			Game.updateScore(0, 0, 1);
			if (!Game.isReplaying()) {
				Game.updateReplay(GameAction.SHOOT_ALT_AMMO, 0, 0);
			}
			this.laser = true;
			this.res = true;
		} else if (this.lt == LaserType.STUNNER) {
			ArenaManager.setDirty(true);
			Game.updateUndo(false, false, true, false, false, false, false, false, false, false);
			TankInventory.fireStunner();
			Game.updateScore(0, 0, 1);
			if (!Game.isReplaying()) {
				Game.updateReplay(GameAction.SHOOT_ALT_AMMO, 0, 0);
			}
			this.laser = true;
			this.res = true;
		} else if (this.lt == LaserType.BLUE) {
			ArenaManager.setDirty(true);
			Game.updateUndo(false, false, false, false, false, true, false, false, false, false);
			TankInventory.fireBlueLaser();
			Game.updateScore(0, 0, 1);
			if (!Game.isReplaying()) {
				Game.updateReplay(GameAction.SHOOT_ALT_AMMO, 0, 0);
			}
			var snd = this.createSoundForLaserType();
			if (snd != null && snd != Sound._NONE) {
				Sounds.play(snd);
			}
			this.laser = true;
			this.res = true;
		}
	}

	void clearLastLaser() {
		final var pz = Game.getPlayerLocationZ();
		if (this.laser) {
			// Clear last laser
			try {
				ArenaManager.getArena().setVirtualCell(new ArenaObject(GameObjectID.PLACEHOLDER),
						this.ox + this.cumX - this.incX, this.oy + this.cumY - this.incY, pz, this.l.layer());
				Game.redrawArena();
			} catch (final ArrayIndexOutOfBoundsException aioobe) {
				// Ignore
			}
			Game.laserDone();
			if (this.shooter.canShoot()) {
				this.shooter.laserDoneAction();
			}
			this.laser = false;
		}
	}

	private void doLasersOnce(final boolean tracking) {
		final var g = new ArenaObject(GameObjectID.GROUND);
		final var px = Game.getPlayerLocationX();
		final var py = Game.getPlayerLocationY();
		final var pz = Game.getPlayerLocationZ();
		final var m = ArenaManager.getArena();
		ArenaObject lol = null;
		ArenaObject lou = null;
		try {
			lol = m.getCell(this.ox + this.cumX, this.oy + this.cumY, pz, Layer.LOWER_OBJECTS.ordinal());
			lou = m.getCell(this.ox + this.cumX, this.oy + this.cumY, pz, Layer.UPPER_OBJECTS.ordinal());
		} catch (final ArrayIndexOutOfBoundsException ae) {
			this.res = false;
			lol = g;
			lou = g;
		}
		if (this.res) {
			int[] resolved;
			Direction laserDir;
			this.l = MovingLaserTracker.createLaserForType(this.lt);
			if (this.lt == LaserType.MISSILE) {
				final var suffix = DirectionHelper.resolveRelative(this.incX, this.incY);
				this.l.setDirection(suffix);
			} else if (this.lt == LaserType.STUNNER) {
				// Do nothing
			} else {
				final var suffix = DirectionHelper.resolveRelativeHV(this.incX, this.incY);
				this.l.setDirection(suffix);
			}
			final var oldincX = this.incX;
			final var oldincY = this.incY;
			try {
				if (lol.canLasersPassThrough() && lou.canLasersPassThrough()) {
					m.setVirtualCell(this.l, this.ox + this.cumX, this.oy + this.cumY, pz, this.l.layer());
				}
			} catch (final ArrayIndexOutOfBoundsException aioobe) {
				// Ignore
			}
			try {
				m.setVirtualCell(new ArenaObject(GameObjectID.PLACEHOLDER), this.ox + this.cumX - this.incX,
						this.oy + this.cumY - this.incY, pz, this.l.layer());
			} catch (final ArrayIndexOutOfBoundsException aioobe) {
				// Ignore
			}
			final var oldLaserDir = this.l.getDirection();
			laserDir = oldLaserDir;
			final var laserKill = this.ox + this.cumX == px && this.oy + this.cumY == py;
			if (laserKill) {
				Game.gameOver();
				return;
			}
			var dir = lou.laserEnteredAction(this.ox + this.cumX, this.oy + this.cumY, pz, this.incX, this.incY,
					this.lt, ArenaObject.getImbuedForce(this.l.material()));
			if (dir != Direction.NONE) {
				dir = lol.laserEnteredAction(this.ox + this.cumX, this.oy + this.cumY, pz, this.incX, this.incY,
						this.lt, ArenaObject.getImbuedForce(this.l.material()));
			}
			if (dir == Direction.NONE) {
				this.res = false;
				// Clear laser, because it died
				try {
					m.setVirtualCell(new ArenaObject(GameObjectID.PLACEHOLDER), this.ox + this.cumX,
							this.oy + this.cumY, pz, this.l.layer());
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					// Ignore
				}
				return;
			}
			resolved = DirectionHelper.unresolveRelative(dir);
			var resX = resolved[0];
			var resY = resolved[1];
			laserDir = DirectionHelper.resolveRelativeHV(resX, resY);
			this.l.setDirection(laserDir);
			this.incX = resX;
			this.incY = resY;
			dir = lou.laserExitedAction(oldincX, oldincY, pz, this.incX, this.incY, this.lt);
			if (dir != Direction.NONE) {
				dir = lol.laserExitedAction(oldincX, oldincY, pz, this.incX, this.incY, this.lt);
			}
			if (dir == Direction.NONE) {
				this.res = false;
				// Clear laser, because it died
				try {
					m.setVirtualCell(new ArenaObject(GameObjectID.PLACEHOLDER), this.ox + this.cumX,
							this.oy + this.cumY, pz, this.l.layer());
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					// Ignore
				}
				return;
			}
			resolved = DirectionHelper.unresolveRelative(dir);
			resX = resolved[0];
			resY = resolved[1];
			laserDir = DirectionHelper.resolveRelativeHV(resX, resY);
			this.l.setDirection(laserDir);
			this.incX = resX;
			this.incY = resY;
			if (m.isVerticalWraparoundEnabled()) {
				this.cumX = MovingLaserTracker.normalizeColumn(this.cumX + this.incX, Arena.getMinColumns());
			} else {
				this.cumX += this.incX;
			}
			if (m.isHorizontalWraparoundEnabled()) {
				this.cumY = MovingLaserTracker.normalizeRow(this.cumY + this.incY, Arena.getMinRows());
			} else {
				this.cumY += this.incY;
			}
			if (oldLaserDir != laserDir && tracking) {
				try {
					m.setVirtualCell(new ArenaObject(GameObjectID.PLACEHOLDER), this.ox + this.cumX - this.incX,
							this.oy + this.cumY - this.incY, pz, this.l.layer());
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					// Ignore
				}
				if (m.isVerticalWraparoundEnabled()) {
					this.cumX = MovingLaserTracker.normalizeColumn(this.cumX + this.incX, Arena.getMinColumns());
				} else {
					this.cumX += this.incX;
				}
				if (m.isHorizontalWraparoundEnabled()) {
					this.cumY = MovingLaserTracker.normalizeColumn(this.cumY + this.incY, Arena.getMinColumns());
				} else {
					this.cumY += this.incY;
				}
			}
		}
		Game.redrawArena();
	}

	boolean isChecking() {
		return this.res;
	}

	boolean isTracking() {
		return this.laser;
	}

	void trackPart1(final boolean tracking) {
		if (this.laser && this.res) {
			this.doLasersOnce(tracking);
		}
	}

	boolean trackPart2(final int nsx, final int nsy, final boolean nMover) {
		var sx = nsx;
		var sy = nsy;
		var mover = nMover;
		if (!this.res && this.laser) {
			if (Game.getTank().getSavedObject().movesTanks(Game.getTank().getSavedObject().getDirection())) {
				final var dir = Game.getTank().getSavedObject().getDirection();
				final var unres = DirectionHelper.unresolveRelative(dir);
				sx = unres[0];
				sy = unres[1];
				mover = true;
			}
			if (mover && !MovingLaserTracker.canMoveThere(sx, sy)) {
				MLOTask.activateAutomaticMovement();
			}
			this.clearLastLaser();
		}
		return mover;
	}
}

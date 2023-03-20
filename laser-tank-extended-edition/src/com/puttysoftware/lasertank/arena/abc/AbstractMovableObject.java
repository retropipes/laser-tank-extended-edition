/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import java.io.IOException;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.objects.Empty;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.utility.ArenaObjectList;

public abstract class AbstractMovableObject extends ArenaObject {
	// Fields
	private boolean waitingOnTunnel;

	// Constructors
	protected AbstractMovableObject() {
		super();
		this.setSavedObject(new Empty());
		this.waitingOnTunnel = false;
		this.addType(GameType.MOVABLE);
	}

	@Override
	public ArenaObject clone() {
		final var copy = (AbstractMovableObject) super.clone();
		if (this.getSavedObject() != null) {
			copy.setSavedObject(this.getSavedObject().clone());
		}
		return copy;
	}

	@Override
	public boolean doLasersPassThrough() {
		return false;
	}

	@Override
	public int getCustomFormat() {
		return ArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE;
	}

	@Override
	public int getCustomProperty(final int propID) {
		return ArenaObject.DEFAULT_CUSTOM_VALUE;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final LaserType laserType, final int forceUnits) {
		final var app = LaserTankEE.getApplication();
		if (!this.canMove() || (forceUnits < this.getMinimumReactionForce())) {
			// Not enough force
			return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
		try {
			final var mof = app.getArenaManager().getArena().getCell(locX + dirX, locY + dirY, locZ, this.getLayer());
			final var mor = app.getArenaManager().getArena().getCell(locX - dirX, locY - dirY, locZ, this.getLayer());
			if (this.getMaterial() == Material.MAGNETIC) {
				if (laserType == LaserType.BLUE && mof != null
						&& (mof.canControl() || !mof.isSolid())) {
					app.getGameManager().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
				} else if (mor != null && (mor.canControl() || !mor.isSolid())) {
					app.getGameManager().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
				} else {
					// Object doesn't react to this type of laser
					return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
				}
			} else {
				if (laserType == LaserType.BLUE && mor != null
						&& (mor.canControl() || !mor.isSolid())) {
					app.getGameManager().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
				} else if (mof != null && (mof.canControl() || !mof.isSolid())) {
					app.getGameManager().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
				} else {
					// Object doesn't react to this type of laser
					return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
				}
			}
			this.playSoundHook();
		} catch (final ArrayIndexOutOfBoundsException aioobe) {
			// Object can't go that way
			return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
		return Direction.NONE;
	}

	public abstract void playSoundHook();

	@Override
	protected ArenaObject readArenaObjectHookG2(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		this.setSavedObject(ArenaObjectList.readArenaObjectG2(reader, formatVersion));
		return this;
	}

	@Override
	protected ArenaObject readArenaObjectHookG3(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		this.setSavedObject(ArenaObjectList.readArenaObjectG3(reader, formatVersion));
		return this;
	}

	@Override
	protected ArenaObject readArenaObjectHookG4(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		this.setSavedObject(ArenaObjectList.readArenaObjectG4(reader, formatVersion));
		return this;
	}

	@Override
	protected ArenaObject readArenaObjectHookG5(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		this.setSavedObject(ArenaObjectList.readArenaObjectG5(reader, formatVersion));
		return this;
	}

	@Override
	protected ArenaObject readArenaObjectHookG6(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		this.setSavedObject(ArenaObjectList.readArenaObjectG6(reader, formatVersion));
		return this;
	}

	@Override
	public void setCustomProperty(final int propID, final int value) {
		// Do nothing
	}

	public final void setWaitingOnTunnel(final boolean value) {
		this.waitingOnTunnel = value;
	}

	public final boolean waitingOnTunnel() {
		return this.waitingOnTunnel;
	}

	@Override
	protected void writeArenaObjectHook(final DataIOWriter writer) throws IOException {
		this.getSavedObject().writeArenaObject(writer);
	}
}
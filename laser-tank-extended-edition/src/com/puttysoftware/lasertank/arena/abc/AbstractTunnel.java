/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.objects.Tunnel;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.GameColorHelper;
import com.puttysoftware.lasertank.index.GameColor;
import com.puttysoftware.lasertank.index.GameType;

public abstract class AbstractTunnel extends AbstractArenaObject {
	// Fields
	private final static boolean[] tunnelsFull = new boolean[GameColorHelper.COUNT];
	private final static int SCAN_RADIUS = 24;

	// Static methods
	public static void checkTunnels() {
		for (var x = 0; x < GameColorHelper.COUNT; x++) {
			AbstractTunnel.checkTunnelsOfColor(GameColorHelper.fromOrdinal(x));
		}
	}

	private static void checkTunnelsOfColor(final GameColor color) {
		final var app = LaserTankEE.getApplication();
		final var tx = app.getGameManager().getPlayerManager().getPlayerLocationX();
		final var ty = app.getGameManager().getPlayerManager().getPlayerLocationY();
		final var pgrmdest = app.getArenaManager().getArena().circularScanTunnel(0, 0, 0, AbstractTunnel.SCAN_RADIUS,
				tx, ty, AbstractTunnel.getTunnelOfColor(color), false);
		if (pgrmdest != null) {
			AbstractTunnel.tunnelsFull[color.ordinal()] = false;
		} else {
			AbstractTunnel.tunnelsFull[color.ordinal()] = true;
		}
	}

	private static AbstractTunnel getTunnelOfColor(final GameColor color) {
		return new Tunnel(color);
	}

	public static boolean tunnelsFull(final GameColor color) {
		return AbstractTunnel.tunnelsFull[color.ordinal()];
	}

	// Constructors
	protected AbstractTunnel() {
		super();
		this.addType(GameType.TUNNEL);
	}

	@Override
	public int getCustomProperty(final int propID) {
		return AbstractArenaObject.DEFAULT_CUSTOM_VALUE;
	}

	// Scriptability
	@Override
	public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
		final var app = LaserTankEE.getApplication();
		final var tx = app.getGameManager().getPlayerManager().getPlayerLocationX();
		final var ty = app.getGameManager().getPlayerManager().getPlayerLocationY();
		final var pgrmdest = app.getArenaManager().getArena().circularScanTunnel(dirX, dirY, dirZ,
				AbstractTunnel.SCAN_RADIUS, tx, ty, AbstractTunnel.getTunnelOfColor(this.getColor()), true);
		if (pgrmdest != null) {
			app.getGameManager().updatePositionAbsoluteNoEvents(pgrmdest[0], pgrmdest[1], pgrmdest[2]);
			Sounds.play(Sound.WARP_TANK);
		}
	}

	@Override
	public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
		final var app = LaserTankEE.getApplication();
		final var tx = app.getGameManager().getPlayerManager().getPlayerLocationX();
		final var ty = app.getGameManager().getPlayerManager().getPlayerLocationY();
		final var color = this.getColor();
		final var pgrmdest = app.getArenaManager().getArena().circularScanTunnel(x, y, z, AbstractTunnel.SCAN_RADIUS,
				tx, ty, AbstractTunnel.getTunnelOfColor(this.getColor()), false);
		if (pgrmdest != null) {
			AbstractTunnel.tunnelsFull[color.ordinal()] = false;
			app.getGameManager().updatePushedIntoPositionAbsolute(pgrmdest[0], pgrmdest[1], pgrmdest[2], x, y, z,
					pushed, this);
			Sounds.play(Sound.WARP_OBJECT);
		} else {
			AbstractTunnel.tunnelsFull[color.ordinal()] = true;
			pushed.setWaitingOnTunnel(true);
		}
		return false;
	}

	@Override
	public void setCustomProperty(final int propID, final int value) {
		// Do nothing
	}
}

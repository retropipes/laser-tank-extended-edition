/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import com.puttysoftware.diane.storage.NumberStorage;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.helper.Players;

final class PlayerLocationManager {
	// Fields
	private int playerInstance;
	private NumberStorage playerData;
	private NumberStorage savedPlayerData;
	private NumberStorage savedRemoteData;

	// Constructors
	PlayerLocationManager() {
		this.playerInstance = 0;
		this.playerData = new NumberStorage(Players.DIMENSIONS, Players.COUNT);
		this.playerData.fill(-1);
		this.savedPlayerData = new NumberStorage(Players.DIMENSIONS, Players.COUNT);
		this.savedPlayerData.fill(-1);
		this.savedRemoteData = new NumberStorage(Players.DIMENSIONS, Players.COUNT);
		this.savedRemoteData.fill(-1);
	}

	// Methods
	int getActivePlayerNumber() {
		return this.playerInstance;
	}

	int getPlayerLocationX() {
		return this.playerData.getCell(1, this.playerInstance);
	}

	int getPlayerLocationY() {
		return this.playerData.getCell(0, this.playerInstance);
	}

	int getPlayerLocationZ() {
		return this.playerData.getCell(2, this.playerInstance);
	}

	void resetPlayerLocation() {
		final var a = LaserTankEE.getArenaManager().getArena();
		for (var pi = 0; pi < Players.COUNT; pi++) {
			final var found = a.findPlayer(pi);
			if (found != null) {
				final var valX = found[0];
				final var valY = found[1];
				final var valZ = found[2];
				this.initPlayerLocation(valX, valY, valZ, pi);
			}
		}
	}

	void setActivePlayerNumber(final int value) {
		this.playerInstance = value;
	}

	void setPlayerLocation(final int valX, final int valY, final int valZ) {
		this.setPlayerLocationX(valX);
		this.setPlayerLocationY(valY);
		this.setPlayerLocationZ(valZ);
	}

	void togglePlayerInstance() {
		var doesNotExist = true;
		while (doesNotExist) {
			this.playerInstance++;
			if (this.playerInstance >= Players.COUNT) {
				this.playerInstance = 0;
			}
			final var px = this.getPlayerLocationX();
			final var py = this.getPlayerLocationY();
			final var pz = this.getPlayerLocationZ();
			if (px != -1 && py != -1 && pz != -1) {
				doesNotExist = false;
			}
		}
	}

	void offsetPlayerLocationX(final int val) {
		this.playerData.setCell(this.getPlayerLocationX() + val, 1, this.playerInstance);
	}

	void offsetPlayerLocationY(final int val) {
		this.playerData.setCell(this.getPlayerLocationY() + val, 0, this.playerInstance);
	}

	void restorePlayerLocation() {
		this.playerData = new NumberStorage(this.savedPlayerData);
	}

	void restoreRemoteLocation() {
		this.playerData = new NumberStorage(this.savedRemoteData);
	}

	void savePlayerLocation() {
		this.savedPlayerData = new NumberStorage(this.playerData);
	}

	void saveRemoteLocation() {
		this.savedRemoteData = new NumberStorage(this.playerData);
	}

	private void initPlayerLocation(final int valX, final int valY, final int valZ, final int pi) {
		this.playerData.setCell(valX, 1, pi);
		this.playerData.setCell(valY, 0, pi);
		this.playerData.setCell(valZ, 2, pi);
	}

	private void setPlayerLocationX(final int val) {
		this.playerData.setCell(val, 1, this.playerInstance);
	}

	private void setPlayerLocationY(final int val) {
		this.playerData.setCell(val, 0, this.playerInstance);
	}

	private void setPlayerLocationZ(final int val) {
		this.playerData.setCell(val, 2, this.playerInstance);
	}
}

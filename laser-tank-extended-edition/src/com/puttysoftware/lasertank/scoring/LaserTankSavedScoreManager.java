/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.scoring;

import java.io.IOException;

import com.puttysoftware.lasertank.fileio.GameIODataReader;
import com.puttysoftware.lasertank.fileio.GameIODataWriter;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

public class LaserTankSavedScoreManager extends LaserTankScores {
	// Fields
	private final String scoresFilename;

	// Constructors
	public LaserTankSavedScoreManager(final int length, final LaserTankScoreSortOrder laserTankScoreSortOrder,
			final String customTitle, final String scoresFile) {
		super(length, laserTankScoreSortOrder, customTitle);
		this.scoresFilename = scoresFile;
		try {
			this.readScoresFile();
		} catch (final IOException io) {
			// Do nothing
		}
	}

	// Methods
	@Override
	public boolean add(final long newMoves, final long newShots) {
		final var success = super.add(newMoves, newShots);
		try {
			this.writeScoresFile();
		} catch (final IOException io) {
			AppTaskManager.logWarningDirectly(io);
		}
		return success;
	}

	@Override
	public boolean add(final long newMoves, final long newShots, final String newName) {
		final var success = super.add(newMoves, newShots, newName);
		try {
			this.writeScoresFile();
		} catch (final IOException io) {
			AppTaskManager.logWarningDirectly(io);
		}
		return success;
	}

	private void readScoresFile() throws IOException {
		try (var gio = new GameIODataReader(this.scoresFilename)) {
			this.table = LaserTankSortedScoreTable.load(gio);
		}
	}

	private void writeScoresFile() throws IOException {
		try (var gio = new GameIODataWriter(this.scoresFilename)) {
			this.table.save(gio);
		}
	}
}

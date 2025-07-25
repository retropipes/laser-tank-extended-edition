/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.tasks;

import java.io.File;

import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.fileio.utility.DirectoryUtilities;

class CleanupTask {
	static void cleanUp() {
		try {
			final var dirToDelete = new File(Arena.getArenaTempFolder());
			DirectoryUtilities.removeDirectory(dirToDelete);
		} catch (final Throwable t) {
			// Ignore
		}
	}

	private CleanupTask() {
		// Do nothing
	}
}

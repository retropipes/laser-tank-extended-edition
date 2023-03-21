/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.v4;

import java.io.FileInputStream;

import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaData;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

class V4File {
	static void loadOldFile(final Arena a, final FileInputStream file) throws InvalidArenaException {
		ArenaData t = null;
		var levelCount = 0;
		do {
			a.switchLevel(levelCount);
			t = V4FileLevel.loadAndConvert(file, a);
			if (t != null) {
				levelCount++;
				a.setData(t, levelCount);
				final var found = a.findPlayer(1);
				if (found == null) {
					throw new InvalidArenaException(Strings.loadError(ErrorString.TANK_LOCATION));
				}
				a.setStartColumn(0, found[0]);
				a.setStartRow(0, found[1]);
				a.setStartFloor(0, found[2]);
				a.save();
				a.switchLevel(levelCount);
			}
		} while (t != null);
	}

	private V4File() {
		// Do nothing
	}
}

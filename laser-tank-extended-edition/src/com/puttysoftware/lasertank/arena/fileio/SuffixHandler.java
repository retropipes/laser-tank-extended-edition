/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.fileio;

import java.io.IOException;

import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.fileio.DataIOWriter;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.GameFormatHelper;
import com.puttysoftware.lasertank.index.GameFormat;

public class SuffixHandler implements DataIOSuffixHandler {
    @Override
    public void readSuffix(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
	if (GameFormatHelper.isValidG1(formatVersion)) {
	    Game.get().loadGameHookG1(reader);
	} else if (GameFormatHelper.isValidG2(formatVersion)) {
	    Game.get().loadGameHookG2(reader);
	} else if (GameFormatHelper.isValidG3(formatVersion)) {
	    Game.get().loadGameHookG3(reader);
	} else if (GameFormatHelper.isValidG4(formatVersion)) {
	    Game.get().loadGameHookG4(reader);
	} else if (GameFormatHelper.isValidG5(formatVersion)) {
	    Game.get().loadGameHookG5(reader);
	} else if (GameFormatHelper.isValidG6(formatVersion)) {
	    Game.get().loadGameHookG6(reader);
	}
    }

    @Override
    public void writeSuffix(final DataIOWriter writer) throws IOException {
	Game.get().saveGameHook(writer);
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena;

import java.io.IOException;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.helper.GameFormatHelper;
import com.puttysoftware.lasertank.index.GameFormat;

public class SuffixHandler implements DataIOSuffixHandler {
    @Override
    public void readSuffix(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
	if (GameFormatHelper.isValidG1(formatVersion)) {
	    LaserTankEE.getApplication().getGameManager().loadGameHookG1(reader);
	} else if (GameFormatHelper.isValidG2(formatVersion)) {
	    LaserTankEE.getApplication().getGameManager().loadGameHookG2(reader);
	} else if (GameFormatHelper.isValidG3(formatVersion)) {
	    LaserTankEE.getApplication().getGameManager().loadGameHookG3(reader);
	} else if (GameFormatHelper.isValidG4(formatVersion)) {
	    LaserTankEE.getApplication().getGameManager().loadGameHookG4(reader);
	} else if (GameFormatHelper.isValidG5(formatVersion)) {
	    LaserTankEE.getApplication().getGameManager().loadGameHookG5(reader);
	} else if (GameFormatHelper.isValidG6(formatVersion)) {
	    LaserTankEE.getApplication().getGameManager().loadGameHookG6(reader);
	}
    }

    @Override
    public void writeSuffix(final DataIOWriter writer) throws IOException {
	LaserTankEE.getApplication().getGameManager().saveGameHook(writer);
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.fileio;

import java.io.IOException;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.lasertank.index.GameFormat;

public interface DataIOSuffixHandler {
    void readSuffix(DataIOReader reader, GameFormat formatVersion) throws IOException;

    void writeSuffix(DataIOWriter writer) throws IOException;
}

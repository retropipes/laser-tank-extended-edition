/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.fileio;

import java.io.IOException;

import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.fileio.DataIOWriter;
import com.puttysoftware.lasertank.helper.GameFormatHelper;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;

public class PrefixHandler implements DataIOPrefixHandler {
    private static final byte FORMAT_VERSION = (byte) GameFormatHelper.FORMAT_LATEST.ordinal();

    private static boolean checkFormatVersion(final GameFormat version) {
        if (version.ordinal() > PrefixHandler.FORMAT_VERSION) {
            return false;
        }
        return true;
    }

    private static GameFormat readFormatVersion(final DataIOReader reader) throws IOException {
        return GameFormatHelper.fromOrdinal(reader.readByte());
    }

    private static void writeFormatVersion(final DataIOWriter writer) throws IOException {
        writer.writeByte(PrefixHandler.FORMAT_VERSION);
    }

    @Override
    public GameFormat readPrefix(final DataIOReader reader) throws IOException {
        final var formatVer = PrefixHandler.readFormatVersion(reader);
        final var res = PrefixHandler.checkFormatVersion(formatVer);
        if (!res) {
            throw new IOException(Strings.loadError(ErrorString.UNKNOWN_ARENA_FORMAT));
        }
        return formatVer;
    }

    @Override
    public void writePrefix(final DataIOWriter writer) throws IOException {
        PrefixHandler.writeFormatVersion(writer);
    }
}

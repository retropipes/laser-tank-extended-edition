package com.puttysoftware.lasertank.arena.fileio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

public class ProtectionWrapper {
    // Constants
    private static final int BLOCK_MULTIPLIER = 16;

    private static char[] getTransform() {
	return CommonDialogs.showPasswordInputDialog(Strings.loadDialog(DialogString.PROTECTION_PROMPT),
		Strings.loadDialog(DialogString.PROTECTION_TITLE));
    }

    public static void protect(final File src, final File dst) {
	try (var in = new FileInputStream(src); var out = new FileOutputStream(dst)) {
	    final var transform = ProtectionWrapper.getTransform();
	    if (transform == null) {
		throw new ProtectionCancelException();
	    }
	    final var buf = new byte[transform.length * ProtectionWrapper.BLOCK_MULTIPLIER];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		for (var x = 0; x < buf.length; x++) {
		    buf[x] += transform[x % transform.length];
		}
		out.write(buf, 0, len);
	    }
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }

    public static void unprotect(final File src, final File dst) {
	try (var in = new FileInputStream(src); var out = new FileOutputStream(dst)) {
	    final var transform = ProtectionWrapper.getTransform();
	    if (transform == null) {
		throw new ProtectionCancelException();
	    }
	    final var buf = new byte[transform.length * ProtectionWrapper.BLOCK_MULTIPLIER];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		for (var x = 0; x < buf.length; x++) {
		    buf[x] -= transform[x % transform.length];
		}
		out.write(buf, 0, len);
	    }
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }

    private ProtectionWrapper() {
	// Do nothing
    }
}

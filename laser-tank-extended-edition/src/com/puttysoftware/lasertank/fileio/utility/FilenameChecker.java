/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.fileio.utility;

public class FilenameChecker {
    public static boolean isFilenameOK(final String filename) {
	if (filename.contains("/") || filename.contains("?") || filename.contains("<") || filename.contains(">")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    return false;
	}
	if (filename.contains("\\") || filename.contains(":") || filename.contains("*") || filename.contains("|")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    return false;
	}
	if (filename.contains("\"") || "con".equals(filename) || "nul".equals(filename) || "prn".equals(filename)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    return false;
	}
	if (filename.length() == 4 && filename.matches("com[1-9]") //$NON-NLS-1$
		|| filename.length() == 4 && filename.matches("lpt[1-9]")) { //$NON-NLS-1$
	    return false;
	}
	return true;
    }

    // Private constructor
    private FilenameChecker() {
	// Do nothing
    }
}

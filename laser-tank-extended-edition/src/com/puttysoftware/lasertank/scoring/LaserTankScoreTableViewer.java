/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.scoring;

import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

public final class LaserTankScoreTableViewer {
    // Private constants
    private static final int ENTRIES_PER_PAGE = 10;

    // Methods
    public static void view(final LaserTankScoreTable table, final String customTitle) {
	final var msgBuilder = new StringBuilder();
	String msg = null;
	String title = null;
	if (customTitle == null || customTitle.isEmpty()) {
	    title = Strings.loadDialog(DialogString.SCORES_HEADER);
	} else {
	    title = customTitle;
	}
	int x;
	int y;
	for (x = 0; x < table.getLength(); x += LaserTankScoreTableViewer.ENTRIES_PER_PAGE) {
	    msg = Strings.loadCommon(CommonString.EMPTY);
	    for (y = 1; y <= LaserTankScoreTableViewer.ENTRIES_PER_PAGE; y++) {
		try {
		    msgBuilder.append(Strings.loadDialog(DialogString.SCORE_ENTRY, table.getEntryName(x + y - 1),
			    table.getEntryMoves(x + y - 1), table.getEntryShots(x + y - 1)));
		    msgBuilder.append(Strings.loadCommon(CommonString.NEWLINE));
		} catch (final ArrayIndexOutOfBoundsException ae) {
		    // Do nothing
		}
	    }
	    msg = msgBuilder.toString();
	    // Strip final newline character
	    msg = msg.substring(0, msg.length() - 1);
	    CommonDialogs.showTitledDialog(msg, title);
	}
    }

    // Private constructor
    private LaserTankScoreTableViewer() {
	// Do nothing
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.utility;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

public class CustomDialogs {
	public static int showDeadDialog() {
		return CommonDialogs.showCustomDialogWithDefault(Strings.loadDialog(DialogString.DEAD_MESSAGE),
				Strings.loadDialog(DialogString.DEAD_TITLE),
				new String[] { Strings.loadDialog(DialogString.UNDO_BUTTON),
						Strings.loadDialog(DialogString.RESTART_BUTTON), Strings.loadDialog(DialogString.END_BUTTON) },
				Strings.loadDialog(DialogString.UNDO_BUTTON));
	}

	private CustomDialogs() {
		// Do nothing
	}
}
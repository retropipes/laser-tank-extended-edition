/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.cheat;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.GameString;
import com.puttysoftware.lasertank.locale.Strings;

public final class Cheats {
    // Fields
    private final CheatList cheatCache;
    private int cheatCount;

    // Constructor
    public Cheats() {
	this.cheatCache = new CheatList();
	this.loadCheatCache();
    }

    public String enterCheat() {
	final var userInput = CommonDialogs.showTextInputDialog(Strings.loadGame(GameString.CHEAT_PROMPT),
		Strings.loadDialog(DialogString.CHEATS));
	if (userInput == null) {
	    return null;
	}
	final var index = this.cheatCache.indexOf(userInput.toLowerCase());
	if (index == -1) {
	    CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.INVALID_CHEAT),
		    Strings.loadDialog(DialogString.CHEATS));
	    return null;
	}
	final var value = CommonDialogs.showConfirmDialog(Strings.loadGame(GameString.CHEAT_ACTION),
		Strings.loadDialog(DialogString.CHEATS));
	if (value == JOptionPane.YES_OPTION) {
	    return Strings.loadGame(GameString.ENABLE_CHEAT) + Strings.loadCommon(CommonString.SPACE)
		    + userInput.toLowerCase();
	} else {
	    return Strings.loadGame(GameString.DISABLE_CHEAT) + Strings.loadCommon(CommonString.SPACE)
		    + userInput.toLowerCase();
	}
    }

    public int getCheatCount() {
	return this.cheatCount;
    }

    // Methods
    private void loadCheatCache() {
	final var instant = ResourceBundle.getBundle("locale.cheats_instant"); //$NON-NLS-1$
	final var toggle = ResourceBundle.getBundle("locale.cheats_toggle"); //$NON-NLS-1$
	final var iLimit = Cheat.instantCount();
	for (var i = 0; i < iLimit; i++) {
	    final var code = instant.getString(Integer.toString(i));
	    this.cheatCache.add(new InstantCheat(code, CheatEffect.values()[i]));
	}
	final var tLimit = Cheat.count();
	for (var t = iLimit; t < tLimit; t++) {
	    final var code = toggle.getString(Integer.toString(t));
	    this.cheatCache.add(new ToggleCheat(code, CheatEffect.values()[t]));
	}
	this.cheatCount = tLimit;
    }

    public int queryCheatCache(final String query) {
	return this.cheatCache.indexOf(query);
    }
}

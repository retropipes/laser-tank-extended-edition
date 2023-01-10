/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena;

import java.io.File;
import java.io.FileNotFoundException;

import com.puttysoftware.diane.fileio.utility.ZipUtilities;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.datatype.FileExtensions;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.MessageString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public class SaveTask extends Thread {
    private static boolean hasExtension(final String s) {
	String ext = null;
	final var i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(i + 1).toLowerCase();
	}
	if (ext == null) {
	    return false;
	}
	return true;
    }

    // Fields
    private String filename;
    private final boolean saveProtected;
    private final boolean isSavedGame;

    // Constructors
    public SaveTask(final String file, final boolean saved, final boolean protect) {
	this.filename = file;
	this.isSavedGame = saved;
	this.saveProtected = protect;
	this.setName(GlobalStrings.loadUntranslated(UntranslatedString.NEW_AG_SAVER_NAME));
    }

    @Override
    public void run() {
	final var app = LaserTankEE.getApplication();
	var success = true;
	// filename check
	final var hasExtension = SaveTask.hasExtension(this.filename);
	if (!hasExtension) {
	    if (this.isSavedGame) {
		this.filename += FileExtensions.getGameExtensionWithPeriod();
	    } else {
		this.filename += FileExtensions.getArenaExtensionWithPeriod();
	    }
	}
	final var arenaFile = new File(this.filename);
	final var tempLock = new File(
		Arena.getArenaTempFolder() + GlobalStrings.loadUntranslated(UntranslatedString.LOCK_TEMP_FILE));
	try {
	    // Set prefix handler
	    app.getArenaManager().getArena().setPrefixHandler(new PrefixHandler());
	    // Set suffix handler
	    if (this.isSavedGame) {
		app.getArenaManager().getArena().setSuffixHandler(new SuffixHandler());
	    } else {
		app.getArenaManager().getArena().setSuffixHandler(null);
	    }
	    app.getArenaManager().getArena().writeArena();
	    if (this.saveProtected) {
		ZipUtilities.zipDirectory(new File(app.getArenaManager().getArena().getBasePath()), tempLock);
		// Protect the arena
		ProtectionWrapper.protect(tempLock, arenaFile);
		tempLock.delete();
		app.getArenaManager().setArenaProtected(true);
	    } else {
		ZipUtilities.zipDirectory(new File(app.getArenaManager().getArena().getBasePath()), arenaFile);
		app.getArenaManager().setArenaProtected(false);
	    }
	} catch (final FileNotFoundException fnfe) {
	    if (this.isSavedGame) {
		CommonDialogs.showDialog(Strings.loadDialog(DialogString.GAME_SAVING_FAILED));
	    } else {
		CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_SAVING_FAILED));
	    }
	    success = false;
	} catch (final ProtectionCancelException pce) {
	    success = false;
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
	if (this.isSavedGame) {
	    LaserTankEE.getApplication().showMessage(Strings.loadMessage(MessageString.GAME_SAVED));
	} else {
	    LaserTankEE.getApplication().showMessage(Strings.loadMessage(MessageString.ARENA_SAVED));
	}
	app.getArenaManager().handleDeferredSuccess(success);
    }
}

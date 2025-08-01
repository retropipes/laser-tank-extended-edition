/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.fileio;

import java.io.File;
import java.io.FileNotFoundException;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.fileio.utility.ZipUtilities;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.MessageString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.tasks.AppTaskManager;
import com.puttysoftware.lasertank.utility.FileExtensions;

public class SaveTask implements Runnable {
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
	}

	@Override
	public void run() {
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
			ArenaManager.getArena().setPrefixHandler(new PrefixHandler());
			// Set suffix handler
			if (this.isSavedGame) {
				ArenaManager.getArena().setSuffixHandler(new SuffixHandler());
			} else {
				ArenaManager.getArena().setSuffixHandler(null);
			}
			ArenaManager.getArena().writeArena();
			if (this.saveProtected) {
				ZipUtilities.zipDirectory(new File(ArenaManager.getArena().getBasePath()), tempLock);
				// Protect the arena
				ProtectionWrapper.protect(tempLock, arenaFile);
				tempLock.delete();
				ArenaManager.setArenaProtected(true);
			} else {
				ZipUtilities.zipDirectory(new File(ArenaManager.getArena().getBasePath()), arenaFile);
				ArenaManager.setArenaProtected(false);
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
			AppTaskManager.error(ex);
		}
		if (this.isSavedGame) {
			LaserTankEE.showMessage(Strings.loadMessage(MessageString.GAME_SAVED));
		} else {
			LaserTankEE.showMessage(Strings.loadMessage(MessageString.ARENA_SAVED));
		}
		ArenaManager.handlePostFileLoad(success);
	}
}

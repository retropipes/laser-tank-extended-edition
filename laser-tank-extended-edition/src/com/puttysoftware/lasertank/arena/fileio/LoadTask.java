/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.fileio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.fileio.utility.ZipUtilities;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

public class LoadTask extends Thread {
	// Fields
	private final String filename;
	private final boolean isSavedGame;
	private final JFrame loadFrame;
	private final boolean arenaProtected;

	// Constructors
	public LoadTask(final String file, final boolean saved, final boolean protect) {
		JProgressBar loadBar;
		this.filename = file;
		this.isSavedGame = saved;
		this.arenaProtected = protect;
		this.setName(GlobalStrings.loadUntranslated(UntranslatedString.NEW_AG_LOADER_NAME));
		this.loadFrame = new JFrame(Strings.loadDialog(DialogString.LOADING));
		loadBar = new JProgressBar();
		loadBar.setIndeterminate(true);
		this.loadFrame.getContentPane().add(loadBar);
		this.loadFrame.setResizable(false);
		this.loadFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.loadFrame.pack();
	}

	// Methods
	@Override
	public void run() {
		this.loadFrame.setVisible(true);
		if (this.isSavedGame) {
			LaserTankEE.getGame().setSavedGameFlag(true);
		} else {
			LaserTankEE.getGame().setSavedGameFlag(false);
		}
		try {
			final var arenaFile = new File(this.filename);
			final var tempLock = new File(
					Arena.getArenaTempFolder() + GlobalStrings.loadUntranslated(UntranslatedString.LOCK_TEMP_FILE));
			var gameArena = ArenaManager.createArena();
			if (this.arenaProtected) {
				// Attempt to unprotect the file
				ProtectionWrapper.unprotect(arenaFile, tempLock);
				try {
					ZipUtilities.unzipDirectory(tempLock, new File(gameArena.getBasePath()));
					LaserTankEE.getArenaManager().setArenaProtected(true);
				} catch (final ZipException ze) {
					CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.BAD_PROTECTION_KEY),
							Strings.loadError(ErrorString.PROTECTION));
					LaserTankEE.getArenaManager().handleDeferredSuccess(false);
					return;
				} finally {
					tempLock.delete();
				}
			} else {
				ZipUtilities.unzipDirectory(arenaFile, new File(gameArena.getBasePath()));
				LaserTankEE.getArenaManager().setArenaProtected(false);
			}
			// Set prefix handler
			gameArena.setPrefixHandler(new PrefixHandler());
			// Set suffix handler
			if (this.isSavedGame) {
				gameArena.setSuffixHandler(new SuffixHandler());
			} else {
				gameArena.setSuffixHandler(null);
			}
			gameArena = gameArena.readArena();
			if (gameArena == null) {
				throw new InvalidArenaException(Strings.loadError(ErrorString.UNKNOWN_OBJECT));
			}
			LaserTankEE.getArenaManager().setArena(gameArena);
			final var playerExists = gameArena.doesPlayerExist(0);
			if (playerExists) {
				LaserTankEE.getGame().getPlayerManager().resetPlayerLocation();
			}
			if (!this.isSavedGame) {
				gameArena.save();
			}
			// Final cleanup
			final var lum = LaserTankEE.getArenaManager().getLastUsedArena();
			final var lug = LaserTankEE.getArenaManager().getLastUsedGame();
			LaserTankEE.getArenaManager().clearLastUsedFilenames();
			if (this.isSavedGame) {
				LaserTankEE.getArenaManager().setLastUsedGame(lug);
			} else {
				LaserTankEE.getArenaManager().setLastUsedArena(lum);
			}
			LaserTankEE.getEditor().arenaChanged();
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.GAME_LOADING_SUCCESS));
			} else {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_SUCCESS));
			}
			LaserTankEE.getArenaManager().handleDeferredSuccess(true);
		} catch (final FileNotFoundException fnfe) {
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.GAME_LOADING_FAILED));
			} else {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_FAILED));
			}
			LaserTankEE.getArenaManager().handleDeferredSuccess(false);
		} catch (final ProtectionCancelException pce) {
			LaserTankEE.getArenaManager().handleDeferredSuccess(false);
		} catch (final IOException ie) {
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.GAME_LOADING_FAILED));
			} else {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_FAILED));
			}
			LaserTankEE.logWarning(ie);
			LaserTankEE.getArenaManager().handleDeferredSuccess(false);
		} catch (final Exception ex) {
			LaserTankEE.logError(ex);
		} finally {
			this.loadFrame.setVisible(false);
		}
	}
}
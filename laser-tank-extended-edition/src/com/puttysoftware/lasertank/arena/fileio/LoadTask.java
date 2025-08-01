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

import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.fileio.utility.ZipUtilities;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.tasks.AppTaskManager;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

public class LoadTask implements Runnable {
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
			Game.setSavedGameFlag(true);
		} else {
			Game.setSavedGameFlag(false);
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
					ArenaManager.setArenaProtected(true);
				} catch (final ZipException ze) {
					CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.BAD_PROTECTION_KEY),
							Strings.loadError(ErrorString.PROTECTION));
					ArenaManager.handlePostFileLoad(false);
					return;
				} finally {
					tempLock.delete();
				}
			} else {
				ZipUtilities.unzipDirectory(arenaFile, new File(gameArena.getBasePath()));
				ArenaManager.setArenaProtected(false);
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
			ArenaManager.setArena(gameArena);
			final var playerExists = gameArena.doesPlayerExist(0);
			if (playerExists) {
				Game.resetPlayerLocation();
			}
			if (!this.isSavedGame) {
				gameArena.save();
			}
			// Final cleanup
			final var lum = ArenaManager.getLastUsedArena();
			final var lug = ArenaManager.getLastUsedGame();
			ArenaManager.clearLastUsedFilenames();
			if (this.isSavedGame) {
				ArenaManager.setLastUsedGame(lug);
			} else {
				ArenaManager.setLastUsedArena(lum);
			}
			Editor.get().arenaChanged();
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.GAME_LOADING_SUCCESS));
			} else {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_SUCCESS));
			}
			ArenaManager.handlePostFileLoad(true);
		} catch (final FileNotFoundException fnfe) {
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.GAME_LOADING_FAILED));
			} else {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_FAILED));
			}
			ArenaManager.handlePostFileLoad(false);
		} catch (final ProtectionCancelException pce) {
			ArenaManager.handlePostFileLoad(false);
		} catch (final IOException ie) {
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.GAME_LOADING_FAILED));
			} else {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_FAILED));
			}
			AppTaskManager.warning(ie);
			ArenaManager.handlePostFileLoad(false);
		} catch (final Exception ex) {
			AppTaskManager.error(ex);
		} finally {
			this.loadFrame.setVisible(false);
		}
	}
}

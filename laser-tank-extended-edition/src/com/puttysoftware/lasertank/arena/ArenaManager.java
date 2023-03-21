/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import com.puttysoftware.diane.fileio.utility.FilenameChecker;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.current.CurrentArena;
import com.puttysoftware.lasertank.arena.v4.V4LevelLoadTask;
import com.puttysoftware.lasertank.datatype.FileExtensions;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.MessageString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.CleanupTask;

public class ArenaManager {
	// Methods
	public static Arena createArena() throws IOException {
		return new CurrentArena();
	}

	private static String getExtension(final String s) {
		String ext = null;
		final var i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	private static String getFileNameOnly(final String s) {
		String fno = null;
		final var i = s.lastIndexOf(File.separatorChar);
		if (i > 0 && i < s.length() - 1) {
			fno = s.substring(i + 1);
		} else {
			fno = s;
		}
		return fno;
	}

	private static String getNameWithoutExtension(final String s) {
		String ext = null;
		final var i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(0, i);
		} else {
			ext = s;
		}
		return ext;
	}

	private static void loadFile(final String filename, final boolean isSavedGame, final boolean protect) {
		if (!FilenameChecker
				.isFilenameOK(ArenaManager.getNameWithoutExtension(ArenaManager.getFileNameOnly(filename)))) {
			CommonDialogs.showErrorDialog(Strings.loadDialog(DialogString.ILLEGAL_CHARACTERS),
					Strings.loadDialog(DialogString.LOAD));
		} else {
			// Run cleanup task
			CleanupTask.cleanUp();
			// Load file
			final var xlt = new LoadTask(filename, isSavedGame, protect);
			xlt.start();
		}
	}

	private static void saveFile(final String filename, final boolean isSavedGame, final boolean protect) {
		if (isSavedGame) {
			LaserTankEE.showMessage(Strings.loadMessage(MessageString.SAVING_GAME));
		} else {
			LaserTankEE.showMessage(Strings.loadMessage(MessageString.SAVING_ARENA));
		}
		final var xst = new SaveTask(filename, isSavedGame, protect);
		xst.start();
	}

	public static int showSaveDialog() {
		String type, source;
		if (LaserTankEE.isInEditorMode()) {
			type = Strings.loadDialog(DialogString.PROMPT_SAVE_ARENA);
			source = Strings.loadEditor(EditorString.EDITOR);
		} else if (LaserTankEE.isInGameMode()) {
			type = Strings.loadDialog(DialogString.PROMPT_SAVE_GAME);
			source = GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME);
		} else {
			// Not in the game or editor, so abort
			return CommonDialogs.NO_OPTION;
		}
		return CommonDialogs.showYNCConfirmDialog(type, source);
	}

	// Fields
	private Arena gameArena;
	private boolean loaded, isDirty;
	private String scoresFileName;
	private String lastUsedArenaFile;
	private String lastUsedGameFile;
	private boolean arenaProtected;

	// Constructors
	public ArenaManager() {
		this.loaded = false;
		this.isDirty = false;
		this.lastUsedArenaFile = Strings.loadCommon(CommonString.EMPTY);
		this.lastUsedGameFile = Strings.loadCommon(CommonString.EMPTY);
		this.scoresFileName = Strings.loadCommon(CommonString.EMPTY);
	}

	public void clearLastUsedFilenames() {
		this.lastUsedArenaFile = Strings.loadCommon(CommonString.EMPTY);
		this.lastUsedGameFile = Strings.loadCommon(CommonString.EMPTY);
	}

	public Arena getArena() {
		return this.gameArena;
	}

	public boolean getDirty() {
		return this.isDirty;
	}

	public String getLastUsedArena() {
		return this.lastUsedArenaFile;
	}

	public String getLastUsedGame() {
		return this.lastUsedGameFile;
	}

	public boolean getLoaded() {
		return this.loaded;
	}

	public String getScoresFileName() {
		return this.scoresFileName;
	}

	public void handleDeferredSuccess(final boolean value) {
		if (value) {
			this.setLoaded(true);
		}
		this.setDirty(false);
		LaserTankEE.getEditor().arenaChanged();
		LaserTankEE.getMenuManager().updateMenuItemState();
	}

	public boolean isArenaProtected() {
		return this.arenaProtected;
	}

	public boolean loadArena() {
		return this.loadArenaImpl(Settings.getLastDirOpen());
	}

	public boolean loadArenaDefault() {
		try {
			return this.loadArenaImpl(
					ArenaManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()
							+ File.separator + GlobalStrings.loadUntranslated(UntranslatedString.COMMON_DIR)
							+ File.separator + GlobalStrings.loadUntranslated(UntranslatedString.LEVELS_DIR));
		} catch (final URISyntaxException e) {
			return this.loadArena();
		}
	}

	private boolean loadArenaImpl(final String initialDirectory) {
		var status = 0;
		var saved = true;
		String filename, extension, file, dir;
		final var fd = new FileDialog((JFrame) null, Strings.loadDialog(DialogString.LOAD), FileDialog.LOAD);
		fd.setDirectory(initialDirectory);
		if (this.getDirty()) {
			status = ArenaManager.showSaveDialog();
			if (status == CommonDialogs.YES_OPTION) {
				saved = this.saveArena(this.isArenaProtected());
			} else if (status == CommonDialogs.CANCEL_OPTION) {
				saved = false;
			} else {
				this.setDirty(false);
			}
		}
		if (saved) {
			fd.setVisible(true);
			file = fd.getFile();
			dir = fd.getDirectory();
			if (file != null && dir != null) {
				Settings.setLastDirOpen(dir);
				filename = dir + file;
				extension = ArenaManager.getExtension(filename);
				if (extension.equals(FileExtensions.getArenaExtension())) {
					this.lastUsedArenaFile = filename;
					this.scoresFileName = ArenaManager.getNameWithoutExtension(file);
					ArenaManager.loadFile(filename, false, false);
				} else if (extension.equals(FileExtensions.getProtectedArenaExtension())) {
					this.lastUsedArenaFile = filename;
					this.scoresFileName = ArenaManager.getNameWithoutExtension(file);
					ArenaManager.loadFile(filename, false, true);
				} else if (extension.equals(FileExtensions.getGameExtension())) {
					this.lastUsedGameFile = filename;
					ArenaManager.loadFile(filename, true, false);
				} else if (extension.equals(FileExtensions.getOldLevelExtension())) {
					this.lastUsedArenaFile = filename;
					this.scoresFileName = ArenaManager.getNameWithoutExtension(file);
					final var ollt = new V4LevelLoadTask(filename);
					ollt.start();
				} else {
					CommonDialogs.showDialog(Strings.loadDialog(DialogString.NON_ARENA_FILE));
				}
			} else // User cancelled
			if (this.loaded) {
				return true;
			}
		}
		return false;
	}

	public boolean saveArena(final boolean protect) {
		if (LaserTankEE.isInGameMode()) {
			if (this.lastUsedGameFile == null || this.lastUsedGameFile.equals(Strings.loadCommon(CommonString.EMPTY))) {
				return this.saveArenaAs(protect);
			}
			final var extension = ArenaManager.getExtension(this.lastUsedGameFile);
			if (extension != null) {
				if (!extension.equals(FileExtensions.getGameExtension())) {
					this.lastUsedGameFile = ArenaManager.getNameWithoutExtension(this.lastUsedGameFile)
							+ FileExtensions.getGameExtensionWithPeriod();
				}
			} else {
				this.lastUsedGameFile += FileExtensions.getGameExtensionWithPeriod();
			}
			ArenaManager.saveFile(this.lastUsedGameFile, true, false);
		} else {
			if (protect) {
				if (this.lastUsedArenaFile == null
						|| this.lastUsedArenaFile.equals(Strings.loadCommon(CommonString.EMPTY))) {
					return this.saveArenaAs(protect);
				}
				final var extension = ArenaManager.getExtension(this.lastUsedArenaFile);
				if (extension != null) {
					if (!extension.equals(FileExtensions.getProtectedArenaExtension())) {
						this.lastUsedArenaFile = ArenaManager.getNameWithoutExtension(this.lastUsedArenaFile)
								+ FileExtensions.getProtectedArenaExtensionWithPeriod();
					}
				} else {
					this.lastUsedArenaFile += FileExtensions.getProtectedArenaExtensionWithPeriod();
				}
			} else {
				if (this.lastUsedArenaFile == null
						|| this.lastUsedArenaFile.equals(Strings.loadCommon(CommonString.EMPTY))) {
					return this.saveArenaAs(protect);
				}
				final var extension = ArenaManager.getExtension(this.lastUsedArenaFile);
				if (extension != null) {
					if (!extension.equals(FileExtensions.getArenaExtension())) {
						this.lastUsedArenaFile = ArenaManager.getNameWithoutExtension(this.lastUsedArenaFile)
								+ FileExtensions.getArenaExtensionWithPeriod();
					}
				} else {
					this.lastUsedArenaFile += FileExtensions.getArenaExtensionWithPeriod();
				}
			}
			ArenaManager.saveFile(this.lastUsedArenaFile, false, protect);
		}
		return false;
	}

	public boolean saveArenaAs(final boolean protect) {
		var filename = Strings.loadCommon(CommonString.EMPTY);
		var fileOnly = GlobalStrings.loadUntranslated(UntranslatedString.DOUBLE_BACKSLASH);
		String extension, file, dir;
		final var lastSave = Settings.getLastDirSave();
		final var fd = new FileDialog((JFrame) null, Strings.loadDialog(DialogString.SAVE), FileDialog.SAVE);
		fd.setDirectory(lastSave);
		while (!FilenameChecker.isFilenameOK(fileOnly)) {
			fd.setVisible(true);
			file = fd.getFile();
			dir = fd.getDirectory();
			if (file == null || dir == null) {
				break;
			}
			extension = ArenaManager.getExtension(file);
			filename = dir + file;
			fileOnly = filename.substring(dir.length() + 1);
			if (!FilenameChecker.isFilenameOK(fileOnly)) {
				CommonDialogs.showErrorDialog(Strings.loadDialog(DialogString.ILLEGAL_CHARACTERS),
						Strings.loadDialog(DialogString.SAVE));
			} else {
				Settings.setLastDirSave(dir);
				if (LaserTankEE.isInGameMode()) {
					if (extension != null) {
						if (!extension.equals(FileExtensions.getGameExtension())) {
							filename = ArenaManager.getNameWithoutExtension(file)
									+ FileExtensions.getGameExtensionWithPeriod();
						}
					} else {
						filename += FileExtensions.getGameExtensionWithPeriod();
					}
					this.lastUsedGameFile = filename;
					ArenaManager.saveFile(filename, true, false);
				} else {
					if (protect) {
						if (extension != null) {
							if (!extension.equals(FileExtensions.getProtectedArenaExtension())) {
								filename = ArenaManager.getNameWithoutExtension(file)
										+ FileExtensions.getProtectedArenaExtensionWithPeriod();
							}
						} else {
							filename += FileExtensions.getProtectedArenaExtensionWithPeriod();
						}
					} else if (extension != null) {
						if (!extension.equals(FileExtensions.getArenaExtension())) {
							filename = ArenaManager.getNameWithoutExtension(file)
									+ FileExtensions.getArenaExtensionWithPeriod();
						}
					} else {
						filename += FileExtensions.getArenaExtensionWithPeriod();
					}
					this.lastUsedArenaFile = filename;
					this.scoresFileName = ArenaManager.getNameWithoutExtension(file);
					ArenaManager.saveFile(filename, false, protect);
				}
			}
		}
		return false;
	}

	public void setArena(final Arena newArena) {
		this.gameArena = newArena;
	}

	public void setArenaProtected(final boolean value) {
		this.arenaProtected = value;
	}

	public void setDirty(final boolean newDirty) {
		this.isDirty = newDirty;
		MainWindow.mainWindow().setDirty(newDirty);
		LaserTankEE.getMenuManager().updateMenuItemState();
	}

	public void setLastUsedArena(final String newFile) {
		this.lastUsedArenaFile = newFile;
	}

	public void setLastUsedGame(final String newFile) {
		this.lastUsedGameFile = newFile;
	}

	public void setLoaded(final boolean status) {
		this.loaded = status;
		LaserTankEE.getMenuManager().updateMenuItemState();
	}

	public void setScoresFileName(final String filename) {
		this.scoresFileName = filename;
	}
}

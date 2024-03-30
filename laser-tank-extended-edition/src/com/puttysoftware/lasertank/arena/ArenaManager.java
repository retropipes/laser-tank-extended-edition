/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.fileio.LoadTask;
import com.puttysoftware.lasertank.arena.fileio.SaveTask;
import com.puttysoftware.lasertank.datatype.LaserTankV4LevelLoadTask;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.fileio.utility.FilenameChecker;
import com.puttysoftware.lasertank.gui.MainWindow;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.MessageString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;
import com.puttysoftware.lasertank.utility.FileExtensions;

public class ArenaManager {
    // Static fields
    private static Arena gameArena;
    private static boolean loaded = false;
    private static boolean isDirty = false;
    private static String scoresFileName = Strings.loadCommon(CommonString.EMPTY);
    private static String lastUsedArenaFile = Strings.loadCommon(CommonString.EMPTY);
    private static String lastUsedGameFile = Strings.loadCommon(CommonString.EMPTY);
    private static boolean arenaProtected = false;

    private static boolean checkSaved() {
	var status = 0;
	var saved = true;
	if (ArenaManager.getDirty()) {
	    status = ArenaManager.showSaveDialog();
	    if (status == CommonDialogs.YES_OPTION) {
		saved = ArenaManager.saveArena(ArenaManager.isArenaProtected());
	    } else if (status == CommonDialogs.CANCEL_OPTION) {
		saved = false;
	    } else {
		ArenaManager.setDirty(false);
	    }
	}
	return saved;
    }

    public static void clearLastUsedFilenames() {
	ArenaManager.lastUsedArenaFile = Strings.loadCommon(CommonString.EMPTY);
	ArenaManager.lastUsedGameFile = Strings.loadCommon(CommonString.EMPTY);
    }

    public static Arena createArena() throws IOException {
	return new CurrentArena();
    }

    public static ArenaData createArenaData() {
	return new CurrentArenaData();
    }

    public static Arena getArena() {
	return ArenaManager.gameArena;
    }

    public static boolean getDirty() {
	return ArenaManager.isDirty;
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

    public static String getLastUsedArena() {
	return ArenaManager.lastUsedArenaFile;
    }

    public static String getLastUsedGame() {
	return ArenaManager.lastUsedGameFile;
    }

    public static boolean getLoaded() {
	return ArenaManager.loaded;
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

    public static String getScoresFileName() {
	return ArenaManager.scoresFileName;
    }

    public static void handlePostFileLoad(final boolean value) {
	ArenaManager.setLoaded(value);
	ArenaManager.setDirty(false);
	Editor.get().arenaChanged();
	LaserTankEE.updateMenuItemState();
    }

    public static boolean isArenaProtected() {
	return ArenaManager.arenaProtected;
    }

    public static void loadArena() {
	final var initialDirectory = Settings.getLastDirOpen();
	final var saved = ArenaManager.checkSaved();
	if (saved) {
	    final var filename = ArenaManager.promptOpenFile(initialDirectory);
	    if (filename != null) {
		final var extension = ArenaManager.getExtension(filename);
		if (extension.equals(FileExtensions.getArenaExtension())) {
		    ArenaManager.lastUsedArenaFile = filename;
		    ArenaManager.scoresFileName = ArenaManager.getNameWithoutExtension(filename);
		    ArenaManager.loadFile(filename, false, false);
		} else if (extension.equals(FileExtensions.getProtectedArenaExtension())) {
		    ArenaManager.lastUsedArenaFile = filename;
		    ArenaManager.scoresFileName = ArenaManager.getNameWithoutExtension(filename);
		    ArenaManager.loadFile(filename, false, true);
		} else if (extension.equals(FileExtensions.getGameExtension())) {
		    ArenaManager.lastUsedGameFile = filename;
		    ArenaManager.loadFile(filename, true, false);
		} else if (extension.equals(FileExtensions.getOldLevelExtension())) {
		    ArenaManager.lastUsedArenaFile = filename;
		    ArenaManager.scoresFileName = ArenaManager.getNameWithoutExtension(filename);
		    final var ollt = new LaserTankV4LevelLoadTask(filename);
		    AppTaskManager.runTask(ollt);
		} else {
		    CommonDialogs.showDialog(Strings.loadDialog(DialogString.NON_ARENA_FILE));
		}
	    }
	}
    }

    public static void loadArenaDefault() {
	AppTaskManager.runTask(new DefaultArenaLoadTask());
    }

    private static void loadFile(final String filename, final boolean isSavedGame, final boolean protect) {
	if (!FilenameChecker
		.isFilenameOK(ArenaManager.getNameWithoutExtension(ArenaManager.getFileNameOnly(filename)))) {
	    CommonDialogs.showErrorDialog(Strings.loadDialog(DialogString.ILLEGAL_CHARACTERS),
		    Strings.loadDialog(DialogString.LOAD));
	} else {
	    // Run cleanup task
	    AppTaskManager.cleanUp();
	    // Load file
	    AppTaskManager.runTask(new LoadTask(filename, isSavedGame, protect));
	}
    }

    private static String promptOpenFile(final String initialDirectory) {
	String file, dir;
	String filename = null;
	final var fd = new FileDialog((JFrame) null, Strings.loadDialog(DialogString.LOAD), FileDialog.LOAD);
	fd.setDirectory(initialDirectory);
	fd.setVisible(true);
	file = fd.getFile();
	dir = fd.getDirectory();
	if (file != null && dir != null) {
	    Settings.setLastDirOpen(dir);
	    filename = dir + file;
	}
	return filename;
    }

    public static boolean saveArena(final boolean protect) {
	if (LaserTankEE.onGameScreen()) {
	    if (ArenaManager.lastUsedGameFile == null
		    || ArenaManager.lastUsedGameFile.equals(Strings.loadCommon(CommonString.EMPTY))) {
		return ArenaManager.saveArenaAs(protect);
	    }
	    final var extension = ArenaManager.getExtension(ArenaManager.lastUsedGameFile);
	    if (extension != null) {
		if (!extension.equals(FileExtensions.getGameExtension())) {
		    ArenaManager.lastUsedGameFile = ArenaManager.getNameWithoutExtension(ArenaManager.lastUsedGameFile)
			    + FileExtensions.getGameExtensionWithPeriod();
		}
	    } else {
		ArenaManager.lastUsedGameFile += FileExtensions.getGameExtensionWithPeriod();
	    }
	    ArenaManager.saveFile(ArenaManager.lastUsedGameFile, true, false);
	} else {
	    if (protect) {
		if (ArenaManager.lastUsedArenaFile == null
			|| ArenaManager.lastUsedArenaFile.equals(Strings.loadCommon(CommonString.EMPTY))) {
		    return ArenaManager.saveArenaAs(protect);
		}
		final var extension = ArenaManager.getExtension(ArenaManager.lastUsedArenaFile);
		if (extension != null) {
		    if (!extension.equals(FileExtensions.getProtectedArenaExtension())) {
			ArenaManager.lastUsedArenaFile = ArenaManager.getNameWithoutExtension(
				ArenaManager.lastUsedArenaFile) + FileExtensions.getProtectedArenaExtensionWithPeriod();
		    }
		} else {
		    ArenaManager.lastUsedArenaFile += FileExtensions.getProtectedArenaExtensionWithPeriod();
		}
	    } else {
		if (ArenaManager.lastUsedArenaFile == null
			|| ArenaManager.lastUsedArenaFile.equals(Strings.loadCommon(CommonString.EMPTY))) {
		    return ArenaManager.saveArenaAs(protect);
		}
		final var extension = ArenaManager.getExtension(ArenaManager.lastUsedArenaFile);
		if (extension != null) {
		    if (!extension.equals(FileExtensions.getArenaExtension())) {
			ArenaManager.lastUsedArenaFile = ArenaManager.getNameWithoutExtension(
				ArenaManager.lastUsedArenaFile) + FileExtensions.getArenaExtensionWithPeriod();
		    }
		} else {
		    ArenaManager.lastUsedArenaFile += FileExtensions.getArenaExtensionWithPeriod();
		}
	    }
	    ArenaManager.saveFile(ArenaManager.lastUsedArenaFile, false, protect);
	}
	return false;
    }

    public static boolean saveArenaAs(final boolean protect) {
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
		if (LaserTankEE.onGameScreen()) {
		    if (extension != null) {
			if (!extension.equals(FileExtensions.getGameExtension())) {
			    filename = ArenaManager.getNameWithoutExtension(file)
				    + FileExtensions.getGameExtensionWithPeriod();
			}
		    } else {
			filename += FileExtensions.getGameExtensionWithPeriod();
		    }
		    ArenaManager.lastUsedGameFile = filename;
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
		    ArenaManager.lastUsedArenaFile = filename;
		    ArenaManager.scoresFileName = ArenaManager.getNameWithoutExtension(file);
		    ArenaManager.saveFile(filename, false, protect);
		}
	    }
	}
	return false;
    }

    private static void saveFile(final String filename, final boolean isSavedGame, final boolean protect) {
	if (isSavedGame) {
	    LaserTankEE.showMessage(Strings.loadMessage(MessageString.SAVING_GAME));
	} else {
	    LaserTankEE.showMessage(Strings.loadMessage(MessageString.SAVING_ARENA));
	}
	AppTaskManager.runTask(new SaveTask(filename, isSavedGame, protect));
    }

    public static void setArena(final Arena newArena) {
	ArenaManager.gameArena = newArena;
    }

    public static void setArenaProtected(final boolean value) {
	ArenaManager.arenaProtected = value;
    }

    public static void setDirty(final boolean newDirty) {
	ArenaManager.isDirty = newDirty;
	MainWindow.mainWindow().setDirty(newDirty);
	LaserTankEE.updateMenuItemState();
    }

    public static void setLastUsedArena(final String newFile) {
	ArenaManager.lastUsedArenaFile = newFile;
    }

    public static void setLastUsedGame(final String newFile) {
	ArenaManager.lastUsedGameFile = newFile;
    }

    public static void setLoaded(final boolean status) {
	ArenaManager.loaded = status;
	LaserTankEE.updateMenuItemState();
    }

    public static void setScoresFileName(final String filename) {
	ArenaManager.scoresFileName = filename;
    }

    public static int showSaveDialog() {
	String type, source;
	if (LaserTankEE.onEditorScreen()) {
	    type = Strings.loadDialog(DialogString.PROMPT_SAVE_ARENA);
	    source = Strings.loadEditor(EditorString.EDITOR);
	} else if (LaserTankEE.onGameScreen()) {
	    type = Strings.loadDialog(DialogString.PROMPT_SAVE_GAME);
	    source = GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME);
	} else {
	    // Not in the game or editor, so abort
	    return CommonDialogs.NO_OPTION;
	}
	return CommonDialogs.showYNCConfirmDialog(type, source);
    }

    // Private constructor
    private ArenaManager() {
    }
}

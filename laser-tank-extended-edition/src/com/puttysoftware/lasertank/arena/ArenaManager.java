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
import com.puttysoftware.lasertank.utility.CleanupTask;
import com.puttysoftware.lasertank.utility.FileExtensions;
import com.puttysoftware.lasertank.utility.TaskRunner;

public class ArenaManager {
    // Static fields
    private static ArenaManager instance;

    public static Arena createArena() throws IOException {
	return new CurrentArena();
    }

    // Used for conversion
    public static ArenaData createArenaData() {
	return new CurrentArenaData();
    }

    // Methods
    public static ArenaManager get() {
	if (ArenaManager.instance == null) {
	    ArenaManager.instance = new ArenaManager();
	}
	return ArenaManager.instance;
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
	    TaskRunner.runTask(new LoadTask(filename, isSavedGame, protect));
	}
    }

    private static void saveFile(final String filename, final boolean isSavedGame, final boolean protect) {
	if (isSavedGame) {
	    LaserTankEE.showMessage(Strings.loadMessage(MessageString.SAVING_GAME));
	} else {
	    LaserTankEE.showMessage(Strings.loadMessage(MessageString.SAVING_ARENA));
	}
	TaskRunner.runTask(new SaveTask(filename, isSavedGame, protect));
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

    // Fields
    private Arena gameArena;
    private boolean loaded, isDirty;
    private String scoresFileName;
    private String lastUsedArenaFile;
    private String lastUsedGameFile;
    private boolean arenaProtected;

    // Constructors
    private ArenaManager() {
	this.loaded = false;
	this.isDirty = false;
	this.lastUsedArenaFile = Strings.loadCommon(CommonString.EMPTY);
	this.lastUsedGameFile = Strings.loadCommon(CommonString.EMPTY);
	this.scoresFileName = Strings.loadCommon(CommonString.EMPTY);
    }

    private boolean checkSaved() {
	var status = 0;
	var saved = true;
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
	return saved;
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

    public void handlePostFileLoad(final boolean value) {
	this.setLoaded(value);
	this.setDirty(false);
	Editor.get().arenaChanged();
	LaserTankEE.updateMenuItemState();
    }

    public boolean isArenaProtected() {
	return this.arenaProtected;
    }

    public void loadArena() {
	final var initialDirectory = Settings.getLastDirOpen();
	final var saved = this.checkSaved();
	if (saved) {
	    final var filename = this.promptOpenFile(initialDirectory);
	    if (filename != null) {
		final var extension = ArenaManager.getExtension(filename);
		if (extension.equals(FileExtensions.getArenaExtension())) {
		    this.lastUsedArenaFile = filename;
		    this.scoresFileName = ArenaManager.getNameWithoutExtension(filename);
		    ArenaManager.loadFile(filename, false, false);
		} else if (extension.equals(FileExtensions.getProtectedArenaExtension())) {
		    this.lastUsedArenaFile = filename;
		    this.scoresFileName = ArenaManager.getNameWithoutExtension(filename);
		    ArenaManager.loadFile(filename, false, true);
		} else if (extension.equals(FileExtensions.getGameExtension())) {
		    this.lastUsedGameFile = filename;
		    ArenaManager.loadFile(filename, true, false);
		} else if (extension.equals(FileExtensions.getOldLevelExtension())) {
		    this.lastUsedArenaFile = filename;
		    this.scoresFileName = ArenaManager.getNameWithoutExtension(filename);
		    final var ollt = new LaserTankV4LevelLoadTask(filename);
		    TaskRunner.runTask(ollt);
		} else {
		    CommonDialogs.showDialog(Strings.loadDialog(DialogString.NON_ARENA_FILE));
		}
	    }
	}
    }

    public void loadArenaDefault() {
	TaskRunner.runTask(new DefaultArenaLoadTask());
    }

    private String promptOpenFile(final String initialDirectory) {
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

    public boolean saveArena(final boolean protect) {
	if (LaserTankEE.onGameScreen()) {
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
		if (LaserTankEE.onGameScreen()) {
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
	LaserTankEE.updateMenuItemState();
    }

    public void setLastUsedArena(final String newFile) {
	this.lastUsedArenaFile = newFile;
    }

    public void setLastUsedGame(final String newFile) {
	this.lastUsedGameFile = newFile;
    }

    public void setLoaded(final boolean status) {
	this.loaded = status;
	LaserTankEE.updateMenuItemState();
    }

    public void setScoresFileName(final String filename) {
	this.scoresFileName = filename;
    }
}

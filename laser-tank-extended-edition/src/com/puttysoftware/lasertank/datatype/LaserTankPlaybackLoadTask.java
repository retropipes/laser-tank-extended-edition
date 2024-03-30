/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.datatype;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.gui.MainWindow;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.GameString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

class LaserTankPlaybackLoadTask implements Runnable {
    // Fields
    private final File file;
    private final JPanel loadContent;

    // Constructors
    LaserTankPlaybackLoadTask(final File theFile) {
	this.file = theFile;
	this.loadContent = new JPanel();
	final var loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.loadContent.add(loadBar);
    }

    // Methods
    @Override
    public void run() {
	MainWindow.mainWindow().setAndSave(this.loadContent, Strings.loadDialog(DialogString.LOADING));
	Game.setSavedGameFlag(false);
	try {
	    LaserTankPlayback.loadFromFile(this.file);
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog(Strings.loadGame(GameString.PLAYBACK_LOAD_FAILED));
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	} catch (final Exception ex) {
	    AppTaskManager.error(ex);
	} finally {
	    MainWindow.mainWindow().restoreSaved();
	}
    }
}

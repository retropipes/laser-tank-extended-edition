/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.datatype;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.Application;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.GameString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

class LaserTankPlaybackLoadTask extends Thread {
    // Fields
    private final File file;
    private final JPanel loadContent;

    // Constructors
    LaserTankPlaybackLoadTask(final File theFile) {
	this.file = theFile;
	this.setName(GlobalStrings.loadUntranslated(UntranslatedString.PLAYBACK_LOADER_NAME));
	this.loadContent = new JPanel();
	JProgressBar loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.loadContent.add(loadBar);
    }

    // Methods
    @Override
    public void run() {
	MainWindow.mainWindow().setAndSave(this.loadContent, Strings.loadDialog(DialogString.LOADING));
	final Application app = LaserTankEE.getApplication();
	app.getGameManager().setSavedGameFlag(false);
	try {
	    LaserTankPlayback.loadFromFile(this.file);
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog(Strings.loadGame(GameString.PLAYBACK_LOAD_FAILED));
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	} finally {
	    MainWindow.mainWindow().restoreSaved();
	}
    }
}

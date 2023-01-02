/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.v4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.Application;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public class V4LevelLoadTask extends Thread {
    // Fields
    private final String filename;
    private final JFrame loadFrame;

    // Constructors
    public V4LevelLoadTask(final String file) {
	JProgressBar loadBar;
	this.filename = file;
	this.setName(GlobalStrings.loadUntranslated(UntranslatedString.OLD_AG_LOADER_NAME));
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
	final Application app = LaserTankEE.getApplication();
	app.getGameManager().setSavedGameFlag(false);
	try (FileInputStream arenaFile = new FileInputStream(this.filename)) {
	    final Arena gameArena = ArenaManager.createArena();
	    V4File.loadOldFile(gameArena, arenaFile);
	    arenaFile.close();
	    app.getArenaManager().setArena(gameArena);
	    final boolean playerExists = gameArena.doesPlayerExist(0);
	    if (playerExists) {
		app.getGameManager().getPlayerManager().resetPlayerLocation();
	    }
	    gameArena.save();
	    // Final cleanup
	    final String lum = app.getArenaManager().getLastUsedArena();
	    app.getArenaManager().clearLastUsedFilenames();
	    app.getArenaManager().setLastUsedArena(lum);
	    app.updateLevelInfoList();
	    app.getEditor().arenaChanged();
	    CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_SUCCESS));
	    app.getArenaManager().handleDeferredSuccess(true);
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_FAILED));
	    app.getArenaManager().handleDeferredSuccess(false);
	} catch (final IOException ie) {
	    LaserTankEE.logWarning(ie);
	    app.getArenaManager().handleDeferredSuccess(false);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	} finally {
	    this.loadFrame.setVisible(false);
	}
    }
}

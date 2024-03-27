/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.datatype;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

public class LaserTankV4LevelLoadTask implements Runnable {
    // Fields
    private final String filename;
    private final JFrame loadFrame;

    // Constructors
    public LaserTankV4LevelLoadTask(final String file) {
	JProgressBar loadBar;
	this.filename = file;
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
	Game.get().setSavedGameFlag(false);
	try (var arenaFile = new FileInputStream(this.filename)) {
	    final var gameArena = ArenaManager.createArena();
	    LaserTankV4File.loadOldFile(gameArena, arenaFile);
	    arenaFile.close();
	    ArenaManager.get().setArena(gameArena);
	    final var playerExists = gameArena.doesPlayerExist(0);
	    if (playerExists) {
		Game.get().resetPlayerLocation();
	    }
	    gameArena.save();
	    // Final cleanup
	    final var lum = ArenaManager.get().getLastUsedArena();
	    ArenaManager.get().clearLastUsedFilenames();
	    ArenaManager.get().setLastUsedArena(lum);
	    LaserTankEE.updateLevelInfoList();
	    Editor.get().arenaChanged();
	    CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_SUCCESS));
	    ArenaManager.get().handlePostFileLoad(true);
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog(Strings.loadDialog(DialogString.ARENA_LOADING_FAILED));
	    ArenaManager.get().handlePostFileLoad(false);
	} catch (final IOException ie) {
	    LaserTankEE.logWarning(ie);
	    ArenaManager.get().handlePostFileLoad(false);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	} finally {
	    this.loadFrame.setVisible(false);
	}
    }
}

package com.puttysoftware.lasertank.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.utility.TaskRunner;

class GameWindowEventHandler extends WindowAdapter implements Runnable {
    public GameWindowEventHandler() {
    }

    @Override
    public void run() {
	try {
	    var success = false;
	    var status = 0;
	    if (ArenaManager.getDirty()) {
		status = ArenaManager.showSaveDialog();
		if (status == CommonDialogs.YES_OPTION) {
		    success = ArenaManager.saveArena(ArenaManager.isArenaProtected());
		    if (success) {
			Game.exitGame();
			LaserTankEE.showGUI();
		    }
		} else if (status == CommonDialogs.NO_OPTION) {
		    Game.exitGame();
		    LaserTankEE.showGUI();
		} else {
		    // Don't stop controls from working
		    Game.moving = false;
		    Game.laserActive = false;
		}
	    } else {
		Game.exitGame();
		LaserTankEE.showGUI();
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    @Override
    public void windowClosing(final WindowEvent we) {
	TaskRunner.runTask(this);
    }
}
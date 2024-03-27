package com.puttysoftware.lasertank.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.utility.TaskRunner;

class GameDifficultyWindowEventHandler extends WindowAdapter implements Runnable {
    private final Game game;

    public GameDifficultyWindowEventHandler(final Game theGame) {
	this.game = theGame;
    }

    @Override
    public void run() {
	if (this.game != null) {
	    this.game.cancelButtonClicked();
	}
    }

    @Override
    public void windowClosing(final WindowEvent e) {
	TaskRunner.runTask(this);
    }
}
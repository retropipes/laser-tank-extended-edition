package com.puttysoftware.lasertank.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.utility.TaskRunner;

class GameDifficultyWindowEventHandler extends WindowAdapter implements Runnable {
    public GameDifficultyWindowEventHandler() {
    }

    @Override
    public void run() {
	Game.cancelButtonClicked();
    }

    @Override
    public void windowClosing(final WindowEvent e) {
	TaskRunner.runTask(this);
    }
}
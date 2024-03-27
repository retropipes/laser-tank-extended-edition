package com.puttysoftware.lasertank.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.utility.TaskRunner;

class GameDifficultyActionEventHandler implements ActionListener, Runnable {
    private final Game game;
    private ActionEvent event;

    public GameDifficultyActionEventHandler(final Game theGame) {
	this.game = theGame;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	final var cmd = this.event.getActionCommand();
	final var gm = GameDifficultyActionEventHandler.this.game;
	if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
	    gm.okButtonClicked();
	} else {
	    gm.cancelButtonClicked();
	}
    }
}
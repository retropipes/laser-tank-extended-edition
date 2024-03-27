package com.puttysoftware.lasertank.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.utility.TaskRunner;

class GameDifficultyActionEventHandler implements ActionListener, Runnable {
    private ActionEvent event;

    public GameDifficultyActionEventHandler() {
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	final var cmd = this.event.getActionCommand();
	if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
	    Game.okButtonClicked();
	} else {
	    Game.cancelButtonClicked();
	}
    }
}
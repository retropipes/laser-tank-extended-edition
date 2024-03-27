package com.puttysoftware.lasertank.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.utility.TaskRunner;

class LevelSettingsActionEventHandler implements ActionListener, Runnable {
    private final LevelSettings levelSettings;
    private ActionEvent event;

    public LevelSettingsActionEventHandler(final LevelSettings theLevelSettings) {
	this.levelSettings = theLevelSettings;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	try {
	    if (this.levelSettings != null && this.event != null) {
		final var cmd = this.event.getActionCommand();
		if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
		    this.levelSettings.setSettings();
		    this.levelSettings.hideSettings();
		} else if (cmd.equals(Strings.loadDialog(DialogString.CANCEL_BUTTON))) {
		    this.levelSettings.hideSettings();
		}
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }
}
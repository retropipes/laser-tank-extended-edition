package com.puttysoftware.lasertank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.utility.TaskRunner;

class AboutDialogEventHandler implements ActionListener, Runnable {
    private final AboutDialog aboutDialog;
    private ActionEvent event;

    public AboutDialogEventHandler(final AboutDialog theAboutDialog) {
	this.aboutDialog = theAboutDialog;
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	try {
	    if (this.aboutDialog != null && this.event != null) {
		final var cmd = this.event.getActionCommand();
		if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
		    this.aboutDialog.hideScreen();
		}
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }
}
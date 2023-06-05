package com.puttysoftware.lasertank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

class AboutDialogEventHandler implements ActionListener {
    private final AboutDialog aboutDialog;

    public AboutDialogEventHandler(final AboutDialog theAboutDialog) {
	this.aboutDialog = theAboutDialog;
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    final var ad = AboutDialogEventHandler.this.aboutDialog;
		    final var cmd = e.getActionCommand();
		    if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
			ad.hideScreen();
		    }
		} catch (final Exception ex) {
		    LaserTankEE.logError(ex);
		}
	    }
	}.start();
    }
}
package com.puttysoftware.lasertank.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

class LevelSettingsEventHandler implements ActionListener, WindowListener {
    private final LevelSettings levelSettings;

    public LevelSettingsEventHandler(final LevelSettings theLevelSettings) {
	this.levelSettings = theLevelSettings;
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    final var lpm = LevelSettingsEventHandler.this.levelSettings;
		    final var cmd = e.getActionCommand();
		    if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
			lpm.setSettings();
			lpm.hideSettings();
		    } else if (cmd.equals(Strings.loadDialog(DialogString.CANCEL_BUTTON))) {
			lpm.hideSettings();
		    }
		} catch (final Exception ex) {
		    LaserTankEE.logError(ex);
		}
	    }
	}.start();
    }

    @Override
    public void windowActivated(final WindowEvent e) {
	// Do nothing
    }

    @Override
    public void windowClosed(final WindowEvent e) {
	// Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		LevelSettingsEventHandler.this.levelSettings.hideSettings();
	    }
	}.start();
    }

    @Override
    public void windowDeactivated(final WindowEvent e) {
	// Do nothing
    }

    @Override
    public void windowDeiconified(final WindowEvent e) {
	// Do nothing
    }

    @Override
    public void windowIconified(final WindowEvent e) {
	// Do nothing
    }

    // handle window
    @Override
    public void windowOpened(final WindowEvent e) {
	// Do nothing
    }
}
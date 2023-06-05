package com.puttysoftware.lasertank.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

class SettingsGUIEventHandler implements ActionListener, WindowListener {
    private final SettingsGUI settingsGUI;

    public SettingsGUIEventHandler(final SettingsGUI theSettingsGUI) {
	this.settingsGUI = theSettingsGUI;
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    final var pm = SettingsGUIEventHandler.this.settingsGUI;
		    final var cmd = e.getActionCommand();
		    if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
			pm.setSettings();
		    } else if (cmd.equals(Strings.loadDialog(DialogString.CANCEL_BUTTON))) {
			pm.hideSettings();
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
		SettingsGUIEventHandler.this.settingsGUI.hideSettings();
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

    @Override
    public void windowOpened(final WindowEvent e) {
	// Do nothing
    }
}
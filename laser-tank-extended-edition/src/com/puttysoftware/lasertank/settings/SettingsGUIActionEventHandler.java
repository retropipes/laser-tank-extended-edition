package com.puttysoftware.lasertank.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

class SettingsGUIActionEventHandler implements ActionListener, Runnable {
    private final SettingsGUI settingsGUI;
    private ActionEvent event;

    SettingsGUIActionEventHandler(final SettingsGUI theSettingsGUI) {
	this.settingsGUI = theSettingsGUI;
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
	this.event = e;
	AppTaskManager.runTask(this);
    }

    @Override
    public void run() {
	try {
	    final var cmd = this.event.getActionCommand();
	    if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
		this.settingsGUI.setSettings();
	    } else if (cmd.equals(Strings.loadDialog(DialogString.CANCEL_BUTTON))) {
		this.settingsGUI.hideSettings();
	    }
	} catch (final Exception ex) {
	    AppTaskManager.error(ex);
	}
    }
}
package com.puttysoftware.lasertank.settings;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.utility.TaskRunner;

class SettingsGUIWindowEventHandler extends WindowAdapter implements Runnable {
    private final SettingsGUI settingsGUI;

    public SettingsGUIWindowEventHandler(final SettingsGUI theSettingsGUI) {
	this.settingsGUI = theSettingsGUI;
    }

    @Override
    public void run() {
	this.settingsGUI.hideSettings();
    }

    @Override
    public void windowClosing(final WindowEvent e) {
	TaskRunner.runTask(this);
    }
}
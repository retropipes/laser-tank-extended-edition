package com.puttysoftware.lasertank.settings;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.tasks.AppTaskManager;

class SettingsGUIWindowEventHandler extends WindowAdapter implements Runnable {
    private final SettingsGUI settingsGUI;

    SettingsGUIWindowEventHandler(final SettingsGUI theSettingsGUI) {
	this.settingsGUI = theSettingsGUI;
    }

    @Override
    public void run() {
	this.settingsGUI.hideSettings();
    }

    @Override
    public void windowClosing(final WindowEvent e) {
	AppTaskManager.runTask(this);
    }
}
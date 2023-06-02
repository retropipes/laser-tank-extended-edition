package com.puttysoftware.lasertank;

import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;

import com.puttysoftware.lasertank.settings.Settings;

class SettingsInvoker implements PreferencesHandler {
    public SettingsInvoker() {
    }

    @Override
    public void handlePreferences(final PreferencesEvent e) {
	Settings.showSettings();
    }
}
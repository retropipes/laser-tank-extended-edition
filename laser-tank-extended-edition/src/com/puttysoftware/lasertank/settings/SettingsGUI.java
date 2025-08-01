/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.settings;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.puttysoftware.lasertank.gui.MainWindow;
import com.puttysoftware.lasertank.helper.EditorLayoutHelper;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.SettingsString;
import com.puttysoftware.lasertank.locale.Strings;

class SettingsGUI {
	private static final int GRID_LENGTH = 12;
	// Fields
	private MainWindow settingsWindow;
	private JPanel mainSettingsPane;
	private JCheckBox sounds;
	private JCheckBox music;
	private JCheckBox checkUpdatesStartup;
	private JCheckBox enableAnimation;
	private JCheckBox moveOneAtATime;
	private JComboBox<String> actionDelay;
	private JComboBox<String> languageList;
	private JComboBox<String> editorLayoutList;
	private JCheckBox editorShowAllObjects;

	// Constructors
	SettingsGUI() {
		this.setUpGUI();
		this.setDefaultSettings();
	}

	// Methods
	void activeLanguageChanged() {
		this.setUpGUI();
		Settings.writeSettings();
		this.loadSettings();
	}

	void hideSettings() {
		this.settingsWindow.restoreSaved();
		Settings.writeSettings();
	}

	private void loadSettings() {
		this.enableAnimation.setSelected(Settings.enableAnimation());
		this.sounds.setSelected(Settings.areSoundsEnabled());
		this.music.setSelected(Settings.isMusicEnabled());
		this.checkUpdatesStartup.setSelected(Settings.shouldCheckUpdatesAtStartup());
		this.moveOneAtATime.setSelected(Settings.oneMove());
		this.actionDelay.setSelectedIndex(Settings.getActionDelay());
		this.languageList.setSelectedIndex(Settings.getLanguageID());
		this.editorLayoutList.setSelectedIndex(Settings.getEditorLayoutID());
		this.editorShowAllObjects.setSelected(Settings.getEditorShowAllObjects());
	}

	private void setDefaultSettings() {
		Settings.readSettings();
		this.loadSettings();
	}

	void setSettings() {
		Settings.setEnableAnimation(this.enableAnimation.isSelected());
		Settings.setSoundsEnabled(this.sounds.isSelected());
		Settings.setMusicEnabled(this.music.isSelected());
		Settings.setCheckUpdatesAtStartup(this.checkUpdatesStartup.isSelected());
		Settings.setOneMove(this.moveOneAtATime.isSelected());
		Settings.setActionDelay(this.actionDelay.getSelectedIndex());
		Settings.setLanguageID(this.languageList.getSelectedIndex());
		Settings.setEditorLayoutID(this.editorLayoutList.getSelectedIndex());
		Settings.setEditorShowAllObjects(this.editorShowAllObjects.isSelected());
		this.hideSettings();
	}

	private void setUpGUI() {
		final var ahandler = new SettingsGUIActionEventHandler(this);
		final var whandler = new SettingsGUIWindowEventHandler(this);
		this.settingsWindow = MainWindow.mainWindow();
		this.mainSettingsPane = this.settingsWindow.createContent();
		final var buttonPane = new Container();
		final var settingsPane = new Container();
		final var prefsOK = new JButton(Strings.loadDialog(DialogString.OK_BUTTON));
		prefsOK.setDefaultCapable(true);
		this.settingsWindow.setDefaultButton(prefsOK);
		final var prefsCancel = new JButton(Strings.loadDialog(DialogString.CANCEL_BUTTON));
		prefsCancel.setDefaultCapable(false);
		this.sounds = new JCheckBox(Strings.loadSettings(SettingsString.ENABLE_SOUNDS), true);
		this.music = new JCheckBox(Strings.loadSettings(SettingsString.ENABLE_MUSIC), true);
		this.checkUpdatesStartup = new JCheckBox(Strings.loadSettings(SettingsString.STARTUP_UPDATES), true);
		this.moveOneAtATime = new JCheckBox(Strings.loadSettings(SettingsString.ONE_MOVE), true);
		this.enableAnimation = new JCheckBox(Strings.loadSettings(SettingsString.ENABLE_ANIMATION), true);
		this.actionDelay = new JComboBox<>(new String[] { Strings.loadSettings(SettingsString.SPEED_1),
				Strings.loadSettings(SettingsString.SPEED_2), Strings.loadSettings(SettingsString.SPEED_3),
				Strings.loadSettings(SettingsString.SPEED_4), Strings.loadSettings(SettingsString.SPEED_5) });
		this.languageList = new JComboBox<>(Strings.loadLanguagesList());
		this.editorLayoutList = new JComboBox<>(EditorLayoutHelper.getNames());
		this.editorShowAllObjects = new JCheckBox(Strings.loadSettings(SettingsString.SHOW_ALL_OBJECTS), true);
		this.settingsWindow.addWindowListener(whandler);
		this.mainSettingsPane.setLayout(new BorderLayout());
		settingsPane.setLayout(new GridLayout(SettingsGUI.GRID_LENGTH, 1));
		settingsPane.add(this.sounds);
		settingsPane.add(this.music);
		settingsPane.add(this.enableAnimation);
		settingsPane.add(this.checkUpdatesStartup);
		settingsPane.add(this.moveOneAtATime);
		settingsPane.add(new JLabel(Strings.loadSettings(SettingsString.SPEED_LABEL)));
		settingsPane.add(this.actionDelay);
		settingsPane.add(new JLabel(Strings.loadSettings(SettingsString.ACTIVE_LANGUAGE_LABEL)));
		settingsPane.add(this.languageList);
		settingsPane.add(new JLabel(Strings.loadSettings(SettingsString.EDITOR_LAYOUT_LABEL)));
		settingsPane.add(this.editorLayoutList);
		settingsPane.add(this.editorShowAllObjects);
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(prefsOK);
		buttonPane.add(prefsCancel);
		this.mainSettingsPane.add(settingsPane, BorderLayout.CENTER);
		this.mainSettingsPane.add(buttonPane, BorderLayout.SOUTH);
		prefsOK.addActionListener(ahandler);
		prefsCancel.addActionListener(ahandler);
	}

	void showSettings() {
		this.settingsWindow.setAndSave(this.mainSettingsPane, Strings.loadSettings(SettingsString.TITLE));
	}
}

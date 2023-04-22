/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.settings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.datatype.FileExtensions;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.index.EditorLayout;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

public class Settings {
	// Fields
	private final static SettingsFile storeMgr = new SettingsFile();
	private final static SettingsGUI guiMgr = new SettingsGUI();
	private final static int FALLBACK_LANGUAGE_ID = 0;
	private final static int DEFAULT_EDITOR_LAYOUT_ID = EditorLayout.MODERN_V12;

	// Methods
	public static void activeLanguageChanged() {
		Settings.guiMgr.activeLanguageChanged();
	}

	public static boolean enableAnimation() {
		return Settings.storeMgr
				.getBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_ANIMATION), true);
	}

	static int getActionDelay() {
		return Settings.storeMgr
				.getInteger(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ACTION_DELAY), 2);
	}

	public static long getActionSpeed() {
		return (Settings.getActionDelay() + 1) * 5;
	}

	public static ArenaObject getEditorDefaultFill() {
		return new ArenaObject(GameObjectID.GROUND);
	}

	public static int getEditorLayoutID() {
		return Settings.storeMgr.getInteger(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_EDITOR_LAYOUT_ID),
				Settings.DEFAULT_EDITOR_LAYOUT_ID);
	}

	public static boolean getEditorShowAllObjects() {
		return Settings.storeMgr
				.getBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_EDITOR_SHOW_ALL), true);
	}

	public static int getLanguageID() {
		return Settings.storeMgr.getInteger(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LANGUAGE_ID),
				Settings.FALLBACK_LANGUAGE_ID);
	}

	public static String getLastDirOpen() {
		return Settings.storeMgr.getString(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LAST_DIR_OPEN),
				Strings.loadCommon(CommonString.EMPTY));
	}

	public static String getLastDirSave() {
		return Settings.storeMgr.getString(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LAST_DIR_SAVE),
				Strings.loadCommon(CommonString.EMPTY));
	}

	public static boolean isMusicEnabled() {
		return Settings.storeMgr
				.getBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_MUSIC), true);
	}

	private static String getSettingsDirectory() {
		final var osName = System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.OS_NAME));
		if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.MAC_OS_X)) != -1) {
			// Mac OS X
			return GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_SETTINGS_MAC);
		}
		if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.WINDOWS)) != -1) {
			// Windows
			return GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_SETTINGS_WINDOWS);
		}
		// Other - assume UNIX-like
		return GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_SETTINGS_UNIX);
	}

	private static String getSettingsDirPrefix() {
		final var osName = System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.OS_NAME));
		if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.MAC_OS_X)) != -1) {
			// Mac OS X
			return System.getenv(GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_UNIX_HOME));
		}
		if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.WINDOWS)) != -1) {
			// Windows
			return System.getenv(GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_WINDOWS_APPDATA));
		}
		// Other - assume UNIX-like
		return System.getenv(GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_UNIX_HOME));
	}

	private static String getSettingsFile() {
		final var b = new StringBuilder();
		b.append(Settings.getSettingsDirPrefix());
		b.append(Settings.getSettingsDirectory());
		b.append(Settings.getSettingsFileName());
		b.append(Settings.getSettingsFileExtension());
		return b.toString();
	}

	private static String getSettingsFileExtension() {
		return Strings.loadCommon(CommonString.NOTL_PERIOD) + FileExtensions.getPreferencesExtension();
	}

	private static String getSettingsFileName() {
		final var osName = System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.OS_NAME));
		if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.MAC_OS_X)) != -1) {
			// Mac OS X
			return GlobalStrings.loadUntranslated(UntranslatedString.FILE_SETTINGS_MAC);
		}
		if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.WINDOWS)) != -1) {
			// Windows
			return GlobalStrings.loadUntranslated(UntranslatedString.FILE_SETTINGS_WINDOWS);
		}
		// Other - assume UNIX-like
		return GlobalStrings.loadUntranslated(UntranslatedString.FILE_SETTINGS_UNIX);
	}

	public static long getReplaySpeed() {
		return (Settings.getActionDelay() + 1) * 10;
	}

	public static boolean areSoundsEnabled() {
		return Settings.storeMgr
				.getBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_SOUNDS), true);
	}

	public static boolean isDeadlyDifficultyEnabled() {
		return Settings.storeMgr.getBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_DEADLY), true);
	}

	public static boolean isEasyDifficultyEnabled() {
		return Settings.storeMgr.getBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_EASY), true);
	}

	public static boolean isHardDifficultyEnabled() {
		return Settings.storeMgr.getBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_HARD), true);
	}

	public static boolean isKidsDifficultyEnabled() {
		return Settings.storeMgr.getBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_KIDS), true);
	}

	public static boolean isMediumDifficultyEnabled() {
		return Settings.storeMgr.getBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_MEDIUM), true);
	}

	public static boolean oneMove() {
		return Settings.storeMgr.getBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ONE_MOVE),
				true);
	}

	public static void readSettings() {
		try (var buf = new BufferedInputStream(new FileInputStream(Settings.getSettingsFile()))) {
			// Read new preferences
			Settings.storeMgr.loadStore(buf);
		} catch (final FileNotFoundException fnfe) {
			// Populate store with defaults
			Settings.storeMgr.setString(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LAST_DIR_OPEN),
					Strings.loadCommon(CommonString.EMPTY));
			Settings.storeMgr.setString(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LAST_DIR_SAVE),
					Strings.loadCommon(CommonString.EMPTY));
			Settings.storeMgr
					.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_UPDATES_STARTUP), true);
			Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ONE_MOVE),
					true);
			Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_SOUNDS),
					true);
			Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_MUSIC),
					true);
			Settings.storeMgr
					.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_ANIMATION), true);
			Settings.storeMgr.setBoolean(
					GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_KIDS), true);
			Settings.storeMgr.setBoolean(
					GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_EASY), true);
			Settings.storeMgr.setBoolean(
					GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_MEDIUM), true);
			Settings.storeMgr.setBoolean(
					GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_HARD), true);
			Settings.storeMgr.setBoolean(
					GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_DEADLY), true);
			Settings.storeMgr.setInteger(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ACTION_DELAY),
					2);
			Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_CLASSIC_ACCEL),
					false);
			Settings.storeMgr.setInteger(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LANGUAGE_ID),
					Settings.FALLBACK_LANGUAGE_ID);
			Settings.storeMgr.setInteger(
					GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_EDITOR_LAYOUT_ID),
					Settings.DEFAULT_EDITOR_LAYOUT_ID);
			Settings.storeMgr
					.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_EDITOR_SHOW_ALL), true);
		} catch (final IOException ioe) {
			LaserTankEE.logError(ioe);
		}
	}

	static void setActionDelay(final int value) {
		Settings.storeMgr.setInteger(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ACTION_DELAY),
				value);
	}

	static void setCheckUpdatesAtStartup(final boolean value) {
		Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_UPDATES_STARTUP),
				value);
	}

	public static void setClassicAccelerators(final boolean value) {
		Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_CLASSIC_ACCEL),
				value);
	}

	public static void setDeadlyDifficultyEnabled(final boolean value) {
		Settings.storeMgr.setBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_DEADLY), value);
	}

	public static void setEasyDifficultyEnabled(final boolean value) {
		Settings.storeMgr.setBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_EASY), value);
	}

	public static void setEditorLayoutID(final int value) {
		Settings.storeMgr.setInteger(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_EDITOR_LAYOUT_ID),
				value);
		Editor.get().rebuildGUI();
	}

	public static void setEditorShowAllObjects(final boolean value) {
		Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_EDITOR_SHOW_ALL),
				value);
		Editor.get().rebuildGUI();
	}

	static void setEnableAnimation(final boolean value) {
		Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_ANIMATION),
				value);
	}

	public static void setHardDifficultyEnabled(final boolean value) {
		Settings.storeMgr.setBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_HARD), value);
	}

	public static void setKidsDifficultyEnabled(final boolean value) {
		Settings.storeMgr.setBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_KIDS), value);
	}

	public static void setLanguageID(final int value) {
		final var oldValue = Settings.getLanguageID();
		Settings.storeMgr.setInteger(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LANGUAGE_ID),
				value);
		if (oldValue != value) {
			Strings.activeLanguageChanged(value);
		}
	}

	public static void setLastDirOpen(final String value) {
		Settings.storeMgr.setString(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LAST_DIR_OPEN),
				value);
	}

	public static void setLastDirSave(final String value) {
		Settings.storeMgr.setString(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_LAST_DIR_SAVE),
				value);
	}

	public static void setMediumDifficultyEnabled(final boolean value) {
		Settings.storeMgr.setBoolean(
				GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_DIFFICULTY_MEDIUM), value);
	}

	static void setMusicEnabled(final boolean status) {
		Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_MUSIC),
				status);
	}

	static void setOneMove(final boolean value) {
		Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ONE_MOVE), value);
	}

	static void setSoundsEnabled(final boolean status) {
		Settings.storeMgr.setBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_ENABLE_SOUNDS),
				status);
	}

	public static boolean shouldCheckUpdatesAtStartup() {
		return Settings.storeMgr
				.getBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_UPDATES_STARTUP), true);
	}

	public static void showSettings() {
		Settings.guiMgr.showSettings();
	}

	public static boolean useClassicAccelerators() {
		return Settings.storeMgr
				.getBoolean(GlobalStrings.loadUntranslated(UntranslatedString.SETTINGS_KEY_CLASSIC_ACCEL), false);
	}

	public static void writeSettings() {
		try (var buf = new BufferedOutputStream(new FileOutputStream(Settings.getSettingsFile()))) {
			Settings.storeMgr.saveStore(buf);
		} catch (final IOException ioe) {
			throw new InvalidArenaException(ioe);
		}
	}

	// Private constructor
	private Settings() {
		// Do nothing
	}
}

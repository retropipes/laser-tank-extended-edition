/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.locale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.puttysoftware.diane.fileio.utility.ResourceStreamReader;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.asset.Images;
import com.puttysoftware.lasertank.helper.DifficultyHelper;
import com.puttysoftware.lasertank.helper.EraHelper;
import com.puttysoftware.lasertank.helper.LayerHelper;
import com.puttysoftware.lasertank.index.Difficulty;
import com.puttysoftware.lasertank.index.Era;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.settings.Settings;

public class Strings {
    private static final String REPLACE_PREFIX = "$"; //$NON-NLS-1$
    private static final String LOCALIZED_LANGUAGES_FILE_NAME = "localizedlanguages.txt"; //$NON-NLS-1$
    private static final String LOAD_PATH = "/locale/"; //$NON-NLS-1$
    private static Class<?> LOAD_CLASS = Strings.class;
    private static ArrayList<String> LOCALIZED_LANGUAGES_CACHE;
    private static int LANGUAGE_ID = 0;
    private static String LANGUAGE_NAME = null;

    public static void activeLanguageChanged(final int newLanguageID) {
	Strings.LOCALIZED_LANGUAGES_CACHE = null;
	Strings.LANGUAGE_ID = newLanguageID;
	Strings.LANGUAGE_NAME = GlobalStrings.loadLanguage(Strings.LANGUAGE_ID) + "/"; //$NON-NLS-1$
	DifficultyHelper.activeLanguageChanged();
	EraHelper.activeLanguageChanged();
	LayerHelper.activeLanguageChanged();
	LaserTankEE.getApplication().activeLanguageChanged();
	Settings.activeLanguageChanged();
	Images.activeLanguageChanged();
    }

    public static String getLanguageName() {
	return Strings.LANGUAGE_NAME;
    }

    public static String[] loadLocalizedLanguagesList() {
	if (Strings.LOCALIZED_LANGUAGES_CACHE == null) {
	    Strings.LOCALIZED_LANGUAGES_CACHE = new ArrayList<>();
	    final var filename = Strings.LOCALIZED_LANGUAGES_FILE_NAME;
	    final var llpath = Strings.LOAD_PATH + Strings.LANGUAGE_NAME + filename;
	    try (final var is = Strings.LOAD_CLASS.getResourceAsStream(llpath);
		    final var rsr = new ResourceStreamReader(is, "UTF-8")) { //$NON-NLS-1$
		var line = Strings.loadCommon(CommonString.EMPTY);
		while (line != null) {
		    // Read line
		    line = rsr.readString();
		    if (line != null) {
			// Parse line
			Strings.LOCALIZED_LANGUAGES_CACHE.add(line);
		    }
		}
	    } catch (final IOException ioe) {
		LaserTankEE.logErrorDirectly(ioe);
	    }
	}
	final var size = Strings.LOCALIZED_LANGUAGES_CACHE.size();
	return Strings.LOCALIZED_LANGUAGES_CACHE.toArray(new String[size]);
    }

    public static void setDefaultLanguage() {
	Strings.LOCALIZED_LANGUAGES_CACHE = null;
	Strings.LANGUAGE_ID = 0;
	Strings.LANGUAGE_NAME = GlobalStrings.loadLanguage(Strings.LANGUAGE_ID) + "/"; //$NON-NLS-1$
    }

    private static ResourceBundle loadFile(final StringFile file) {
	return ResourceBundle.getBundle("locale." + Strings.LANGUAGE_NAME + file.getName()); //$NON-NLS-1$
    }

    public static String loadCommon(final CommonString str) {
	return str.getValue();
    }

    public static String loadDialog(final DialogString str, final Object... values) {
	var string = Strings.loadFile(StringFile.DIALOGS).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadDifficulty(final Difficulty d) {
	return Strings.loadFile(StringFile.DIFFICULTY).getString(Integer.toString(d.ordinal()));
    }

    public static String loadEditorLayout(final int el) {
	return Strings.loadFile(StringFile.SETTINGS)
		.getString(Integer.toString(el + SettingsString.EDITOR_LAYOUT_CLASSIC.ordinal()));
    }

    public static String loadEditor(final EditorString str, final Object... values) {
	var string = Strings.loadFile(StringFile.EDITOR).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadEra(final Era e) {
	return Strings.loadFile(StringFile.TIME).getString(Integer.toString(e.ordinal()));
    }

    public static String loadError(final ErrorString str, final Object... values) {
	var string = Strings.loadFile(StringFile.ERRORS).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadGame(final GameString str, final Object... values) {
	var string = Strings.loadFile(StringFile.GAME).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadGeneric(final GenericString str, final Object... values) {
	var string = Strings.loadFile(StringFile.GENERIC).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadMenu(final MenuString str, final Object... values) {
	var string = Strings.loadFile(StringFile.MENUS).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadMessage(final MessageString str, final Object... values) {
	var string = Strings.loadFile(StringFile.MESSAGES).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadObjectCustomText(final GameObjectID objID, final Object... values) {
	var string = Strings.loadFile(StringFile.OBJECT_CUSTOM_TEXT).getString(Integer.toString(objID.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadOverride(final OverrideString str, final Object... values) {
	var string = Strings.loadFile(StringFile.OVERRIDES).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadSettings(final SettingsString str, final Object... values) {
	var string = Strings.loadFile(StringFile.SETTINGS).getString(Integer.toString(str.ordinal()));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }

    public static String loadEra(final int strID, final Object... values) {
	var string = Strings.loadFile(StringFile.TIME).getString(Integer.toString(strID));
	for (var v = 0; v < values.length; v++) {
	    string = string.replace(Strings.REPLACE_PREFIX + Integer.toString(v), values[v].toString());
	}
	return string;
    }
}

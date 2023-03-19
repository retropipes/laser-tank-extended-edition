/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.gui.MainContentFactory;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.integration.Integration;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.settings.Settings;

public class LaserTankEE {
	// Constants
	private static Application application;
	private static String PROGRAM_NAME = "LaserTankEE"; //$NON-NLS-1$
	private static String ERROR_MESSAGE = null;
	private static String ERROR_TITLE = null;
	private static String WARNING_MESSAGE = null;
	private static String WARNING_TITLE = null;
	private static final int CONTENT_SIZE = 768;

	// Methods
	public static Application getApplication() {
		return LaserTankEE.application;
	}

	public static void logError(final Throwable t) {
		CommonDialogs.showErrorDialog(LaserTankEE.ERROR_MESSAGE, LaserTankEE.ERROR_TITLE);
		Diane.handleError(t);
	}

	public static void logErrorDirectly(final Throwable t) {
		Diane.handleError(t);
	}

	public static void logWarning(final Throwable t) {
		Diane.handleWarning(t);
		CommonDialogs.showTitledDialog(LaserTankEE.WARNING_MESSAGE, LaserTankEE.WARNING_TITLE);
	}

	public static void logWarningDirectly(final Throwable t) {
		Diane.handleWarning(t);
	}

	private static void initStrings() {
		Strings.setDefaultLanguage();
		LaserTankEE.ERROR_TITLE = Strings.loadError(ErrorString.ERROR_TITLE);
		LaserTankEE.ERROR_MESSAGE = Strings.loadError(ErrorString.ERROR_MESSAGE);
		LaserTankEE.WARNING_TITLE = Strings.loadError(ErrorString.WARNING_TITLE);
		LaserTankEE.WARNING_MESSAGE = Strings.loadError(ErrorString.WARNING_MESSAGE);
	}

	public static void main(final String[] args) {
		// Integrate with host platform
		final var ni = new Integration();
		ni.configureLookAndFeel();
		// Install error handler
		Diane.installDefaultErrorHandler(LaserTankEE.PROGRAM_NAME);
		// Initialize strings
		LaserTankEE.initStrings();
		// Set Up Common Dialogs
		CommonDialogs.setDefaultTitle(LaserTankEE.PROGRAM_NAME);
		// Create main window
		MainContentFactory.setContentSize(LaserTankEE.CONTENT_SIZE, LaserTankEE.CONTENT_SIZE);
		MainWindow.createMainWindow(LaserTankEE.CONTENT_SIZE, LaserTankEE.CONTENT_SIZE);
		// Create and initialize application
		LaserTankEE.application = new Application();
		LaserTankEE.application.init();
		// Initialize preferences
		Settings.readSettings();
		Strings.activeLanguageChanged(Settings.getLanguageID());
		// Register platform hooks
		MainWindow.mainWindow().setMenus(LaserTankEE.getApplication().getMenuManager().getMenuBar());
		ni.setAboutHandler(LaserTankEE.application.getAboutDialog());
		ni.setPreferencesHandler(new SettingsInvoker());
		ni.setQuitHandler(LaserTankEE.application.getGUIManager());
		// Display GUI
		LaserTankEE.application.bootGUI();
	}

	private LaserTankEE() {
		// Do nothing
	}

	private static class SettingsInvoker implements PreferencesHandler {
		public SettingsInvoker() {
		}

		@Override
		public void handlePreferences(final PreferencesEvent e) {
			Settings.showSettings();
		}
	}
}

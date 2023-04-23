/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import java.awt.Dimension;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.gui.Screen;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.MessageString;
import com.puttysoftware.lasertank.locale.Strings;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.gui.MainContentFactory;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.integration.Integration;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.settings.Settings;

public class LaserTankEE {
	// Constants
	private static String PROGRAM_NAME = "LaserTankEE"; //$NON-NLS-1$
	private static CustomErrorHandler errorHandler;
	static String ERROR_MESSAGE = null;
	static String ERROR_TITLE = null;
	static String WARNING_MESSAGE = null;
	static String WARNING_TITLE = null;
	static final boolean DEBUG = true;
	private static final int CONTENT_SIZE = 768;
	private static MainWindow masterFrame;
	private static AboutDialog about;
	private static Game game;
	private static ArenaManager arenaMgr;
	private static MenubarHost menus;
	private static Editor editor;
	private static MainScreen mainScreen;
	private static Screen currentScreen, lastScreen;

	private static final int VERSION_MAJOR = 18;
	private static final int VERSION_MINOR = 0;
	private static final int VERSION_BUGFIX = 0;
	private static final int VERSION_BETA = 2;

	public static String getLogoVersionString() {
		if (isBetaModeEnabled()) {
			return Strings.loadCommon(CommonString.LOGO_VERSION_PREFIX) + VERSION_MAJOR
					+ Strings.loadCommon(CommonString.NOTL_PERIOD) + VERSION_MINOR
					+ Strings.loadCommon(CommonString.NOTL_PERIOD) + VERSION_BUGFIX
					+ Strings.loadCommon(CommonString.BETA_SHORT) + VERSION_BETA;
		}
		return Strings.loadCommon(CommonString.LOGO_VERSION_PREFIX) + VERSION_MAJOR
				+ Strings.loadCommon(CommonString.NOTL_PERIOD) + VERSION_MINOR
				+ Strings.loadCommon(CommonString.NOTL_PERIOD) + VERSION_BUGFIX;
	}

	private static String getVersionString() {
		if (isBetaModeEnabled()) {
			return Strings.loadCommon(CommonString.EMPTY) + VERSION_MAJOR
					+ Strings.loadCommon(CommonString.NOTL_PERIOD) + VERSION_MINOR
					+ Strings.loadCommon(CommonString.NOTL_PERIOD) + VERSION_BUGFIX
					+ Strings.loadMessage(MessageString.BETA) + VERSION_BETA;
		}
		return Strings.loadCommon(CommonString.EMPTY) + VERSION_MAJOR
				+ Strings.loadCommon(CommonString.NOTL_PERIOD) + VERSION_MINOR
				+ Strings.loadCommon(CommonString.NOTL_PERIOD) + VERSION_BUGFIX;
	}

	private static boolean isBetaModeEnabled() {
		return VERSION_BETA > 0;
	}

	public static void logError(final Throwable t) {
		errorHandler.handleError(t);
	}

	public static void logErrorDirectly(final Throwable t) {
		errorHandler.handleErrorDirectly(t);
	}

	public static void logWarning(final Throwable t) {
		errorHandler.handleWarning(t);
	}

	public static void logWarningDirectly(final Throwable t) {
		errorHandler.handleWarningDirectly(t);
	}

	private static void initStrings() {
		Strings.setDefaultLanguage();
		ERROR_TITLE = Strings.loadError(ErrorString.ERROR_TITLE);
		ERROR_MESSAGE = Strings.loadError(ErrorString.ERROR_MESSAGE);
		WARNING_TITLE = Strings.loadError(ErrorString.WARNING_TITLE);
		WARNING_MESSAGE = Strings.loadError(ErrorString.WARNING_MESSAGE);
	}

	public static void activeLanguageChanged() {
		// Rebuild menus
		menus.populateMenuBar();
		// Fire hooks
		Game.get().activeLanguageChanged();
		Editor.get().activeLanguageChanged();
	}

	static void leaveScreen() {
		currentScreen.hideScreen();
	}

	static AboutDialog getAboutDialog() {
		return about;
	}

	public static Screen getLastScreen() {
		return lastScreen;
	}

	public static MainScreen getMainScreen() {
		return mainScreen;
	}

	public static String[] getLevelInfoList() {
		return arenaMgr.getArena().getLevelInfoList();
	}

	public static MenubarHost getMenus() {
		return menus;
	}

	public static boolean onGameScreen() {
		return currentScreen == game;
	}

	public static boolean onEditorScreen() {
		return currentScreen == editor;
	}

	public static boolean onMainScreen() {
		return currentScreen == mainScreen;
	}

	public static void setOnEditorScreen() {
		lastScreen = currentScreen;
		currentScreen = editor;
		tearDownFormerMode();
		editor.showScreen();
		menus.activateEditorCommands();
	}

	public static void setOnGameScreen() {
		lastScreen = currentScreen;
		currentScreen = game;
		tearDownFormerMode();
		game.showScreen();
		menus.activateGameCommands();
	}

	static void setOnMainScreen() {
		lastScreen = currentScreen;
		currentScreen = mainScreen;
		tearDownFormerMode();
		mainScreen.showScreen();
		menus.activateGUICommands();
		masterFrame.pack();
	}

	public static void showMessage(final String msg) {
		currentScreen.statusMessage(msg);
	}

	private static void tearDownFormerMode() {
		if (lastScreen != null) {
			lastScreen.hideScreen();
		}
	}

	public static void updateLevelInfoList() {
		JFrame loadFrame;
		JProgressBar loadBar;
		loadFrame = new JFrame(Strings.loadDialog(DialogString.UPDATING_LEVEL_INFO));
		loadBar = new JProgressBar();
		loadBar.setIndeterminate(true);
		loadBar.setPreferredSize(new Dimension(600, 20));
		loadFrame.getContentPane().add(loadBar);
		loadFrame.setResizable(false);
		loadFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		loadFrame.pack();
		loadFrame.setVisible(true);
		arenaMgr.getArena().generateLevelInfoList();
		loadFrame.setVisible(false);
	}

	public static void main(final String[] args) {
		// Integrate with host platform
		final var ni = new Integration();
		ni.configureLookAndFeel();
		// Install error handler
		errorHandler = new CustomErrorHandler(PROGRAM_NAME);
		Diane.installCustomErrorHandler(errorHandler);
		// Initialize strings
		initStrings();
		// Set Up Common Dialogs
		CommonDialogs.setDefaultTitle(PROGRAM_NAME);
		// Create main window
		MainContentFactory.setContentSize(CONTENT_SIZE, CONTENT_SIZE);
		MainWindow.createMainWindow(CONTENT_SIZE, CONTENT_SIZE);
		// Create and initialize game
		masterFrame = MainWindow.mainWindow();
		currentScreen = null;
		lastScreen = null;
		// Create Managers
		menus = new MenubarHost();
		about = new AboutDialog(getVersionString());
		mainScreen = new MainScreen();
		game = Game.get();
		editor = Editor.get();
		// Initialize preferences
		Settings.readSettings();
		Strings.activeLanguageChanged(Settings.getLanguageID());
		// Register platform hooks
		MainWindow.mainWindow().setMenus(getMenus().getMenuBar());
		ni.setAboutHandler(getAboutDialog());
		ni.setPreferencesHandler(new SettingsInvoker());
		ni.setQuitHandler(getMainScreen());
		// Set up default error handling
		final UncaughtExceptionHandler eh = (t, e) -> logWarning(e);
		final Runnable doRun = () -> Thread.currentThread().setUncaughtExceptionHandler(eh);
		try {
			SwingUtilities.invokeAndWait(doRun);
		} catch (InvocationTargetException | InterruptedException e) {
			logError(e);
		}
		// Display GUI
		mainScreen.showGUI();
	}

	private LaserTankEE() {
		// Do nothing
	}
}

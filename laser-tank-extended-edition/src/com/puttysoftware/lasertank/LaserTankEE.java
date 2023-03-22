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
import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
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
	static String ERROR_MESSAGE = null;
	static String ERROR_TITLE = null;
	static String WARNING_MESSAGE = null;
	static String WARNING_TITLE = null;
	private static boolean DIALOG_SHOWING = false;
	private static final int CONTENT_SIZE = 768;
	private static MainWindow masterFrame;
	private static AboutDialog about;
	private static Game gameMgr;
	private static ArenaManager arenaMgr;
	private static MenubarHost menubarHost;
	private static Editor editor;
	private static MainScreen mainScreen;
	private static Screen mode, formerMode;

	private static final int VERSION_MAJOR = 18;
	private static final int VERSION_MINOR = 0;
	private static final int VERSION_BUGFIX = 0;
	private static final int VERSION_BETA = 1;

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
		if (!DIALOG_SHOWING) {
			DIALOG_SHOWING = true;
			new Thread() {
				@Override
				public void run() {
					Sounds.play(Sound.ERROR);
					CommonDialogs.showErrorDialog(LaserTankEE.ERROR_MESSAGE, LaserTankEE.ERROR_TITLE);
					Diane.handleError(t);
					DIALOG_SHOWING = false;
				}
			}.start();
		} else {
			logErrorDirectly(t);
		}
	}

	public static void logErrorDirectly(final Throwable t) {
		Diane.handleError(t);
	}

	public static void logWarning(final Throwable t) {
		Diane.handleWarning(t);
		if (!DIALOG_SHOWING) {
			DIALOG_SHOWING = true;
			new Thread() {
				@Override
				public void run() {
					Sounds.play(Sound.ERROR);
					CommonDialogs.showTitledDialog(LaserTankEE.WARNING_MESSAGE, LaserTankEE.WARNING_TITLE);
					DIALOG_SHOWING = false;
				}
			}.start();
		} else {
			logWarningDirectly(t);
		}
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

	public static void activeLanguageChanged() {
		// Rebuild menus
		menubarHost.populateMenuBar();
		// Fire hooks
		getGame().activeLanguageChanged();
		getEditor().activeLanguageChanged();
	}

	static void exitCurrentMode() {
		mode.hideScreen();
	}

	static AboutDialog getAboutDialog() {
		return about;
	}

	public static ArenaManager getArenaManager() {
		if (arenaMgr == null) {
			arenaMgr = new ArenaManager();
		}
		return arenaMgr;
	}

	public static Editor getEditor() {
		return editor;
	}

	public static Screen getFormerMode() {
		return formerMode;
	}

	public static Game getGame() {
		return gameMgr;
	}

	public static MainScreen getMainScreen() {
		return mainScreen;
	}

	public static String[] getLevelInfoList() {
		return arenaMgr.getArena().getLevelInfoList();
	}

	public static MenubarHost getMenus() {
		return menubarHost;
	}

	public static boolean isInGameMode() {
		return mode == gameMgr;
	}

	public static boolean isInEditorMode() {
		return mode == editor;
	}

	public static boolean isInMainMode() {
		return mode == mainScreen;
	}

	public static void setInEditorMode() {
		formerMode = mode;
		mode = editor;
		tearDownFormerMode();
		editor.showScreen();
		menubarHost.activateEditorCommands();
	}

	public static void setInGameMode() {
		formerMode = mode;
		mode = gameMgr;
		tearDownFormerMode();
		gameMgr.showScreen();
		menubarHost.activateGameCommands();
	}

	static void setInMainMode() {
		formerMode = mode;
		mode = mainScreen;
		tearDownFormerMode();
		mainScreen.showScreen();
		menubarHost.activateGUICommands();
		masterFrame.pack();
	}

	public static void showMessage(final String msg) {
		mode.statusMessage(msg);
	}

	private static void tearDownFormerMode() {
		if (formerMode != null) {
			formerMode.hideScreen();
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
		Diane.installDefaultErrorHandler(LaserTankEE.PROGRAM_NAME);
		// Initialize strings
		LaserTankEE.initStrings();
		// Set Up Common Dialogs
		CommonDialogs.setDefaultTitle(LaserTankEE.PROGRAM_NAME);
		// Create main window
		MainContentFactory.setContentSize(LaserTankEE.CONTENT_SIZE, LaserTankEE.CONTENT_SIZE);
		MainWindow.createMainWindow(LaserTankEE.CONTENT_SIZE, LaserTankEE.CONTENT_SIZE);
		// Create and initialize game
		masterFrame = MainWindow.mainWindow();
		mode = null;
		formerMode = null;
		// Create Managers
		menubarHost = new MenubarHost();
		about = new AboutDialog(getVersionString());
		mainScreen = new MainScreen();
		gameMgr = new Game();
		editor = new Editor();
		// Initialize preferences
		Settings.readSettings();
		Strings.activeLanguageChanged(Settings.getLanguageID());
		// Register platform hooks
		MainWindow.mainWindow().setMenus(getMenus().getMenuBar());
		ni.setAboutHandler(getAboutDialog());
		ni.setPreferencesHandler(new SettingsInvoker());
		ni.setQuitHandler(getMainScreen());
		// Set up default error handling
		final UncaughtExceptionHandler eh = (t, e) -> LaserTankEE.logWarning(e);
		final Runnable doRun = () -> Thread.currentThread().setUncaughtExceptionHandler(eh);
		try {
			SwingUtilities.invokeAndWait(doRun);
		} catch (InvocationTargetException | InterruptedException e) {
			LaserTankEE.logError(e);
		}
		// Display GUI
		mainScreen.showGUI();
	}

	private LaserTankEE() {
		// Do nothing
	}
}

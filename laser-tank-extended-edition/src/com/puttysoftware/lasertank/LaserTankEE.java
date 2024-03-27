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

import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.asset.music.MusicIndex;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.error.ErrorHandlerInstaller;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.gui.MainContentFactory;
import com.puttysoftware.lasertank.gui.MainWindow;
import com.puttysoftware.lasertank.gui.Screen;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.integration.Integration;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.update.ProductData;

public class LaserTankEE {
    // Constants
    private static String PROGRAM_NAME = "LaserTankEE"; //$NON-NLS-1$
    private static GameErrorHandler errorHandler;
    static String ERROR_MESSAGE = null;
    static String ERROR_TITLE = null;
    static String WARNING_MESSAGE = null;
    static String WARNING_TITLE = null;
    static final boolean DEBUG = true;
    private static final int CONTENT_SIZE = 768;
    private static MainWindow masterFrame;
    private static AboutDialog about;
    private static Game game;
    private static MenubarHost menus;
    private static Editor editor;
    private static MainScreen mainScreen;
    private static Screen currentScreen, lastScreen;
    private static final ProductData VERSION = new ProductData(1, 0, 0, 1, 1);

    public static void activeLanguageChanged() {
	// Rebuild menus
	LaserTankEE.menus.populateMenuBar();
	// Fire hooks
	Game.get().activeLanguageChanged();
	Editor.get().activeLanguageChanged();
    }

    public static void disableRecording() {
	LaserTankEE.menus.disableRecording();
    }

    static AboutDialog getAboutDialog() {
	return LaserTankEE.about;
    }

    public static Screen getLastScreen() {
	return LaserTankEE.lastScreen;
    }

    public static String[] getLevelInfoList() {
	return ArenaManager.getArena().getLevelInfoList();
    }

    public static String getLogoVersionString() {
	return "v" + LaserTankEE.VERSION.getShortVersionString(); //$NON-NLS-1$
    }

    static MenubarHost getMenus() {
	return LaserTankEE.menus;
    }

    public static MusicIndex getScreenMusic() {
	if (LaserTankEE.currentScreen != null) {
	    return LaserTankEE.currentScreen.music();
	}
	return null;
    }

    private static String getVersionString() {
	return LaserTankEE.VERSION.getVersionString();
    }

    private static void initStrings() {
	Strings.setDefaultLanguage();
	LaserTankEE.ERROR_TITLE = Strings.loadError(ErrorString.ERROR_TITLE);
	LaserTankEE.ERROR_MESSAGE = Strings.loadError(ErrorString.ERROR_MESSAGE);
	LaserTankEE.WARNING_TITLE = Strings.loadError(ErrorString.WARNING_TITLE);
	LaserTankEE.WARNING_MESSAGE = Strings.loadError(ErrorString.WARNING_MESSAGE);
    }

    static void leaveScreen() {
	LaserTankEE.currentScreen.hideScreen();
    }

    public static void logError(final Throwable t) {
	LaserTankEE.errorHandler.handleError(t);
    }

    public static void logErrorDirectly(final Throwable t) {
	LaserTankEE.errorHandler.handleErrorDirectly(t);
    }

    public static void logWarning(final Throwable t) {
	LaserTankEE.errorHandler.handleWarning(t);
    }

    public static void logWarningDirectly(final Throwable t) {
	LaserTankEE.errorHandler.handleWarningDirectly(t);
    }

    public static void main(final String[] args) {
	// Integrate with host platform
	final var ni = new Integration();
	ni.configureLookAndFeel();
	// Install error handler
	LaserTankEE.errorHandler = new GameErrorHandler(LaserTankEE.PROGRAM_NAME);
	ErrorHandlerInstaller.installErrorHandler(LaserTankEE.errorHandler);
	// Initialize strings
	LaserTankEE.initStrings();
	// Set Up Common Dialogs
	CommonDialogs.setDefaultTitle(LaserTankEE.PROGRAM_NAME);
	// Create main window
	MainContentFactory.setContentSize(LaserTankEE.CONTENT_SIZE, LaserTankEE.CONTENT_SIZE);
	MainWindow.createMainWindow(LaserTankEE.CONTENT_SIZE, LaserTankEE.CONTENT_SIZE);
	// Create and initialize game
	LaserTankEE.masterFrame = MainWindow.mainWindow();
	LaserTankEE.currentScreen = null;
	LaserTankEE.lastScreen = null;
	// Create Managers
	LaserTankEE.menus = new MenubarHost();
	LaserTankEE.about = new AboutDialog(LaserTankEE.getVersionString());
	LaserTankEE.mainScreen = new MainScreen();
	LaserTankEE.game = Game.get();
	LaserTankEE.editor = Editor.get();
	// Initialize preferences
	Settings.readSettings();
	Strings.activeLanguageChanged(Settings.getLanguageID());
	// Register platform hooks
	MainWindow.mainWindow().setMenus(LaserTankEE.menus.getMenuBar());
	ni.setAboutHandler(LaserTankEE.getAboutDialog());
	ni.setPreferencesHandler(new SettingsInvoker());
	ni.setQuitHandler(LaserTankEE.mainScreen);
	// Set up default error handling
	final UncaughtExceptionHandler eh = (t, e) -> LaserTankEE.logWarning(e);
	final Runnable doRun = () -> Thread.currentThread().setUncaughtExceptionHandler(eh);
	try {
	    SwingUtilities.invokeAndWait(doRun);
	} catch (InvocationTargetException | InterruptedException e) {
	    LaserTankEE.logError(e);
	}
	// Display GUI
	LaserTankEE.mainScreen.showGUI();
    }

    public static boolean onEditorScreen() {
	return LaserTankEE.currentScreen == LaserTankEE.editor;
    }

    public static boolean onGameScreen() {
	return LaserTankEE.currentScreen == LaserTankEE.game;
    }

    public static boolean onMainScreen() {
	return LaserTankEE.currentScreen == LaserTankEE.mainScreen;
    }

    public static boolean quitHandler() {
	return LaserTankEE.mainScreen.quitHandler();
    }

    public static void setOnEditorScreen() {
	LaserTankEE.lastScreen = LaserTankEE.currentScreen;
	LaserTankEE.currentScreen = LaserTankEE.editor;
	LaserTankEE.tearDownFormerMode();
	LaserTankEE.editor.showScreen();
	LaserTankEE.menus.activateEditorCommands();
    }

    public static void setOnGameScreen() {
	LaserTankEE.lastScreen = LaserTankEE.currentScreen;
	LaserTankEE.currentScreen = LaserTankEE.game;
	LaserTankEE.tearDownFormerMode();
	LaserTankEE.game.showScreen();
	LaserTankEE.menus.activateGameCommands();
    }

    static void setOnMainScreen() {
	LaserTankEE.lastScreen = LaserTankEE.currentScreen;
	LaserTankEE.currentScreen = LaserTankEE.mainScreen;
	LaserTankEE.tearDownFormerMode();
	LaserTankEE.mainScreen.showScreen();
	LaserTankEE.menus.activateGUICommands();
	LaserTankEE.masterFrame.pack();
    }

    public static void showGUI() {
	LaserTankEE.mainScreen.showGUI();
    }

    public static void showMessage(final String msg) {
	LaserTankEE.currentScreen.statusMessage(msg);
    }

    private static void tearDownFormerMode() {
	if (LaserTankEE.lastScreen != null) {
	    LaserTankEE.lastScreen.hideScreen();
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
	ArenaManager.getArena().generateLevelInfoList();
	loadFrame.setVisible(false);
    }

    public static void updateMenuItemState() {
	LaserTankEE.menus.updateMenuItemState();
    }

    private LaserTankEE() {
	// Do nothing
    }
}

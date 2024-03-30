/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.asset.music.MusicIndex;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.gui.MainContentFactory;
import com.puttysoftware.lasertank.gui.MainWindow;
import com.puttysoftware.lasertank.gui.Screen;
import com.puttysoftware.lasertank.integration.Integration;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;
import com.puttysoftware.lasertank.update.ProductData;

public class LaserTankEE {
    // Constants
    private static final int CONTENT_SIZE = 768;
    private static MainWindow masterFrame;
    private static AboutDialog about;
    private static MenubarHost menus;
    private static Editor editor;
    private static MainScreen mainScreen;
    private static Screen currentScreen, lastScreen;
    private static ProductData VERSION;

    public static void activeLanguageChanged() {
	// Rebuild menus
	LaserTankEE.menus.populateMenuBar();
	// Fire hooks
	Game.activeLanguageChanged();
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

    static void leaveScreen() {
	LaserTankEE.currentScreen.hideScreen();
    }

    public static void main(final String[] args) {
	// Integrate with host platform
	final var ni = new Integration();
	ni.configureLookAndFeel();
	// Install error handler
	AppTaskManager.installDefaultErrorHandler();
	// Create main window
	MainContentFactory.setContentSize(LaserTankEE.CONTENT_SIZE, LaserTankEE.CONTENT_SIZE);
	MainWindow.createMainWindow(LaserTankEE.CONTENT_SIZE, LaserTankEE.CONTENT_SIZE);
	// Create and initialize game
	LaserTankEE.masterFrame = MainWindow.mainWindow();
	LaserTankEE.currentScreen = null;
	LaserTankEE.lastScreen = null;
	// Create Managers
	LaserTankEE.VERSION = new ProductData(1, 0, 0, 1, 1);
	LaserTankEE.menus = new MenubarHost();
	LaserTankEE.about = new AboutDialog(LaserTankEE.getVersionString());
	LaserTankEE.mainScreen = new MainScreen();
	LaserTankEE.editor = Editor.get();
	// Initialize preferences
	Settings.readSettings();
	Strings.activeLanguageChanged(Settings.getLanguageID());
	// Register platform hooks
	MainWindow.mainWindow().setMenus(LaserTankEE.menus.getMenuBar());
	ni.setAboutHandler(LaserTankEE.getAboutDialog());
	ni.setPreferencesHandler(new SettingsInvoker());
	ni.setQuitHandler(LaserTankEE.mainScreen);
	// Display GUI
	MainScreen.showGUI();
    }

    public static boolean onEditorScreen() {
	return LaserTankEE.currentScreen == LaserTankEE.editor;
    }

    public static boolean onGameScreen() {
	return LaserTankEE.currentScreen == Game.get();
    }

    public static boolean onMainScreen() {
	return LaserTankEE.currentScreen == LaserTankEE.mainScreen;
    }

    public static boolean quitHandler() {
	return MainScreen.quitHandler();
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
	LaserTankEE.currentScreen = Game.get();
	LaserTankEE.tearDownFormerMode();
	Game.get().showScreen();
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
	MainScreen.showGUI();
    }

    public static void showMessage(final String msg) {
	Screen.statusMessage(msg);
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

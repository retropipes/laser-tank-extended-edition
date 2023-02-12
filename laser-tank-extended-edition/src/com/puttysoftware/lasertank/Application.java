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

public final class Application {
    private static final int VERSION_MAJOR = 18;
    private static final int VERSION_MINOR = 0;
    private static final int VERSION_BUGFIX = 0;
    private static final int VERSION_BETA = 1;

    public static String getLogoVersionString() {
	if (Application.isBetaModeEnabled()) {
	    return Strings.loadCommon(CommonString.LOGO_VERSION_PREFIX) + Application.VERSION_MAJOR
		    + Strings.loadCommon(CommonString.NOTL_PERIOD) + Application.VERSION_MINOR
		    + Strings.loadCommon(CommonString.NOTL_PERIOD) + Application.VERSION_BUGFIX
		    + Strings.loadCommon(CommonString.BETA_SHORT) + Application.VERSION_BETA;
	}
	return Strings.loadCommon(CommonString.LOGO_VERSION_PREFIX) + Application.VERSION_MAJOR
		+ Strings.loadCommon(CommonString.NOTL_PERIOD) + Application.VERSION_MINOR
		+ Strings.loadCommon(CommonString.NOTL_PERIOD) + Application.VERSION_BUGFIX;
    }

    private static String getVersionString() {
	if (Application.isBetaModeEnabled()) {
	    return Strings.loadCommon(CommonString.EMPTY) + Application.VERSION_MAJOR
		    + Strings.loadCommon(CommonString.NOTL_PERIOD) + Application.VERSION_MINOR
		    + Strings.loadCommon(CommonString.NOTL_PERIOD) + Application.VERSION_BUGFIX
		    + Strings.loadMessage(MessageString.BETA) + Application.VERSION_BETA;
	}
	return Strings.loadCommon(CommonString.EMPTY) + Application.VERSION_MAJOR
		+ Strings.loadCommon(CommonString.NOTL_PERIOD) + Application.VERSION_MINOR
		+ Strings.loadCommon(CommonString.NOTL_PERIOD) + Application.VERSION_BUGFIX;
    }

    private static boolean isBetaModeEnabled() {
	return Application.VERSION_BETA > 0;
    }

    // Fields
    private final MainWindow masterFrame;
    private AboutDialog about;
    private Game gameMgr;
    private ArenaManager arenaMgr;
    private MenuManager menuMgr;
    private Editor editor;
    private GUIManager guiMgr;
    private Screen mode, formerMode;

    // Constructors
    public Application() {
	this.masterFrame = MainWindow.mainWindow();
	this.mode = null;
	this.formerMode = null;
    }

    void init() {
	// Create Managers
	this.menuMgr = new MenuManager();
	this.about = new AboutDialog(Application.getVersionString());
	this.guiMgr = new GUIManager();
	this.gameMgr = new Game();
	this.editor = new Editor();
    }

    void bootGUI() {
	// Set up default error handling
	final UncaughtExceptionHandler eh = (t, e) -> LaserTankEE.logWarning(e);
	final Runnable doRun = () -> Thread.currentThread().setUncaughtExceptionHandler(eh);
	try {
	    SwingUtilities.invokeAndWait(doRun);
	} catch (InvocationTargetException | InterruptedException e) {
	    LaserTankEE.logError(e);
	}
	// Boot GUI
	this.guiMgr.showGUI();
    }

    // Methods
    public void activeLanguageChanged() {
	// Rebuild menus
	this.menuMgr.populateMenuBar();
	// Fire hooks
	this.getGameManager().activeLanguageChanged();
	this.getEditor().activeLanguageChanged();
    }

    void exitCurrentMode() {
	this.mode.hideScreen();
    }

    AboutDialog getAboutDialog() {
	return this.about;
    }

    public ArenaManager getArenaManager() {
	if (this.arenaMgr == null) {
	    this.arenaMgr = new ArenaManager();
	}
	return this.arenaMgr;
    }

    public Editor getEditor() {
	return this.editor;
    }

    public Screen getFormerMode() {
	return this.formerMode;
    }

    public Game getGameManager() {
	return this.gameMgr;
    }

    public GUIManager getGUIManager() {
	return this.guiMgr;
    }

    public String[] getLevelInfoList() {
	return this.arenaMgr.getArena().getLevelInfoList();
    }

    public MenuManager getMenuManager() {
	return this.menuMgr;
    }

    public boolean isInGameMode() {
	return this.mode == this.gameMgr;
    }

    public boolean isInEditorMode() {
	return this.mode == this.editor;
    }

    public boolean isInGUIMode() {
	return this.mode == this.guiMgr;
    }

    public void setInEditor() {
	this.formerMode = this.mode;
	this.mode = this.editor;
	this.tearDownFormerMode();
	this.editor.showScreen();
	this.menuMgr.activateEditorCommands();
    }

    public void setInGame() {
	this.formerMode = this.mode;
	this.mode = this.gameMgr;
	this.tearDownFormerMode();
	this.gameMgr.showScreen();
	this.menuMgr.activateGameCommands();
    }

    void setInGUI() {
	this.formerMode = this.mode;
	this.mode = this.guiMgr;
	this.tearDownFormerMode();
	this.guiMgr.showScreen();
	this.menuMgr.activateGUICommands();
	this.masterFrame.pack();
    }

    public void showMessage(final String msg) {
	this.mode.statusMessage(msg);
    }

    private void tearDownFormerMode() {
	if (this.formerMode != null) {
	    this.formerMode.hideScreen();
	}
    }

    public void updateLevelInfoList() {
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
	this.arenaMgr.getArena().generateLevelInfoList();
	loadFrame.setVisible(false);
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import java.awt.GridLayout;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.assets.Logos;
import com.puttysoftware.lasertank.assets.Music;
import com.puttysoftware.lasertank.assets.Musics;
import com.puttysoftware.lasertank.engine.gui.MainWindow;
import com.puttysoftware.lasertank.engine.gui.Screen;
import com.puttysoftware.lasertank.engine.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.CleanupTask;

public class MainScreen extends Screen implements QuitHandler {
    // Fields
    private JLabel logoLabel;
    private final MainScreenCloseHandler cHandler = new MainScreenCloseHandler(this);
    private final MainScreenFocusHandler fHandler = new MainScreenFocusHandler();

    // Constructors
    public MainScreen() {
    }

    @Override
    public void handleQuitRequestWith(final QuitEvent e, final QuitResponse response) {
	final var okToQuit = this.quitHandler();
	if (okToQuit) {
	    response.performQuit();
	} else {
	    response.cancelQuit();
	}
    }

    @Override
    protected void hideScreenHook() {
	MainWindow.mainWindow().removeWindowFocusListener(this.fHandler);
	MainWindow.mainWindow().removeWindowListener(this.cHandler);
    }

    @Override
    protected void populateMainPanel() {
	this.theContent.setLayout(new GridLayout(1, 1));
	this.logoLabel = new JLabel(Strings.loadCommon(CommonString.EMPTY), null, SwingConstants.CENTER);
	this.logoLabel.setLabelFor(null);
	this.logoLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
	final var logo = Logos.getOpening();
	this.logoLabel.setIcon(logo);
	this.theContent.add(this.logoLabel);
	this.setTitle(GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
    }

    // Methods
    public boolean quitHandler() {
	final var mm = ArenaManager.get();
	var saved = true;
	var status = CommonDialogs.DEFAULT_OPTION;
	if (mm.getDirty()) {
	    status = ArenaManager.showSaveDialog();
	    if (status == CommonDialogs.YES_OPTION) {
		saved = mm.saveArena(mm.isArenaProtected());
	    } else if (status == CommonDialogs.CANCEL_OPTION) {
		saved = false;
	    } else {
		mm.setDirty(false);
	    }
	}
	if (saved) {
	    Settings.writeSettings();
	    // Run cleanup task
	    CleanupTask.cleanUp();
	}
	return saved;
    }

    public void showGUI() {
	LaserTankEE.setOnMainScreen();
    }

    @Override
    protected void showScreenHook() {
	MainWindow.mainWindow().addWindowListener(this.cHandler);
	MainWindow.mainWindow().addWindowFocusListener(this.fHandler);
	Musics.play(Music.MAIN_SCREEN);
    }
}

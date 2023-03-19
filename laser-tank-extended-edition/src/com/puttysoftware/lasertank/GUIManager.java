/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import java.awt.GridLayout;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.gui.Screen;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.asset.Logos;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.CleanupTask;

public class GUIManager extends Screen implements QuitHandler {
	private class CloseHandler implements WindowListener {
		public CloseHandler() {
			// Do nothing
		}

		@Override
		public void windowActivated(final WindowEvent arg0) {
			// Do nothing
		}

		@Override
		public void windowClosed(final WindowEvent arg0) {
			// Do nothing
		}

		@Override
		public void windowClosing(final WindowEvent arg0) {
			if (GUIManager.this.quitHandler()) {
				System.exit(0);
			}
		}

		@Override
		public void windowDeactivated(final WindowEvent arg0) {
			// Do nothing
		}

		@Override
		public void windowDeiconified(final WindowEvent arg0) {
			// Do nothing
		}

		@Override
		public void windowIconified(final WindowEvent arg0) {
			// Do nothing
		}

		@Override
		public void windowOpened(final WindowEvent arg0) {
			// Do nothing
		}
	}

	private static class FocusHandler implements WindowFocusListener {
		public FocusHandler() {
			// Do nothing
		}

		@Override
		public void windowGainedFocus(final WindowEvent e) {
			LaserTankEE.getApplication().getMenuManager().updateMenuItemState();
		}

		@Override
		public void windowLostFocus(final WindowEvent e) {
			// Do nothing
		}
	}

	// Fields
	private JLabel logoLabel;
	private final CloseHandler cHandler = new CloseHandler();
	private final FocusHandler fHandler = new FocusHandler();

	// Constructors
	public GUIManager() {
	}

	// Methods
	public boolean quitHandler() {
		final var mm = LaserTankEE.getApplication().getArenaManager();
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

	public void showGUI() {
		final var app = LaserTankEE.getApplication();
		app.setInGUI();
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
	protected void showScreenHook() {
		MainWindow.mainWindow().addWindowListener(this.cHandler);
		MainWindow.mainWindow().addWindowFocusListener(this.fHandler);
	}

	@Override
	protected void hideScreenHook() {
		MainWindow.mainWindow().removeWindowFocusListener(this.fHandler);
		MainWindow.mainWindow().removeWindowListener(this.cHandler);
	}
}

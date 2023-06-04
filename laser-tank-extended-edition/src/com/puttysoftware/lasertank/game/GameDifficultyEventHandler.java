package com.puttysoftware.lasertank.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

class GameDifficultyEventHandler implements ActionListener, WindowListener {
    /**
     *
     */
    private final Game game;

    public GameDifficultyEventHandler(final Game theGame) {
	this.game = theGame;
	// Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		final var cmd = e.getActionCommand();
		final var gm = GameDifficultyEventHandler.this.game;
		if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
		    gm.okButtonClicked();
		} else {
		    gm.cancelButtonClicked();
		}
	    }
	}.start();
    }

    @Override
    public void windowActivated(final WindowEvent e) {
	// Ignore
    }

    @Override
    public void windowClosed(final WindowEvent e) {
	// Ignore
    }

    @Override
    public void windowClosing(final WindowEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		GameDifficultyEventHandler.this.game.cancelButtonClicked();
	    }
	}.start();
    }

    @Override
    public void windowDeactivated(final WindowEvent e) {
	// Ignore
    }

    @Override
    public void windowDeiconified(final WindowEvent e) {
	// Ignore
    }

    @Override
    public void windowIconified(final WindowEvent e) {
	// Ignore
    }

    @Override
    public void windowOpened(final WindowEvent e) {
	// Ignore
    }
}
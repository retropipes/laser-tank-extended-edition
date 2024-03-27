package com.puttysoftware.lasertank.game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import com.puttysoftware.lasertank.LaserTankEE;

class GameFocusHandler implements WindowFocusListener {
    public GameFocusHandler() {
	// Do nothing
    }

    @Override
    public void windowGainedFocus(final WindowEvent e) {
	LaserTankEE.updateMenuItemState();
    }

    @Override
    public void windowLostFocus(final WindowEvent e) {
	// Do nothing
    }
}
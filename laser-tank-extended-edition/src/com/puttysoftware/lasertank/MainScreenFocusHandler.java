package com.puttysoftware.lasertank;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

class MainScreenFocusHandler implements WindowFocusListener {
    public MainScreenFocusHandler() {
	// Do nothing
    }

    @Override
    public void windowGainedFocus(final WindowEvent e) {
	LaserTankEE.getMenus().updateMenuItemState();
    }

    @Override
    public void windowLostFocus(final WindowEvent e) {
	// Do nothing
    }
}
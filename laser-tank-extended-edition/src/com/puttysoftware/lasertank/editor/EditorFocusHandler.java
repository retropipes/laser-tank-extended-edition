package com.puttysoftware.lasertank.editor;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import com.puttysoftware.lasertank.LaserTankEE;

class EditorFocusHandler implements WindowFocusListener {
    public EditorFocusHandler() {
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
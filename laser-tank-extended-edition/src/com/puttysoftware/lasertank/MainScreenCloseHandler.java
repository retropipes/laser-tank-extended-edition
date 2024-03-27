package com.puttysoftware.lasertank;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MainScreenCloseHandler extends WindowAdapter {
    public MainScreenCloseHandler() {
    }

    @Override
    public void windowClosing(final WindowEvent arg0) {
	if (MainScreen.quitHandler()) {
	    System.exit(0);
	}
    }
}
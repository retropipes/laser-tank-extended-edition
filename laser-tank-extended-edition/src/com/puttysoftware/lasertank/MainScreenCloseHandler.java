package com.puttysoftware.lasertank;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MainScreenCloseHandler extends WindowAdapter {
    private final MainScreen mainScreen;

    public MainScreenCloseHandler(final MainScreen theMainScreen) {
	this.mainScreen = theMainScreen;
    }

    @Override
    public void windowClosing(final WindowEvent arg0) {
	if (this.mainScreen.quitHandler()) {
	    System.exit(0);
	}
    }
}
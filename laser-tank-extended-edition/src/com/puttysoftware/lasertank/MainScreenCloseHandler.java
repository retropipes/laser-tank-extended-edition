package com.puttysoftware.lasertank;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class MainScreenCloseHandler implements WindowListener {
	/**
	 *
	 */
	private final MainScreen guiManager;

	public MainScreenCloseHandler(MainScreen guiManager) {
		this.guiManager = guiManager;
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
		if (this.guiManager.quitHandler()) {
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
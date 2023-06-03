package com.puttysoftware.lasertank.editor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.LaserTankEE;

class EditorWindowEventHandler extends WindowAdapter {
    private final Editor editor;

    public EditorWindowEventHandler(Editor theEditor) {
	this.editor = theEditor;
    }

    // handle window
    @Override
    public void windowClosing(final WindowEvent we) {
	new Thread() {
	    @Override
	    public void run() {
		EditorWindowEventHandler.this.editor.handleCloseWindow();
		LaserTankEE.getMainScreen().showGUI();
	    }
	}.start();
    }
}
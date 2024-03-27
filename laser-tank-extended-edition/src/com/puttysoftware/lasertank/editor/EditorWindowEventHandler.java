package com.puttysoftware.lasertank.editor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.utility.TaskRunner;

class EditorWindowEventHandler extends WindowAdapter implements Runnable {
    private final Editor editor;

    public EditorWindowEventHandler(final Editor theEditor) {
	this.editor = theEditor;
    }

    @Override
    public void run() {
	this.editor.handleCloseWindow();
	LaserTankEE.showGUI();
    }

    // handle window
    @Override
    public void windowClosing(final WindowEvent we) {
	TaskRunner.runTask(this);
    }
}
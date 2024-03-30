package com.puttysoftware.lasertank.editor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

class EditorWindowEventHandler extends WindowAdapter implements Runnable {
    public EditorWindowEventHandler() {
    }

    @Override
    public void run() {
	Editor.handleCloseWindow();
	LaserTankEE.showGUI();
    }

    // handle window
    @Override
    public void windowClosing(final WindowEvent we) {
	AppTaskManager.runTask(this);
    }
}
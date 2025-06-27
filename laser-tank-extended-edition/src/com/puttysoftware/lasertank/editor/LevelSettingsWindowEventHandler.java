package com.puttysoftware.lasertank.editor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.puttysoftware.lasertank.tasks.AppTaskManager;

class LevelSettingsWindowEventHandler extends WindowAdapter implements Runnable {
    private final LevelSettings levelSettings;

    public LevelSettingsWindowEventHandler(final LevelSettings theLevelSettings) {
        this.levelSettings = theLevelSettings;
    }

    @Override
    public void run() {
        LevelSettingsWindowEventHandler.this.levelSettings.hideSettings();
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        AppTaskManager.runTask(this);
    }
}
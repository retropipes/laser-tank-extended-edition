package com.puttysoftware.lasertank.tasks;

import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;

class ShowErrorDialogTask implements Runnable {
    private final Throwable t;
    private final ErrorLogger log;

    ShowErrorDialogTask(final Throwable problem, final ErrorLogger logger) {
        this.t = problem;
        this.log = logger;
    }

    @Override
    public void run() {
        Sounds.play(Sound.FATAL);
        CommonDialogs.showErrorDialog(AppTaskManager.ERROR_MESSAGE, AppTaskManager.ERROR_TITLE);
        this.log.logError(this.t);
        GameErrorHandler.DIALOG_SHOWING = false;
    }
}

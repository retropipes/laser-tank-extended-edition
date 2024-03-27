package com.puttysoftware.lasertank;

import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;

class ShowWarningDialogTask implements Runnable {
    private final Throwable t;
    private final ErrorLogger log;

    ShowWarningDialogTask(final Throwable problem, final ErrorLogger logger) {
	this.t = problem;
	this.log = logger;
    }

    @Override
    public void run() {
	Sounds.play(Sound.WARNING);
	CommonDialogs.showTitledDialog(LaserTankEE.WARNING_MESSAGE, LaserTankEE.WARNING_TITLE);
	this.log.logWarning(this.t);
	GameErrorHandler.DIALOG_SHOWING = false;
    }
}

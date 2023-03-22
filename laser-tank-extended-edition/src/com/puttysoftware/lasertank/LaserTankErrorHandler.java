/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.ErrorHandler;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;

class LaserTankErrorHandler implements ErrorHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        new Thread() {
            @Override
            public void run() {
                Sounds.play(Sound.ERROR);
                CommonDialogs.showErrorDialog(LaserTankEE.ERROR_MESSAGE, LaserTankEE.ERROR_TITLE);
                Diane.handleError(e);
            }
        }.start();
    }

    @Override
    public void handleWarning(final Throwable t) {
        Diane.handleWarning(t);
        new Thread() {
            @Override
            public void run() {
                Sounds.play(Sound.ERROR);
                CommonDialogs.showTitledDialog(LaserTankEE.WARNING_MESSAGE, LaserTankEE.WARNING_TITLE);
            }
        }.start();
    }
}

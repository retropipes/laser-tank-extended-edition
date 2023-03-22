/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import com.puttysoftware.diane.ErrorHandler;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.internal.ErrorLogger;
import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;

class CustomErrorHandler implements ErrorHandler {
    private final String programName;
    private final ErrorLogger logger;
    private static boolean DIALOG_SHOWING = false;

    public CustomErrorHandler(final String name) {
        this.programName = name;
        this.logger = new ErrorLogger(this.programName);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.handleError(e);
    }

    public void handleError(final Throwable t) {
        if (LaserTankEE.DEBUG) {
            t.printStackTrace();
            System.exit(1);
        } else {
            if (!DIALOG_SHOWING) {
                DIALOG_SHOWING = true;
                new Thread() {
                    @Override
                    public void run() {
                        Sounds.play(Sound.ERROR);
                        CommonDialogs.showErrorDialog(LaserTankEE.ERROR_MESSAGE, LaserTankEE.ERROR_TITLE);
                        CustomErrorHandler.this.logger.logError(t);
                        DIALOG_SHOWING = false;
                    }
                }.start();
            } else {
                this.logger.logError(t);
            }
        }
    }

    public void handleErrorDirectly(final Throwable t) {
        this.logger.logError(t);
    }

    @Override
    public void handleWarning(final Throwable t) {
        if (LaserTankEE.DEBUG) {
            t.printStackTrace();
            System.exit(2);
        } else {
            this.logger.logWarning(t);
            if (!DIALOG_SHOWING) {
                DIALOG_SHOWING = true;
                new Thread() {
                    @Override
                    public void run() {
                        Sounds.play(Sound.ERROR);
                        CommonDialogs.showTitledDialog(LaserTankEE.WARNING_MESSAGE, LaserTankEE.WARNING_TITLE);
                        DIALOG_SHOWING = false;
                    }
                }.start();
            }
        }
    }

    public void handleWarningDirectly(final Throwable t) {
        this.logger.logWarning(t);
    }
}

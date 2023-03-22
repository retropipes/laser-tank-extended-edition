/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import com.puttysoftware.diane.ErrorHandler;

class LaserTankErrorHandler implements ErrorHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LaserTankEE.logError(e);
    }

    @Override
    public void handleWarning(final Throwable t) {
        LaserTankEE.logWarning(t);
    }
}

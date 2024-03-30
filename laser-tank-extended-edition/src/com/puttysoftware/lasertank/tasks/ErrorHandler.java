/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.tasks;

public interface ErrorHandler extends Thread.UncaughtExceptionHandler {
    void handleWarning(Throwable t);
}

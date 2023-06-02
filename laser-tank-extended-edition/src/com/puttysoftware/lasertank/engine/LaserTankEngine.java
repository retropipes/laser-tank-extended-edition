/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine;

import com.puttysoftware.lasertank.engine.internal.DefaultErrorHandler;

public class LaserTankEngine {
    private static ErrorHandler errHandler;

    public static void handleError(final Throwable t) {
	LaserTankEngine.errHandler.uncaughtException(Thread.currentThread(), t);
    }

    public static void handleWarning(final Throwable t) {
	LaserTankEngine.errHandler.handleWarning(t);
    }

    public static void installCustomErrorHandler(final ErrorHandler handler) {
	// Install custom error handler
	LaserTankEngine.errHandler = handler;
	Thread.setDefaultUncaughtExceptionHandler(LaserTankEngine.errHandler);
    }

    public static void installDefaultErrorHandler(final String programName) {
	// Install default error handler
	LaserTankEngine.errHandler = new DefaultErrorHandler(programName);
	Thread.setDefaultUncaughtExceptionHandler(LaserTankEngine.errHandler);
    }

    private LaserTankEngine() {
    }
}

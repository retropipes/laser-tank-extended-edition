/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.error;

public class ErrorHandlerInstaller {
    private static ErrorHandler errHandler;

    public static void handleError(final Throwable t) {
	ErrorHandlerInstaller.errHandler.uncaughtException(Thread.currentThread(), t);
    }

    public static void handleWarning(final Throwable t) {
	ErrorHandlerInstaller.errHandler.handleWarning(t);
    }

    public static void installErrorHandler(final ErrorHandler handler) {
	// Install custom error handler
	ErrorHandlerInstaller.errHandler = handler;
	Thread.setDefaultUncaughtExceptionHandler(ErrorHandlerInstaller.errHandler);
    }

    private ErrorHandlerInstaller() {
    }
}

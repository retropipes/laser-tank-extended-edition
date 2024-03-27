/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

final class ErrorLogger {
    // Fields
    private final String name;

    // Constructor
    ErrorLogger(final String programName) {
	this.name = programName;
    }

    // Methods
    void logError(final Throwable t) {
	final var elw = new ErrorLogWriter(t, this.name);
	elw.writeErrorInfo();
	System.exit(1);
    }

    void logWarning(final Throwable t) {
	final var wlw = new WarningLogWriter(t, this.name);
	wlw.writeLogInfo();
    }
}

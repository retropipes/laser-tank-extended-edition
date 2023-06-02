/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.internal;

import com.puttysoftware.lasertank.engine.ErrorHandler;

public class DefaultErrorHandler implements ErrorHandler {
    private final String programName;
    private final ErrorLogger logger;

    public DefaultErrorHandler(final String name) {
	this.programName = name;
	this.logger = new ErrorLogger(this.programName);
    }

    @Override
    public void handleWarning(final Throwable t) {
	this.logger.logWarning(t);
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
	this.logger.logError(e);
    }
}

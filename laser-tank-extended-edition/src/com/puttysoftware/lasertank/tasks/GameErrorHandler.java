/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.tasks;

class GameErrorHandler implements ErrorHandler {
	static boolean DIALOG_SHOWING = false;
	private final String programName;
	private final ErrorLogger logger;

	public GameErrorHandler(final String name) {
		this.programName = name;
		this.logger = new ErrorLogger(this.programName);
	}

	public void handleError(final Throwable t) {
		if (!GameErrorHandler.DIALOG_SHOWING) {
			CleanupTask.cleanUp();
			GameErrorHandler.DIALOG_SHOWING = true;
			AppTaskManager.runTask(new ShowErrorDialogTask(t, this.logger));
		} else {
			CleanupTask.cleanUp();
			this.logger.logError(t);
		}
	}

	public void handleErrorDirectly(final Throwable t) {
		CleanupTask.cleanUp();
		this.logger.logError(t);
	}

	@Override
	public void handleWarning(final Throwable t) {
		this.logger.logWarning(t);
		if (!GameErrorHandler.DIALOG_SHOWING) {
			GameErrorHandler.DIALOG_SHOWING = true;
			AppTaskManager.runTask(new ShowWarningDialogTask(t, this.logger));
		}
	}

	public void handleWarningDirectly(final Throwable t) {
		this.logger.logWarning(t);
	}

	@Override
	public void uncaughtException(final Thread t, final Throwable e) {
		this.handleError(e);
	}
}

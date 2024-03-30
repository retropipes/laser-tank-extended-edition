/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.tasks;

import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;

public class AppTaskManager {
    private static GameErrorHandler errHandler;
    private static String PROGRAM_NAME = "LaserTankEE"; //$NON-NLS-1$
    static String ERROR_MESSAGE = null;
    static String ERROR_TITLE = null;
    static String WARNING_MESSAGE = null;
    static String WARNING_TITLE = null;
    
    public static void cleanUp() {
	CleanupTask.cleanUp();
    }

    public static void error(final Throwable t) {
	AppTaskManager.errHandler.handleError(t);
    }

    public static void logErrorDirectly(final Throwable t) {
	AppTaskManager.errHandler.handleErrorDirectly(t);
    }

    public static void warning(final Throwable t) {
	AppTaskManager.errHandler.handleWarning(t);
    }

    public static void logWarningDirectly(final Throwable t) {
	AppTaskManager.errHandler.handleWarningDirectly(t);
    }

    public static void installDefaultErrorHandler() {
	// Initialize strings
	Strings.setDefaultLanguage();
	AppTaskManager.ERROR_TITLE = Strings.loadError(ErrorString.ERROR_TITLE);
	AppTaskManager.ERROR_MESSAGE = Strings.loadError(ErrorString.ERROR_MESSAGE);
	AppTaskManager.WARNING_TITLE = Strings.loadError(ErrorString.WARNING_TITLE);
	AppTaskManager.WARNING_MESSAGE = Strings.loadError(ErrorString.WARNING_MESSAGE);
	// Set Up Common Dialogs
	CommonDialogs.setDefaultTitle(AppTaskManager.PROGRAM_NAME);
	// Install custom error handler
	AppTaskManager.errHandler = new GameErrorHandler(AppTaskManager.PROGRAM_NAME);
	Thread.setDefaultUncaughtExceptionHandler(AppTaskManager.errHandler);
    }

    public static void runTask(final Runnable task) {
	Thread.ofPlatform().uncaughtExceptionHandler(AppTaskManager.errHandler).start(task);
    }

    public static Thread runTrackedTask(final Runnable task) {
	return Thread.ofPlatform().uncaughtExceptionHandler(AppTaskManager.errHandler).start(task);
    }

    private AppTaskManager() {
    }
}

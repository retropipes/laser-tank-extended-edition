/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.gui;

public interface ModeCommands {
    void createCommandsPane();

    void disableDirtyCommands();

    void disableLoadedCommands();

    void disableModeCommands();

    void enableDirtyCommands();

    void enableLoadedCommands();

    void enableModeCommands();

    void enterMode();

    void exitMode();

    void setInitialState();

    void setStatusMessage(String msg);
}

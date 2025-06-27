package com.puttysoftware.lasertank.gui;

class ScreenValueTask implements Runnable {
    private final Screen screen;

    ScreenValueTask(final Screen theScreen) {
        this.screen = theScreen;
    }

    @Override
    public void run() {
        this.screen.checkView();
        this.screen.showScreen();
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.gui;

import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.puttysoftware.lasertank.asset.music.MusicIndex;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.utility.TaskRunner;

public abstract class Screen extends WindowAdapter {
    // Fields
    protected final MainWindow theFrame;
    protected JPanel theContent;
    private String value;
    private ScreenValueTask valueTask;
    private boolean viewReady;
    private String title;
    private MusicIndex music;
    private JButton defaultButton;

    // Constructors
    protected Screen() {
	this.theFrame = MainWindow.mainWindow();
	this.viewReady = false;
    }

    public void addKeyListener(final KeyListener l) {
	this.theFrame.addKeyListener(l);
    }

    public void addWindowFocusListener(final WindowFocusListener l) {
	this.theFrame.addWindowFocusListener(l);
    }

    public void addWindowListener(final WindowListener l) {
	this.theFrame.addWindowListener(l);
    }

    void checkView() {
	if (!this.viewReady) {
	    this.setUpView();
	    this.viewReady = true;
	}
    }

    JPanel content() {
	this.checkView();
	return this.theContent;
    }

    public JButton defaultButton() {
	return this.defaultButton;
    }

    public final void hideScreen() {
	this.checkView();
	this.hideScreenHook();
	this.theFrame.removeWindowListener(this);
	this.theFrame.restoreSaved();
    }

    protected void hideScreenHook() {
    }

    public MusicIndex music() {
	return this.music;
    }

    public void pack() {
	this.theFrame.pack();
    }

    protected abstract void populateMainPanel();

    public void removeKeyListener(final KeyListener l) {
	this.theFrame.removeKeyListener(l);
    }

    public void removeWindowFocusListener(final WindowFocusListener l) {
	this.theFrame.removeWindowFocusListener(l);
    }

    public void removeWindowListener(final WindowListener l) {
	this.theFrame.removeWindowListener(l);
    }

    public void setDefaultButton(final JButton newDefaultButton) {
	this.defaultButton = newDefaultButton;
    }

    public void setMusic(final MusicIndex newMusic) {
	this.music = newMusic;
    }

    public void setTitle(final String newTitle) {
	this.title = newTitle;
    }

    final void setUpView() {
	this.theContent = MainContentFactory.content();
	this.populateMainPanel();
	this.theContent.setOpaque(true);
    }

    public synchronized final void setValue(final String v) {
	this.value = v;
	this.valueTask.notifyAll();
    }

    public final void showScreen() {
	this.checkView();
	this.theFrame.setAndSave(this.theContent, this.title);
	this.theFrame.addWindowListener(this);
	this.showScreenHook();
	this.theFrame.pack();
    }

    protected void showScreenHook() {
    }

    public final String showValueScreen() {
	this.valueTask = new ScreenValueTask(this);
	final var t = TaskRunner.runTrackedTask(this.valueTask);
	try {
	    t.join();
	} catch (final InterruptedException e) {
	    return null;
	}
	return this.value;
    }

    public void statusMessage(final String msg) {
	CommonDialogs.showDialogLater(msg);
    }

    public String title() {
	return this.title;
    }

    public void updateDirtyWindow(final boolean appDirty) {
	this.theFrame.setDirty(appDirty);
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.puttysoftware.lasertank.engine.gui.Screen;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public class AboutDialog extends Screen implements AboutHandler {
    private class EventHandler implements ActionListener {
	public EventHandler() {
	    // Do nothing
	}

	// Handle buttons
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final var ad = AboutDialog.this;
		final var cmd = e.getActionCommand();
		if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
		    ad.hideScreen();
		}
	    } catch (final Exception ex) {
		LaserTankEE.logError(ex);
	    }
	}
    }

    // Fields
    private final String ver;

    // Constructors
    AboutDialog(final String versionString) {
	this.ver = versionString;
    }

    @Override
    public void handleAbout(final AboutEvent e) {
	this.showScreen();
    }

    @Override
    protected void populateMainPanel() {
	JPanel textPane, buttonPane, logoPane;
	JButton aboutOK;
	EventHandler handler;
	handler = new EventHandler();
	this.setTitle(Strings.loadDialog(DialogString.ABOUT) + Strings.loadCommon(CommonString.SPACE)
		+ GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
	textPane = new JPanel();
	buttonPane = new JPanel();
	logoPane = new JPanel();
	aboutOK = new JButton(Strings.loadDialog(DialogString.OK_BUTTON));
	aboutOK.setDefaultCapable(true);
	this.setDefaultButton(aboutOK);
	this.theContent.setLayout(new BorderLayout());
	logoPane.setLayout(new FlowLayout());
	textPane.setLayout(new GridLayout(4, 1));
	textPane.add(new JLabel(GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME)
		+ Strings.loadCommon(CommonString.SPACE) + Strings.loadDialog(DialogString.VERSION)
		+ Strings.loadCommon(CommonString.SPACE) + this.ver));
	textPane.add(new JLabel(Strings.loadDialog(DialogString.AUTHOR)
		+ GlobalStrings.loadUntranslated(UntranslatedString.AUTHOR_NAME)));
	textPane.add(new JLabel(Strings.loadDialog(DialogString.WEB_SITE)
		+ GlobalStrings.loadUntranslated(UntranslatedString.GAME_WEB_URL)));
	textPane.add(new JLabel(Strings.loadDialog(DialogString.BUG_REPORTS)
		+ GlobalStrings.loadUntranslated(UntranslatedString.GAME_EMAIL)));
	buttonPane.setLayout(new FlowLayout());
	buttonPane.add(aboutOK);
	this.theContent.add(logoPane, BorderLayout.WEST);
	this.theContent.add(textPane, BorderLayout.CENTER);
	this.theContent.add(buttonPane, BorderLayout.SOUTH);
	aboutOK.addActionListener(handler);
	this.pack();
    }
}
/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.utility;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.puttysoftware.lasertank.arena.Arena;

public class RCLGenerator {
	public static JPanel generateRowColumnLabels() {
		final var outerOutputPane = new JPanel();
		outerOutputPane.setLayout(new BorderLayout());
		final var rowsPane = new JPanel();
		rowsPane.setLayout(new BoxLayout(rowsPane, BoxLayout.Y_AXIS));
		// Generate row labels
		rowsPane.add(Box.createVerticalGlue());
		for (var r = 1; r <= Arena.getMinRows(); r++) {
			final var j = new JLabel(Integer.toString(r));
			j.setLabelFor(null);
			j.setHorizontalAlignment(SwingConstants.RIGHT);
			j.setVerticalAlignment(SwingConstants.CENTER);
			rowsPane.add(j);
			if (r < Arena.getMinRows()) {
				rowsPane.add(Box.createVerticalGlue());
			}
		}
		final var columnsPane = new JPanel();
		columnsPane.setLayout(new BoxLayout(columnsPane, BoxLayout.X_AXIS));
		// Generate column labels
		columnsPane.add(Box.createHorizontalGlue());
		for (var c = 1; c <= Arena.getMinColumns(); c++) {
			final var j = new JLabel(Character.toString((char) (c + 64)));
			j.setLabelFor(null);
			j.setHorizontalAlignment(SwingConstants.CENTER);
			j.setVerticalAlignment(SwingConstants.BOTTOM);
			columnsPane.add(j);
			if (c < Arena.getMinColumns()) {
				columnsPane.add(Box.createHorizontalGlue());
			}
		}
		outerOutputPane.add(rowsPane, BorderLayout.WEST);
		outerOutputPane.add(columnsPane, BorderLayout.NORTH);
		return outerOutputPane;
	}

	// Constructor
	private RCLGenerator() {
		// Do nothing
	}
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.gui;

import com.puttysoftware.lasertank.engine.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.engine.storage.ObjectStorage;

public class DrawGrid extends ObjectStorage {
    public DrawGrid(final int numSquares) {
	super(numSquares, numSquares);
    }

    public BufferedImageIcon getImageCell(final int row, final int col) {
	return (BufferedImageIcon) this.getCell(row, col);
    }

    public void setImageCell(final BufferedImageIcon bii, final int row, final int col) {
	this.setCell(bii, row, col);
    }
}

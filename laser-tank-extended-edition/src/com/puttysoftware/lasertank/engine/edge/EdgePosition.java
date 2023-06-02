/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.edge;

public class EdgePosition {
    // Constants
    public static final EdgePosition MIDDLE = new EdgePosition(0.5);
    // Fields
    private final double offset;

    // Constructor
    private EdgePosition(final double newOffset) {
	this.offset = newOffset;
    }

    // Methods
    public double getOffset() {
	return this.offset;
    }
}

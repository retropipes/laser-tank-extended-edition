/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.point;

public class Point4D extends Point3D {
    public int w;

    public Point4D() {
    }

    public Point4D(final int nx, final int ny, final int nz, final int nw) {
	super(nx, ny, nz);
	this.w = nw;
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.point;

public class Point3D extends Point2D {
    public int z;

    public Point3D() {
    }

    public Point3D(final int nx, final int ny, final int nz) {
	super(nx, ny);
	this.z = nz;
    }
}

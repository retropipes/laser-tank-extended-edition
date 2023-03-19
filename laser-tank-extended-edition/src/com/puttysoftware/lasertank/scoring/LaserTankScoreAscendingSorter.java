/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.scoring;

import java.io.Serializable;
import java.util.Comparator;

public class LaserTankScoreAscendingSorter implements Comparator<LaserTankScore>, Serializable {
	private static final long serialVersionUID = 30523263423564L;

	@Override
	public int compare(final LaserTankScore o1, final LaserTankScore o2) {
		if (o2.shots > o1.shots) {
			return 1;
		}
		if (o2.shots < o1.shots) {
			return -1;
		}
		if (o2.moves > o1.moves) {
			return 1;
		} else if (o2.moves < o1.moves) {
			return -1;
		} else {
			return 0;
		}
	}
}
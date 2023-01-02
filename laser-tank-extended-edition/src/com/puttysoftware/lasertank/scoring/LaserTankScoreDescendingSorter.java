package com.puttysoftware.lasertank.scoring;

import java.io.Serializable;
import java.util.Comparator;

public class LaserTankScoreDescendingSorter implements Comparator<LaserTankScore>, Serializable {
    private static final long serialVersionUID = 30523263423565L;

    @Override
    public int compare(final LaserTankScore o1, final LaserTankScore o2) {
	if (o2.shots > o1.shots) {
	    return -1;
	} else if (o2.shots < o1.shots) {
	    return 1;
	} else {
	    if (o2.moves > o1.moves) {
		return -1;
	    } else if (o2.moves < o1.moves) {
		return 1;
	    } else {
		return 0;
	    }
	}
    }
}
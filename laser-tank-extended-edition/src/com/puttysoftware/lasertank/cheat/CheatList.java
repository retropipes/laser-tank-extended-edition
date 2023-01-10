package com.puttysoftware.lasertank.cheat;

import java.util.ArrayList;

class CheatList extends ArrayList<Cheat> {
    private static final long serialVersionUID = 2216922516012569257L;

    public int indexOf(final String code) {
	if (code == null) {
	    return -1;
	}
	var counter = -1;
	for (final Cheat c : this) {
	    counter += 1;
	    if (c.getCode().equals(code)) {
		return counter;
	    }
	}
	return -1;
    }
}

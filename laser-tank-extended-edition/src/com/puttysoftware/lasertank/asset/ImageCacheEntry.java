/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.asset;

import com.puttysoftware.lasertank.asset.image.BufferedImageIcon;

class ImageCacheEntry {
    // Fields
    private BufferedImageIcon entry;
    private String nameEntry;

    // Constructor
    ImageCacheEntry() {
	// Do nothing
    }

    // Methods
    BufferedImageIcon getEntry() {
	return this.entry;
    }

    String getNameEntry() {
	return this.nameEntry;
    }

    void setEntry(final BufferedImageIcon entry1) {
	this.entry = entry1;
    }

    void setNameEntry(final String nameEntry1) {
	this.nameEntry = nameEntry1;
    }
}
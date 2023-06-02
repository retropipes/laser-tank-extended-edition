/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.map;

import com.puttysoftware.lasertank.engine.objectmodel.ObjectModel;
import com.puttysoftware.lasertank.engine.storage.ObjectStorage;

public class Map {
    // Properties
    private final ObjectStorage data;

    // Constructors
    public Map(final int... dimensions) {
	this.data = new ObjectStorage(dimensions);
    }

    public void fill(final ObjectModel with) {
	this.data.fill(with);
    }

    public ObjectModel getCell(final int... location) {
	return (ObjectModel) this.data.getCell(location);
    }

    public int getSize(final int dimension) {
	return this.data.getShape()[dimension];
    }

    public void setCell(final ObjectModel o, final int... location) {
	this.data.setCell(o, location);
    }
}

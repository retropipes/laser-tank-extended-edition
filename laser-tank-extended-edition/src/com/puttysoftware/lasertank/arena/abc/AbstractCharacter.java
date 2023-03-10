/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import java.awt.Color;
import java.io.IOException;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.lasertank.arena.objects.Empty;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.utility.ArenaObjectList;

public abstract class AbstractCharacter extends AbstractArenaObject {
    // Fields
    private final int characterNumber;

    // Constructors
    protected AbstractCharacter(final int number) {
	super();
	this.setSavedObject(new Empty());
	this.activateTimer(1);
	this.addType(GameType.CHARACTER);
	this.characterNumber = number;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj) || !(obj instanceof final AbstractCharacter other) || (this.characterNumber != other.characterNumber)) {
	    return false;
	}
	return true;
    }

    @Override
    public int getCustomFormat() {
	return AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractArenaObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public String getCustomText() {
	return Integer.toString(this.characterNumber);
    }

    @Override
    public Color getCustomTextColor() {
	return Color.white;
    }

    // Methods
    public int getNumber() {
	return this.characterNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final var prime = 31;
	final var result = super.hashCode();
	return prime * result + this.characterNumber;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    protected AbstractArenaObject readArenaObjectHookG2(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	this.setSavedObject(ArenaObjectList.readArenaObjectG2(reader, formatVersion));
	return this;
    }

    @Override
    protected AbstractArenaObject readArenaObjectHookG3(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	this.setSavedObject(ArenaObjectList.readArenaObjectG3(reader, formatVersion));
	return this;
    }

    @Override
    protected AbstractArenaObject readArenaObjectHookG4(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	this.setSavedObject(ArenaObjectList.readArenaObjectG4(reader, formatVersion));
	return this;
    }

    @Override
    protected AbstractArenaObject readArenaObjectHookG5(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	this.setSavedObject(ArenaObjectList.readArenaObjectG5(reader, formatVersion));
	return this;
    }

    @Override
    protected AbstractArenaObject readArenaObjectHookG6(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	this.setSavedObject(ArenaObjectList.readArenaObjectG6(reader, formatVersion));
	return this;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    @Override
    public void timerExpiredAction(final int x, final int y) {
	if (this.getSavedObject() instanceof AbstractMovableObject) {
	    this.getSavedObject().timerExpiredAction(x, y);
	}
	this.activateTimer(1);
    }

    @Override
    protected void writeArenaObjectHook(final DataIOWriter writer) throws IOException {
	this.getSavedObject().writeArenaObject(writer);
    }
}
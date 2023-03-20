/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;


public abstract class AbstractDoor extends ArenaObject {
    // Fields
    private AbstractKey key;

    // Constructors
    protected AbstractDoor(final AbstractKey mgk) {
        super();
        this.key = mgk;
    }

    @Override
    public AbstractDoor clone() {
        final var copy = (AbstractDoor) super.clone();
        copy.key = (AbstractKey) this.key.clone();
        return copy;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final var other = (AbstractDoor) obj;
        if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final var hash = 7;
        return 71 * hash + (this.key != null ? this.key.hashCode() : 0);
    }
}
package com.puttysoftware.lasertank.cheat;

class ToggleCheat extends Cheat {
    // Fields
    private boolean state;

    // Constructor
    public ToggleCheat(final String activator, final CheatEffect doesWhat) {
	super(activator, doesWhat);
	this.state = false;
    }

    @Override
    public boolean getState() {
	return this.state;
    }

    @Override
    public boolean hasState() {
	return true;
    }

    @Override
    public void toggleState() {
	this.state = !this.state;
    }
}

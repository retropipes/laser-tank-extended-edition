package com.puttysoftware.lasertank.cheat;

import java.util.Objects;

abstract class Cheat {
    // Fields
    private static final int INSTANTS = 12;
    private final String code;
    private final CheatEffect cheatEffect;

    // Constructor
    public Cheat(final String activator, final CheatEffect doesWhat) {
	this.code = activator;
	this.cheatEffect = doesWhat;
    }

    public abstract boolean getState();

    public abstract boolean hasState();

    public abstract void toggleState();

    public final String getCode() {
	return this.code;
    }

    public final CheatEffect getEffect() {
	return this.cheatEffect;
    }

    public static int instantCount() {
	return Cheat.INSTANTS;
    }

    public static int count() {
	return CheatEffect.values().length;
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.code, this.cheatEffect);
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof final Cheat other)) {
	    return false;
	}
	return Objects.equals(this.code, other.code) && this.cheatEffect == other.cheatEffect;
    }
}

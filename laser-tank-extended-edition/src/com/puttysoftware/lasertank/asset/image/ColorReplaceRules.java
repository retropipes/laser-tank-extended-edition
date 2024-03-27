/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.asset.image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;

public final class ColorReplaceRules {
    // Fields
    private final ArrayList<ColorReplaceRule> rules;

    // Constructor
    public ColorReplaceRules() {
	this.rules = new ArrayList<>();
    }

    // Methods
    public void add(final Color find, final Color replace) {
	final var value = new ColorReplaceRule(find, replace);
	this.rules.add(value);
    }

    public BufferedImageIcon applyAll(final BufferedImageIcon input) {
	if (input == null) {
	    throw new IllegalArgumentException(Strings.loadError(ErrorString.NULL_INPUT));
	}
	var result = input;
	for (final ColorReplaceRule rule : this.rules) {
	    result = rule.apply(result);
	}
	return result;
    }

    public void clear() {
	this.rules.clear();
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof final ColorReplaceRules other)) {
	    return false;
	}
	return Objects.equals(this.rules, other.rules);
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.rules);
    }
}

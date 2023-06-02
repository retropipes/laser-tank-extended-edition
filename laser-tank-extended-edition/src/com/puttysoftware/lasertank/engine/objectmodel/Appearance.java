/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.objectmodel;

import com.puttysoftware.lasertank.engine.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.engine.asset.image.ColorReplaceRules;
import com.puttysoftware.lasertank.engine.asset.image.ColorShader;
import com.puttysoftware.lasertank.engine.asset.image.LTEImageIndex;

public abstract class Appearance {
    private final String cacheName;
    private final LTEImageIndex whichImage;
    private final ColorShader shading;
    private final ColorReplaceRules replacements;

    public Appearance(final String name, final LTEImageIndex imageIndex) {
	this.cacheName = name;
	this.whichImage = imageIndex;
	this.shading = null;
	this.replacements = null;
    }

    public Appearance(final String name, final LTEImageIndex imageIndex, final ColorReplaceRules replaceRules) {
	this.cacheName = name;
	this.whichImage = imageIndex;
	this.shading = null;
	this.replacements = replaceRules;
    }

    public Appearance(final String name, final LTEImageIndex imageIndex, final ColorShader shader) {
	this.cacheName = name;
	this.whichImage = imageIndex;
	this.shading = shader;
	this.replacements = null;
    }

    public final String getCacheName() {
	return this.cacheName;
    }

    public abstract BufferedImageIcon getImage();

    public final ColorReplaceRules getReplacementRules() {
	return this.replacements;
    }

    public final ColorShader getShading() {
	return this.shading;
    }

    protected final LTEImageIndex getWhichImage() {
	return this.whichImage;
    }

    public final boolean hasReplacementRules() {
	return this.replacements != null;
    }

    public final boolean hasShading() {
	return this.shading != null;
    }
}

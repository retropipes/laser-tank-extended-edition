package com.puttysoftware.lasertank.locale;

public enum CommonString {
    LOGO_VERSION_PREFIX("V"), //$NON-NLS-1$
    EMPTY(""), //$NON-NLS-1$
    SPACE(" "), //$NON-NLS-1$
    UNDERSCORE("_"), //$NON-NLS-1$
    NOTL_PERIOD("."), //$NON-NLS-1$
    NOTL_IMAGE_EXTENSION_PNG(".png"), //$NON-NLS-1$
    ZERO("0"), //$NON-NLS-1$
    BETA_SHORT("b"), //$NON-NLS-1$
    COLON(":"), //$NON-NLS-1$
    CLOSE_PARENTHESES(")"), //$NON-NLS-1$
    OPEN_PARENTHESES("("), //$NON-NLS-1$
    SPACE_DASH_SPACE(" - "), //$NON-NLS-1$
    NULL("null"), //$NON-NLS-1$
    LTG_ID("LTG1"), //$NON-NLS-1$
    NEWLINE("\n"); //$NON-NLS-1$

    String internalValue;

    CommonString(final String v) {
	this.internalValue = v;
    }

    String getValue() {
	return this.internalValue;
    }
}

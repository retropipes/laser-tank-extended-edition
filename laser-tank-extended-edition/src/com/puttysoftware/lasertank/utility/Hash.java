/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	public static byte[] hash(final byte[] input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512"); //$NON-NLS-1$
		} catch (final NoSuchAlgorithmException nsae) {
			return null;
		}
		return md.digest(input);
	}

	public static byte[] hash(final byte[] input, final String algorithm) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (final NoSuchAlgorithmException nsae) {
			return null;
		}
		return md.digest(input);
	}

	private Hash() {
		// Do nothing
	}
}

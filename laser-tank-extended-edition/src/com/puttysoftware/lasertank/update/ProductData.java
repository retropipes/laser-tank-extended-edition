/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

public class ProductData {
	public static final int CODE_ALPHA = 1;
	public static final int CODE_BETA = 2;
	public static final int CODE_STABLE = 4;
	// Fields
	private final URL updateURL;
	private final URL newVersionURL;
	private final String versionString, shortVersionString;
	private final int majorVersion;
	private final int minorVersion;
	private final int bugfixVersion;
	private final int codeVersion;
	private final int prereleaseVersion;

	// Constructors
	public ProductData(final int major, final int minor, final int bugfix, final int code, final int beta) {
		String rt_vl, rt_vs;
		if (code == ProductData.CODE_ALPHA) {
			if (beta > 0) {
				rt_vl = Strings.loadDialog(DialogString.ALPHA_VERSION) + beta;
				rt_vs = Strings.VERSION_ALPHA_INDICATOR + beta;
			} else {
				rt_vl = Strings.loadDialog(DialogString.ALPHA_VERSION);
				rt_vs = Strings.VERSION_ALPHA_INDICATOR;
			}
		} else if (code == ProductData.CODE_BETA) {
			if (beta > 0) {
				rt_vl = Strings.loadDialog(DialogString.BETA_VERSION) + beta;
				rt_vs = Strings.VERSION_BETA_INDICATOR + beta;
			} else {
				rt_vl = Strings.loadDialog(DialogString.BETA_VERSION);
				rt_vs = Strings.VERSION_BETA_INDICATOR;
			}
		} else {
			rt_vl = Strings.EMPTY;
			rt_vs = Strings.EMPTY;
		}
		this.versionString = major + Strings.VERSION_SEPARATOR + minor + Strings.VERSION_SEPARATOR + bugfix + rt_vl;
		this.shortVersionString = major + Strings.VERSION_SEPARATOR + minor + Strings.VERSION_SEPARATOR + bugfix
				+ rt_vs;
		this.updateURL = null;
		this.newVersionURL = null;
		this.majorVersion = major;
		this.minorVersion = minor;
		this.bugfixVersion = bugfix;
		this.codeVersion = code;
		this.prereleaseVersion = beta;
	}

	public ProductData(final String update, final String newVersion, final int major, final int minor, final int bugfix,
			final int code, final int beta) throws MalformedURLException, URISyntaxException {
		String rt_url;
		String rt_vl, rt_vs;
		if (code == ProductData.CODE_ALPHA) {
			if (beta > 0) {
				rt_vl = Strings.loadDialog(DialogString.ALPHA_VERSION) + beta;
				rt_vs = Strings.VERSION_ALPHA_INDICATOR + beta;
			} else {
				rt_vl = Strings.loadDialog(DialogString.ALPHA_VERSION);
				rt_vs = Strings.VERSION_ALPHA_INDICATOR;
			}
			rt_url = Strings.ALPHA_URL_PART;
		} else if (code == ProductData.CODE_BETA) {
			if (beta > 0) {
				rt_vl = Strings.loadDialog(DialogString.BETA_VERSION) + beta;
				rt_vs = Strings.VERSION_BETA_INDICATOR + beta;
			} else {
				rt_vl = Strings.loadDialog(DialogString.BETA_VERSION);
				rt_vs = Strings.VERSION_BETA_INDICATOR;
			}
			rt_url = Strings.BETA_URL_PART;
		} else {
			rt_url = Strings.STABLE_URL_PART;
			rt_vl = Strings.EMPTY;
			rt_vs = Strings.EMPTY;
		}
		this.versionString = major + Strings.VERSION_SEPARATOR + minor + Strings.VERSION_SEPARATOR + bugfix + rt_vl;
		this.shortVersionString = major + Strings.VERSION_SEPARATOR + minor + Strings.VERSION_SEPARATOR + bugfix
				+ rt_vs;
		final var updatetxt = Strings.VERSION_FILE;
		this.updateURL = new URI(update + rt_url + updatetxt).toURL();
		this.newVersionURL = new URI(newVersion).toURL();
		this.majorVersion = major;
		this.minorVersion = minor;
		this.bugfixVersion = bugfix;
		this.codeVersion = code;
		this.prereleaseVersion = beta;
	}

	/**
	 * Performs a check for updates.
	 *
	 * @return true if an update is available; false otherwise
	 */
	public UpdateCheckResults checkForUpdates() throws IOException {
		if (this.updateURL != null && this.newVersionURL != null) {
			var newMajor = this.majorVersion;
			var newMinor = this.minorVersion;
			var newBugfix = this.bugfixVersion;
			var newPrerelease = this.prereleaseVersion;
			try (var isr = new InputStreamReader(this.updateURL.openStream()); var br = new BufferedReader(isr)) {
				newMajor = Integer.parseInt(br.readLine());
				newMinor = Integer.parseInt(br.readLine());
				newBugfix = Integer.parseInt(br.readLine());
				newPrerelease = Integer.parseInt(br.readLine());
			}
			final var hasUpdate = new UpdateCheckResults(newMajor, newMinor, newBugfix, newPrerelease);
			if (newMajor > this.majorVersion || newMajor == this.majorVersion && newMinor > this.minorVersion) {
				return hasUpdate;
			}
			if (newMajor == this.majorVersion && newMinor == this.minorVersion && newBugfix > this.bugfixVersion) {
				return hasUpdate;
			}
			if (newMajor == this.majorVersion && newMinor == this.minorVersion && newBugfix == this.bugfixVersion
					&& newPrerelease > this.prereleaseVersion) {
				return hasUpdate;
			}
		}
		return new UpdateCheckResults();
	}

	public int getBugfixVersion() {
		return this.bugfixVersion;
	}

	public int getCodeVersion() {
		return this.codeVersion;
	}

	public int getMajorVersion() {
		return this.majorVersion;
	}

	public int getMinorVersion() {
		return this.minorVersion;
	}

	public URL getNewVersionURL() {
		return this.newVersionURL;
	}

	public int getPrereleaseVersion() {
		return this.prereleaseVersion;
	}

	public String getShortVersionString() {
		return this.shortVersionString;
	}

	// Methods
	public URL getUpdateURL() {
		return this.updateURL;
	}

	public String getVersionString() {
		return this.versionString;
	}
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.update;

public final class UpdateCheckResults {
    // Fields
    private final int majorVersion;
    private final int minorVersion;
    private final int bugfixVersion;
    private final int prereleaseVersion;
    private final boolean hasUpdate;

    // Constructors
    public UpdateCheckResults() {
        this.hasUpdate = false;
        this.majorVersion = -1;
        this.minorVersion = -1;
        this.bugfixVersion = -1;
        this.prereleaseVersion = -1;
    }

    public UpdateCheckResults(final boolean update, final int major, final int minor, final int bugfix,
            final int beta) {
        this.hasUpdate = update;
        this.majorVersion = major;
        this.minorVersion = minor;
        this.bugfixVersion = bugfix;
        this.prereleaseVersion = beta;
    }

    public UpdateCheckResults(final int major, final int minor, final int bugfix, final int beta) {
        this.hasUpdate = true;
        this.majorVersion = major;
        this.minorVersion = minor;
        this.bugfixVersion = bugfix;
        this.prereleaseVersion = beta;
    }

    public int getBugfixVersion() {
        return this.bugfixVersion;
    }

    public int getMajorVersion() {
        return this.majorVersion;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public int getPrereleaseVersion() {
        return this.prereleaseVersion;
    }

    // Methods
    /**
     *
     * @return the has update status
     */
    public boolean hasUpdate() {
        return this.hasUpdate;
    }
}

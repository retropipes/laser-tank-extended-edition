/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.sandbox;

import java.io.File;

public abstract class Sandbox {
    // Fields
    private static Sandbox SANDBOX;

    // Static methods
    public static Sandbox getSandbox() {
        if (Sandbox.SANDBOX == null) {
            final var osName = System.getProperty("os.name"); //$NON-NLS-1$
            if ("Mac OS X".equals(osName)) { //$NON-NLS-1$
                Sandbox.SANDBOX = new MacOSSandbox();
            } else if (osName.startsWith("Windows")) { //$NON-NLS-1$
                Sandbox.SANDBOX = new WindowsSandbox();
            } else {
                Sandbox.SANDBOX = new JavaSandbox();
            }
        }
        return Sandbox.SANDBOX;
    }

    // Constructor
    protected Sandbox() {
        new File(this.getDirectory(SystemDir.LIBRARY)).mkdirs();
        new File(this.getDirectory(SystemDir.APPLICATION_SUPPORT)).mkdirs();
        new File(this.getDirectory(SystemDir.DOCUMENTS)).mkdirs();
    }

    public abstract void cacheFlags();

    protected abstract String getDirectory(final SystemDir dir);

    // Methods
    public final String getDocumentsDirectory() {
        return this.getDirectory(SystemDir.DOCUMENTS);
    }

    public abstract boolean getFlag(final SandboxFlag flag);

    public final String getSupportDirectory() {
        return this.getDirectory(SystemDir.APPLICATION_SUPPORT);
    }
}

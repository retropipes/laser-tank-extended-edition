/*
 * LaserTank: An Arena-Solving Game Copyright (C) 2008-present Eric Ahnell
 *
 * Any questions should be directed to the author via email at:
 * products@puttysoftware.com
 */
module com.puttysoftware.lasertankee {
    requires java.desktop;

    // Open all packages to tests
    opens com.puttysoftware.lasertank to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.accelerator to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.arena to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.arena.fileio to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.arena.objects to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.arena.objects.predefined to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.asset to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.asset.image to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.asset.music to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.asset.sound to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.cheat to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.datatype to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.editor to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.error to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.fileio to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.fileio.utility to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.game to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.gui to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.gui.dialog to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.helper to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.index to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.integration to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.locale to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.locale.global to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.point to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.random to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.sandbox to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.scoring to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.settings to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.storage to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.update to com.puttysoftware.lasertankee.tests;
    opens com.puttysoftware.lasertank.utility to com.puttysoftware.lasertankee.tests;
}
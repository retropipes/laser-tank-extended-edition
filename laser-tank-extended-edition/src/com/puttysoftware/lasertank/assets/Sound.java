package com.puttysoftware.lasertank.assets;

import java.net.URL;
import java.util.Locale;

import com.puttysoftware.diane.asset.sound.DianeSoundIndex;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public enum Sound implements DianeSoundIndex {
    ANTI_DIE,
    ANTI_FIRE,
    BREAK_BRICKS,
    BUMP_HEAD,
    DEAD,
    END_LEVEL,
    FIRE_LASER,
    LASER_DIE,
    MOVE,
    PUSH_ANTI_TANK,
    PUSH_BOX,
    PUSH_MIRROR,
    REFLECT,
    ROTATE,
    SINK,
    TURN,
    ROLL,
    PROXIMITY,
    BOOM,
    BOOST,
    BURN,
    BUTTON,
    CHANGE,
    COOL_OFF,
    CRACK,
    CRUSH,
    CUT,
    DEFROST,
    DOOR_CLOSES,
    DOOR_OPENS,
    DOWN,
    ERA_CHANGE,
    FREEZE,
    GRAB,
    JUMPING,
    KILL,
    MAGNET,
    MELT,
    MISSILE,
    POWER_LASER,
    POWER_TURRET,
    POWERFUL,
    PREPARE,
    RETURN,
    STUN,
    STUNNED,
    STUNNER,
    UNLOCK,
    UP,
    FALL,
    SPRING,
    WARP_OBJECT,
    WARP_TANK;

    @Override
    public String getName() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public URL getURL() {
        return Sound.class.getResource(GlobalStrings.loadUntranslated(UntranslatedString.SOUND_PATH) + this.getName()
                + GlobalStrings.loadUntranslated(UntranslatedString.SOUND_EXTENSION));
    }
}

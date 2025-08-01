package com.puttysoftware.lasertank.asset;

import java.net.URL;
import java.util.Locale;

import com.puttysoftware.lasertank.asset.sound.SoundIndex;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public enum Sound implements SoundIndex {
    _NONE,
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
    BLUE_LASER,
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
    WARP_TANK,
    ERROR,
    WARNING,
    FATAL;

    @Override
    public String getName() {
        return this == _NONE ? null : this.toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public URL getURL() {
        return this == _NONE ? null
                : Sound.class.getResource(GlobalStrings.loadUntranslated(UntranslatedString.SOUND_PATH) + this.getName()
                        + GlobalStrings.loadUntranslated(UntranslatedString.SOUND_EXTENSION));
    }
}

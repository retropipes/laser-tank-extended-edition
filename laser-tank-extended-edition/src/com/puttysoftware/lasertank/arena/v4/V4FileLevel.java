/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.v4;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaData;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameColor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

class V4FileLevel {
    // Fields
    private static byte[] objects;
    private static byte[] name;
    private static byte[] hint;
    private static byte[] author;
    private static byte[] difficulty;
    private static final int OBJECTS_SIZE = 256;
    private static final int NAME_SIZE = 31;
    private static final int HINT_SIZE = 256;
    private static final int AUTHOR_SIZE = 31;
    private static final int DIFFICULTY_SIZE = 2;

    // Methods
    static ArenaData loadAndConvert(final FileInputStream file, final Arena a) {
	try {
	    V4FileLevel.objects = new byte[V4FileLevel.OBJECTS_SIZE];
	    V4FileLevel.name = new byte[V4FileLevel.NAME_SIZE];
	    V4FileLevel.hint = new byte[V4FileLevel.HINT_SIZE];
	    V4FileLevel.author = new byte[V4FileLevel.AUTHOR_SIZE];
	    V4FileLevel.difficulty = new byte[V4FileLevel.DIFFICULTY_SIZE];
	    final var t = ArenaManager.createArenaData();
	    // Convert object byte map
	    var bytesRead = file.read(V4FileLevel.objects, 0, V4FileLevel.OBJECTS_SIZE);
	    if (bytesRead != V4FileLevel.OBJECTS_SIZE) {
		return null;
	    }
	    for (var x = 0; x < 16; x++) {
		for (var y = 0; y < 16; y++) {
		    final var z = x * 16 + y;
		    ArenaObject ao = null;
		    final var b = V4FileLevel.objects[z];
		    switch (b) {
		    case 0:
			ao = new ArenaObject(GameObjectID.GROUND);
			break;
		    case 1:
			ao = new ArenaObject(GameObjectID.TANK, 1);
			break;
		    case 2:
			ao = new ArenaObject(GameObjectID.FLAG);
			break;
		    case 3:
			ao = new ArenaObject(GameObjectID.WATER);
			break;
		    case 4:
			ao = new ArenaObject(GameObjectID.WALL);
			break;
		    case 5:
			ao = new ArenaObject(GameObjectID.BOX);
			break;
		    case 6:
			ao = new ArenaObject(GameObjectID.BRICKS);
			break;
		    case 7:
			ao = new ArenaObject(GameObjectID.ANTI_TANK);
			ao.setDirection(Direction.NORTH);
			break;
		    case 8:
			ao = new ArenaObject(GameObjectID.ANTI_TANK);
			ao.setDirection(Direction.EAST);
			break;
		    case 9:
			ao = new ArenaObject(GameObjectID.ANTI_TANK);
			ao.setDirection(Direction.SOUTH);
			break;
		    case 10:
			ao = new ArenaObject(GameObjectID.ANTI_TANK);
			ao.setDirection(Direction.WEST);
			break;
		    case 11:
			ao = new ArenaObject(GameObjectID.MIRROR);
			ao.setDirection(Direction.NORTHWEST);
			break;
		    case 12:
			ao = new ArenaObject(GameObjectID.MIRROR);
			ao.setDirection(Direction.NORTHEAST);
			break;
		    case 13:
			ao = new ArenaObject(GameObjectID.MIRROR);
			ao.setDirection(Direction.SOUTHEAST);
			break;
		    case 14:
			ao = new ArenaObject(GameObjectID.MIRROR);
			ao.setDirection(Direction.SOUTHWEST);
			break;
		    case 15:
			ao = new ArenaObject(GameObjectID.TANK_MOVER);
			ao.setDirection(Direction.NORTH);
			break;
		    case 16:
			ao = new ArenaObject(GameObjectID.TANK_MOVER);
			ao.setDirection(Direction.EAST);
			break;
		    case 17:
			ao = new ArenaObject(GameObjectID.TANK_MOVER);
			ao.setDirection(Direction.SOUTH);
			break;
		    case 18:
			ao = new ArenaObject(GameObjectID.TANK_MOVER);
			ao.setDirection(Direction.WEST);
			break;
		    case 19:
			ao = new ArenaObject(GameObjectID.CRYSTAL_BLOCK);
			break;
		    case 20:
			ao = new ArenaObject(GameObjectID.ROTARY_MIRROR);
			ao.setDirection(Direction.NORTHWEST);
			break;
		    case 21:
			ao = new ArenaObject(GameObjectID.ROTARY_MIRROR);
			ao.setDirection(Direction.NORTHEAST);
			break;
		    case 22:
			ao = new ArenaObject(GameObjectID.ROTARY_MIRROR);
			ao.setDirection(Direction.SOUTHEAST);
			break;
		    case 23:
			ao = new ArenaObject(GameObjectID.ROTARY_MIRROR);
			ao.setDirection(Direction.SOUTHWEST);
			break;
		    case 24:
			ao = new ArenaObject(GameObjectID.ICE);
			break;
		    case 25:
			ao = new ArenaObject(GameObjectID.THIN_ICE);
			break;
		    case 64:
		    case 65:
			ao = new ArenaObject(GameObjectID.TUNNEL);
			ao.setColor(GameColor.RED);
			break;
		    case 66:
		    case 67:
			ao = new ArenaObject(GameObjectID.TUNNEL);
			ao.setColor(GameColor.GREEN);
			break;
		    case 68:
		    case 69:
			ao = new ArenaObject(GameObjectID.TUNNEL);
			ao.setColor(GameColor.BLUE);
			break;
		    case 70:
		    case 71:
			ao = new ArenaObject(GameObjectID.TUNNEL);
			ao.setColor(GameColor.CYAN);
			break;
		    case 72:
		    case 73:
			ao = new ArenaObject(GameObjectID.TUNNEL);
			ao.setColor(GameColor.YELLOW);
			break;
		    case 74:
		    case 75:
			ao = new ArenaObject(GameObjectID.TUNNEL);
			ao.setColor(GameColor.MAGENTA);
			break;
		    case 76:
		    case 77:
			ao = new ArenaObject(GameObjectID.TUNNEL);
			ao.setColor(GameColor.WHITE);
			break;
		    case 78:
		    case 79:
			ao = new ArenaObject(GameObjectID.TUNNEL);
			ao.setColor(GameColor.GRAY);
			break;
		    default:
			ao = new ArenaObject(GameObjectID.PLACEHOLDER);
		    }
		    t.setCell(a, ao, x, y, 0, ao.getLayer());
		}
	    }
	    // Convert level name
	    bytesRead = file.read(V4FileLevel.name, 0, V4FileLevel.NAME_SIZE);
	    if (bytesRead != V4FileLevel.NAME_SIZE) {
		return null;
	    }
	    final var levelName = Charset.forName(GlobalStrings.loadUntranslated(UntranslatedString.DEFAULT_CHARSET))
		    .decode(ByteBuffer.wrap(V4FileLevel.name)).toString();
	    a.setName(levelName);
	    // Convert level hint
	    bytesRead = file.read(V4FileLevel.hint, 0, V4FileLevel.HINT_SIZE);
	    if (bytesRead != V4FileLevel.HINT_SIZE) {
		return null;
	    }
	    final var levelHint = Charset.forName(GlobalStrings.loadUntranslated(UntranslatedString.DEFAULT_CHARSET))
		    .decode(ByteBuffer.wrap(V4FileLevel.hint)).toString();
	    a.setHint(levelHint);
	    // Convert level author
	    bytesRead = file.read(V4FileLevel.author, 0, V4FileLevel.AUTHOR_SIZE);
	    if (bytesRead != V4FileLevel.AUTHOR_SIZE) {
		return null;
	    }
	    final var levelAuthor = Charset.forName(GlobalStrings.loadUntranslated(UntranslatedString.DEFAULT_CHARSET))
		    .decode(ByteBuffer.wrap(V4FileLevel.author)).toString();
	    a.setAuthor(levelAuthor);
	    // Convert level difficulty
	    bytesRead = file.read(V4FileLevel.difficulty, 0, V4FileLevel.DIFFICULTY_SIZE);
	    if (bytesRead != V4FileLevel.DIFFICULTY_SIZE) {
		return null;
	    }
	    final var tempDiff = V4FileLevel.toInt(V4FileLevel.difficulty);
	    switch (tempDiff) {
	    case 1:
		a.setDifficulty(1);
		break;
	    case 2:
		a.setDifficulty(2);
		break;
	    case 4:
		a.setDifficulty(3);
		break;
	    case 8:
		a.setDifficulty(4);
		break;
	    case 16:
		a.setDifficulty(5);
		break;
	    default:
		a.setDifficulty(3);
		break;
	    }
	    t.fillNulls(a, new ArenaObject(GameObjectID.GROUND), new ArenaObject(GameObjectID.WALL), true);
	    t.resize(a, Arena.getMinFloors(), new ArenaObject(GameObjectID.PLACEHOLDER));
	    t.fillVirtual();
	    return t;
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }

    private static int toInt(final byte[] data) {
	if (data == null || data.length != 2) {
	    return 0x0;
	}
	return (0xff & data[0]) << 0 | (0xff & data[1]) << 8;
    }

    // Constructors
    private V4FileLevel() {
	// Do nothing
    }
}

package com.puttysoftware.lasertank.datatype;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.diane.fileio.GameIODataReader;
import com.puttysoftware.diane.fileio.GameIODataWriter;
import com.puttysoftware.diane.storage.StringStorage;
import com.puttysoftware.lasertank.helper.DifficultyHelper;
import com.puttysoftware.lasertank.index.Difficulty;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.Frame;
import com.puttysoftware.lasertank.index.GameColor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Index;

public class LaserTankLevel {
    // Constants for files
    private static final int METADATA_INDEX_NAME = 0;
    private static final int METADATA_INDEX_AUTHOR = 1;
    private static final int METADATA_INDEX_HINT = 2;
    private static final int METADATA_INDEXES = 3;
    private static final int OBJECT_DATA_ROWS = 24;
    private static final int OBJECT_DATA_COLS = 24;
    private static final int OBJECT_DATA_FLOORS = 9;
    private static final int OBJECT_DATA_METAS = 5;
    private static final int OBJECT_META_INDEX_OBJECTS = 0;
    private static final int OBJECT_META_INDEX_ATTRIBUTES = 1;
    private static final int OBJECT_META_INDEX_DIRECTIONS = 2;
    private static final int OBJECT_META_INDEX_INDEXES = 3;
    private static final int OBJECT_META_INDEX_FRAMES = 4;
    private static final int LVL_OBJECT_DATA_LEN = 256;
    private static final int LVL_NAME_LEN = 31;
    private static final int LVL_HINT_LEN = 256;
    private static final int LVL_AUTHOR_LEN = 31;

    private static void decodeObjectData(final byte[] rawData, final LaserTankLevelStorage storage) {
	final var floorIndex = 0;
	for (var x = 0; x < 16; x++) {
	    for (var y = 0; y < 16; y++) {
		final var z = x * 16 + y;
		final var oo = rawData[z];
		GameObjectID objID;
		Direction dirID;
		int indexID;
		Frame frameID;
		final var attrID = 0; // No attribute
		dirID = Direction.NONE; // No direction
		indexID = Index.NONE.ordinal(); // No index
		frameID = Frame.NONE; // Not animated
		switch (oo) {
		case 0:
		    objID = GameObjectID.GROUND; // Ground
		    break;
		case 1:
		    objID = GameObjectID.TANK; // Tank
		    dirID = Direction.NORTH; // North
		    break;
		case 2:
		    objID = GameObjectID.FLAG; // Flag
		    frameID = Frame.FIRST; // Animated
		    break;
		case 3:
		    objID = GameObjectID.WATER; // Water
		    frameID = Frame.FIRST; // Animated
		    break;
		case 4:
		    objID = GameObjectID.WALL; // Wall
		    break;
		case 5:
		    objID = GameObjectID.BOX; // Box
		    break;
		case 6:
		    objID = GameObjectID.BRICKS; // Bricks
		    break;
		case 7:
		    objID = GameObjectID.ANTI_TANK; // Anti-Tank
		    dirID = Direction.NORTH; // North
		    frameID = Frame.FIRST; // Animated
		    break;
		case 8:
		    objID = GameObjectID.ANTI_TANK; // Anti-Tank
		    dirID = Direction.EAST; // East
		    frameID = Frame.FIRST; // Animated
		    break;
		case 9:
		    objID = GameObjectID.ANTI_TANK; // Anti-Tank
		    dirID = Direction.SOUTH; // South
		    frameID = Frame.FIRST; // Animated
		    break;
		case 10:
		    objID = GameObjectID.ANTI_TANK; // Anti-Tank
		    dirID = Direction.WEST; // West
		    frameID = Frame.FIRST; // Animated
		    break;
		case 11:
		    objID = GameObjectID.MIRROR; // Mirror
		    dirID = Direction.NORTHWEST; // Northwest
		    break;
		case 12:
		    objID = GameObjectID.MIRROR; // Mirror
		    dirID = Direction.NORTHEAST; // Northeast
		    break;
		case 13:
		    objID = GameObjectID.MIRROR; // Mirror
		    dirID = Direction.SOUTHEAST; // Southeast
		    break;
		case 14:
		    objID = GameObjectID.MIRROR; // Mirror
		    dirID = Direction.SOUTHWEST; // Southwest
		    break;
		case 15:
		    objID = GameObjectID.TANK_MOVER; // Tank Mover
		    dirID = Direction.NORTH; // North
		    frameID = Frame.FIRST; // Animated
		    break;
		case 16:
		    objID = GameObjectID.TANK_MOVER; // Tank Mover
		    dirID = Direction.EAST; // East
		    frameID = Frame.FIRST; // Animated
		    break;
		case 17:
		    objID = GameObjectID.TANK_MOVER; // Tank Mover
		    dirID = Direction.SOUTH; // South
		    frameID = Frame.FIRST; // Animated
		    break;
		case 18:
		    objID = GameObjectID.TANK_MOVER; // Tank Mover
		    dirID = Direction.WEST; // West
		    frameID = Frame.FIRST; // Animated
		    break;
		case 19:
		    objID = GameObjectID.CRYSTAL_BLOCK; // Crystal Block
		    break;
		case 20:
		    objID = GameObjectID.ROTARY_MIRROR; // Rotary Mirror
		    dirID = Direction.NORTHWEST; // Northwest
		    break;
		case 21:
		    objID = GameObjectID.ROTARY_MIRROR; // Rotary Mirror
		    dirID = Direction.NORTHEAST; // Northeast
		    break;
		case 22:
		    objID = GameObjectID.ROTARY_MIRROR; // Rotary Mirror
		    dirID = Direction.SOUTHEAST; // Southeast
		    break;
		case 23:
		    objID = GameObjectID.ROTARY_MIRROR; // Rotary Mirror
		    dirID = Direction.SOUTHWEST; // Southwest
		    break;
		case 24:
		    objID = GameObjectID.ICE; // Ice
		    break;
		case 25:
		    objID = GameObjectID.THIN_ICE; // Thin Ice
		    break;
		case 64:
		case 65:
		    objID = GameObjectID.TUNNEL; // Tunnel
		    indexID = GameColor.RED.ordinal(); // Red
		    break;
		case 66:
		case 67:
		    objID = GameObjectID.TUNNEL; // Tunnel
		    indexID = GameColor.GREEN.ordinal(); // Green
		    break;
		case 68:
		case 69:
		    objID = GameObjectID.TUNNEL; // Tunnel
		    indexID = GameColor.BLUE.ordinal(); // Blue
		    break;
		case 70:
		case 71:
		    objID = GameObjectID.TUNNEL; // Tunnel
		    indexID = GameColor.CYAN.ordinal(); // Cyan
		    break;
		case 72:
		case 73:
		    objID = GameObjectID.TUNNEL; // Tunnel
		    indexID = GameColor.YELLOW.ordinal(); // Yellow
		    break;
		case 74:
		case 75:
		    objID = GameObjectID.TUNNEL; // Tunnel
		    indexID = GameColor.MAGENTA.ordinal(); // Magenta
		    break;
		case 76:
		case 77:
		    objID = GameObjectID.TUNNEL; // Tunnel
		    indexID = GameColor.WHITE.ordinal(); // White
		    break;
		case 78:
		case 79:
		    objID = GameObjectID.TUNNEL; // Tunnel
		    indexID = GameColor.GRAY.ordinal(); // Gray
		    break;
		default:
		    objID = GameObjectID.PLACEHOLDER; // Placeholder
		}
		// Populate Object ID
		storage.setCell(objID.ordinal(), x, y, floorIndex, LaserTankLevel.OBJECT_META_INDEX_OBJECTS);
		// Populate Attribute ID
		storage.setCell(attrID, x, y, floorIndex, LaserTankLevel.OBJECT_META_INDEX_ATTRIBUTES);
		// Populate Direction ID
		storage.setCell(dirID.ordinal(), x, y, floorIndex, LaserTankLevel.OBJECT_META_INDEX_DIRECTIONS);
		// Populate Index ID
		storage.setCell(indexID, x, y, floorIndex, LaserTankLevel.OBJECT_META_INDEX_INDEXES);
		// Populate Frame ID
		storage.setCell(frameID.ordinal(), x, y, floorIndex, LaserTankLevel.OBJECT_META_INDEX_FRAMES);
	    }
	}
    }

    public static int getColumns() {
	return LaserTankLevel.OBJECT_DATA_COLS;
    }

    public static int getFloors() {
	return LaserTankLevel.OBJECT_DATA_FLOORS;
    }

    public static int getRows() {
	return LaserTankLevel.OBJECT_DATA_ROWS;
    }

    public static LaserTankLevel load(final String filename) throws IOException {
	try (var gio = new GameIODataReader(filename)) {
	    return LaserTankLevel.loadFromGameIOData(gio);
	}
    }

    public static LaserTankLevel loadLegacy(final String filename) throws IOException {
	try (var gio = new GameIODataReader(filename)) {
	    return LaserTankLevel.loadLegacyFromGameIOData(gio);
	}
    }

    // Internal stuff
    private static LaserTankLevel loadFromGameIOData(final GameIODataReader gio) throws IOException {
	// Create a level object
	final var levelData = new LaserTankLevel();
	final var levelCount = gio.readInt();
	for (var l = 0; l < levelCount; l++) {
	    // Add a level
	    levelData.addLevel();
	    // Load name
	    levelData.setName(gio.readString(), l);
	    // Load author
	    levelData.setAuthor(gio.readString(), l);
	    // Load hint
	    levelData.setHint(gio.readString(), l);
	    // Load difficulty
	    levelData.setDifficulty(DifficultyHelper.fromOrdinal(gio.readByte()), l);
	    // Load object data
	    levelData.objectData.set(l, LaserTankLevelStorage.load(gio));
	}
	return levelData;
    }

    private static LaserTankLevel loadLegacyFromGameIOData(final GameIODataReader gio) throws IOException {
	// Create a level object
	final var levelData = new LaserTankLevel();
	var levelIndex = 0;
	while (!gio.atEOF()) {
	    // Add a level
	    levelData.addLevel();
	    // Load and decode object data
	    LaserTankLevel.decodeObjectData(gio.readBytes(LaserTankLevel.LVL_OBJECT_DATA_LEN),
		    levelData.objectData.get(levelIndex));
	    // Load name
	    final var nameData = new byte[LaserTankLevel.LVL_NAME_LEN];
	    final var loadName = gio.readWindowsString(nameData);
	    // Load author
	    final var authorData = new byte[LaserTankLevel.LVL_AUTHOR_LEN];
	    final var loadAuthor = gio.readWindowsString(authorData);
	    // Load hint
	    final var hintData = new byte[LaserTankLevel.LVL_HINT_LEN];
	    final var loadHint = gio.readWindowsString(hintData);
	    // Load difficulty
	    final var loadDifficulty = DifficultyHelper.fromOrdinal(gio.readUnsignedShortByteArrayAsInt());
	    // Populate metadata
	    levelData.setName(loadName, levelIndex);
	    levelData.setAuthor(loadAuthor, levelIndex);
	    levelData.setHint(loadHint, levelIndex);
	    levelData.setDifficulty(loadDifficulty, levelIndex);
	    // Next level
	    levelIndex += 1;
	}
	return levelData;
    }

    // Fields
    private int levelCount;
    private final ArrayList<StringStorage> metaData;
    private final ArrayList<Difficulty> difficulty;
    private final ArrayList<LaserTankLevelStorage> objectData;

    // Constructors - used only internally
    private LaserTankLevel() {
	this.levelCount = 0;
	this.metaData = new ArrayList<>();
	this.difficulty = new ArrayList<>();
	this.objectData = new ArrayList<>();
    }

    private LaserTankLevel(final LaserTankLevel source) {
	this.levelCount = source.levelCount;
	this.metaData = new ArrayList<>();
	for (final var item : source.metaData) {
	    this.metaData.add(new StringStorage(item));
	}
	this.difficulty = new ArrayList<>();
	this.difficulty.addAll(source.difficulty);
	this.objectData = new ArrayList<>();
	for (final var item : source.objectData) {
	    this.objectData.add(new LaserTankLevelStorage(item));
	}
    }

    // Methods
    public final void addLevel() {
	this.metaData.add(new StringStorage(LaserTankLevel.METADATA_INDEXES));
	this.difficulty.add(Difficulty.KIDS);
	this.objectData.add(new LaserTankLevelStorage(LaserTankLevel.OBJECT_DATA_ROWS, LaserTankLevel.OBJECT_DATA_COLS,
		LaserTankLevel.OBJECT_DATA_FLOORS, LaserTankLevel.OBJECT_DATA_METAS));
	this.levelCount += 1;
    }

    public final int getAttributeID(final int row, final int col, final int floor, final int level) {
	return this.objectData.get(level).getCell(row, col, floor, LaserTankLevel.OBJECT_META_INDEX_ATTRIBUTES);
    }

    public final String getAuthor(final int level) {
	return this.metaData.get(level).getCell(LaserTankLevel.METADATA_INDEX_AUTHOR);
    }

    public final Difficulty getDifficulty(final int level) {
	return this.difficulty.get(level);
    }

    public final int getDirectionID(final int row, final int col, final int floor, final int level) {
	return this.objectData.get(level).getCell(row, col, floor, LaserTankLevel.OBJECT_META_INDEX_DIRECTIONS);
    }

    public final int getFrameID(final int row, final int col, final int floor, final int level) {
	return this.objectData.get(level).getCell(row, col, floor, LaserTankLevel.OBJECT_META_INDEX_FRAMES);
    }

    public final String getHint(final int level) {
	return this.metaData.get(level).getCell(LaserTankLevel.METADATA_INDEX_HINT);
    }

    public final int getIndexID(final int row, final int col, final int floor, final int level) {
	return this.objectData.get(level).getCell(row, col, floor, LaserTankLevel.OBJECT_META_INDEX_INDEXES);
    }

    public final int getLevels() {
	return this.levelCount;
    }

    public final String getName(final int level) {
	return this.metaData.get(level).getCell(LaserTankLevel.METADATA_INDEX_NAME);
    }

    public final int getObjectID(final int row, final int col, final int floor, final int level) {
	return this.objectData.get(level).getCell(row, col, floor, LaserTankLevel.OBJECT_META_INDEX_OBJECTS);
    }

    public final void removeLevel(final int level) {
	this.metaData.remove(level);
	this.difficulty.remove(level);
	this.objectData.remove(level);
	this.levelCount -= 1;
    }

    public void save(final String filename) throws IOException {
	try (var gio = new GameIODataWriter(filename)) {
	    this.saveToGameIOData(gio);
	}
    }

    private void saveToGameIOData(final GameIODataWriter gio) throws IOException {
	gio.writeInt(this.levelCount);
	for (var l = 0; l < this.levelCount; l++) {
	    gio.writeString(this.getName(l));
	    gio.writeString(this.getAuthor(l));
	    gio.writeString(this.getHint(l));
	    gio.writeByte((byte) this.getDifficulty(l).ordinal());
	    this.objectData.get(l).save(gio);
	}
    }

    public final void setAttributeID(final int newID, final int row, final int col, final int floor, final int level) {
	this.objectData.get(level).setCell(newID, row, col, floor, LaserTankLevel.OBJECT_META_INDEX_ATTRIBUTES);
    }

    public final void setAuthor(final String newValue, final int level) {
	this.metaData.get(level).setCell(newValue, LaserTankLevel.METADATA_INDEX_AUTHOR);
    }

    public final void setDifficulty(final Difficulty newValue, final int level) {
	this.difficulty.set(level, newValue);
    }

    public final void setDirectionID(final int newID, final int row, final int col, final int floor, final int level) {
	this.objectData.get(level).setCell(newID, row, col, floor, LaserTankLevel.OBJECT_META_INDEX_DIRECTIONS);
    }

    public final void setFrameID(final int newID, final int row, final int col, final int floor, final int level) {
	this.objectData.get(level).setCell(newID, row, col, floor, LaserTankLevel.OBJECT_META_INDEX_FRAMES);
    }

    public final void setHint(final String newValue, final int level) {
	this.metaData.get(level).setCell(newValue, LaserTankLevel.METADATA_INDEX_HINT);
    }

    public final void setIndexID(final int newID, final int row, final int col, final int floor, final int level) {
	this.objectData.get(level).setCell(newID, row, col, floor, LaserTankLevel.OBJECT_META_INDEX_INDEXES);
    }

    public final void setName(final String newValue, final int level) {
	this.metaData.get(level).setCell(newValue, LaserTankLevel.METADATA_INDEX_NAME);
    }

    public final void setObjectID(final int newID, final int row, final int col, final int floor, final int level) {
	this.objectData.get(level).setCell(newID, row, col, floor, LaserTankLevel.OBJECT_META_INDEX_OBJECTS);
    }
}

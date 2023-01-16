/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.Objects;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.objects.Empty;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.datatype.GameObjectData;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.GameColorHelper;
import com.puttysoftware.lasertank.helper.GameTypeHelper;
import com.puttysoftware.lasertank.helper.LaserTypeHelper;
import com.puttysoftware.lasertank.helper.RangeTypeHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameColor;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;
import com.puttysoftware.lasertank.locale.global.DataLoaderString;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;

public abstract class AbstractArenaObject {
    private static class ObjectImageResolver {
	public static String getImageName(final GameObjectID objID) {
	    return Integer.toString(objID.ordinal());
	}

	public static String getImageName(final GameObjectID objID, final int frameID) {
	    return Integer.toString(objID.ordinal()) + GlobalStrings.loadDataLoader(DataLoaderString.SUB_SEPARATOR)
		    + Integer.toString(frameID);
	}

	public static String getImageName(final GameObjectID objID, final Direction dir) {
	    return Integer.toString(objID.ordinal())
		    + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR)
		    + DirectionHelper.toStringValue(dir);
	}

	public static String getImageName(final GameObjectID objID, final Direction dir, final int frameID) {
	    return Integer.toString(objID.ordinal())
		    + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR)
		    + DirectionHelper.toStringValue(dir) + GlobalStrings.loadDataLoader(DataLoaderString.SUB_SEPARATOR)
		    + Integer.toString(frameID);
	}

	// Private constructor
	private ObjectImageResolver() {
	    // Do nothing
	}
    }

    static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;

    public static final int getImbuedRangeForce(final Material materialID) {
	return GameObjectData.getImbuedRangeForce(materialID);
    }

    // Properties
    private BitSet type;
    private int timerValue;
    private boolean timerActive;
    private int frameNumber;
    private Direction direction;
    private GameColor color;
    private int index;
    private boolean imageEnabled;
    private AbstractArenaObject savedObject;
    private AbstractArenaObject previousState;

    // Constructors
    public AbstractArenaObject() {
	this.type = new BitSet(GameTypeHelper.COUNT);
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = this.isAnimated() ? 1 : 0;
	this.direction = this.getInitialDirection();
	this.color = GameColor.NONE;
	this.imageEnabled = true;
	this.index = 0;
    }

    public final boolean acceptTick(final GameAction actionType) {
	return GameObjectData.acceptTick(this.getID(), actionType);
    }

    public final void activateTimer(final int ticks) {
	this.timerActive = true;
	this.timerValue = ticks;
    }

    protected final void addType(final GameType newType) {
	this.type.set(newType.ordinal());
    }

    public AbstractArenaObject attributeGameRenderHook() {
	return null;
    }

    public final boolean canMove() {
	return GameObjectData.canMove(this.getID());
    }

    public final boolean canShoot() {
	return GameObjectData.canShoot(this.getID());
    }

    /**
     *
     * @param materialID
     * @return
     */
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	return this;
    }

    // Methods
    @Override
    public AbstractArenaObject clone() {
	try {
	    final AbstractArenaObject copy = this.getClass().getConstructor().newInstance();
	    copy.type = (BitSet) this.type.clone();
	    copy.timerValue = this.timerValue;
	    copy.timerActive = this.timerActive;
	    copy.frameNumber = this.frameNumber;
	    copy.direction = this.direction;
	    copy.color = this.color;
	    return copy;
	} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
	    LaserTankEE.logError(e);
	    return null;
	}
    }

    public boolean defersSetProperties() {
	return false;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public final void determineCurrentAppearance(final int x, final int y, final int z) {
	// Do nothing
    }

    public boolean doLasersPassThrough() {
	return !this.isSolid();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void editorPlaceHook(final int x, final int y, final int z) {
	// Do nothing
    }

    public AbstractArenaObject editorPropertiesHook() {
	if (this.hasDirection()) {
	    this.toggleDirection();
	    return this;
	}
	return null;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void editorRemoveHook(final int x, final int y, final int z) {
	// Do nothing
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof final AbstractArenaObject other)) {
	    return false;
	}
	if (!Objects.equals(this.type, other.type)) {
	    return false;
	}
	if (this.direction != other.direction) {
	    return false;
	}
	if (this.color != other.color) {
	    return false;
	}
	return true;
    }

    public final int getBlockHeight() {
	return GameObjectData.getBlockHeight(this.getID());
    }

    public final GameColor getColor() {
	return this.color;
    }

    public int getCustomFormat() {
	return 0;
    }

    abstract public int getCustomProperty(int propID);

    public String getCustomText() {
	return null;
    }

    public Color getCustomTextColor() {
	return null;
    }

    public final Direction getDirection() {
	return this.direction;
    }

    public final int getFrameNumber() {
	return this.frameNumber;
    }

    private final int getLastFrameNumber() {
	return GameObjectData.getLastFrameNumber(this.getID());
    }

    private final String getIdentifier() {
	return this.getImageName();
    }

    public final String getImageName() {
	if (this.hasDirection() && this.isAnimated()) {
	    return ObjectImageResolver.getImageName(this.getID(), this.direction, this.frameNumber);
	}
	if (!this.hasDirection() && this.isAnimated()) {
	    return ObjectImageResolver.getImageName(this.getID(), this.frameNumber);
	}
	if (this.hasDirection() && !this.isAnimated()) {
	    return ObjectImageResolver.getImageName(this.getID(), this.direction);
	} else {
	    return ObjectImageResolver.getImageName(this.getID());
	}
    }

    private final Direction getInitialDirection() {
	return GameObjectData.getValidDirections(this.getID())[0];
    }

    public final int getLayer() {
	return GameObjectData.getLayer(getID());
    }

    public final Material getMaterial() {
	return GameObjectData.getMaterial(getID());
    }

    public final int getMinimumReactionForce() {
	return GameObjectData.getMinimumReactionForce(this.getMaterial());
    }

    public final AbstractArenaObject getPreviousState() {
	return this.previousState;
    }

    public final AbstractArenaObject getSavedObject() {
	return this.savedObject;
    }

    abstract public GameObjectID getID();

    private final boolean hasDirection() {
	return this.direction != Direction.NONE;
    }

    public final boolean hasFriction() {
	return GameObjectData.hasFriction(this.getID());
    }

    @Override
    public int hashCode() {
	final var prime = 31;
	var result = 1;
	result = prime * result + (this.timerActive ? 1231 : 1237);
	result = prime * result + this.timerValue;
	result = prime * result + (this.type == null ? 0 : this.type.hashCode());
	result = prime * result + this.direction.hashCode();
	return prime * result + this.color.ordinal();
    }

    public final boolean hasPreviousState() {
	return this.previousState != null;
    }

    public final boolean hitReflectiveSide(final Direction dir) {
	if (!GameObjectData.isReflective(this.getID(), dir)) {
	    return false;
	}
	return GameObjectData.hitReflectiveSide(dir);
    }

    private final boolean isAnimated() {
	return GameObjectData.isAnimated(this.getID());
    }

    public boolean isConditionallySolid() {
	return this.isSolid();
    }

    public boolean isEnabled() {
	return this.imageEnabled;
    }

    public final boolean isOfType(final GameType testType) {
	return this.type.get(testType.ordinal());
    }

    public final boolean isPushable() {
	return GameObjectData.isPushable(this.getID());
    }

    public final boolean isSolid() {
	return GameObjectData.isSolid(this.getID());
    }

    public final boolean killsOnMove() {
	return GameObjectData.killsOnMove(this.getID(), this.index);
    }

    public void laserDoneAction() {
	// Do nothing
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param laserType
     * @param forceUnits
     * @return
     */
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final var dir = DirectionHelper.resolveRelative(dirX, dirY);
	if (this.isSolid()) {
	    if (forceUnits > this.getMinimumReactionForce() && this.canMove()) {
		try {
		    final var nextObj = LaserTankEE.getApplication().getArenaManager().getArena().getCell(locX + dirX,
			    locY + dirY, locZ, this.getLayer());
		    final var nextObj2 = LaserTankEE.getApplication().getArenaManager().getArena()
			    .getCell(locX + dirX * 2, locY + dirY * 2, locZ, this.getLayer());
		    if (this instanceof final AbstractMovableObject gmo && nextObj instanceof AbstractMovableObject gmo2
			    && nextObj.canMove()
			    && (nextObj2 != null && !nextObj2.isConditionallySolid() || forceUnits > 2)) {
			LaserTankEE.getApplication().getGameManager().updatePushedPositionLater(locX, locY, dirX, dirY,
				gmo, locX + dirX, locY + dirY, gmo2, laserType,
				forceUnits - Math.max(1, this.getMinimumReactionForce()));
		    } else {
			// Object crushed by impact
			this.pushCrushAction(locX, locY, locZ);
		    }
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Object crushed by impact
		    this.pushCrushAction(locX, locY, locZ);
		}
	    } else {
		final var adj = LaserTankEE.getApplication().getArenaManager().getArena().getCell(locX - dirX,
			locY - dirY, locZ, this.getLayer());
		if (adj != null && !adj.rangeAction(locX - 2 * dirX, locY - 2 * dirY, locZ, dirX, dirY,
			LaserTypeHelper.rangeType(laserType), 1)) {
		    Sounds.play(Sound.LASER_DIE);
		}
	    }
	    return Direction.NONE;
	}
	if (GameObjectData.isReflective(this.getID(), dir) && GameObjectData.hitReflectiveSide(dir)) {
	    return this.direction;
	}
	return DirectionHelper.resolveRelative(dirX, dirY);
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param laserType
     * @return
     */
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType) {
	final var dir = DirectionHelper.resolveRelative(dirX, dirY);
	if (!GameObjectData.isReflective(this.getID(), dir) || !GameObjectData.hitReflectiveSide(dir)) {
	    return DirectionHelper.resolveRelative(dirX, dirY);
	}
	// Finish reflecting laser
	Sounds.play(Sound.REFLECT);
	final var oldlaser = DirectionHelper.resolveRelativeInvert(locX, locY);
	final var currdir = this.getDirection();
	if (oldlaser == Direction.NORTH) {
	    if (currdir == Direction.NORTHWEST) {
		return Direction.WEST;
	    }
	    if (currdir == Direction.NORTHEAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.SOUTH) {
	    if (currdir == Direction.SOUTHWEST) {
		return Direction.WEST;
	    }
	    if (currdir == Direction.SOUTHEAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.WEST) {
	    if (currdir == Direction.SOUTHWEST) {
		return Direction.SOUTH;
	    }
	    if (currdir == Direction.NORTHWEST) {
		return Direction.NORTH;
	    }
	} else if (oldlaser == Direction.EAST) {
	    if (currdir == Direction.SOUTHEAST) {
		return Direction.SOUTH;
	    }
	    if (currdir == Direction.NORTHEAST) {
		return Direction.NORTH;
	    }
	}
	return Direction.NONE;
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     */
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	Sounds.play(Sound.BUMP_HEAD);
    }

    public final void nextFrame() {
	if (this.isAnimated()) {
	    this.frameNumber++;
	    if (this.frameNumber > this.getLastFrameNumber()) {
		this.frameNumber = 1;
	    }
	}
    }

    // Scripting
    public abstract void postMoveAction(final int dirX, final int dirY, int dirZ);

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     */
    public void pushCollideAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// Do nothing
    }

    protected void pushCrushAction(final int x, final int y, final int z) {
	// Object crushed
	Sounds.play(Sound.CRUSH);
	LaserTankEE.getApplication().getGameManager().morph(new Empty(), x, y, z, this.getLayer());
    }

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// Do nothing
	return true;
    }

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     */
    public void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// Do nothing
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param rangeType
     * @param forceUnits
     * @return
     */
    public boolean rangeAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.getMaterial() == Material.WOODEN
		&& this.changesToOnExposure(Material.FIRE) != null) {
	    // Burn wooden object
	    Sounds.play(Sound.BURN);
	    LaserTankEE.getApplication().getGameManager().morph(this.changesToOnExposure(Material.FIRE), locX + dirX,
		    locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypeHelper.material(rangeType) == Material.ICE && (this.getMaterial() == Material.METALLIC
		|| this.getMaterial() == Material.WOODEN || this.getMaterial() == Material.PLASTIC)
		&& this.changesToOnExposure(Material.ICE) != null) {
	    // Freeze metal, wooden, or plastic object
	    Sounds.play(Sound.FREEZE);
	    LaserTankEE.getApplication().getGameManager().morph(this.changesToOnExposure(Material.ICE), locX + dirX,
		    locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.getMaterial() == Material.ICE
		&& this.changesToOnExposure(Material.FIRE) != null) {
	    // Melt icy object
	    Sounds.play(Sound.DEFROST);
	    LaserTankEE.getApplication().getGameManager().morph(this.changesToOnExposure(Material.FIRE), locX + dirX,
		    locY + dirY, locZ, this.getLayer());
	    return true;
	} else if (RangeTypeHelper.material(rangeType) == Material.ICE && this.getMaterial() == Material.FIRE
		&& this.changesToOnExposure(Material.ICE) != null) {
	    // Cool hot object
	    Sounds.play(Sound.COOL_OFF);
	    LaserTankEE.getApplication().getGameManager().morph(this.changesToOnExposure(Material.ICE), locX + dirX,
		    locY + dirY, locZ, this.getLayer());
	    return true;
	} else if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.getMaterial() == Material.METALLIC
		&& this.changesToOnExposure(Material.FIRE) != null) {
	    // Melt metal object
	    Sounds.play(Sound.MELT);
	    LaserTankEE.getApplication().getGameManager().morph(this.changesToOnExposure(Material.FIRE), locX + dirX,
		    locY + dirY, locZ, this.getLayer());
	    return true;
	}
	return false;
    }

    public final AbstractArenaObject readArenaObjectG2(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    reader.readInt();
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    return this.readArenaObjectHookG2(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractArenaObject readArenaObjectG3(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    // Discard material
	    reader.readInt();
	    return this.readArenaObjectHookG3(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	// Discard material
	reader.readInt();
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractArenaObject readArenaObjectG4(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    return this.readArenaObjectHookG4(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractArenaObject readArenaObjectG5(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    return this.readArenaObjectHookG5(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractArenaObject readArenaObjectG6(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = GameColorHelper.fromOrdinal(reader.readInt());
	    return this.readArenaObjectHookG6(reader, formatVersion);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = GameColorHelper.fromOrdinal(reader.readInt());
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractArenaObject readArenaObjectHookG2(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractArenaObject readArenaObjectHookG3(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractArenaObject readArenaObjectHookG4(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractArenaObject readArenaObjectHookG5(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractArenaObject readArenaObjectHookG6(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    public final void setColor(final GameColor col) {
	this.color = col;
    }

    abstract public void setCustomProperty(int propID, int value);

    public final void setDirection(final Direction dir) {
	this.direction = dir;
    }

    public void setEnabled(final boolean value) {
	this.imageEnabled = value;
    }

    public final void setPreviousState(final AbstractArenaObject obj) {
	this.previousState = obj;
    }

    public final void setSavedObject(final AbstractArenaObject obj) {
	this.savedObject = obj;
    }

    public final boolean solvesOnMove() {
	return GameObjectData.solvesOnMove(getID());
    }

    public final void tickTimer(final int dirX, final int dirY, final GameAction actionType) {
	if (this.timerActive && this.acceptTick(actionType)) {
	    this.timerValue--;
	    if (this.timerValue == 0) {
		this.timerActive = false;
		this.timerExpiredAction(dirX, dirY);
	    }
	}
    }

    /**
     *
     * @param dirX
     * @param dirY
     */
    public void timerExpiredAction(final int dirX, final int dirY) {
	// Do nothing
    }

    public final void toggleDirection() {
	this.direction = DirectionHelper.nextOrthogonal(this.direction);
    }

    public final void toggleDirectionInvert() {
	this.direction = DirectionHelper.previousOrthogonal(this.direction);
    }

    public final void writeArenaObject(final DataIOWriter writer) throws IOException {
	writer.writeString(this.getIdentifier());
	final var cc = this.getCustomFormat();
	if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color.ordinal());
	    this.writeArenaObjectHook(writer);
	} else {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color.ordinal());
	    for (var x = 0; x < cc; x++) {
		final var cx = this.getCustomProperty(x + 1);
		writer.writeInt(cx);
	    }
	}
    }

    /**
     *
     * @param writer
     * @throws IOException
     */
    protected void writeArenaObjectHook(final DataIOWriter writer) throws IOException {
	// Do nothing - but let subclasses override
    }
}

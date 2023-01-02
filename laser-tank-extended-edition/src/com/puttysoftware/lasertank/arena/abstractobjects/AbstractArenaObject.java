/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.objects.Empty;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
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
import com.puttysoftware.lasertank.utility.ObjectImageResolver;

public abstract class AbstractArenaObject {
    static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;
    private static final int PLASTIC_MINIMUM_REACTION_FORCE = 0;
    private static final int DEFAULT_MINIMUM_REACTION_FORCE = 1;
    private static final int METAL_MINIMUM_REACTION_FORCE = 2;

    public static final int getImbuedRangeForce(final Material materialID) {
	if (materialID == Material.PLASTIC) {
	    return AbstractArenaObject.PLASTIC_MINIMUM_REACTION_FORCE;
	} else if (materialID == Material.METALLIC) {
	    return AbstractArenaObject.METAL_MINIMUM_REACTION_FORCE;
	} else {
	    return AbstractArenaObject.DEFAULT_MINIMUM_REACTION_FORCE;
	}
    }

    public static boolean hitReflectiveSide(final Direction dir) {
	Direction trigger1, trigger2;
	trigger1 = DirectionHelper.previous(dir);
	trigger2 = DirectionHelper.next(dir);
	return dir == trigger1 || dir == trigger2;
    }

    // Properties
    private boolean solid;
    private boolean pushable;
    private boolean friction;
    private BitSet type;
    private int timerValue;
    private boolean timerActive;
    private int frameNumber;
    private Direction direction;
    private boolean diagonalOnly;
    private GameColor color;
    private Material material;
    private boolean imageEnabled;
    private AbstractArenaObject savedObject;
    private AbstractArenaObject previousState;

    public AbstractArenaObject() {
	this.solid = false;
	this.pushable = false;
	this.friction = true;
	this.type = new BitSet(GameTypeHelper.COUNT);
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.diagonalOnly = false;
	this.color = GameColor.NONE;
	this.material = Material.NONE;
	this.imageEnabled = true;
    }

    // Constructors
    AbstractArenaObject(final boolean isSolid) {
	this.solid = isSolid;
	this.pushable = false;
	this.friction = true;
	this.type = new BitSet(GameTypeHelper.COUNT);
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.diagonalOnly = false;
	this.color = GameColor.NONE;
	this.material = Material.NONE;
	this.imageEnabled = true;
    }

    AbstractArenaObject(final boolean isSolid, final boolean isPushable, final boolean hasFriction) {
	this.solid = isSolid;
	this.pushable = isPushable;
	this.friction = hasFriction;
	this.type = new BitSet(GameTypeHelper.COUNT);
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.diagonalOnly = false;
	this.color = GameColor.NONE;
	this.material = Material.NONE;
	this.imageEnabled = true;
    }

    /**
     *
     * @param actionType
     * @return
     */
    public boolean acceptTick(final GameAction actionType) {
	return true;
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

    public boolean canMove() {
	return false;
    }

    public boolean canShoot() {
	return false;
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
	    copy.solid = this.solid;
	    copy.pushable = this.pushable;
	    copy.friction = this.friction;
	    copy.type = (BitSet) this.type.clone();
	    copy.timerValue = this.timerValue;
	    copy.timerActive = this.timerActive;
	    copy.frameNumber = this.frameNumber;
	    copy.direction = this.direction;
	    copy.diagonalOnly = this.diagonalOnly;
	    copy.color = this.color;
	    copy.material = this.material;
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
	return true;
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
	} else {
	    return null;
	}
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
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof AbstractArenaObject)) {
	    return false;
	}
	final AbstractArenaObject other = (AbstractArenaObject) obj;
	if (this.friction != other.friction) {
	    return false;
	}
	if (this.pushable != other.pushable) {
	    return false;
	}
	if (this.solid != other.solid) {
	    return false;
	}
	if (this.type == null) {
	    if (other.type != null) {
		return false;
	    }
	} else if (!this.type.equals(other.type)) {
	    return false;
	}
	if (this.direction != other.direction) {
	    return false;
	}
	if (this.color != other.color) {
	    return false;
	}
	if (this.material != other.material) {
	    return false;
	}
	return true;
    }

    public int getBlockHeight() {
	return 1;
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

    private final String getIdentifier() {
	return this.getImageName();
    }

    public final String getImageName() {
	if (this.hasDirection() && this.isAnimated()) {
	    return ObjectImageResolver.getImageName(getStringBaseID(), this.direction, this.frameNumber);
	} else if (!this.hasDirection() && this.isAnimated()) {
	    return ObjectImageResolver.getImageName(getStringBaseID(), this.frameNumber);
	} else if (this.hasDirection() && !this.isAnimated()) {
	    return ObjectImageResolver.getImageName(getStringBaseID(), this.direction);
	} else {
	    return ObjectImageResolver.getImageName(getStringBaseID());
	}
    }

    abstract public int getLayer();

    public final Material getMaterial() {
	return this.material;
    }

    public final int getMinimumReactionForce() {
	if (this.material == Material.PLASTIC) {
	    return AbstractArenaObject.PLASTIC_MINIMUM_REACTION_FORCE;
	} else if (this.material == Material.METALLIC) {
	    return AbstractArenaObject.METAL_MINIMUM_REACTION_FORCE;
	} else {
	    return AbstractArenaObject.DEFAULT_MINIMUM_REACTION_FORCE;
	}
    }

    public final AbstractArenaObject getPreviousState() {
	return this.previousState;
    }

    public final AbstractArenaObject getSavedObject() {
	return this.savedObject;
    }

    abstract public GameObjectID getStringBaseID();

    private final boolean hasDirection() {
	return this.direction != Direction.NONE;
    }

    public final boolean hasFriction() {
	return this.friction;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (this.friction ? 1231 : 1237);
	result = prime * result + (this.pushable ? 1231 : 1237);
	result = prime * result + (this.solid ? 1231 : 1237);
	result = prime * result + (this.timerActive ? 1231 : 1237);
	result = prime * result + this.timerValue;
	result = prime * result + (this.type == null ? 0 : this.type.hashCode());
	result = prime * result + this.direction.hashCode();
	result = prime * result + this.color.ordinal();
	return prime * result + this.material.ordinal();
    }

    public final boolean hasPreviousState() {
	return this.previousState != null;
    }

    private final boolean isAnimated() {
	return this.frameNumber > 0;
    }

    public boolean isConditionallySolid() {
	return this.solid;
    }

    public boolean isEnabled() {
	return this.imageEnabled;
    }

    public final boolean isOfType(final GameType testType) {
	return this.type.get(testType.ordinal());
    }

    public final boolean isPushable() {
	return this.pushable;
    }

    public final boolean isSolid() {
	return this.solid;
    }

    public boolean killsOnMove() {
	return false;
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
	if (this.isSolid()) {
	    if (forceUnits > this.getMinimumReactionForce() && this.canMove()) {
		try {
		    final AbstractArenaObject nextObj = LaserTankEE.getApplication().getArenaManager().getArena()
			    .getCell(locX + dirX, locY + dirY, locZ, this.getLayer());
		    final AbstractArenaObject nextObj2 = LaserTankEE.getApplication().getArenaManager().getArena()
			    .getCell(locX + dirX * 2, locY + dirY * 2, locZ, this.getLayer());
		    if (this instanceof AbstractMovableObject && nextObj != null
			    && nextObj instanceof AbstractMovableObject && nextObj.canMove()
			    && (nextObj2 != null && !nextObj2.isConditionallySolid() || forceUnits > 2)) {
			// Move BOTH this object and the one in front of it
			final AbstractMovableObject gmo = (AbstractMovableObject) this;
			final AbstractMovableObject gmo2 = (AbstractMovableObject) nextObj;
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
		final AbstractArenaObject adj = LaserTankEE.getApplication().getArenaManager().getArena()
			.getCell(locX - dirX, locY - dirY, locZ, this.getLayer());
		if (adj != null && !adj.rangeAction(locX - 2 * dirX, locY - 2 * dirY, locZ, dirX, dirY,
			LaserTypeHelper.rangeType(laserType), 1)) {
		    Sounds.play(Sound.LASER_DIE);
		}
	    }
	    return Direction.NONE;
	} else {
	    return DirectionHelper.resolveRelative(dirX, dirY);
	}
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
	return DirectionHelper.resolveRelative(dirX, dirY);
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
	} else if (RangeTypeHelper.material(rangeType) == Material.ICE && (this.getMaterial() == Material.METALLIC
		|| this.getMaterial() == Material.WOODEN || this.getMaterial() == Material.PLASTIC)
		&& this.changesToOnExposure(Material.ICE) != null) {
	    // Freeze metal, wooden, or plastic object
	    Sounds.play(Sound.FREEZE);
	    LaserTankEE.getApplication().getGameManager().morph(this.changesToOnExposure(Material.ICE), locX + dirX,
		    locY + dirY, locZ, this.getLayer());
	    return true;
	} else if (RangeTypeHelper.material(rangeType) == Material.FIRE && this.getMaterial() == Material.ICE
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
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		reader.readInt();
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		return this.readArenaObjectHookG2(reader, formatVersion);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    public final AbstractArenaObject readArenaObjectG3(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		// Discard material
		reader.readInt();
		return this.readArenaObjectHookG3(reader, formatVersion);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		// Discard material
		reader.readInt();
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    public final AbstractArenaObject readArenaObjectG4(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		return this.readArenaObjectHookG4(reader, formatVersion);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    public final AbstractArenaObject readArenaObjectG5(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		return this.readArenaObjectHookG5(reader, formatVersion);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    public final AbstractArenaObject readArenaObjectG6(final DataIOReader reader, final String ident,
	    final GameFormat formatVersion) throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		return this.readArenaObjectHookG6(reader, formatVersion);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = GameColorHelper.fromOrdinal(reader.readInt());
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
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

    public final void setDiagonalOnly(final boolean value) {
	this.diagonalOnly = value;
    }

    public final void setDirection(final Direction dir) {
	this.direction = dir;
    }

    public void setEnabled(final boolean value) {
	this.imageEnabled = value;
    }

    public final void setFrameNumber(final int frame) {
	this.frameNumber = frame;
    }

    protected final void setMaterial(final Material materialID) {
	this.material = materialID;
    }

    public final void setPreviousState(final AbstractArenaObject obj) {
	this.previousState = obj;
    }

    public final void setSavedObject(final AbstractArenaObject obj) {
	this.savedObject = obj;
    }

    public boolean solvesOnMove() {
	return false;
    }

    public final void tickTimer(final int dirX, final int dirY, final GameAction actionType) {
	if (this.timerActive) {
	    if (this.acceptTick(actionType)) {
		this.timerValue--;
		if (this.timerValue == 0) {
		    this.timerActive = false;
		    this.timerExpiredAction(dirX, dirY);
		}
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

    public final void toggleFrameNumber() {
	if (this.isAnimated()) {
	    this.frameNumber++;
	    if (this.frameNumber > 3) {
		this.frameNumber = 1;
	    }
	}
    }

    public final void writeArenaObject(final DataIOWriter writer) throws IOException {
	writer.writeString(this.getIdentifier());
	final int cc = this.getCustomFormat();
	if (cc == AbstractArenaObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color.ordinal());
	    this.writeArenaObjectHook(writer);
	} else {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color.ordinal());
	    for (int x = 0; x < cc; x++) {
		final int cx = this.getCustomProperty(x + 1);
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

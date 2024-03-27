/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena;

import java.io.IOException;

import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.fileio.DataIOWriter;
import com.puttysoftware.lasertank.helper.Players;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.GenericString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.storage.NumberStorage;

final class CurrentArenaLevelInfo implements ArenaLevelInfo {
    public static CurrentArenaLevelInfo readLevelInfo(final DataIOReader reader) throws IOException {
        final var li = new CurrentArenaLevelInfo();
        int x, y;
        for (y = 0; y < 3; y++) {
            for (x = 0; x < Players.COUNT; x++) {
        	li.playerData.setCell(reader.readInt(), y, x);
            }
        }
        li.horizontalWraparoundEnabled = reader.readBoolean();
        li.verticalWraparoundEnabled = reader.readBoolean();
        li.thirdDimensionWraparoundEnabled = reader.readBoolean();
        li.name = reader.readString();
        li.hint = reader.readString();
        li.author = reader.readString();
        li.difficulty = reader.readInt();
        li.moveShootAllowed = reader.readBoolean();
        return li;
    }

    private NumberStorage playerData;
    private boolean horizontalWraparoundEnabled;
    private boolean verticalWraparoundEnabled;
    private boolean thirdDimensionWraparoundEnabled;
    private String name;
    private String hint;
    private String author;
    private int difficulty;
    private boolean moveShootAllowed;
    
    // Constructors
    public CurrentArenaLevelInfo() {
	super();
	this.playerData = new NumberStorage(Players.DIMENSIONS, Players.COUNT);
	this.playerData.fill(-1);
	this.horizontalWraparoundEnabled = false;
	this.verticalWraparoundEnabled = false;
	this.thirdDimensionWraparoundEnabled = false;
	this.name = Strings.loadGeneric(GenericString.UN_NAMED_LEVEL);
	this.hint = Strings.loadCommon(CommonString.EMPTY);
	this.author = Strings.loadGeneric(GenericString.UNKNOWN_AUTHOR);
	this.difficulty = 1;
	this.moveShootAllowed = false;
    }

    public CurrentArenaLevelInfo(final CurrentArenaLevelInfo source) {
	super();
	this.playerData = new NumberStorage(source.playerData);
	this.horizontalWraparoundEnabled = source.horizontalWraparoundEnabled;
	this.verticalWraparoundEnabled = source.verticalWraparoundEnabled;
	this.thirdDimensionWraparoundEnabled = source.thirdDimensionWraparoundEnabled;
	this.name = source.name;
	this.hint = source.hint;
	this.author = source.author;
	this.difficulty = source.difficulty;
	this.moveShootAllowed = source.moveShootAllowed;
    }

    @Override
    public void disableHorizontalWraparound(ArenaLevelInfo currentArenaLevelInfo) {
        this.horizontalWraparoundEnabled = false;
    }

    @Override
    public void disableThirdDimensionWraparound(ArenaLevelInfo currentArenaLevelInfo) {
        this.thirdDimensionWraparoundEnabled = false;
    }

    @Override
    public void disableVerticalWraparound(ArenaLevelInfo currentArenaLevelInfo) {
        this.verticalWraparoundEnabled = false;
    }

    @Override
    public boolean doesPlayerExist(final int pi, ArenaLevelInfo currentArenaLevelInfo) {
        for (var y = 0; y < Players.DIMENSIONS; y++) {
            if (this.playerData.getCell(y, pi) == -1) {
        	return false;
            }
        }
        return true;
    }

    @Override
    public void enableHorizontalWraparound(ArenaLevelInfo currentArenaLevelInfo) {
        this.horizontalWraparoundEnabled = true;
    }

    @Override
    public void enableThirdDimensionWraparound(ArenaLevelInfo currentArenaLevelInfo) {
        this.thirdDimensionWraparoundEnabled = true;
    }

    @Override
    public void enableVerticalWraparound(ArenaLevelInfo currentArenaLevelInfo) {
        this.verticalWraparoundEnabled = true;
    }

    @Override
    public String getAuthor(ArenaLevelInfo currentArenaLevelInfo) {
        return this.author;
    }

    @Override
    public int getDifficulty(ArenaLevelInfo currentArenaLevelInfo) {
        return this.difficulty;
    }

    @Override
    public String getHint(ArenaLevelInfo currentArenaLevelInfo) {
        return this.hint;
    }

    @Override
    public String getName(ArenaLevelInfo currentArenaLevelInfo) {
        return this.name;
    }

    @Override
    public int getStartColumn(final int pi, ArenaLevelInfo currentArenaLevelInfo) {
        return this.playerData.getCell(0, pi);
    }

    @Override
    public int getStartFloor(final int pi, ArenaLevelInfo currentArenaLevelInfo) {
        return this.playerData.getCell(2, pi);
    }

    @Override
    public int getStartRow(final int pi, ArenaLevelInfo currentArenaLevelInfo) {
        return this.playerData.getCell(1, pi);
    }

    @Override
    public boolean isHorizontalWraparoundEnabled(ArenaLevelInfo currentArenaLevelInfo) {
        return this.horizontalWraparoundEnabled;
    }

    @Override
    public boolean isMoveShootAllowed(ArenaLevelInfo currentArenaLevelInfo) {
        return this.moveShootAllowed;
    }

    @Override
    public boolean isThirdDimensionWraparoundEnabled(ArenaLevelInfo currentArenaLevelInfo) {
        return this.thirdDimensionWraparoundEnabled;
    }

    @Override
    public boolean isVerticalWraparoundEnabled(ArenaLevelInfo currentArenaLevelInfo) {
        return this.verticalWraparoundEnabled;
    }

    @Override
    public void setAuthor(final String newAuthor, ArenaLevelInfo currentArenaLevelInfo) {
        this.author = newAuthor;
    }

    @Override
    public void setDifficulty(final int newDifficulty, ArenaLevelInfo currentArenaLevelInfo) {
        this.difficulty = newDifficulty;
    }

    @Override
    public void setHint(final String newHint, ArenaLevelInfo currentArenaLevelInfo) {
        this.hint = newHint;
    }

    @Override
    public void setMoveShootAllowed(final boolean value, ArenaLevelInfo currentArenaLevelInfo) {
        this.moveShootAllowed = value;
    }

    @Override
    public void setName(final String newName, ArenaLevelInfo currentArenaLevelInfo) {
        this.name = newName;
    }

    @Override
    public void setStartColumn(final int pi, final int value, ArenaLevelInfo currentArenaLevelInfo) {
        this.playerData.setCell(value, 0, pi);
    }

    @Override
    public void setStartFloor(final int pi, final int value, ArenaLevelInfo currentArenaLevelInfo) {
        this.playerData.setCell(value, 2, pi);
    }

    @Override
    public void setStartRow(final int pi, final int value, ArenaLevelInfo currentArenaLevelInfo) {
        this.playerData.setCell(value, 1, pi);
    }

    @Override
    public void writeLevelInfo(final DataIOWriter writer, ArenaLevelInfo currentArenaLevelInfo) throws IOException {
        int x, y;
        for (y = 0; y < Players.DIMENSIONS; y++) {
            for (x = 0; x < Players.COUNT; x++) {
        	writer.writeInt(this.playerData.getCell(y, x));
            }
        }
        writer.writeBoolean(this.horizontalWraparoundEnabled);
        writer.writeBoolean(this.verticalWraparoundEnabled);
        writer.writeBoolean(this.thirdDimensionWraparoundEnabled);
        writer.writeString(this.name);
        writer.writeString(this.hint);
        writer.writeString(this.author);
        writer.writeInt(this.difficulty);
        writer.writeBoolean(this.moveShootAllowed);
    }
}

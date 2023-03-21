/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.current;

import java.io.IOException;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.diane.storage.NumberStorage;
import com.puttysoftware.lasertank.helper.Players;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.GenericString;
import com.puttysoftware.lasertank.locale.Strings;

final class CurrentArenaLevelInfo {
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

	// Properties
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
		this.playerData = new NumberStorage(Players.DIMENSIONS, Players.COUNT);
		this.playerData.fill(-1);
		this.horizontalWraparoundEnabled = false;
		this.verticalWraparoundEnabled = false;
		this.name = Strings.loadGeneric(GenericString.UN_NAMED_LEVEL);
		this.author = Strings.loadGeneric(GenericString.UNKNOWN_AUTHOR);
		this.hint = Strings.loadCommon(CommonString.EMPTY);
		this.difficulty = 1;
		this.moveShootAllowed = false;
	}

	// Methods
	public CurrentArenaLevelInfo clone() {
		final var copy = new CurrentArenaLevelInfo();
		copy.playerData = new NumberStorage(this.playerData);
		copy.horizontalWraparoundEnabled = this.horizontalWraparoundEnabled;
		copy.verticalWraparoundEnabled = this.verticalWraparoundEnabled;
		copy.author = this.author;
		copy.name = this.name;
		copy.hint = this.hint;
		copy.difficulty = this.difficulty;
		copy.moveShootAllowed = this.moveShootAllowed;
		return copy;
	}

	public void disableHorizontalWraparound() {
		this.horizontalWraparoundEnabled = false;
	}

	public void disableThirdDimensionWraparound() {
		this.thirdDimensionWraparoundEnabled = false;
	}

	public void disableVerticalWraparound() {
		this.verticalWraparoundEnabled = false;
	}

	public boolean doesPlayerExist(final int pi) {
		for (var y = 0; y < Players.DIMENSIONS; y++) {
			if (this.playerData.getCell(y, pi) == -1) {
				return false;
			}
		}
		return true;
	}

	public void enableHorizontalWraparound() {
		this.horizontalWraparoundEnabled = true;
	}

	public void enableThirdDimensionWraparound() {
		this.thirdDimensionWraparoundEnabled = true;
	}

	public void enableVerticalWraparound() {
		this.verticalWraparoundEnabled = true;
	}

	public String getAuthor() {
		return this.author;
	}

	public int getDifficulty() {
		return this.difficulty;
	}

	public String getHint() {
		return this.hint;
	}

	public String getName() {
		return this.name;
	}

	public int getStartColumn(final int pi) {
		return this.playerData.getCell(0, pi);
	}

	public int getStartFloor(final int pi) {
		return this.playerData.getCell(2, pi);
	}

	public int getStartRow(final int pi) {
		return this.playerData.getCell(1, pi);
	}

	public boolean isHorizontalWraparoundEnabled() {
		return this.horizontalWraparoundEnabled;
	}

	public boolean isMoveShootAllowed() {
		return this.moveShootAllowed;
	}

	public boolean isThirdDimensionWraparoundEnabled() {
		return this.thirdDimensionWraparoundEnabled;
	}

	public boolean isVerticalWraparoundEnabled() {
		return this.verticalWraparoundEnabled;
	}

	public void setAuthor(final String newAuthor) {
		this.author = newAuthor;
	}

	public void setDifficulty(final int newDifficulty) {
		this.difficulty = newDifficulty;
	}

	public void setHint(final String newHint) {
		this.hint = newHint;
	}

	public void setMoveShootAllowed(final boolean value) {
		this.moveShootAllowed = value;
	}

	public void setName(final String newName) {
		this.name = newName;
	}

	public void setStartColumn(final int pi, final int value) {
		this.playerData.setCell(value, 0, pi);
	}

	public void setStartFloor(final int pi, final int value) {
		this.playerData.setCell(value, 2, pi);
	}

	public void setStartRow(final int pi, final int value) {
		this.playerData.setCell(value, 1, pi);
	}

	public void writeLevelInfo(final DataIOWriter writer) throws IOException {
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

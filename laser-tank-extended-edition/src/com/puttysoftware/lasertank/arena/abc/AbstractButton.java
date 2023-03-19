/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import java.util.Objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.index.GameType;

public abstract class AbstractButton extends ArenaObject {
	// Fields
	private boolean triggered;
	private int doorX, doorY;
	private final AbstractButtonDoor buttonDoor;
	private final boolean universal;

	// Constructors
	protected AbstractButton(final AbstractButtonDoor bd, final boolean isUniversal) {
		super();
		this.triggered = false;
		this.doorX = -1;
		this.doorY = -1;
		this.buttonDoor = bd;
		this.universal = isUniversal;
		this.addType(GameType.BUTTON);
	}

	public boolean boundButtonDoorEquals(final AbstractButton other) {
		if (this == other) {
			return true;
		}
		if (this.buttonDoor == null) {
			if (other.buttonDoor != null) {
				return false;
			}
		} else if (!this.buttonDoor.getClass().equals(other.buttonDoor.getClass())) {
			return false;
		}
		return true;
	}

	public boolean boundToSameButtonDoor(final AbstractButtonDoor other) {
		if (this.buttonDoor == null) {
			if (other != null) {
				return false;
			}
		} else if (!this.buttonDoor.getClass().equals(other.getClass())) {
			return false;
		}
		return true;
	}

	@Override
	public AbstractButton clone() {
		final var copy = (AbstractButton) super.clone();
		copy.triggered = this.triggered;
		copy.doorX = this.doorX;
		copy.doorY = this.doorY;
		return copy;
	}

	@Override
	public void editorPlaceHook(final int x, final int y, final int z) {
		final var app = LaserTankEE.getApplication();
		final var loc = app.getArenaManager().getArena().findObject(z, this.getButtonDoor());
		if (loc != null) {
			this.setDoorX(loc[0]);
			this.setDoorY(loc[1]);
			this.setTriggered(false);
		}
		if (this instanceof AbstractTriggerButton || this instanceof AbstractPressureButton) {
			app.getArenaManager().getArena().fullScanButtonCleanup(x, y, z, this);
		}
		app.getEditor().redrawEditor();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj) || !(obj instanceof final AbstractButton other)
				|| !Objects.equals(this.buttonDoor, other.buttonDoor) || (this.doorX != other.doorX)) {
			return false;
		}
		if (this.doorY != other.doorY) {
			return false;
		}
		if (this.triggered != other.triggered) {
			return false;
		}
		return true;
	}

	public final AbstractButtonDoor getButtonDoor() {
		return this.buttonDoor;
	}

	@Override
	public int getCustomFormat() {
		return 3;
	}

	@Override
	public int getCustomProperty(final int propID) {
		return switch (propID) {
			case 1 -> this.doorX;
			case 2 -> this.doorY;
			case 3 -> this.triggered ? 1 : 0;
			default -> ArenaObject.DEFAULT_CUSTOM_VALUE;
		};
	}

	public int getDoorX() {
		return this.doorX;
	}

	public int getDoorY() {
		return this.doorY;
	}

	@Override
	public int hashCode() {
		final var prime = 31;
		var result = super.hashCode();
		result = prime * result + (this.buttonDoor == null ? 0 : this.buttonDoor.hashCode());
		result = prime * result + this.doorX;
		result = prime * result + this.doorY;
		return prime * result + (this.triggered ? 1231 : 1237);
	}

	public boolean isTriggered() {
		return this.triggered;
	}

	public final boolean isUniversal() {
		return this.universal;
	}

	@Override
	public abstract boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z);

	@Override
	public abstract void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z);

	@Override
	public void setCustomProperty(final int propID, final int value) {
		switch (propID) {
			case 1:
				this.doorX = value;
				break;
			case 2:
				this.doorY = value;
				break;
			case 3:
				this.triggered = (value == 1);
				break;
			default:
				break;
		}
	}

	public void setDoorX(final int newDoorX) {
		this.doorX = newDoorX;
	}

	public void setDoorY(final int newDoorY) {
		this.doorY = newDoorY;
	}

	public void setTriggered(final boolean isTriggered) {
		this.triggered = isTriggered;
	}
}

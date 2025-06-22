package com.puttysoftware.lasertank.arena;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.helper.LayerHelper;
import com.puttysoftware.lasertank.storage.ObjectStorage;

class CurrentArenaStorageTests {
    private CurrentArenaStorage testObject;
    
    @BeforeEach
    void setUp() throws Exception {
	this.testObject = new CurrentArenaStorage(ArenaData.MIN_COLUMNS, ArenaData.MIN_ROWS, ArenaData.MIN_FLOORS,
		LayerHelper.COUNT);
    }

    @Test
    final void canCopyBase() {
	var copy = new ObjectStorage(this.testObject);
	assertNotNull(copy);
    }

    @Test
    final void canFillBase() {
	var fillWith = "Test Object"; //$NON-NLS-1$
	this.testObject.fill(fillWith);
	assertEquals(fillWith, this.testObject.getCell(0, 0, 0, 0));
    }

    @Test
    final void canGetAndSet() {
	var obj = "Test Object"; //$NON-NLS-1$
	this.testObject.setCell(obj, 0, 0, 0, 0);
	assertEquals(obj, this.testObject.getCell(0, 0, 0, 0));
    }

    @Test
    final void canGetShape() {
	assertEquals(4, this.testObject.getShape().length);
    }

    @Test
    final void canFill() {
	var fillWith = new ArenaObject();
	this.testObject.fill(fillWith);
	assertEquals(fillWith, this.testObject.getCell(0, 0, 0, 0));
    }

    @Test
    final void canGetAndSetArenaData() {
	var obj = new ArenaObject();
	this.testObject.setArenaDataCell(obj, 0, 0, 0, 0);
	assertEquals(obj, this.testObject.getArenaDataCell(0, 0, 0, 0));
    }
}

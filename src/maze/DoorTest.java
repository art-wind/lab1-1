package maze;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class DoorTest {
	private static Room room0 = new Room(0), room1 = new Room(1);
	private static Door door = new Door(room0, room1);

	@Test
	public void testGetColor() {
		assertEquals(Color.LIGHT_GRAY, door.getColor());
	}

	@Test
	public void testIsOpen() {
		assertEquals(false, door.isOpen());
	}

	@Test
	public void testGetOtherSide() {
		assertEquals(room1, door.getOtherSide(room0));
		assertEquals(room0, door.getOtherSide(room1));
		assertEquals(null, door.getOtherSide(new Room(123)));
		assertEquals(null, door.getOtherSide(null));
	}
	
	@Test
	public void testSetColor() {
		Door.setColor(Color.RED);
		assertEquals(Color.RED , door.getColor());
		Door.setColor(Color.LIGHT_GRAY);
	}

}

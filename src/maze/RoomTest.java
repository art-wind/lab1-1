package maze;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

public class RoomTest {
	private static int roomNum = 123;
	private static Room room = new Room(roomNum),
			room0 = new Room(0),
			room1 = new Room(1);
	private static Door door2 = new Door(new Room(1), new Room(2));
	private static Wall wall3 = new Wall();
	
	@Before
	public void setUp() {
		room.sides[0] = room0;
		room.sides[1] = room1;
		room.sides[2] = door2;
		room.sides[3] = wall3;
	}
	
	@Test
	public void testGetColor() {
		assertEquals(Color.WHITE, room.getColor());
	}

	@Test
	public void testGetSide() {
		assertEquals(room0, room.getSide(Direction.North));
		assertEquals(room1, room.getSide(Direction.South));
		assertEquals(door2, room.getSide(Direction.East));
		assertEquals(wall3, room.getSide(Direction.West));
	}

	@Test
	public void testGetNumber() {
		assertEquals(roomNum, room.getNumber());
	}

	@Test
	public void testSetColor() {
		Room.setColor(Color.RED);
		assertEquals(Color.RED , room.getColor());
		Room.setColor(Color.WHITE);
	}

}

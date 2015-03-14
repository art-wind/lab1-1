package maze;

import static org.junit.Assert.*;

import org.junit.Test;

public class MazeTest {
	public static Maze maze = new Maze();

	@Test
	public void testGetRoom() {
		Room room0 = maze.getRoom(0);
		assertEquals(maze.getRoom(1), ((Door)(room0.getSide(Direction.East))).getOtherSide(room0));
	}

	@Test
	public void testGetNumberOfRooms() {
		assertEquals(2, maze.getNumberOfRooms());
	}

}

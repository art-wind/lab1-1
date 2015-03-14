package maze;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

public class WallTest {

	private static Wall w = new Wall();
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetColor() {
		assertEquals(Color.BLACK , w.getColor());
	}
	
	@Test
	public void testSetColor() {
		Wall.setColor(Color.RED);
		assertEquals(Color.RED , w.getColor());
		Wall.setColor(Color.BLACK);
	}

}

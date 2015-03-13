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
    
		assertEquals( Color.BLACK , w.getColor());
	
	}

}

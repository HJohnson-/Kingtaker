package main;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {


    @Test
    public void testGetX() throws Exception {
        Location loc = new Location(0,7);
        assertEquals(new Integer(0), loc.getX());

        loc = new Location(0,0);
        assertEquals(new Integer(0), loc.getX());

        loc = new Location(7,7);
        assertEquals(new Integer(7), loc.getX());

        loc = new Location(3,3);
        assertEquals(new Integer(3), loc.getX());

        loc = new Location(7,0);
        assertEquals(new Integer(7), loc.getX());
    }

    @Test
    public void testGetY() throws Exception {
        Location loc = new Location(0,7);
        assertEquals(new Integer(7), loc.getY());

        loc = new Location(0,0);
        assertEquals(new Integer(0), loc.getY());

        loc = new Location(7,7);
        assertEquals(new Integer(7), loc.getY());

        loc = new Location(3,3);
        assertEquals(new Integer(3), loc.getY());

        loc = new Location(7,0);
        assertEquals(new Integer(0), loc.getY());
    }

    @Test
    public void testIncrX() throws Exception {
        Location loc = new Location(7,7);
        loc.incrX(1);
        assertEquals(new Integer(8), loc.getX());


        loc = new Location(0,0);
        loc.incrX(5);
        assertEquals(new Integer(5), loc.getX());

    }

    @Test
    public void testIncrY() throws Exception {
        Location loc = new Location(7,7);
        loc.incrY(1);
        assertEquals(new Integer(8), loc.getY());


        loc = new Location(0,0);
        loc.incrY(5);
        assertEquals(new Integer(5), loc.getY());
    }

    @Test
    public void testEquals() throws Exception {
        Location loc = new Location(0,0);
        Location loc2 = new Location(0,0);

        assertTrue(loc.equals(loc2) && loc2.equals(loc));

        loc = new Location(7,7);
        assertFalse(loc.equals(loc2) && loc2.equals(loc));

        loc2 = new Location(7,7);
        assertTrue(loc.equals(loc2) && loc2.equals(loc));

        loc = new Location(3,0);
        assertFalse(loc.equals(loc2) && loc2.equals(loc));
    }


}
/*
 * Maze.java
 * Copyright (c) 2008, Drexel University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Drexel University nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY DREXEL UNIVERSITY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DREXEL UNIVERSITY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package maze;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Sunny
 * @version 1.0
 * @since 1.0
 */
public class Maze implements Iterable<Room>
{
	public final Map<Integer, Room> rooms = new HashMap<Integer, Room>();
	public Room current;
	
	/**
	 * Returns the default maze.
	 */
	public Maze()
	{
		createDefaultMaze();
	}
	
	/**
	 * Creates a default maze of two rooms and a door.
	 */
	private void createDefaultMaze() {
		Room r1 = new Room(0);
		Room r2 = new Room(1);
		Door theDoor = new Door(r1, r2);
		rooms.put(r1.number, r1);
		rooms.put(r2.number, r2);
		r1.sides[Direction.North.ordinal()] = new Wall();
		r1.sides[Direction.East.ordinal()] = theDoor;
		r1.sides[Direction.South.ordinal()] = new Wall();
		r1.sides[Direction.West.ordinal()] = new Wall();
		r2.sides[Direction.North.ordinal()] = new Wall();
		r2.sides[Direction.East.ordinal()] = new Wall();
		r2.sides[Direction.South.ordinal()] = new Wall();
		r2.sides[Direction.West.ordinal()] = theDoor;
		current = rooms.get(0);
	}

	/**
	 * Creates a maze from the input arguments¡£<br/>
	 * Returns the default maze if <b>args</b> is empty¡£ 
	 * @param args
	 * color settings (optional) and the .maze file
	 */
	public Maze(String[] args) {
		if (args != null && args.length != 0)
			createMazeByArgs(args);
		else
			createDefaultMaze();
	}

	/**
	 * Create a custom maze.
	 * @param args
	 * it can be like "*.maze" or "wall=?...*.maze"
	 */
	private void createMazeByArgs(String[] args) {
		setColors(args);
		String path = args[args.length-1];
		if (path.matches(".*new.*maze$"))
			createMazeFromFileNew(path);
		else if (path.matches(".*maze$"))
			createMazeFromFileOld(path);
		else
			System.out.println("File not found.");
	}

	/**
	 * Create a custom maze from an old-styled configuration file.
	 * @param path
	 * path of the configuration file
	 */
	private void createMazeFromFileOld(String path) {
		try {
			Scanner scanner = new Scanner(new File(path));
			String line;
			LinkedList<Room> rooms = new LinkedList<>();
			LinkedList<String[]> roomConfs = new LinkedList<>();
			LinkedList<Door> doors = new LinkedList<>();
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				String[] variables = line.split(" ");
				if (variables[0].equals("room")) {
					roomConfs.add(variables);
					Room room = new Room(Integer.parseInt(variables[1]));
					rooms.add(room);
					this.rooms.put(room.number, room);
				}else if (variables[0].equals("door")) {
					Door door = new Door(rooms.get(Integer.parseInt(variables[2])), 
							rooms.get(Integer.parseInt(variables[3])));
					door.setOpen(variables[4].equals("open"));
					doors.add(door);
				}
			}
			int number = rooms.size();
			Direction[] directions = {Direction.North, Direction.South, Direction.East, 
					Direction.West};
			for (int roomId = 0; roomId < number; roomId++) {
				Room room = rooms.get(roomId);
				String[] roomConf = roomConfs.get(roomId);
				for(int confId = 2; confId < 6; confId ++){
					String conf = roomConf[confId];
					MapSite mapSite = null;
					if(conf.equals("wall")){
						mapSite = new Wall();
					}else if (conf.startsWith("d")) {
						mapSite = doors.get(Integer.parseInt(conf.substring(1)));
					}else {
						mapSite = rooms.get(Integer.parseInt(conf));
					}
					room.sides[directions[confId - 2].ordinal()] = mapSite;
				}
			}
			scanner.close();
			this.current = this.rooms.get(0);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		}
	}

	/**
	 * Create a custom maze from a new configuration file.
	 * @param path
	 * path of the configuration file
	 */
	private void createMazeFromFileNew(String path) {
		try {
			Scanner scanner = new Scanner(new File(path));
			String line;
			LinkedList<Room> rooms = new LinkedList<>();
			LinkedList<String[]> roomConfs = new LinkedList<>();
			LinkedList<Door> doors = new LinkedList<>();
			ArrayList<String> roomNames = new ArrayList<String>();
			ArrayList<String> doorNames = new ArrayList<String>();
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				String[] variables = line.split(" ");
				if (variables[0].equals("room")) {
					roomConfs.add(variables);
					roomNames.add(variables[1]);
					Room room = new Room(roomNames.size()-1);
					rooms.add(room);
					this.rooms.put(room.number, room);
				}else if (variables[0].equals("door")) {
					doorNames.add(variables[1]);
					Door door = new Door(rooms.get(roomNames.indexOf(variables[2])), 
							rooms.get(roomNames.indexOf(variables[3])));
					door.setOpen(variables[4].equals("open"));
					doors.add(door);
				}
			}
			int number = rooms.size();
			Direction[] directions = {Direction.North, Direction.South, Direction.East, 
					Direction.West};
			for (int roomId = 0; roomId < number; roomId++) {
				Room room = rooms.get(roomId);
				String[] roomConf = roomConfs.get(roomId);
				for(int confId = 2; confId < 6; confId ++){
					String conf = roomConf[confId];
					MapSite mapSite = null;
					if(conf.equals("wall")){
						mapSite = new Wall();
					}else if (doorNames.contains(conf)) {
						mapSite = doors.get(doorNames.indexOf(conf));
					}else {
						mapSite = rooms.get(roomNames.indexOf(conf));
					}
					room.sides[directions[confId - 2].ordinal()] = mapSite;
				}
			}
			scanner.close();
			this.current = this.rooms.get(0);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		}
		
	}

	/**
	 * Set colors for Wall, Door and Room, according to <b>args</b>
	 * @param args
	 * same as the <b>args</b> of main()
	 */
	private void setColors(String[] args) {
		try {
			for (int i = 0; i < args.length-1; ++i) {
				int eq = args[i].indexOf('=');
				if (eq == -1)
					//no '=' in the args, indicating a grammar error 
					throw new Exception();
				String obj = args[i].substring(0, eq).toLowerCase(),
						color = args[i].substring(eq + 1).toLowerCase();
				switch (obj) {
					case "wall":
						Wall.setColor((Color)(Color.class.getField(color).get(null)));
						break;
					case "door":
						Door.setColor((Color)(Color.class.getField(color).get(null)));
						break;
					case "room":
						Room.setColor((Color)(Color.class.getField(color).get(null)));
						break;
					default:
						//the args is not in [wall, door, room], indicating a grammar error.
						throw new Exception();
				}
			}
		} catch (Exception e1) {
			System.out.println("Grammar error in \""+ args +"\". Now using the default colors.");
			Wall.setColor(Color.BLACK);
			Door.setColor(Color.LIGHT_GRAY);
			Room.setColor(Color.WHITE);
		}
	}

	public final Room getRoom(int number)
	{
		return rooms.get(number);
	}
	
	@Override
	public Iterator<Room> iterator()
	{
		return rooms.values().iterator();
	}

	public int getNumberOfRooms()
	{
		return rooms.size();
	}

	public final Room getCurrentRoom()
	{
		return current;
	}

	public final void setCurrentRoom(final Room room)
	{
		current = room;
	}
}

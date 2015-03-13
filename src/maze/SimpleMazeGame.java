/*
 * SimpleMazeGame.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import maze.ui.MazeViewer;

/**
 * 
 * @author Sunny
 * @version 1.0
 * @since 1.0
 */
public class SimpleMazeGame
{
	public static void main(String[] args)
	{
		Maze maze = null;
		if(args.length == 0){
			maze = new Maze();
			Room r1 = new Room(0);
			Room r2 = new Room(1);
			Door theDoor = new Door(r1, r2);
			maze.rooms.put(r1.number, r1);
			maze.rooms.put(r2.number, r2);
			r1.sides[Direction.North.ordinal()] = new Wall();
			r1.sides[Direction.East.ordinal()] = theDoor;
			r1.sides[Direction.South.ordinal()] = new Wall();
			r1.sides[Direction.West.ordinal()] = new Wall();
			r2.sides[Direction.North.ordinal()] = new Wall();
			r2.sides[Direction.East.ordinal()] = new Wall();
			r2.sides[Direction.South.ordinal()] = new Wall();
			r2.sides[Direction.West.ordinal()] = theDoor;
			maze.current = maze.rooms.get(0);
		}else {
			try {
				maze = new Maze();				
				Scanner scanner = new Scanner(new File(args[0]));
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
						maze.rooms.put(room.number, room);
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
				maze.current = maze.rooms.get(0);
			} catch (FileNotFoundException e) {
				System.out.println("File Not Found");
			}
		}
		MazeViewer viewer = new MazeViewer(maze);
		viewer.run();
	}
}

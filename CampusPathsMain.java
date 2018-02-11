package hw9;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import hw8.CampusBuildingsParser;
import hw8.CampusPathsParser;
import hw8.Location;

/**
 *  This class represents a map of the UW campus and the paths to which one can travel
 *  from one building to another. This class prints out the shortest path between
 *  two buildings when given a selected start building and a selected end building.
 *  Once the shortest path between two buildings is printed out, the class can reset
 *  the map to its original state.
 */

public class CampusPathsMain {
	public static Map<String, Location> locations;
	public static Map<String, String> shortToLongNames;
	private static JFrame frame;
	private static Map<Location, Map<Location, Double>> paths;
	public static final String campusPathsFile = "src/hw8/data/campus_paths.dat";
	public static final String campusBuildingsFile = "src/hw8/data/campus_buildings.dat";
	
	
	public static void main(String[] args) {
		shortToLongNames = new HashMap<String, String>();
		locations = new HashMap<String, Location>();
		paths = new HashMap<Location, Map<Location, Double>>();
		// Parse the campus buildings and campus paths files to load the campus data
		try {
			CampusBuildingsParser.parseData(campusBuildingsFile, shortToLongNames, locations);
			CampusPathsParser.parseData(campusPathsFile, paths);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Establish the window frame
		frame = new JFrame();
		CampusMapPanel mapPanel = new CampusMapPanel();
		frame.setPreferredSize(new Dimension(1024, 768));
		frame.add(mapPanel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);	
	}
}
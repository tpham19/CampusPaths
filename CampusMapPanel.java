package hw9;

import hw8.CampusPathFinderModel;
import hw8.Location;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The CampusMapPanel class displays a map of the UW campus, tool bars and text on a
 * panel. The class can display the shortest path between two selected buildings.
 *  
 */
public class CampusMapPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String campusMapImage = "src/hw8/data/campus_map.jpg";
	private BufferedImage uwMapImage;
	private Location startLocation;
	private Location endLocation;
	private String startBuilding;
	private String endBuilding;
	private boolean paint;
	private JButton path;
	private JButton reset;


	public CampusMapPanel() {
		try {
			uwMapImage = ImageIO.read(new File(campusMapImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel start = new JLabel("Start");
		add(start);
		// List of start buildings
		JComboBox<String> from = new JComboBox<String>();
		for (String shortName : CampusPathsMain.shortToLongNames.keySet()) {
			from.addItem(shortName);
		}
		from.addActionListener(new ActionListener() {
			/**
			 * Marks the start building and the start location
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				startBuilding = from.getSelectedItem().toString();
				startLocation = CampusPathsMain.locations.get(startBuilding);
				paint = false;
				repaint();
			}
		});
		add(from);
		JLabel end = new JLabel("End");
		add(end);
		// List of end buildings
		JComboBox<String> to = new JComboBox<String>();
		for (String shortName : CampusPathsMain.shortToLongNames.keySet()) {
			to.addItem(shortName);
		}
		to.addActionListener(new ActionListener() {
			/**
			 * Marks the end building and the end location
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				endBuilding = to.getSelectedItem().toString();
				endLocation = CampusPathsMain.locations.get(endBuilding);
				paint = false;
				repaint();
			}
		});
		add(to);
		path = new JButton("Get Path");
		add(path);
		path.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Sets paint to be true so that the path can be drawn
				if (startLocation != null && endLocation != null) {
					paint = true;
					// Paints the path
					repaint();
				}
			}
		});
		reset = new JButton("Reset");
		add(reset);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Sets paint to be false so that the path can be erased
				// and the path finder can be reset
				paint = false;
				startLocation = null;
				endLocation = null;
				repaint();
			}
		});
		paint = false;
	}

	/**
	 * Prints out the shortest path between selected buildings when the 
	 * "Get Path" button is clicked. Marks the starting and ending buildings
	 * with a circle
	 */
	@Override
	public void paintComponent(Graphics brush) {
		// Uses the getWidth() and getHeight() methods to allow the map
		// to grow or shrink to fit in the window as the window is resized
		brush.drawImage(uwMapImage, 0, 0, getWidth(), getHeight(), null);
		// Set start building color to be red
		brush.setColor(Color.RED);
		if (startLocation != null) {
			brush.fillOval(getX(startLocation) - 10, getY(startLocation) - 10, 15, 15);
		}
		// Set end building color to be green
		brush.setColor(Color.GREEN);
		if (endLocation != null) {
			brush.fillOval(getX(endLocation) - 10, getY(endLocation) - 10, 15, 15);
		}
		// Draw the path from the start building to the end building if paint is true
		if (paint) {
			if (startLocation != null && endLocation != null) {
				try {
					List<Location> path = findShortestPath();
					for (int i = 1; i < path.size(); i++) {
						brush.setColor(Color.BLUE);
						brush.drawLine(getX(path.get(i - 1)), getY(path.get(i - 1)),
									   getX(path.get(i)), getY(path.get(i)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get the shortest path from the selected starting location to the
	 * selected ending location
	 * 
	 * @return A list of location objects to which makes up the path
	 */
	private List<Location> findShortestPath() throws Exception {
		CampusPathFinderModel uwMap = new CampusPathFinderModel();
		Map<Location, Double> pathMap = uwMap.findShortestPath(startBuilding, endBuilding);
		// Stores the location objects from the keys of the pathMap
		List<Location> locations = new ArrayList<Location>(pathMap.keySet());
		return locations;
	}

	/**
	 * Returns a y coordinate based on the window size
	 * 
	 * @param point A building location
	 * @return The y coordinate of a given location based on the window size
	 */
	private int getY(Location point) {
		return (int) (point.getY() * getHeight() / uwMapImage.getHeight());
	}

	/**
	 * Returns a x coordinate based on the window size
	 * 
	 * @param point A building location
	 * @return The x coordinate of a given location based on the window size
	 */
	private int getX(Location point) {
		return (int) (point.getX() * getWidth() / uwMapImage.getWidth());
	}
}
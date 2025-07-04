import java.util.Arrays;

import stdlib.StdOut;

public class Location implements Comparable<Location> {
    private String name; // location name
    private double lat;  // latitude
    private double lon;  // longitude

    // Constructs a new location given its name, latitude, and longitude.
    public Location(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    // Returns the great-circle distance between this location and other.
    public double distanceTo(Location other) {
        double x = Math.sin(Math.toRadians(this.lat))*Math.sin(Math.toRadians(other.lat));
        double y = Math.cos(Math.toRadians(this.lat))*Math.cos(Math.toRadians(other.lat))*Math.cos(Math.toRadians(this.lon-other.lon));
        return 6359.83*Math.acos(x+y);
    }

    // Returns true if this location is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Location t = (Location) other;
        return this.lat == t.lat && this.lon == t.lon;
    }

    // Returns a string representation of this location.
    public String toString() {
        return name + " (" + lat + ", " + lon + ")";
    }

    // Returns a comparison of this location with other based on their respective distances to
    // the origin, Parthenon (Greece) @ 37.971525, 23.726726.
    public int compareTo(Location that) {
        double thisloc = this.distanceTo(new Location("Parthenon", 37.971525, 23.726726));
        double thatloc = that.distanceTo(new Location("Parthenon", 37.971525, 23.726726));
        return Double.compare(thisloc, thatloc);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        int rank = Integer.parseInt(args[0]);
        String name = args[1];
        double lat = Double.parseDouble(args[2]);
        double lon = Double.parseDouble(args[3]);
        Location[] wonders = new Location[7];
        wonders[0] = new Location("The Great Wall of China (China)", 40.6769, 117.2319);
        wonders[1] = new Location("Petra (Jordan)", 30.3286, 35.4419);
        wonders[2] = new Location("The Colosseum (Italy)", 41.8902, 12.4923);
        wonders[3] = new Location("Chichen Itza (Mexico)", 20.6829, -88.5686);
        wonders[4] = new Location("Machu Picchu (Peru)", -13.1633, -72.5456);
        wonders[5] = new Location("Taj Mahal (India)", 27.1750, 78.0419);
        wonders[6] = new Location("Christ the Redeemer (Brazil)", 22.9519, -43.2106);
        Arrays.sort(wonders);
        StdOut.println("Seven wonders, in the order of their distance to Parthenon (Greece):");
        for (Location wonder : wonders) {
            StdOut.println("  " + wonder);
        }
        Location loc = new Location(name, lat, lon);
        StdOut.print("wonders[" + rank + "] == " + loc + "? ");
        StdOut.println(wonders[rank].equals(loc));
    }
}

package org.me.gcu.rai_roman_s2335895;

public class GeoPoint {
    private final String label;
    private final double lat;
    private final double lon;

    public GeoPoint(String label, double lat, double lon) {
        this.label = label;
        this.lat = lat;
        this.lon = lon;
    }

    public String getLabel() {return label;}
    public double getLat() {return lat;}
    public double getLon() {return lon;}
}
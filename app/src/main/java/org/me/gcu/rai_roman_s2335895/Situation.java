package org.me.gcu.rai_roman_s2335895;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Situation {
    private String type;
    private String description;
    private String road;
    private String startTime;
    private String endTime;
    private String delayType;
    private String direction;
    private double fromLat = Double.NaN;
    private double fromLon = Double.NaN;
    private double toLat = Double.NaN;
    private double toLon = Double.NaN;


    public Situation() {
    }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getRoad() {return road;}
    public void setRoad(String road) {this.road = road;}

    public String getStartTime() {return startTime;}
    public void setStartTime(String startTime) {this.startTime = startTime;}

    public String getEndTime() {return endTime;}
    public void setEndTime(String endTime) {this.endTime = endTime;}

    public String getDelayType() {return delayType;}
    public void setDelayType(String delayType) {this.delayType = delayType;}

    public String getDirection() {return direction;}
    public void setDirection(String direction) {this.direction = direction;}

    public double getFromLat() {return fromLat;}
    public void setFromLat(double fromLat) {this.fromLat = fromLat;}

    public double getFromLon() {return fromLon;}
    public void setFromLon(double fromLon) {this.fromLon = fromLon;}

    public double getToLat() {return toLat;}
    public void setToLat(double toLat) {this.toLat = toLat;}

    public double getToLon() {return toLon;}
    public void setToLon(double toLon) {this.toLon = toLon;}


    public long getDurationDays() {
        try {
            OffsetDateTime start = OffsetDateTime.parse(startTime);
            OffsetDateTime end = OffsetDateTime.parse(endTime);
            return ChronoUnit.DAYS.between(start, end);
        } catch (Exception e) {
            return -1;
        }
    }

    public double distanceKmFrom(double lat, double lon) {
        double pLat = !Double.isNaN(fromLat) ? fromLat : toLat;
        double pLon = !Double.isNaN(fromLon) ? fromLon : toLon;
        if (Double.isNaN(pLat) || Double.isNaN(pLon)) return Double.NaN;
        double dLat = Math.toRadians(pLat - lat);
        double dLon = Math.toRadians(pLon - lon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(pLat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 6371.0 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }


    // "2023-07-22T00:00:00+01:00" -> "22 Jul 2023, 00:00"
    public static String prettyDate(String iso) {
        if (iso == null) return "Not given";
        try {
            return OffsetDateTime.parse(iso)
                    .format(DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.UK));
        } catch (Exception e) {
            return iso;
        }
    }

    // "longDelays" -> "Long delays", "MaintenanceWorks" -> "Maintenance works"
    public static String humanise(String camel) {
        if (camel == null || camel.isEmpty()) return "Not given";
        String spaced = camel.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }

    @Override
    public String toString() {
        return "Situation{type='" + type + "', road='" + road + "', startTime='" + startTime + "'," +
                " endTime='" + endTime + "', delayType='" + delayType + "'}";
    }
}
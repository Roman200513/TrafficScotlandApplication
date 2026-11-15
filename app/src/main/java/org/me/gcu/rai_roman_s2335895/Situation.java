package org.me.gcu.rai_roman_s2335895;

public class Situation {
    private String type;
    private String description;
    private String road;
    private String startTime;
    private String endTime;
    private String delayType;

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

    @Override
    public String toString() {
        return "Situation{type'" + type + "', road='" + road + "', startTime='" + startTime + "'," +
                " endTime='" + endTime + "', delayType='" + delayType + "'}";

    }
}
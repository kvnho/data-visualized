package com.kevinho.BikeShareDataVisual.entity;

public class Trip {

    private String duration;
    private String startTime;
    private String endTime;
    private String startStop;
    private String startStopLong;
    private String startStopLat;
    private String endStop;
    private String endStopLong;
    private String endStopLat;
    private String plan;
    private String routeCategory;
    private String passholderType;

    public Trip(){

    }


    public Trip(String duration, String startTime, String endTime, String startStop, String startStopLat, String startStopLong, String endStop, String endStopLat, String endStopLong, String plan, String routeCategory, String passholderType) {
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startStop = startStop;
        this.startStopLat = startStopLat;
        this.startStopLong = startStopLong;
        this.endStop = endStop;
        this.endStopLat = endStopLat;
        this.plan = plan;
        this.endStopLong = endStopLong;
        this.routeCategory = routeCategory;
        this.passholderType = passholderType;
    }

    public String toString(){
        return "Duration: " + duration + ", " + "Start Time: " + startTime + ", " + "End Time: " + endTime + ", " + "Start Stop: " + startStop + ", " + "End Stop: " + endStop + ", " + "Plan: " + plan + ", " + "Route Category: " + routeCategory + ", " + "Passholder Type: " + passholderType;
    }

    public double getDistanceTraveled(){
        double startLat = Double.parseDouble(this.startStopLat);
        double startLong = Double.parseDouble(this.startStopLong);
        double endLat = Double.parseDouble(this.endStopLat);
        double endLong = Double.parseDouble(this.endStopLong);

        double theta = startLong - endLong;
        double distance = Math.sin(degreeToRadians(startLat)) * Math.sin(degreeToRadians(endLat)) + Math.cos(degreeToRadians(degreeToRadians(startLat)) * Math.cos(degreeToRadians(endLat)) * Math.cos(degreeToRadians(theta)));
        distance = distance * 60 * 1.1515;
        return distance;
    }

    private double degreeToRadians(double degree){
        return (degree * Math.PI / 180.0);
    }

    public String getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartStop() {
        return startStop;
    }

    public String getStartStopLat() {
        return startStopLat;
    }

    public String getStartStopLong() {
        return startStopLong;
    }

    public String getEndStop() {
        return endStop;
    }

    public String getEndStopLat() {
        return endStopLat;
    }

    public String getEndStopLong() {
        return endStopLong;
    }


    public String getPlan() {
        return plan;
    }

    public String getRouteCategory() {
        return routeCategory;
    }

    public String getPassholderType() {
        return passholderType;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartStop(String startStop) {
        this.startStop = startStop;
    }

    public void setStartStopLat(String startStopLat) {
        this.startStopLat = startStopLat;
    }

    public void setStartStopLong(String startStopLong) {
        this.startStopLong = startStopLong;
    }

    public void setEndStop(String endStop) {
        this.endStop = endStop;
    }

    public void setEndStopLat(String endStopLat) {
        this.endStopLat = endStopLat;
    }

    public void setEndStopLong(String endStopLong) {
        this.endStopLong = endStopLong;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setRouteCategory(String routeCategory) {
        this.routeCategory = routeCategory;
    }

    public void setPassholderType(String passholderType) {
        this.passholderType = passholderType;
    }
}


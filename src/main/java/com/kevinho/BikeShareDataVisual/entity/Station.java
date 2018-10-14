package com.kevinho.BikeShareDataVisual.entity;

public class Station {

    private int id;
    //private String name;
    private String address;
    private int visits;
    private String bikes;
    private String docks;
    private String status;

    public Station(){

    }

    public String toString(){
        return "ID: " + id + " || VISITS: " + visits;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    */

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public String getBikes() {
        return bikes;
    }

    public void setBikes(String bikes) {
        this.bikes = bikes;
    }

    public String getDocks() {
        return docks;
    }

    public void setDocks(String docks) {
        this.docks = docks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

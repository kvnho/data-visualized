package com.kevinho.BikeShareDataVisual.service;


import com.kevinho.BikeShareDataVisual.entity.Trip;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataService {
    private static final String CSV_FILE_PATH = "src/main/resources/static/metro.csv";
    private static HashMap<Integer, Trip> tripMap = DataService.readAllDataAtOnce();

    public static HashMap readAllDataAtOnce() {
        Map<Integer, Trip> map = new HashMap<Integer, Trip>();

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(CSV_FILE_PATH);
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.skip(1);

            List<String[]> allData = csvReader.readAll();

            for (String[] row : allData) {
                List<String> listObject = new ArrayList<>();
                Trip trip = new Trip();
                for (String cell : row) {
                    //if(cell == "") break;
                    listObject.add(cell);
                    // add into an arraylist
                }
                trip.setDuration(listObject.get(1));
                trip.setStartTime(listObject.get(2));
                trip.setEndTime(listObject.get(3));
                trip.setStartStop(listObject.get(4));
                trip.setStartStopLat(listObject.get(5));
                trip.setStartStopLong(listObject.get(6));
                trip.setEndStop(listObject.get(7));
                trip.setEndStopLat(listObject.get(8));
                trip.setEndStopLong(listObject.get(9));
                trip.setPlan(listObject.get(11));
                trip.setRouteCategory(listObject.get(12));
                trip.setPassholderType(listObject.get(13));
                // convert the array list to object
                map.put(Integer.parseInt(listObject.get(0)), trip);
                // add object to hashmap
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (HashMap) map;
    }

    public static HashMap<Integer, Integer> popularStartStations(){
        Map<Integer, Integer> popularStartStationsMap = new HashMap<Integer, Integer>();
        for(Trip trip : tripMap.values()){
            try {
                int start = Integer.parseInt(trip.getStartStop());
                if(popularStartStationsMap.containsKey(start)){
                    popularStartStationsMap.put(start, popularStartStationsMap.get(start)+1);
                }
                else{
                    popularStartStationsMap.put(start, 1);
                }
            }
            catch (Exception e){
                continue;
            }
        }

        return (HashMap) popularStartStationsMap;

    }

    public static HashMap<Integer, Integer> popularEndStations(){
        Map<Integer, Integer> popularEndStationsMap = new HashMap<Integer, Integer>();
        for(Trip trip : tripMap.values()){
            try{
                int end = Integer.parseInt(trip.getEndStop());

                if(popularEndStationsMap.containsKey(end)){
                    popularEndStationsMap.put(end, popularEndStationsMap.get(end)+1);
                }
                else{
                    popularEndStationsMap.put(end, 1);
                }
            }
            catch (Exception e){
                continue;
            }

        }

        return (HashMap) popularEndStationsMap;
    }

    public static ArrayList<Map.Entry<Integer, Integer>> ridersPerMonth(){
        Map<Integer, Integer> ridersPerMonthMap = new HashMap<Integer, Integer>();
        for(Trip trip : tripMap.values()){
            int month = Integer.parseInt(trip.getStartTime().substring(trip.getStartTime().indexOf('-')+1, trip.getStartTime().indexOf('-', trip.getStartTime().indexOf('-'))+3));
            if(ridersPerMonthMap.containsKey(month)){
                ridersPerMonthMap.put(month, ridersPerMonthMap.get(month)+1);
            }
            else{
                ridersPerMonthMap.put(month, 1);
            }
        }
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(ridersPerMonthMap.entrySet());
        list.sort(Map.Entry.comparingByKey());

        return (ArrayList) list;
    }

    public static int[] whichPasses(){
        int passes[] = new int[3];
        for(Trip trip : tripMap.values()){
            if(trip.getPassholderType().equals("Monthly Pass")){
                passes[0]++;
            }
            else if(trip.getPassholderType().equals("Flex Pass")){
                passes[1]++;
            }
            else if(trip.getPassholderType().equals("Walk-up")){
                passes[2]++;
            }
        }
        return passes;
    }

    public static int[] routeCategories(){
        int[] routes = new int[2];
        for(Trip trip : tripMap.values()){
            if(trip.getRouteCategory().equals("Round Trip")){
                routes[0]++;
            }
            else if(trip.getRouteCategory().equals("One Way")){
                routes[1]++;
            }
        }
        return routes;
    }

    public static int averageRideTime(){
        int totalTime = 0;
        for(Trip trip : tripMap.values()){
            totalTime += Integer.parseInt(trip.getDuration());
        }
        return totalTime / tripMap.size();
    }

    public static HashMap<String, Integer> passAndTripCombos(){
        HashMap<String, Integer> comboMap = new HashMap<>();

        for(Trip trip : tripMap.values()){
            String key = trip.getPassholderType() + trip.getRouteCategory();
            if(comboMap.containsKey(key)){
                comboMap.put(key, comboMap.get(key)+1);
            }
            else{
                comboMap.put(key, 1);
            }
        }

        return comboMap;
    }

    public static HashMap<String, Integer> getPassholderType(){
        HashMap<String, Integer> passholderMap = new HashMap<>();

        for(Trip trip : tripMap.values()){
            String key = trip.getPassholderType();
            if(passholderMap.containsKey(key)){
                passholderMap.put(key, passholderMap.get(key)+1);
            }
            else{
                passholderMap.put(key, 1);
            }
        }
        return passholderMap;
    }
}

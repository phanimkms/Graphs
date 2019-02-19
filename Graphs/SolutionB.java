package com.company;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

class Road {
    public String city1;
    public String city2;
    public int safetyScore;

    public Road(String city1, String city2, int safetyScore) {
        this.city1 = city1;
        this.city2 = city2;
        this.safetyScore = safetyScore;
    }
}

class RoadMap {
    Map<String, Set<Road>> roadMap = new HashMap<String, Set<Road>>();

    //This function helps to get all the cities in the graph
    public Set<String> getAllCities() {
        return this.roadMap.keySet();
    }

    //This function will read the input
    public void readLine(String line) {
        String[] csv = line.split(",");
        String city1 = csv[0];
        String city2 = csv[1];
        int safety = Integer.parseInt(csv[2]);
        addRoad(city1, city2, safety);
    }

    private void addCity(String city) {
        this.roadMap.put(city, new HashSet<Road>());
    }

    //This function will add both outgoing and incoming roads between two cities
    private void addRoad(String city1, String city2, int safetyScore) {
        Road road1 = new Road(city1, city2, safetyScore);
        Road road2 = new Road(city2, city1, safetyScore);
        if (!this.roadMap.containsKey(city1)) {
            addCity(city1);
        }
        if (!this.roadMap.containsKey(city2)) {
            addCity(city2);
        }

        this.roadMap.get(city1).add(road1);
        this.roadMap.get(city2).add(road2);
    }

    //This function will return all the outgoing roads from a city
    public Set<Road> getAllOutgoingRoads(String node) {
        return this.roadMap.get(node);
    }

}

public class SolutionB {

    static RoadMap roadMap = new RoadMap();
    static int maxValue = 100000;

    private static void relax(Road road, Map<String, Integer> safety, Map<String, String> safetyPath) {
        int possible = safety.get(road.city1) + road.safetyScore;
        if (safety.get(road.city2) > possible) {
            safety.put(road.city2, possible);
            safetyPath.put(road.city2, road.city1);
        }
    }

    static class SafetyComparator implements Comparator<String> {
        private final Map<String, Integer> safety;

        public SafetyComparator(Map<String, Integer> safety) {
            this.safety = safety;
        }

        public int compare(String c1, String c2) {
            if (this.safety.get(c1) < this.safety.get(c2)) {
                return -1;
            } else if (this.safety.get(c1) > this.safety.get(c2)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void readReport(Scanner scanner) {
        while (true) {
            String mapLine = scanner.nextLine();
            if (mapLine.equals("")) {
                break;
            }
            roadMap.readLine(mapLine);
        }
    }
    //Prints safest route between source and destination if it exists or prints "Not Reachable" in case safest route doesn't exist
    //Dijkstra's algorithm is used to define the method
    public static void findSafestRouteToCity(String source, String destination) {
        Map<String, Integer> safety = new HashMap<String, Integer>();
        Map<String, String> safetyPath = new HashMap<String, String>();
        String path=source;
        for (String node : roadMap.getAllCities()) {
            safety.put(node, maxValue);
        }

        safety.put(source, 0);
        String nextCity = source;
        for (Road road : roadMap.getAllOutgoingRoads(nextCity)) {
            relax(road, safety, safetyPath);
        }
        PriorityQueue<String> queue = new PriorityQueue<String>(11, new SafetyComparator(safety));
        for (String n : roadMap.getAllCities()) {
            queue.add(n);
        }
        while (queue.isEmpty() == false) {
            String dequeued = queue.poll();
            nextCity = queue.peek();
            path=path+" -> "+nextCity;
            if (nextCity != null) {
                if ((nextCity.equals(destination)) && ((safetyPath.containsKey(source)) || (safetyPath.containsKey(destination)))) {
                    System.out.println(path);
                }
                for (Road road : roadMap.getAllOutgoingRoads(nextCity)) {
                    relax(road, safety, safetyPath);
                }
            }
        }
        if((!safetyPath.containsKey(source))&&(!safetyPath.containsKey(destination))){
            System.out.println("Not Reachable ");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the graph");
        readReport(scanner);

        System.out.println("Enter the source ");
        String source = scanner.nextLine();
        System.out.println("Enter the destination ");
        String destination = scanner.nextLine();
        System.out.println("The route from "+source+" to "+destination+" is");

        findSafestRouteToCity(source, destination);
    }
}


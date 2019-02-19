package com.company;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

class Road {
    public String city1;
    public String city2;

    public Road(String city1, String city2) {
        this.city1 = city1;
        this.city2 = city2;
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
        addRoad(city1, city2);
    }

    private void addCity(String city) {
        this.roadMap.put(city, new HashSet<Road>());
    }

    //This function will add both outgoing and incoming roads between two cities
    private void addRoad(String city1, String city2) {
        Road road1 = new Road(city1, city2);
        Road road2 = new Road(city2, city1);
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

public class SolutionA {
    static RoadMap roadMap = new RoadMap();

    public static void readMap(Scanner scanner) {
        while (true) {
            String mapLine = scanner.nextLine();
            if (mapLine.equals("")) {
                break;
            }
            roadMap.readLine(mapLine);
        }
        System.out.println("Read map");
    }
    //Prints a path if exists between source and destination or prints "Not Reachable" in case path doesn't exist
    //BFS traversal is used to traverse through the Graph
    public static void findAnyRouteToCity(String source,String destination ) {
        Queue<String> queue = new LinkedList<String>();
        Map<String, Integer> level = new HashMap<String, Integer>();
        queue.add(source);
        level.put(source,0);
        String path=source;
        while (queue.isEmpty() == false) {
            String nextCity = queue.remove();
            Set<Road> neighbouringCities = roadMap.getAllOutgoingRoads(nextCity);
            for (Road neighbour : neighbouringCities) {
                if (level.containsKey(neighbour.city2) == false) {
                    queue.add(neighbour.city2);
                    path=path+" -> "+queue.peek();
                    if (queue.peek().equals(destination)) {
                        System.out.println(path);
                    }
                    level.put(neighbour.city2, level.get(nextCity) + 1);
                }
            }
        }
        if (!level.containsKey(destination)) {
            System.out.println("Not Reachable");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Map");
        readMap(scanner);

        System.out.println("Enter the source ");
        String source = scanner.nextLine();
        System.out.println("Enter the destination ");
        String destination = scanner.nextLine();
        System.out.println("The route from "+source+" to "+destination+" is");

        findAnyRouteToCity(source, destination);
    }
}


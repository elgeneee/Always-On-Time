package sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tour {
    double tourCost;
    ArrayList<LinkedList<Location>> route = new ArrayList<>(); // e.g. {0->1->3->0, 0->2->0}
    double[][] adjMatrix;
    List<Location> c;

    public Tour(Double d){
        tourCost = d;
    }

    public Tour(double[][] adjMatrix, List<Location> c) {
        tourCost = 0;
        this.adjMatrix = adjMatrix;
        this.c = c;
    }

    public void add(LinkedList<Location> r){
        route.add(r);
    }

    public ArrayList<LinkedList<Location>> getRoute(){
        return route;
    }

    public double computeTourCost(){
        tourCost = 0;
        for (int i = 0; i < route.size(); i++) {
            for (int j = 0; j < route.get(i).size()-1; j++) {
                int firstPoint= route.get(i).get(j).id;
                int secondPoint= route.get(i).get(j+1).id;
                tourCost+=adjMatrix[firstPoint][secondPoint];
            }
        }
        return tourCost;
    }

    public double computRouteCost(LinkedList<Location> l){
        double routeCost = 0;
        int i;
        for (i = 0; i < l.size()-1; i++) {
            int firstPoint= l.get(i).id;
            int secondPoint= l.get(i+1).id;
            routeCost+=adjMatrix[firstPoint][secondPoint];
        }
        return routeCost;
    }


    public void addStop(Location n){
        route.get(route.size()-1).add(n);
        tourCost = computeTourCost();
    }

    public Location getLastStop(){
        int routeSize = route.size()-1;
        int linkedListRouteSize = route.get(routeSize).size()-1;
        return route.get(routeSize).get(linkedListRouteSize);
    }

    public void addNewRoute(){
        LinkedList<Location> tempList = new LinkedList<>();
        tempList.add(c.get(0));
        route.add(tempList);
        tourCost = computeTourCost();
    }

    public int computeCapacity(LinkedList<Location> l){
        int capacity = 0;
        for (int i = 1; i < l.size()-1; i++) {
            capacity += l.get(i).demandSize;
        }
        return capacity;
    }

    public double getTourCost() {
        return tourCost;
    }

    public void setTourCost(double tourCost) {
        this.tourCost = tourCost;
    }

    public int getRouteSize(){
        return route.size();
    }

    public void addDepot(){
        route.get(getRouteSize()-1).add(c.get(0));
        tourCost = computeTourCost();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MCTS Simulation Tour\nTour Cost: " + tourCost);
        for (int i = 0; i < route.size(); i++) {
            sb.append("\nVehicle " + (i+1) + "\n");
            for (int j = 0; j < route.get(i).size()-1; j++) {
                sb.append(route.get(i).get(j).id + " -> ");
            }
            sb.append("0\n");
            sb.append("Capacity: " + computeCapacity(route.get(i)) +"\nCost: " + computRouteCost(route.get(i)));
        }
        return sb.toString();
    }
}

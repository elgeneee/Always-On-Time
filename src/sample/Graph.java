package sample;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * The {@code Graph} class represents a data structure that is connected by Customers and Depot. It extends class {@code Thread} that aims to perform parallelism.
 * <p>
 * When a Graph is first created, it calculates the distance between each adjacent {@code Location} and store in the adjacency matrix
 *
 * @author  Elgene Ee, Chong Wen, Chiao Yun, Boon Guan
 * @since   1.0
 */
public class Graph extends Thread{
    double adjMatrix[][];
    int numOfVehicles;
    Depot d;
    ArrayList<Location> c;
    double dist;
    Queue<Integer> q;
    LinkedList<Location> linkedList = new LinkedList<>();
    ArrayList<Vehicle> vehicleList = new ArrayList<>();
    StringBuilder sb;
    double routeCost;
    double tourCost;

    /**
     * param for DFS
     */
    ArrayList<Integer> bestPath;
    double bestCost;
    Instant startTime;

    /**
     * param for MCTS
     */
    int N;
    int ALPHA;
    double z;
    double[][][] policy;
    double[][] globalPolicy;
    ArrayList<Location> location;
    Tour best_tour = new Tour(Double.POSITIVE_INFINITY);
    int timeLimit = 60;

    /**
     * param for Threading
     */
    Component com = Component.getInstance();

    /**
     * Constructs a graph by connecting all nodes between each other.
     * <p>
     * The distance between each node is stored into a 2D array, {@code adjMatrix}
     * <p>
     * Note that the parameters for MCTS algorithm is initialized as well
     *
     * @param   c  list of locations
     */
    public Graph(ArrayList<Location> c){
        this.c = c;
        d=(Depot)c.get(0);
        N = c.size();
        policy = new double[3][N][N];
        globalPolicy = new double[N][N];
        numOfVehicles = 0;
        q = new LinkedList<>();
        adjMatrix = new double[c.size()][c.size()]; //store distance between every 2 node (customer and depot),customer and customer
        //forming the graph
        for (int i = 0; i < c.size(); i++) { //drawing the edge
            for (int j = 0; j < c.size(); j++) {
                if(i==j) continue;
                if(adjMatrix[i][j]!=0)
                    continue;
                dist = Math.sqrt(Math.pow(c.get(i).xCoordinate-c.get(j).xCoordinate,2) + Math.pow(c.get(i).yCoordinate-c.get(j).yCoordinate,2));

                adjMatrix[i][j] = adjMatrix[j][i] = dist;
            }
        }
        location = (ArrayList<Location>) c.clone();
    }

    /**
     * Performs {@code MCTS} algorithm when a thread is started
     */
    @Override
    public void run() {
        com.clearMCTSTour();
        com.setMctsTour(mctsSearch(3,100));
    }

    /**
     * Tests if all locations are visited
     *
     * @return  {@code true} if and only if all locations are visited, {@code false} otherwise.
     */
    public boolean completeVisited(){
        for (int i = 1; i < c.size(); i++) {
            if(!((Customer)c.get(i)).wasVisited) return false;
        }
        return true;
    }

    /**
     * Returns the route cost taken based on the linked list provided
     *
     * @param   linkedList   the location LinkedList.
     * @return  the route cost for a given route
     */
    public double computeRouteCost(LinkedList<Location> linkedList){
        double routeCost=0;

        for (int i = 0; i < linkedList.size()-1; i++) {
            int x= linkedList.get(i).id;
            int y= linkedList.get(i+1).id;
            routeCost+=adjMatrix[x][y];
        }
        return routeCost;
    }

    /**
     * Returns information of each vehicle in the tour
     *
     * @param   vehicleList   the location LinkedList.
     * @return  the information  (route taken, cost, and capacity) of each vehicle
     */
    public String displayVehicle(ArrayList<Vehicle> vehicleList){
        for (int i = 1; i <= vehicleList.size(); i++) {
            sb.append("\nVehicle " + i +"\n" + vehicleList.get(i-1) + "\n");
        }
        return sb.toString();
    }

    /**
     * Returns the route for each vehicle after it performs the Depth-First Search (DFS) algorithm
     *
     * @return the tour that consists of different vehicle using different routes.
     */
    public ArrayList<Vehicle> dfs(){
        bestCost = Double.POSITIVE_INFINITY;
        bestPath = new ArrayList<>();
        startTime=Instant.now();
        vehicleList.clear();
        ArrayList<Integer> tempPath=new ArrayList<>();
        ArrayList<Integer> adjList [] =new ArrayList[c.size()];
        for (int i = 0; i < c.size(); ++i) {
            adjList[i]=new ArrayList<>();
        }
        for (int i = 0; i < adjList.length; ++i) {  //form adjacency list
            for (int j = 1; j <c.size(); j++) {
                //for every adjList[] , they have their own list storing their neighbor excluding itself
                if(j!=i)
                    adjList[i].add(j);
            }
        }

        bestPath=depthFirstSearch(0,adjList,tempPath);

        linkedList.clear();
        int  currentLoad=0;
        linkedList.add(c.get(0));
        double capacity=d.maximumCapacity;
        int index=1;
        for (int i = index; i <bestPath.size(); i++) {  //separate the minCost path into different vehicle
            if(currentLoad+c.get(bestPath.get(i)).demandSize<=d.maximumCapacity) {
                linkedList.add(c.get(bestPath.get(i)));
                currentLoad+=c.get(bestPath.get(i)).demandSize;

                continue;
            }
            linkedList.add(c.get(0));
            routeCost = computeRouteCost(linkedList);
            vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));
            linkedList.clear();
            currentLoad=0;
            linkedList.add(c.get(0));
            i--;
        }
        linkedList.add(c.get(0));
        routeCost = computeRouteCost(linkedList);
        vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));

        //display output
        sb = new StringBuilder();
        sb.append("Basic Simulation Tour\nTour Cost: " + bestCost + "\n");
        displayVehicle(vehicleList);

        return vehicleList;
    }

    /**
     * Returns the tour after it examines the capacity of each vehicle that can be used to visit the customer
     *
     * @param   node   the ID of the node
     * @param  adjList the neighbours of the location
     * @param  tempPath the route sequence
     * @return the best tour after it recursively explores the tree
     */
    public ArrayList<Integer> depthFirstSearch(Integer node,ArrayList<Integer> adjList[] ,ArrayList<Integer> tempPath){
        tempPath.add(node);
        if(node!=0)
            ((Customer)c.get(node)).wasVisited=true;

        //base case + calculate pathCost
        if(completeVisited()){ //form a path consist of every customer, compare if it is a cheaper path
            double tempCost=0;
            int currentNode=0;
            int nextNode=0;
            int remainingCapacity=d.maximumCapacity;
            for (int i = 0; i < tempPath.size()-1; i++) {
                currentNode=tempPath.get(i);
                nextNode=tempPath.get(i+1);
                if(remainingCapacity-c.get(nextNode).demandSize>0) {
                    tempCost += adjMatrix[currentNode][nextNode];
                }
                else {
                    tempCost += adjMatrix[currentNode][0];
                    tempCost += adjMatrix[0][nextNode];
                    remainingCapacity=d.maximumCapacity;

                }
                remainingCapacity-=c.get(nextNode).demandSize;
            }
            tempCost+= adjMatrix[nextNode][0];//back to depot
            if(tempCost<=bestCost){
                bestPath.clear();
                for (int i = 0; i < tempPath.size(); i++) {
                    bestPath.add(tempPath.get(i));
                }
                bestCost=tempCost;
            }
            ((Customer)c.get(node)).wasVisited=false;

            return tempPath;
        }

        for (int i = 0; i < adjList[node].size(); i++) {
            int nextNode=adjList[node].get(i);

            if(!((Customer)c.get(nextNode)).wasVisited) {
                depthFirstSearch(nextNode, adjList,tempPath);
                if(Duration.between(startTime,Instant.now()).getSeconds()>=60)
                    return bestPath;

                tempPath.remove(tempPath.size()-1);
            }
        }
        if(node!=0)
            ((Customer)c.get(node)).wasVisited=false;

        return bestPath;
    }

    /**
     * Returns the route for each vehicle after it performs the greedy search algorithm
     *
     * @return the tour that consists of different vehicle using different routes.
     */
    public ArrayList<Vehicle> greedySearch(){
        int currentLoad;
        tourCost=0;
        ArrayList<Location> greedyList=new ArrayList<>();
        linkedList.clear();
        vehicleList.clear();

        for (int i = 1; i < c.size(); i++) { //resetting all to false to create connections later
            ((Customer)(c.get(i))).wasVisited=false;
        }

        int i=0;  //start from depot
        while(!completeVisited()) {  //if true , greedyList has been generated, can terminate
            double shortest = Double.POSITIVE_INFINITY;
            Customer tempCustomer = (Customer) c.get(1);
            for (int j = 1; j < adjMatrix.length; j++) { //to get shortest distance
                Customer cus = (Customer) c.get(j);
                if (i == j)
                    continue;
                if(cus.wasVisited)
                    continue;
                if (adjMatrix[i][j] < shortest) { //greedy search only consider shortest distance between two node
                    shortest = adjMatrix[i][j];
                    tempCustomer = cus;
                }

            }
            shortest=Double.POSITIVE_INFINITY;
            greedyList.add(tempCustomer);
            tempCustomer.wasVisited=true;

            i = tempCustomer.id;
        }
        for ( i = 1; i < c.size(); i++) {
            ((Customer)(c.get(i))).wasVisited=false;
        }
        tourCost=vehicleDistribution(greedyList);

        //display output
        sb = new StringBuilder();
        sb.append("Greedy Simulation Tour\nTour Cost: " + tourCost + "\n");
        displayVehicle(vehicleList);
        resetVisited();
        return vehicleList;
    }

    /**
     * Returns the route for each vehicle after it performs the Best First Search algorithm
     *
     * @return the tour that consists of different vehicle using different routes.
     */
    public ArrayList<Vehicle> bestFirstSearch(){
        tourCost = 0;
        ArrayList<Location> closed = new ArrayList<>(); //a list storing visited by not yet expand node
        ArrayList<Location> open = new ArrayList<>(); //a list storing visited and expanded node
        List<Double> h = new ArrayList<>(); //a list storing straight line distance from current node to goal node, h(n)
        for (int i = 0; i < adjMatrix[0].length; i++) {
            h.add(adjMatrix[0][i]);
        }
        for (int i = 0; i < h.size(); i++) {  //list that store location, according to h(n)
            closed.add(c.get(i));
        }

        open.add(closed.remove(0)); //start exploring with first node in open
        h.remove(0);
        //f(n) = h(n)
        int currentRouteCapacity = 0;
        while (!closed.isEmpty()) {
            double hMIN = Double.POSITIVE_INFINITY;
            int closedID = -1; //-1 means nextStop not found
            for (int i = 0; i < closed.size(); i++) { //to find best possible nextStop
                //condition 1: lowest h(n) among latest list of closed
                //condition 2: if add this nextStop does not exceed maximumCapacity
                if (h.get(i) < hMIN && currentRouteCapacity + closed.get(i).demandSize <= d.maximumCapacity) {
                    hMIN = h.get(i);
                    closedID = i;
                }
            }
            if (closedID != -1) {
                currentRouteCapacity += closed.get(closedID).demandSize;
                open.add(closed.remove(closedID));
                h.remove(closedID);
            }
            else { //new route, reset data of route
                currentRouteCapacity = 0;
            }
        }
        open.remove(0);

        tourCost=vehicleDistribution(open);
        //display output
        sb = new StringBuilder();
        sb.append("Best First Search Simulation Tour\nTour Cost: " + tourCost + "\n");
        displayVehicle(vehicleList);
        resetVisited();
        return vehicleList;
    }

    /**
     * Returns the route for each vehicle after it performs the A* Search algorithm
     *
     * @return the tour that consists of different vehicle using different routes.
     */
    public ArrayList<Vehicle>  aStarSearch() {
        tourCost = 0;
        ArrayList<Location> open = new ArrayList<>(); //keeps all nodes that are discovered but not yet expanded
        ArrayList<Location> result = new ArrayList<>();
        //store c in open
        for (int i = 0; i<c.size(); i++) {
            open.add(c.get(i));
        }

        //f(n) = g(n) + h(n)
        //g(n) = actual cost from start to current
        //h(n) = estimated cost from current to goal in straight line
        double f = 0;
        double g = 0;
        ArrayList<Double> h = new ArrayList<>(); //list that keeps h(n) values of all nodes
        //store values of h(n)
        for (int i = 0; i< open.size(); i++) {
            h.add(adjMatrix[i][0]);
        }

        result.add(open.remove(0)); //add depot to start
        h.remove(0);
        Location currentStop = result.get(result.size() - 1);

        int maxCapacity = d.maximumCapacity;
        int currentRouteCapacity = 0;

        while (!open.isEmpty()) {
            double fMIN = Double.POSITIVE_INFINITY;

            int openID = -1; //-1 means nextStop not found
            double gTemp = g + 0; //temporary g(n) until nextStop
            for (int i = 0; i<open.size(); i++) { //to find best possible nextStop
                Location nextStop = open.get(i);
                gTemp = g + adjMatrix[currentStop.id][nextStop.id];
                f = gTemp + h.get(i);

                //condition 1: lowest f(n) among latest list of open
                //condition 2: if add this nextStop does not exceed maximumCapacity
                if (f < fMIN && currentRouteCapacity + open.get(i).demandSize <= maxCapacity) {
                    fMIN = f;
                    openID = i;
                }
            }
            if (openID != -1) {
                result.add(open.remove(openID));
                h.remove(openID);
                currentStop = result.get(result.size() - 1); //refresh currentStop
                currentRouteCapacity += result.get(result.size() - 1).demandSize;
                g = gTemp; //update g(n) until nextStop
            }
            else { //new route, reset data of route
                currentStop = result.get(0); //restart currentStop at depot, but not added into (ArrayList) result
                currentRouteCapacity = 0;
                f = 0;
                g = 0;
            }
        }
        result.remove(0);

        tourCost=vehicleDistribution(result);
        //display output
        sb = new StringBuilder();
        sb.append("A* Search Simulation Tour\nTour Cost: " + tourCost + "\n");
        displayVehicle(vehicleList);
        resetVisited();
        return vehicleList;
    }

    /**
     * Returns the route for each vehicle after it performs the Dijkstra algorithm
     *
     * @return the tour that consists of different vehicle using different routes.
     */
    public ArrayList<Vehicle> dijkstra(){
        tourCost = 0;
        ArrayList<Location> open = new ArrayList<>(); //keeps all nodes that are discovered but not yet expanded
        ArrayList<Location> closed = new ArrayList<>();
        //store c in open
        for (int i = 0; i<c.size(); i++) {
            open.add(c.get(i));
        }
        closed.add(open.remove(0)); //add depot to visited
        Location currentNode = closed.get(0);
        int maxCapacity = d.maximumCapacity;
        int currentRouteCapacity = 0;

        while (!open.isEmpty()) {
            double shortest = Double.POSITIVE_INFINITY;
            int openID = -1; //ID of open, if nothing then -1
            for (int i = 0; i<open.size(); i++) { //to find best possible nextStop
                Location nextStop = open.get(i);
                double distance = adjMatrix[currentNode.id][nextStop.id];

                //condition 1: shortest distance among latest list of open
                //condition 2: if add this nextNode does not exceed maximumCapacity
                if (distance < shortest && currentRouteCapacity + open.get(i).demandSize <= maxCapacity) {
                    shortest = distance;
                    openID = i;
                }
            }
            if (openID != -1) {//found suitable next node
                closed.add(open.remove(openID));
                currentNode = closed.get(closed.size() - 1); //refresh currentNode
                currentRouteCapacity += closed.get(closed.size() - 1).demandSize;
            }
            else { //new route, reset data of route
                currentNode = closed.get(0); //restart currentNode at depot, but not added into (ArrayList) closed
                currentRouteCapacity = 0;
            }
        }
        closed.remove(0);

        tourCost=vehicleDistribution(closed);
        //display output
        sb = new StringBuilder();
        sb.append("Dijkstra Simulation Tour\nTour Cost: " + tourCost + "\n");
        displayVehicle(vehicleList);
        resetVisited();
        return vehicleList;
    }

    /**
     * Returns the tour cost that is the summation of routes taken by each vehicle. It distributes the list of locations based on the vehicle maximum capacity and customer demand size,
     * makes into linked list, then stores in vehicleList.
     *
     * @param   list   the list of locations
     * @return the best tour after it recursively explores the tree
     */
    public double vehicleDistribution(ArrayList<Location> list){
        tourCost=0;
        linkedList.clear();
        vehicleList.clear();

        for (int i = 1; i < c.size(); i++) { //resetting all to false to create connections later
            ((Customer)(c.get(i))).wasVisited=false;
        }
        int i=0;
        while(!completeVisited()) {  //for loop to form different combination of customer , makesure all customer are visited
            //suppose is while there are no node unvisited
            linkedList.clear();
            int currentLoad = 0;
            double tempShortestNode=adjMatrix[0][0];  //distance depot to depot = zero
            int tempShortestNodeIndex=0;
            while (i < list.size()) {
                //traverse through greedy list to end to check if there if any combination can be formed, which capacity won't be overload
                if (currentLoad + list.get(i).demandSize<= d.maximumCapacity  && !((Customer)c.get(i+1)).wasVisited) {
                    double shortestFirstNode = adjMatrix[0][list.get(i).id];
                    if (shortestFirstNode < tempShortestNode) {
                        linkedList.add(tempShortestNodeIndex, list.get(i));
                    } else if (linkedList.size() >= 2) {
                        linkedList.add(tempShortestNodeIndex + 1, list.get(i));
                    } else
                        linkedList.add(list.get(i)); //add Customer at the end , but we need to formed the route starting from location closest to the depot
                    ((Customer)c.get(i+1)).wasVisited=true;
                    tempShortestNode = shortestFirstNode;
                    tempShortestNodeIndex = linkedList.indexOf(list.get(i));
                    currentLoad += list.get(i).demandSize;
                }
                i++;
            }
            linkedList.addFirst(c.get(0)); //add depot
            linkedList.add(c.get(0));//complete the path with return to depot
            routeCost = computeRouteCost(linkedList);
            vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));

            tourCost+=routeCost;
            linkedList.clear();
            i=0;
        }
        return tourCost;
    }

    /**
     * Sets the wasVisited status of all locations to {@code false}
     */
    public void resetVisited(){
        for (int i = 1; i < c.size() ; i++) {
            ((Customer)c.get(i)).wasVisited = false;
        }
    }

    /**
     * Returns the tour that conveys information about each route cost for each vehicle
     *
     * @param   level   number of levels for MCTS search tree
     * @param   iterations   number of nodes in every level
     * @return the tour of the MCTS algorithm
     */
    public Tour mctsSearch(int level, int iterations){
        Instant startTime = Instant.now();
        if(level==0){
            return rollout();
        }else{
            policy[level-1] = globalPolicy;
            for (int i = 0; i < iterations; i++) {
                Tour new_tour = mctsSearch(level-1,iterations);
                if(new_tour.getTourCost() < best_tour.getTourCost()){
                    best_tour = new_tour;
                    adapt(best_tour,level);
                }
                if(Duration.between(startTime, Instant.now()).getSeconds() > timeLimit);
            }
            globalPolicy = policy[level-1];
        }
        return best_tour;
    }

    /**
     * To update policy based on {@code a_tour}
     * @param   a_tour   tour that holds information about tour cost based on all routes
     * @param   level   number of levels for MCTS search tree
     */
    public void adapt(Tour a_tour, int level){
        ArrayList<Location> visited = new ArrayList<>();
        //for every route in tour
        for (int i = 0; i < a_tour.getRouteSize(); i++) {
            for (int j = 0; j < a_tour.getRoute().get(i).size()-1; j++) {
                ALPHA=1;
                policy[level-1][a_tour.getRoute().get(i).get(j).id][a_tour.getRoute().get(i).get(j+1).id] += ALPHA;
                z = 0.0;
                //for every possible move that can be made by stop
                for (int k = 0; k < location.size(); k++) {
                    if(location.get(k).id!=a_tour.getRoute().get(i).get(j).id){
                        if(!visited.contains(location.get(k))){
                            z+= Math.exp(globalPolicy[a_tour.getRoute().get(i).get(j).id][location.get(k).id]);
                        }
                    }
                }
                //for every possible move that can be made by stop
                for (int k = 0; k < location.size(); k++) {
                    if(location.get(k).id != a_tour.getRoute().get(i).get(j).id){
                        if(!visited.contains(location.get(k))){
                            policy[level - 1][a_tour.getRoute().get(i).get(j).id][location.get(k).id] -= ALPHA * (Math.exp(globalPolicy[a_tour.getRoute().get(i).get(j).id][location.get(k).id]) / z);
                        }
                    }
                }
                visited.add(a_tour.getRoute().get(i).get(j));
            }
        }
    }

    /**
     * Returns the tour that searhces for routes for the tour according to the policy.
     *
     * @return the tour
     */
    public Tour rollout(){
        Location currentStop;
        Location nextStop;

        ArrayList<Location> possible_successors = (ArrayList<Location>)location.clone();
        possible_successors.remove(0); //remove the depot

        ArrayList<Location> visited = new ArrayList<>();
        ArrayList<Location> checked = new ArrayList<>();

        Tour new_tour = new Tour(adjMatrix,c);
        new_tour.addNewRoute();

        int currentLoad = 0;

        while(true){
            currentStop = new_tour.getLastStop();
            for (int i = 0; i < possible_successors.size(); i++) {
                if(checked.contains(possible_successors.get(i)) || visited.contains(possible_successors.get(i))){
                    possible_successors.remove(i);
                }
            }
            //if no possible successor is available, return to depot
            if(possible_successors.isEmpty()){
                new_tour.addDepot(); //add depot
                //!!!setRouteCost;
                //if all stops are visited
                if(checked.isEmpty()) {
                    //!!!user for loop to compute Tour cost;
                    break; //rollout process is done
                }
                //add new route into new tour
                new_tour.addNewRoute();
                currentLoad = 0;

                for (int i = 0; i < checked.size(); i++) {
                    possible_successors.add(checked.remove(i));
                }
                continue; // skip to next loop to continue
            }
            nextStop = select_next_move(currentStop,possible_successors);

            if(currentLoad+nextStop.demandSize<=d.maximumCapacity){
                new_tour.addStop(nextStop);
                currentLoad += nextStop.demandSize;
                visited.add(nextStop);
            }else{
                checked.add(nextStop);
            }
        }
        return new_tour;
    }

    /**
     * Returns the {@code Location} after it decides which node to go next from current node based on the policy
     *
     * @param   currentStop   current Location at
     * @param   possible_successors   possible move that can be made from current node
     * @return next move taken by the {@code currentStop}
     */
    public Location select_next_move(Location currentStop, ArrayList<Location> possible_successors){ //possible successors = {0,1,2,3,4}
        double[] probability = new double[possible_successors.size()];
        double sum = 0;
        for (int i = 0; i < possible_successors.size(); i++) {
            probability[i] = Math.exp(globalPolicy[currentStop.id][possible_successors.get(i).id]);
            sum+=probability[i];
        }
        double mRand = new Random().nextDouble() * sum;
        int j = 0;
        sum = probability[0];
        while(sum<mRand){
            sum+=probability[++j];
        }
        return possible_successors.get(j);
    }

}

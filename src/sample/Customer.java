package sample;
public class Customer extends Location{
    boolean wasVisited;

    public Customer(int xCoordinate, int yCoordinate, int demandSize) {
        super(xCoordinate,yCoordinate,demandSize);
        wasVisited = false;
    }

    public int getId() {
        return id;
    }

    public void setWasVisited(boolean wasVisited) {
        this.wasVisited = wasVisited;
    }

    @Override
    public String toString() {
        return "[ id: " + id+ " x-coordinate: " + xCoordinate + ", y-coordinate: " + yCoordinate +", demand size: " + demandSize + "]";
    }

    public int getDemandSize() {
        return demandSize;
    }
}
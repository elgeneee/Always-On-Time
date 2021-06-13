package sample;


public class Location{
    public int xCoordinate;
    public int yCoordinate;
    public int demandSize;
    public int id;
    static int serial = 0;
    public boolean wasVisited;
    public boolean wasChecked;

    public Location(){};

    public Location(int xCoordinate, int yCoordinate, int demandSize) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.demandSize = demandSize;
        this.id = serial;
        serial++; //auto increment the ID whenever an instance of customer is created
        wasVisited = false;
        wasChecked = false;
    }

    public void resetSerial(){
        serial = 0;
    }
}
package sample;


public class Location{
    int xCoordinate;
    int yCoordinate;
    int demandSize;
    int id;
    static int serial =0;

    public Location(int xCoordinate, int yCoordinate, int demandSize) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.demandSize = demandSize;
        this.id = serial;
        serial++; //auto increment the ID whenever an instance of customer is created

    }
}
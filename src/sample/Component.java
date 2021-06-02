package sample;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


import java.util.ArrayList;
import java.util.List;

//a singleton
public class Component {
    static Component com = new Component();
    ArrayList<Circle> circleList = new ArrayList<Circle>();
    ArrayList<Line> lineList = new ArrayList<Line>();
    List<Location> location = new ArrayList<>();

    private Component(){}

    public static Component getInstance(){
        return com;
    }

    public void addCircle(Circle c){
        circleList.add(c);
    }

    public void addLine(Line l){
        lineList.add(l);
    }

    public void setCircleList(ArrayList<Circle> c){
        circleList = c;
    }

    public void setLineList(ArrayList<Line> l){
        lineList = l;
    }

    public ArrayList<Circle> getCircleList(){
        return circleList;
    }

    public ArrayList<Line> getLineList(){
        return lineList;
    }

    public void setLocationList(List<Location> list){
        location = list;
    }

    public List<Location> getLocationList(){
        return location;
    }
}

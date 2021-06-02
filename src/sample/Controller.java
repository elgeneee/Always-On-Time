package sample;

import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Controller extends AnchorPane {

    List<Location> list;
    Component com = Component.getInstance();
    ArrayList<Circle> circleList = new ArrayList<>();
    ArrayList<Line> lineList = new ArrayList<>();
    Circle circle;
    Line line;
    double xMax,yMax;
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public Button btn4;

    //for graph visualisation
    ArrayList<Vehicle> vehicleList;
    ArrayList<ImageView> truckList = new ArrayList<>();

    public void loadFile(ActionEvent event) throws Exception{
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile !=null){
            Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Stage window = (Stage) btn1.getScene().getWindow();
            Scene scene = new Scene(root,1000,700);
            try {
                Scanner input = new Scanner(new FileInputStream(selectedFile.getPath()));
                int numOfCustomers = input.nextInt();
                int maxCapacity = input.nextInt();
                int xCoordinate = input.nextInt();
                int yCoordinate = input.nextInt();
                list = new ArrayList<>();
                list.add(new Depot(numOfCustomers, maxCapacity, xCoordinate,yCoordinate)); //instantiate Depot

                circle = new Circle();
                circle.setCenterX(xCoordinate);
                circle.setCenterY(yCoordinate);
                circle.setRadius(8);
                circle.setFill(Color.RED);
                circle.setStroke(Color.BLACK);
                Tooltip tooltip = new Tooltip("Depot\nx-coordinate: " + xCoordinate + "\ny-coordinate: " + yCoordinate + "\nmax Capacity: " +maxCapacity);
                tooltip.setShowDuration(Duration.seconds(100));
                tooltip.setShowDelay(Duration.millis(0));
                Tooltip.install(circle, tooltip);
                circleList.add(circle);

                xMax = xCoordinate;
                yMax = yCoordinate;

                input.nextLine();
                while(input.hasNextLine()) {     //instantiate Customer
                    int x = input.nextInt();
                    int y = input.nextInt();
                    int demandSize = input.nextInt();
                    list.add(new Customer(x,y,demandSize));
                    if(xMax<x) xMax =  x;
                    if(yMax<y) yMax = y;

                    circle = new Circle();
                    circle.setCenterX(x);
                    circle.setCenterY(y);
                    circle.setRadius(8);
                    circle.setFill(Color.BLUEVIOLET);
                    circle.setStroke(Color.BLACK);;
                    tooltip = new Tooltip("Customer ID: " + (list.size()-1) + "\nx-coordinate: " + x + "\ny-coordinate: " + y + "\ndemand size: " +demandSize);
                    tooltip.setShowDuration(Duration.seconds(100));
                    tooltip.setShowDelay(Duration.millis(0));
                    Tooltip.install(circle, tooltip);
                    circleList.add(circle);
                }
                input.close();
            }
            catch(FileNotFoundException e) {
                System.out.println("File was not found");
            }

            xMax = 729/xMax;
            yMax = 700/yMax;

            for (Circle c : circleList) {
               c.setCenterX(275 + c.getCenterX()*(xMax-0.5));
               c.setCenterY((yMax-0.4) * c.getCenterY());
            }
            double startX, startY;
            for (int i = 0; i <circleList.size(); i++) {
                for (int j = i + 1; j < circleList.size(); j++) {
                    line = new Line();
                    line.setStrokeWidth(1.4);
                    line.setStartX(circleList.get(i).getCenterX());
                    line.setStartY(circleList.get(i).getCenterY());
                    line.setEndX(circleList.get(j).getCenterX());
                    line.setEndY(circleList.get(j).getCenterY());
                    line.setOpacity(0.7);
                    lineList.add(line);
                }
            }

            root.getChildren().addAll(lineList);
            root.getChildren().addAll(circleList);

            com.setCircleList(circleList);
            com.setLineList(lineList);
            com.setLocationList(list);

            Location l = new Location();
            l.resetSerial();

            window.setTitle("Always-On-Time");
            window.setResizable(false);
            window.setScene(scene);
            window.show();
        }else{
            System.out.println("Invalid File!");
        }

    }

    public void basicSimulation(ActionEvent event) throws Exception{
        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage window = (Stage) btn2.getScene().getWindow();
        Scene scene = new Scene(root,1000,700);

        root.getChildren().addAll(com.getLineList());
        root.getChildren().addAll(com.getCircleList());

        window.setTitle("Always-On-Time");
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    public void greedySimulation(ActionEvent event) throws Exception{
        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage window = (Stage) btn3.getScene().getWindow();
        Scene scene = new Scene(root,1000,700);
        circleList = com.getCircleList(); //to refer our truck points
        Graph graph = new Graph(com.getLocationList());
        vehicleList = graph.greedySearch();
        for (int i = 0; i < vehicleList.size(); i++) {
            Image image = new Image(new FileInputStream("truck24x24.png"));
            ImageView img = new ImageView();
            img.setImage(image);

            Polyline polyline = new Polyline();
            PathTransition transition = new PathTransition();
            Double[] d = new Double[vehicleList.get(i).list.size()*2];
            int j, k;
            for ( j = 0, k = 0; j < vehicleList.get(i).list.size(); j++, k+=2) { //vehicle 1 = 0->1->2
                d[k] = circleList.get(vehicleList.get(i).list.get(j).id).getCenterX();
                d[k+1] = circleList.get(vehicleList.get(i).list.get(j).id).getCenterY();
            }
            k-=2;
            d[k] = circleList.get(0).getCenterX();
            d[k+1] = circleList.get(0).getCenterY();
            polyline.getPoints().addAll(d);
            transition.setNode(img);
            transition.setDuration(Duration.seconds(10));
            transition.setPath(polyline);
            transition.setCycleCount(TranslateTransition.INDEFINITE);
            transition.play();
            truckList.add(img);
        }
        root.getChildren().addAll(com.getLineList());
        root.getChildren().addAll(com.getCircleList());
        root.getChildren().addAll(truckList);

        window.setTitle("Always-On-Time");
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    public void mctsSimulation(ActionEvent event)throws Exception{
        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage window = (Stage) btn4.getScene().getWindow();
        Scene scene = new Scene(root,1000,700);

        Graph graph = new Graph(com.getLocationList());
        graph.displayVehicle(graph.greedySearch());

        root.getChildren().addAll(com.getLineList());
        root.getChildren().addAll(com.getCircleList());

        window.setTitle("Always-On-Time");
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }


}

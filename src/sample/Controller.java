package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class Controller extends AnchorPane {
    List<Location> list = new ArrayList<>();
    Component com = Component.getInstance();
    ArrayList<Circle> circleList = new ArrayList<>();
    ArrayList<Line> lineList = new ArrayList<>();
    Tooltip tooltip = new Tooltip();
    Circle circle;
    Line line;
    double xMax,yMax;
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public Button btn4;

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

            window.setTitle("Always-On-Time");
            window.setResizable(false);
            window.setScene(scene);
            window.show();
        }else{
            System.out.println("Invalid File!");
        }

    }

    public void basicSimulation(ActionEvent event) throws Exception{
        Pane root2 = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage window2 = (Stage) btn2.getScene().getWindow();
        Scene scene2 = new Scene(root2,1000,700);

        root2.getChildren().addAll(com.getLineList());
        root2.getChildren().addAll(com.getCircleList());

        window2.setTitle("Always-On-Time");
        window2.setResizable(false);
        window2.setScene(scene2);
        window2.show();

    }

    public void setText(){

    }

}

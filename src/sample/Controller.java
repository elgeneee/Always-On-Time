package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller extends AnchorPane {
    List<Location> list = new ArrayList<>();
    ArrayList<Circle> circleList = new ArrayList<>();
    ArrayList<Line> lineList = new ArrayList<>();

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
            Pane root = FXMLLoader.load(getClass().getResource("load.fxml"));
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
                circleList.add(circle);

                xMax = xCoordinate;
                yMax = yCoordinate;

                input.nextLine();
                while(input.hasNextLine()) {     //instantiate Customer
                    int x = input.nextInt();
                    int y = input.nextInt();
                    int demandSize = input.nextInt();
                    if(xMax<x) xMax =  x;
                    if(yMax<y) yMax = y;

                    circle = new Circle();
                    circle.setCenterX(x);
                    circle.setCenterY(y);
                    circle.setRadius(8);
                    circle.setFill(Color.BLUEVIOLET);
                    circle.setStroke(Color.BLACK);
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
//                System.out.println(c.getCenterX() + " " + c.getCenterY());
            }
            double startX, startY;
            for (int i = 0; i < circleList.size(); i++) {
                startX = circleList.get(i).getCenterX();
                startY = circleList.get(i).getCenterY();
                for (int j = 0; j < circleList.size(); j++) {
                    if(i==j) continue;
                    line = new Line();
                    line.setStrokeWidth(1);
                    line.setStartX(startX);
                    line.setStartY(startY);
                    line.setEndX(circleList.get(j).getCenterX());
                    line.setEndY(circleList.get(j).getCenterY());
                    line.setOpacity(0.5);
                    lineList.add(line);

                }
            }
            root.getChildren().addAll(lineList);
            root.getChildren().addAll(circleList);

            window.setTitle("Always-On-Time");
            window.setResizable(false);
            window.setScene(scene);
            window.show();
        }else{
            System.out.println("Invalid File!");
        }
    }


    public void setText(){

    }

}

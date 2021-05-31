package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 1000, 700);
//        ArrayList<Circle> arr = new ArrayList<>();
//
//        Text text = new Text();
//        text.setText("Test");
//
//        Circle circle = new Circle();
//        circle.setCenterX(347);
//        circle.setCenterY(37);
//        circle.setRadius(30);
//        circle.setFill(Color.BLUEVIOLET);
//        circle.setStroke(Color.BLACK);
//
//        Line line = new Line();
//        line.setStrokeWidth(1);
//        line.setStartX(280);
//        line.setStartY(0);
//        line.setEndX(50);
//        line.setEndY(50);
//        line.setOpacity(0.5);

        stage.setTitle("Always-On-Time");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}

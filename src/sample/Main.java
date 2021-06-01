package sample;

import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        ArrayList<ImageView> arr = new ArrayList<>();

        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 1000, 700);


        Image image = new Image(new FileInputStream("truck24x24.png"));
        ImageView img = new ImageView();
        img.setImage(image);

        Image image1 = new Image(new FileInputStream("truck24x24.png"));
        ImageView img1 = new ImageView();
        img1.setImage(image1);

        Polyline polyline = new Polyline();
        polyline.getPoints().addAll(new Double[]{
                0.0,0.0,
                200.0,100.0,
                100.0,200.0});

        PathTransition transition = new PathTransition();
        transition.setNode(img);
        transition.setDuration(Duration.seconds(3));
        transition.setPath(polyline);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.play();

        TranslateTransition translate1 = new TranslateTransition();
        translate1.setNode(img1);
        translate1.setFromX(500);
        translate1.setFromY(50);
        translate1.setToX(250);
        translate1.setToY(250);
        translate1.setDuration(Duration.seconds(3));
        translate1.setCycleCount(TranslateTransition.INDEFINITE);
        translate1.play();


        arr.add(img);
        arr.add(img1);
        root.getChildren().addAll(arr);

        stage.setTitle("Always-On-Time");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

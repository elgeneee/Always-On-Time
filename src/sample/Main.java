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
import javafx.util.Duration;

import java.io.FileInputStream;
import java.util.ArrayList;

public class Main extends Application {

    //1200px x 1000

    @Override
    public void start(Stage primaryStage) throws Exception{

        ArrayList<ImageView> arr = new ArrayList<>();

        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 1000, 700);

//
//        Image image = new Image(new FileInputStream("truck24x24.png"));
//        ImageView img = new ImageView();
//        img.setImage(image);
//
//        Double[] d = {
//                0.0,0.0,
//                200.0,100.0,
//                100.0,200.0};
//
//        Polyline polyline = new Polyline();
//        polyline.getPoints().addAll(d);
//
//        PathTransition transition = new PathTransition();
//        transition.setNode(img);
//        transition.setDuration(Duration.seconds(3));
//        transition.setPath(polyline);
//        transition.setCycleCount(TranslateTransition.INDEFINITE);
//        transition.play();
//
//
//        arr.add(img);
//        root.getChildren().addAll(arr);

        stage.setTitle("Always-On-Time");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    public ScrollPane sp;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 1200, 1000);

//
//        Label l1 = new Label("This is a text\nThis is line 2");
//
//        sp = new ScrollPane();
//        sp.setContent(l1);
//        root.getChildren().add(sp);



        stage.setTitle("Always-On-Time");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

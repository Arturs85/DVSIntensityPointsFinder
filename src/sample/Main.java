package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    static Canvas canvas = new Canvas(FrequencyMapFinder.width*FrequencyMapFinder.maxScale, FrequencyMapFinder.height*FrequencyMapFinder.maxScale);

    @Override
    public void start(Stage primaryStage) throws Exception{
        ScrollPane scrollPane = new ScrollPane(canvas);
        BorderPane root = new BorderPane(scrollPane);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
       primaryStage.show();
    FrequencyMapFinder frequencyMapFinder = new FrequencyMapFinder();
    frequencyMapFinder.openFile("/home/arturs/Downloads/DVS LEDs/2019-11-21_16-42-50.rawdvs.aedat");
    frequencyMapFinder.normalize((byte)127);
    frequencyMapFinder.draw(canvas);
    }


    public static void main(String[] args) {
        launch(args);
    }

}

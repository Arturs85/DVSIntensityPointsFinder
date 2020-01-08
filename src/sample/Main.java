package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    static Canvas canvas = new Canvas(FrequencyMapFinder.width * FrequencyMapFinder.maxScale, FrequencyMapFinder.height * FrequencyMapFinder.maxScale);
    boolean drawingStarted;
    double firstX;
    double firstY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ScrollPane scrollPane = new ScrollPane(canvas);
        BorderPane root = new BorderPane(scrollPane);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 800));
        primaryStage.show();


        FrequencyMapFinder frequencyMapFinder = new FrequencyMapFinder();
        frequencyMapFinder.openFile("/home/vnpc/Downloads/DVS LEDs/2019-11-21_16-42-50.rawdvs.aedat");
        frequencyMapFinder.normalize((byte) 127);
        frequencyMapFinder.draw(canvas);
        canvas.setOnScroll(event -> {

            if (event.getDeltaY() > 0) frequencyMapFinder.zoomIn();
            else if (event.getDeltaY() < 0) frequencyMapFinder.zoomOut();
            System.out.println("canvas scroll delta y : " + event.getDeltaY());

            frequencyMapFinder.draw(canvas);

        });

        canvas.setOnMouseClicked(event -> {
// first check if click is inside some selection ciecle

            firstX = event.getX() / FrequencyMapFinder.scale;
            firstY = event.getY() / FrequencyMapFinder.scale;
            System.out.println("x: " + firstX + "y: " + firstY);

            frequencyMapFinder.onClickAt(firstX, firstY);
            frequencyMapFinder.draw(canvas);

        });

        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    System.out.println("Key Pressed: " + ke.getCode());
                    ke.consume(); // <-- stops passing the event to next node
                }
                if (ke.getCode() == KeyCode.LEFT) {
                    System.out.println("Key Pressed: " + ke.getCode());
                   frequencyMapFinder.moveButtonsX(-1);
                    ke.consume(); // <-- stops passing the event to next node
                }if (ke.getCode() == KeyCode.RIGHT) {
                    System.out.println("Key Pressed: " + ke.getCode());
                    frequencyMapFinder.moveButtonsX(1);

                    ke.consume(); // <-- stops passing the event to next node
                }
                if (ke.getCode() == KeyCode.UP) {
                    System.out.println("Key Pressed: " + ke.getCode());
                    frequencyMapFinder.moveButtonsY(-1);
                    ke.consume(); // <-- stops passing the event to next node
                }if (ke.getCode() == KeyCode.DOWN) {
                    System.out.println("Key Pressed: " + ke.getCode());
                    frequencyMapFinder.moveButtonsY(+1);

                    ke.consume(); // <-- stops passing the event to next node
                }
                if (ke.getCode() == KeyCode.PAGE_UP) {
                    System.out.println("Key Pressed: " + ke.getCode());
                  SelectionCircle.changeStaticRadius(+1);

                    ke.consume(); // <-- stops passing the event to next node
                }
                if (ke.getCode() == KeyCode.PAGE_DOWN) {
                    System.out.println("Key Pressed: " + ke.getCode());
                    SelectionCircle.changeStaticRadius(-1);

                    ke.consume(); // <-- stops passing the event to next node
                }
                frequencyMapFinder.draw(canvas);

            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

}

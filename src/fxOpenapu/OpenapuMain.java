package fxOpenapu;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Jussi
 * @version 14.1.2020
 *
 */
public class OpenapuMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("OpenapuGUIView.fxml"));
            final Pane root = ldr.load();
            //final OpenapuGUIController openapuCtrl = (OpenapuGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("openapu.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Openapu");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}
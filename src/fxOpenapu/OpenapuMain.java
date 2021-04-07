package fxOpenapu;
	
import fi.jyu.mit.fxgui.Dialogs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import openapu.Openapu;

/** 
 * P‰‰luokka ohjelmalle.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 * Ohjelma k‰yttˆliittym‰n alustamiseen
 */
public class OpenapuMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("OpenapuGUIView.fxml"));
		    final Pane root = (Pane)ldr.load();
		    OpenapuGUIController openapuCtrl = (OpenapuGUIController)ldr.getController();
		    
		    final Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("openapu.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show(); 
			primaryStage.setTitle("Openapu");
			
			Openapu openapu = new Openapu();
			openapuCtrl.setOpenapu(openapu);
			openapuCtrl.avaa();
			
	        primaryStage.setOnCloseRequest((event) -> {
	            if ( !openapuCtrl.voikoSulkea() ) {
	                boolean vastaus = Dialogs.showQuestionDialog("Sulkeminen",
	                           "Tietoja ei tallennettu. Tallennetaanko ennen sulkemista?", "Kyll‰", "Ei");
	                if (vastaus) {openapuCtrl.tallenna();}
	            }
	        });
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * P‰‰ohjelma
	 * @param args ei k‰yt
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
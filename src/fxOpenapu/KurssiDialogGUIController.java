package fxOpenapu;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kanta.Pvm;
import openapu.Kurssi;

/**
 * K‰sittelij‰ uuden kurssin luomisn‰kym‰lle. Ei k‰ytˆss‰.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 7.2.2018
 *
 */
public class KurssiDialogGUIController implements ModalControllerInterface<Kurssi>,Initializable{
    
	@FXML private TextField editOppiaine;
	@FXML private TextField editKurssikoodi;
	@FXML private TextField editNimi;
	@FXML private TextField editAloitusPvm;
	@FXML private TextField editLopetusPvm;
	@FXML private Label labelVirhe;
	private Kurssi kurssiKohdalla;
	private Kurssi vastaus = null;
	
	private TextField[] edits;
	
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
	}
	
	
	/**
	 * Alustaa tekstikent‰t ja n‰pp‰imen kuuntelijan.
	 */
    private void alusta() {
    	edits = new TextField[]{editNimi, editKurssikoodi, editOppiaine, editAloitusPvm, editLopetusPvm};
    	int i = 0;
    	for (TextField edit: edits) {
    		final int k = ++i;
    		edit.setOnKeyReleased(e -> kasitteleMuutosKurssiin(k,(TextField)(e.getSource())));
    	}
	}
    
    
    /**
     * Otetaan metodilla tekstikent‰n sis‰ltˆ ja asetetaan se kurssin vastaavalle attribuutille.
     * Palauttaa null jos onnistuu, n‰ytt‰‰ virheen jos ei onnistu.
     * @param k monesko tekstikentt‰ yritet‰‰n vied‰
     * @param edit tekstikentt‰
     */
    private void kasitteleMuutosKurssiin(int k, TextField edit) {
    	if (kurssiKohdalla == null) return;
    	String s = edit.getText();
    	String virhe = null;
    	switch (k) {
    	case 1: virhe = kurssiKohdalla.setNimi(s); break;
    	case 2: virhe = kurssiKohdalla.setKurssiKoodi(s); break;
    	case 3: virhe = kurssiKohdalla.setOppiaine(s); break;
    	case 4: virhe = kurssiKohdalla.setAloitusPvm(s); break;
    	case 5: virhe = kurssiKohdalla.setLopetusPvm(s); break;
    	default: break;
    	}
    	
    	if (virhe == null) {
    		Dialogs.setToolTipText(edit, "");
    		edit.getStyleClass().removeAll("virhe");
    		naytaVirhe(virhe);
    	} else {
    		Dialogs.setToolTipText(edit, virhe);
    		edit.getStyleClass().add("virhe");
    		naytaVirhe(virhe);
    	}
    }


	/**
     * K‰sittelee kurssin tietojen muuttamisen.
     */
    @FXML private void handleOK() {
        if (kurssiKohdalla != null) {
        	if (kurssiKohdalla.getNimi().trim().equals("")) {
                naytaVirhe("Nimi ei saa olla tyhj‰!");
                return;
            }
        
            if (kurssiKohdalla.getOppiaine().trim().equals("")) {
                naytaVirhe("Oppiaine ei saa olla tyhj‰!");
                return;
            }
            
            if (kurssiKohdalla.getKurssiKoodi().trim().equals("")) {
                naytaVirhe("Kurssikoodi ei saa olla tyhj‰!");
                return;
            }
            
            Pvm ap = new Pvm(); Pvm lp = new Pvm(); 
            ap.parse(kurssiKohdalla.getAloitusPvm()); lp.parse(kurssiKohdalla.getLopetusPvm());
            
            if (ap.compareTo(lp) == 1) {
            	naytaVirhe("Aloitusp‰iv‰m‰‰r‰n tulee olla lopetusp‰iv‰m‰‰r‰‰ ennen!");
            	return;
            }
           
        }
        vastaus = kurssiKohdalla;
        ModalController.closeStage(labelVirhe);
    }

    
    /**
     * K‰sittelee muutoksien peruuttamisen.
     */
    @FXML private void handlePeruuta() {
        ModalController.closeStage(labelVirhe);
    }
   
    
    /**
     * N‰ytt‰‰ virheen k‰yttˆliittym‰ss‰.
     * @param virhe joka n‰ytet‰‰n
     */
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add(virhe);
    }

    
    /**
     * Asettaa kurssin tiedot dialogiin.
     * @param kentat tekstikent‰t, jotka asetetaan
     * @param kurssi joka n‰ytet‰‰n
     */
    protected static void naytaKurssi(TextField[] kentat, Kurssi kurssi) {
    	if (kurssi == null) return;
    	
    	kentat[0].setText(kurssi.getNimi());
    	kentat[1].setText(kurssi.getKurssiKoodi());
    	kentat[2].setText(kurssi.getOppiaine());
    	kentat[3].setText(kurssi.getAloitusPvm());
    	kentat[4].setText(kurssi.getLopetusPvm());
    }

    
    /**Luodaan kurssin muokkaamisdialogille ikkuna.
     * @param modalityStage null, mik‰li p‰‰ikkuna
     * @param oletus oletuskurssi, jonka tiedot n‰ytet‰‰n
     * @return kurssi, jonka tiedot n‰ytet‰‰n
     */
    public static Kurssi kysyKurssi(Stage modalityStage, Kurssi oletus) {
        return ModalController.<Kurssi,KurssiDialogGUIController>showModal(
                       KurssiDialogGUIController.class.getResource("KurssiDialogGUIView.fxml"),
                       "Openapu",
                       modalityStage, oletus, null 
                   );
       }

    
    @Override
    public Kurssi getResult() {
        return vastaus;
    }

    
    @Override
    public void handleShown() {
        //Ei k‰ytˆss‰.   
    }

    
    @Override
    public void setDefault(Kurssi oletus) {
        kurssiKohdalla = oletus;
        naytaKurssi(edits, kurssiKohdalla);
    }


}

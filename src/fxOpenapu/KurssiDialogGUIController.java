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
 * Käsittelijä uuden kurssin luomisnäkymälle. Ei käytössä.
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
	 * Alustaa tekstikentät ja näppäimen kuuntelijan.
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
     * Otetaan metodilla tekstikentän sisältö ja asetetaan se kurssin vastaavalle attribuutille.
     * Palauttaa null jos onnistuu, näyttää virheen jos ei onnistu.
     * @param k monesko tekstikenttä yritetään viedä
     * @param edit tekstikenttä
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
     * Käsittelee kurssin tietojen muuttamisen.
     */
    @FXML private void handleOK() {
        if (kurssiKohdalla != null) {
        	if (kurssiKohdalla.getNimi().trim().equals("")) {
                naytaVirhe("Nimi ei saa olla tyhjä!");
                return;
            }
        
            if (kurssiKohdalla.getOppiaine().trim().equals("")) {
                naytaVirhe("Oppiaine ei saa olla tyhjä!");
                return;
            }
            
            if (kurssiKohdalla.getKurssiKoodi().trim().equals("")) {
                naytaVirhe("Kurssikoodi ei saa olla tyhjä!");
                return;
            }
            
            Pvm ap = new Pvm(); Pvm lp = new Pvm(); 
            ap.parse(kurssiKohdalla.getAloitusPvm()); lp.parse(kurssiKohdalla.getLopetusPvm());
            
            if (ap.compareTo(lp) == 1) {
            	naytaVirhe("Aloituspäivämäärän tulee olla lopetuspäivämäärää ennen!");
            	return;
            }
           
        }
        vastaus = kurssiKohdalla;
        ModalController.closeStage(labelVirhe);
    }

    
    /**
     * Käsittelee muutoksien peruuttamisen.
     */
    @FXML private void handlePeruuta() {
        ModalController.closeStage(labelVirhe);
    }
   
    
    /**
     * Näyttää virheen käyttöliittymässä.
     * @param virhe joka näytetään
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
     * @param kentat tekstikentät, jotka asetetaan
     * @param kurssi joka näytetään
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
     * @param modalityStage null, mikäli pääikkuna
     * @param oletus oletuskurssi, jonka tiedot näytetään
     * @return kurssi, jonka tiedot näytetään
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
        //Ei käytössä.   
    }

    
    @Override
    public void setDefault(Kurssi oletus) {
        kurssiKohdalla = oletus;
        naytaKurssi(edits, kurssiKohdalla);
    }


}

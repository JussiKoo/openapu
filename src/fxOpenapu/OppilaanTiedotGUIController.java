package fxOpenapu;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import openapu.KurssikohtTieto;

/**
 * Käsittelijä oppilaan tietojen muokkaamisnäkymälle.
 * 
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 7.2.2018
 *
 */
public class OppilaanTiedotGUIController implements ModalControllerInterface<KurssikohtTieto>, Initializable{
    
    @FXML private TextField textEtunimi;
    @FXML private TextField textSukunimi;
    @FXML private TextField textHetu;
	
    @FXML private TextArea editPoissaolot;
    @FXML private TextArea editKoetulokset;
    @FXML private TextArea editMerkinnat;
	@FXML private Label labelVirhe;
    
	private KurssikohtTieto kktieto;
    private KurssikohtTieto vastaus = null;
    
    private TextArea[] edits;
    
    
    /**
     * Käsittelee oppilaan tietojen muuttamisen.
     */
    @FXML private void handleOK() {
        vastaus = kktieto;
        ModalController.closeStage(labelVirhe);
    }
    
    
    /**
     * Käsittelee muutoksien peruuttamisen.
     */
    @FXML private void handlePeruuta() {
        vastaus = null;
        ModalController.closeStage(labelVirhe);
    }
    
    
    // ================ Tästä eteenpäin ei FXML -koodia ======================
    

    
    /**
     * Alustetaan näytettävät tiedot.
     */
    public void alusta() {
    	edits = new TextArea[] {editPoissaolot, editKoetulokset, editMerkinnat};
        
    	int i = 0;
    	for (TextArea edit: edits) {
    		final int k = ++i;
    		edit.setOnKeyReleased(e -> kasitteleMuutosTietoihin(k,(TextArea)(e.getSource())));
    	}
        naytaTiedot();
    }

    
    /**
     * Otetaan metodilla tekstikentän sisältö ja asetetaan se kurssikohtaisten tietojen vastaavalle attribuutille.
     * Palauttaa null jos onnistuu, näyttää virheen jos ei onnistu.
     * @param k monesko tekstikenttä yritetään viedä
     * @param edit tekstikenttä
     */
    private void kasitteleMuutosTietoihin(int k, TextArea edit) {
    	
    	String s = edit.getText();
    	String virhe = null;
    	
    	switch (k) {
    	case 1: virhe = kktieto.setPoissaolot(s); break;
    	case 2: virhe = kktieto.setArvosana(s); break;
    	case 3: virhe = kktieto.setMerkinnat(s); break;
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
     * Avataan ikkuna oppilaan kurssikohtaisten tietojen muokkaamista varten.
     * @param modalityStage null mikäli pääikkuna
     * @param kktieto Kurssikohtainen tieto jota muokataan
     * @return openapu johon muokattu tiedot
     */
    public static KurssikohtTieto kysyKurssikohtTieto(Stage modalityStage, KurssikohtTieto kktieto) {
        return ModalController.<KurssikohtTieto,OppilaanTiedotGUIController>showModal(
                OppilaanTiedotGUIController.class.getResource("OppilaanTiedotGUIView.fxml"),
                "Openapu",
                modalityStage, 
                kktieto, 
                null
            );
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
     * Näyttää oppilaan tiedot.
     * @param oppilas oppilas kohdalla
     * @param kurssi kurssi kohdalla
     */
    private void naytaTiedot() {
        edits[0].setText(kktieto.getPoissaolot() + "");
        edits[1].setText(kktieto.getArvosana() + "");
        edits[2].setText(kktieto.getMerkinnat() + "");
    }

    
    @Override
    public void setDefault(KurssikohtTieto kktieto) {
        this.kktieto = kktieto;
        alusta();
    }

    
    @Override
    public KurssikohtTieto getResult() {
        return vastaus;
    }
    
    
    @Override
    public void handleShown() {
        //
    }   
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //
    }

}

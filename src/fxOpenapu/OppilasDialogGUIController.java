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
import openapu.Oppilas;

/**
 * Käsittelijä uuden oppilaan luomisnäkymälle.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 7.2.2018
 *
 */
public class OppilasDialogGUIController implements ModalControllerInterface<Oppilas>,Initializable{
    
    @FXML private TextField editEtunimi;
    @FXML private TextField editSukunimi;
    @FXML private TextField editHetu;
    @FXML private Label labelVirhe;
    private Oppilas oppilasKohdalla;
    private Oppilas vastaus = null;
    
    private TextField[] edits;
    
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    
    /**
     * Alustaa tekstikentät ja näppäimen kuuntelijan.
     */
    private void alusta() {
        edits = new TextField[]{editEtunimi, editSukunimi, editHetu};
        int i = 0;
        for (TextField edit: edits) {
            final int k = ++i;
            edit.setOnKeyReleased(e -> kasitteleMuutosOppilaaseen(k,(TextField)(e.getSource())));
        }
    }

    /**
     * Otetaan metodilla tekstikentän sisältö ja asetetaan se oppilaan vastaavalle attribuutille.
     * Palauttaa null jos onnistuu, näyttää virheen jos ei onnistu.
     * @param k monesko tekstikenttä yritetään viedä
     * @param edit tekstikenttä
     */
    private void kasitteleMuutosOppilaaseen(int k, TextField edit) {
        if (oppilasKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
        case 1: virhe = oppilasKohdalla.setEtunimi(s); break;
        case 2: virhe = oppilasKohdalla.setSukunimi(s); break;
        case 3: virhe = oppilasKohdalla.setHetu(s); break;
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
     * Näyttää oppilaan tiedot ikkunassa.
     * @param modalityStage null, mikäli pääikkuna
     * @param oletus oletusoppilas, jonka tiedot näytetään
     * @return oppilas jonka tiedot näytetty
     */
    public static Oppilas kysyOppilas(Stage modalityStage, Oppilas oletus) {
        return ModalController.<Oppilas,OppilasDialogGUIController>showModal(
                OppilasDialogGUIController.class.getResource("OppilasDialogGUIView.fxml"),
                "Openapu",
                modalityStage, oletus, null 
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
     * Asettaa oppilaan tiedot dialogin kenttiin.
     * @param kentat tekstikentät, jotka asetetaan
     * @param oppilas jonka tiedot asetetaan kenttiin
     */
    protected static void naytaOppilas(TextField[] kentat, Oppilas oppilas) {
    	if (oppilas == null) return;
    	
    	kentat[0].setText(oppilas.getEtunimi());
    	kentat[1].setText(oppilas.getSukunimi());
    	kentat[2].setText(oppilas.getHetu());
    }

    
	/**
     * Käsittelee oppilaan tietojen muuttamisen.
     */
    @FXML private void handleOK() {
        if ( oppilasKohdalla != null && oppilasKohdalla.getEtunimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä!");
            return;
        }
        vastaus = oppilasKohdalla;
        ModalController.closeStage(labelVirhe);
    }
    
    /**
     * Käsittelee muutoksien peruuttamisen.
     */
    @FXML private void handlePeruuta() {
        ModalController.closeStage(labelVirhe);
    }

    
    @Override
    public Oppilas getResult() {
        return vastaus;
    }


    @Override
    public void setDefault(Oppilas oletus) {
        oppilasKohdalla = oletus;
        naytaOppilas(edits,oletus);
    }

    @Override
    public void handleShown() {
        //Ei käytössä.
    }


}

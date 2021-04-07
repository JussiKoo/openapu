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
 * K‰sittelij‰ uuden oppilaan luomisn‰kym‰lle.
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
     * Alustaa tekstikent‰t ja n‰pp‰imen kuuntelijan.
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
     * Otetaan metodilla tekstikent‰n sis‰ltˆ ja asetetaan se oppilaan vastaavalle attribuutille.
     * Palauttaa null jos onnistuu, n‰ytt‰‰ virheen jos ei onnistu.
     * @param k monesko tekstikentt‰ yritet‰‰n vied‰
     * @param edit tekstikentt‰
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
     * N‰ytt‰‰ oppilaan tiedot ikkunassa.
     * @param modalityStage null, mik‰li p‰‰ikkuna
     * @param oletus oletusoppilas, jonka tiedot n‰ytet‰‰n
     * @return oppilas jonka tiedot n‰ytetty
     */
    public static Oppilas kysyOppilas(Stage modalityStage, Oppilas oletus) {
        return ModalController.<Oppilas,OppilasDialogGUIController>showModal(
                OppilasDialogGUIController.class.getResource("OppilasDialogGUIView.fxml"),
                "Openapu",
                modalityStage, oletus, null 
            );
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
     * Asettaa oppilaan tiedot dialogin kenttiin.
     * @param kentat tekstikent‰t, jotka asetetaan
     * @param oppilas jonka tiedot asetetaan kenttiin
     */
    protected static void naytaOppilas(TextField[] kentat, Oppilas oppilas) {
    	if (oppilas == null) return;
    	
    	kentat[0].setText(oppilas.getEtunimi());
    	kentat[1].setText(oppilas.getSukunimi());
    	kentat[2].setText(oppilas.getHetu());
    }

    
	/**
     * K‰sittelee oppilaan tietojen muuttamisen.
     */
    @FXML private void handleOK() {
        if ( oppilasKohdalla != null && oppilasKohdalla.getEtunimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhj‰!");
            return;
        }
        vastaus = oppilasKohdalla;
        ModalController.closeStage(labelVirhe);
    }
    
    /**
     * K‰sittelee muutoksien peruuttamisen.
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
        //Ei k‰ytˆss‰.
    }


}

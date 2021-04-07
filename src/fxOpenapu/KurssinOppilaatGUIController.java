package fxOpenapu;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import openapu.Kurssi;
import openapu.Openapu;
import openapu.Oppilas;
import openapu.SailoException;

/**
 * K‰sittelij‰ oppilaiden hallinnoimiseen kurssin sis‰isesti. Ei viel‰ otettu k‰yttˆˆn.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 */
public class KurssinOppilaatGUIController implements ModalControllerInterface<Openapu>, Initializable {
    
	Kurssi kurssiKohdalla;
	Openapu openapu;
	Oppilas oppilasKaikista;
	Oppilas oppilasKurssilla;
	
	@FXML ListChooser<Oppilas> chooserKaikkioppilaat;
	@FXML ListChooser<Oppilas> chooserKurssinoppilaat;
	

	@Override
    public void initialize(URL url, ResourceBundle bundle) {
        //
    }

    
	/**
     * K‰sittelee oppilaan siirt‰misen kurssille.    
     */
    @FXML private void handleLisaaKurssille(){
        lisaaKurssille();
    }
    
    
    /**
     * Poistaa oppilan kurssilta poistamalla kurssikohtaiset tiedot.
     */
    @FXML private void handlePoistaKurssilta() {
        poistaKurssilta();
    }

    
    /**
     * K‰sittelee uuden oppilaan lis‰‰misen tietokantaan.
     */
    @FXML private void handleUusiOppilas() {
        ModalController.showModal(OpenapuGUIController.class.getResource("UusiOppilasGUIView.fxml"), "Oppilas", null, "");
    }
    
    /**
     * K‰sittelee tietojen tallentamisen varmistuksen.
     */
    @FXML private void handleOK() {
        ModalController.closeStage(chooserKaikkioppilaat);
    }
    
    /**
     * K‰sittelee muutosten peruuttamisen.
     */
    @FXML private void handlePeruuta() {
        openapu = null;
        ModalController.closeStage(chooserKaikkioppilaat);
    }

    
    // ========================== T‰st‰ eteenp‰in ei FXML -koodia ================================
   
    private void alusta(Kurssi kurssi) {
    	kurssiKohdalla = kurssi;
        chooserKaikkioppilaat.addSelectionListener(event -> oppilasKaikista = chooserKaikkioppilaat.getSelectedObject()); //Asetetaan kaikista valittu oppilas
        chooserKurssinoppilaat.addSelectionListener(event -> oppilasKurssilla = chooserKurssinoppilaat.getSelectedObject()); //Asetetaan kaikista valittu oppilas
    	haeKaikki(0);
    	haeKurssin(0);
	}
    

    /**
     * Hakee kaikki oppilaat omaan listaan.
     * @param onro joka aktivoidaan haun j‰lkeen
     */
    private void haeKaikki(int onro){
    	chooserKaikkioppilaat.clear();
        
    	int index = 0;
    	Collection<Oppilas> oppilaatColl;
    	try {
    		if (openapu == null) return;
	    	oppilaatColl = openapu.etsiOppilaat();
			int i = 0;
			for (Oppilas o : oppilaatColl) {
				if (o == null) continue;
			    if (o.getID() == onro) index = i;
			    chooserKaikkioppilaat.add(o.toString(), o);
			    i++;
			}
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Ongelma oppilaita haettaessa: " + ex.getMessage());
    	}
        chooserKaikkioppilaat.setSelectedIndex(index);
    }

    
    /**
     * Hakee kurssin oppilaat listaan.
     * @param onro joka aktivoidaan haun j‰lkeen
     */
    private void haeKurssin(int onro){
    	chooserKurssinoppilaat.clear();
        
    	int index = 0;
    	Collection<Oppilas> oppilaatColl;
    	if (openapu == null) return;
		oppilaatColl = openapu.annaOppilaat(kurssiKohdalla);
		int i = 0;
		for (Oppilas o : oppilaatColl) {
			if (o == null) continue;
		    if (o.getID() == onro) index = i;
		    chooserKurssinoppilaat.add(o.toString(), o);
		    i++;
		}
        chooserKurssinoppilaat.setSelectedIndex(index);
    }
    
    
    /**
     * Lis‰‰ valitun oppilaan kurssille.
     */
    private void lisaaKurssille() {
    	try {
			openapu.liitaKurssille(kurssiKohdalla, oppilasKaikista);
			haeKurssin(oppilasKaikista.getID());
		} catch (SailoException e) {
			//
		}
	}
    
    
    /**
     * Poistaa valitun oppilaan kurssilta
     */
    private void poistaKurssilta() {
        openapu.poistaKurssilta(kurssiKohdalla, oppilasKurssilla);
        haeKurssin(0);
    }
    
    
    /**
     * N‰ytt‰‰ ikkunan kurssin oppilastiedoista 
     * @param modalStage null, mik‰li p‰‰ikkuna
     * @param oletus openapu-olio, jonka oppilaita k‰ytet‰‰n
     * @param kurssi jonka oppilaat n‰ytet‰‰n
     * @return muokattu openapu-olio
     */
	public static Openapu kysyTiedot(Stage modalStage, Openapu oletus, Kurssi kurssi) {
        return ModalController.<Openapu,KurssinOppilaatGUIController>showModal(
                KurssinOppilaatGUIController.class.getResource("KurssinOppilaatGUIView.fxml"),
                "Openapu",
                modalStage, oletus, ctrl -> ctrl.alusta(kurssi) //Syˆtt‰‰ alustukselle kurssin, jonka tietoja k‰ytet‰‰n.
            );
	}
	
	
    @Override
    public Openapu getResult() {
        // TODO Auto-generated method stub
        return openapu;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(Openapu oletus) {
    	openapu = oletus;
    	haeKaikki(0);
		haeKurssin(0);
    }

}

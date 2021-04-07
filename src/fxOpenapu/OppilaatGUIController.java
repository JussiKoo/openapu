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
import openapu.Openapu;
import openapu.Oppilas;
import openapu.SailoException;

/**
 * K‰sittelee tietokannan oppilaiden n‰ytt‰misen.
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 19.4.2018
 */
public class OppilaatGUIController implements ModalControllerInterface<Openapu>,Initializable {
	
	@FXML ListChooser<Oppilas> chooserOppilaat;
	private Openapu openapu;
	private Oppilas oppilasKohdalla;
	
	
	@FXML private void handleLisaaOppilas(){
		lisaaOppilas();
	}
	
	
	@FXML private void handleMuokkaaOppilasta(){
		muokkaaOppilasta();
	}
	
	
	@FXML private void handlePoistaOppilas() {
	    poistaOppilas();
	}
	
	
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    
    // ==================== T‰st‰ eteenp‰in ei en‰‰ FXML koodia ==========================
    
    /**
     * Tekee tarvittavat alustukset.
     */
    public void alusta(){
        chooserOppilaat.addSelectionListener(event -> oppilasKohdalla = chooserOppilaat.getSelectedObject()); //Asetetaan valittu oppilas
    	hae(0);
    }
    
    
    /**
     * Hakee oppilaat listaan. Valitsee oppilaan id:n perusteella.
     * @param onro id, joka valitaan listasta.
     */
    public void hae(int onro){
    	chooserOppilaat.clear();
        
    	int index = 0;
    	Collection<Oppilas> oppilaatColl;
    	try {
    		if (openapu == null) return;
	    	oppilaatColl = openapu.etsiOppilaat();
			int i = 0;
			for (Oppilas o : oppilaatColl) {
				if (o == null) continue;
			    if (o.getID() == onro) index = i;
			    chooserOppilaat.add(o.toString(), o);
			    i++;
			}
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Ongelma oppilaita haettaessa: " + ex.getMessage());
    	}
        chooserOppilaat.setSelectedIndex(index);
    }
    
    
    /**
     * Lis‰‰ oppilaan tietorakenteeseen. 
     */
    public void lisaaOppilas(){
        Oppilas oppilas;
		Oppilas oletus = new Oppilas();
		oletus.rekisteroi();
		oppilas = OppilasDialogGUIController.kysyOppilas(null, oletus);
		if (oppilas == null) return;
		openapu.korvaaTaiLisaa(oppilas);
		hae(oppilas.getID());
    }
    
    
    /**
     * Muokkaa valitun oppilaan tietoja.
     */
    public void muokkaaOppilasta(){
        if (oppilasKohdalla == null) return;
        try {
            Oppilas oppilas;
            oppilas = OppilasDialogGUIController.kysyOppilas(null, oppilasKohdalla.clone());
            if (oppilas == null) return;
            openapu.korvaaTaiLisaa(oppilas);
            hae(oppilas.getID());
        } catch (CloneNotSupportedException e) {
            //
        }
    }
    
    
    /**
     * Poistaa oppilaan tietorakenteesta.
     */
    public void poistaOppilas() {
        if (oppilasKohdalla == null) return;
        boolean vastaus = Dialogs.showQuestionDialog("Oppilaan poisto", "Poistetaanko oppilas " + oppilasKohdalla.toString() + "?", "Poista", "Peruuta");
        if (vastaus) openapu.poista(oppilasKohdalla);
        hae(0);
    }
    
    
    /**
     * N‰ytet‰‰n oppilaat.
     * @param modalityStage null, mik‰li p‰‰ikkuna
     * @param oletus oletusopenapu, jonka tiedot n‰ytet‰‰n
     * @return openapu-olio, johon muutetut tiedot. null, mik‰li ei muuteta mit‰‰n.
     */
    public static Openapu kysyOppilaat(Stage modalityStage, Openapu oletus) {
        return ModalController.<Openapu,OppilaatGUIController>showModal(
                       OppilaatGUIController.class.getResource("OppilaatGUIView.fxml"),
                       "Openapu",
                       modalityStage, 
                       oletus,
                       null 
                   );
    }

	@Override
	public Openapu getResult() {
		return openapu;
	}

	@Override
	public void handleShown() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setDefault(Openapu oletus) {
		openapu = oletus;
		hae(0);
	}
}

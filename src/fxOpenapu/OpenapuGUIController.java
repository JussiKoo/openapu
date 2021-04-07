package fxOpenapu;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import openapu.Kurssi;
import openapu.KurssikohtTieto;
import openapu.Openapu;
import openapu.Oppilas;
import openapu.SailoException;


/**
 * Tapahtumank�sittelij� k�ytt�liittym�lle.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 */
public class OpenapuGUIController implements Initializable {
    
    @FXML private ListChooser<Kurssi> chooserKurssit;
    @FXML private ListChooser<Oppilas> chooserOppilaat;
    @FXML private ScrollPane panelKurssi;
    @FXML private Label labelVirhe;
    
	@FXML private TextField editOppiaine;
	@FXML private TextField editKurssikoodi;
	@FXML private TextField editNimi;
	@FXML private TextField editAloitusPvm;
	@FXML private TextField editLopetusPvm;
	
	@FXML private TextArea editPoissaolot;
	@FXML private TextArea editKoetulokset;
	@FXML private TextArea editMuutMerkinnat;
	
	@FXML private TextField searchKurssit;
	
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    
    /**
     * K�sittelee uuden kurssin lis��misen.
     */
    @FXML private void handleUusiKurssi() {
        uusiKurssi();
    }
    
    
    /**
     * K�sittelee olemassaolevan kurssin muokkaamisen.
     */
    @FXML private void handleMuokkaaKurssia() {
        muokkaaKurssia();
    }
    
    
    /**
     * K�sittelee olemassaolevan kurssin poistamisen tietokannasta.
     */
    @FXML private void handlePoistaKurssi() {
        poistaKurssi();
    }
    
    
    /**
     * Avaa dialogin, jossa kaikki oppilaat. Uuden lis��minen tapahtuu t�m�n dialogin kautta.
     */
    @FXML private void handleUusiOppilas() {   
        naytaOppilaat();
    }
    
    /**
     * Avaa dialogin, jonka kautta muokkaaminen tapahtuu.
     */
    @FXML private void handleMuokkaaOppilasta() {   
        naytaOppilaat();
    }
    

    /**
     * K�sittelee olemassaolevan oppilaan tietojen poistamisen. Todellisuudessa avaa dialogin jossa kaikki oppilaat, jonka kautta poistaminen tapahtuu.
     */
    @FXML private void handlePoistaOppilas() {
        naytaOppilaat();
    }
    
    
    /**
     * K�sittelee kurssin oppilaiden hallinnointin�kym�n avaamisen.
     */
    @FXML private void handleKurssinOppilaat() {
        kurssinOppilaat();
    }
        

	/**
     * K�sittelee oppilaan tieton�kym�n avaamisen.
     */
    @FXML private void handleOppilaanTiedot() {
        oppilaanTiedot();
    }
    

    /**
     * K�sittelee dialogin, jossa n�kyy kaikki oppilaat.
     */
    @FXML private void handleNaytaOppilaat(){
    	naytaOppilaat();
    }
    

	/**
     * K�sittelee muokattujen tietojen tallentamisen.
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    

    /**
     * K�sittelee tiedoston avaamisen.
     */
    @FXML private void handleAvaa() {
        avaa();
    }
    
    
    /**
     * K�sittelee avustuksen n�ytt�misen.
     */
    @FXML private void handleApua() {
        avustus();
    }
    
   
    @FXML private void handleLopeta() {
        if ( !this.voikoSulkea() ) {
            boolean vastaus = Dialogs.showQuestionDialog("Sulkeminen",
                       "Tietoja ei tallennettu. Tallennetaanko ennen sulkemista?", "Kyll�", "Ei");
            if (vastaus) {this.tallenna();}
        }
        ModalController.closeStage(chooserKurssit);
    }
    
    
    /**
     * K�sittelee tietojen n�ytt�misen.
     */
    @FXML private void handleTietoja() {
        ModalController.showModal(OpenapuGUIController.class.getResource("AboutView.fxml"), "Openapu", null, "");
    }
    
    /* ======== T�st� eteenp�in ei k�ytt�liittym��n liittyv�� koodia. */
    
    private Openapu openapu = new Openapu();
    private Kurssi kurssiKohdalla;
    private Oppilas oppilasKohdalla;
    
    private String kurssit = "kurssit";
    private String oppilaat = "oppilaat";
    private String kktiedot = "kurssikohttiedot";
    
    private TextField[] edits;
    
    
    /**
     * Tekee tarvittavat alustukset.
     */
    protected void alusta() {       
        chooserKurssit.clear();
        chooserKurssit.addSelectionListener(event -> naytaKurssi());
        chooserOppilaat.clear();
        chooserOppilaat.addSelectionListener(event -> naytaKurssikohtTiedot());
        searchKurssit.setOnKeyReleased(event -> haeKurssit(0 ,searchKurssit.getText()));
        edits = new TextField[]{editNimi, editKurssikoodi, editOppiaine, editAloitusPvm, editLopetusPvm};
    }
    
    
    /**
     * Alustaa kurssit, oppilaat ja kurssikotaiset tiedot tiedostoista.
     * @param kurssitTied kurssien tietojen tiedostn nimi
     * @param oppilaatTied oppilaiden tiedoston nimi
     * @param kkTied kurssikohtaisten tietojen tiedoston nimi
     * @return null jos lukeminen onnistuu, muuten virhe tekstin�
     */
    protected String lueTiedosto(String kurssitTied, String oppilaatTied, String kkTied) {
    	kurssit = kurssitTied;
    	oppilaat = oppilaatTied;
    	kktiedot = kkTied;
    	
    	
    	try {
    		openapu.lueTiedostosta(kurssit, oppilaat, kktiedot);
    		haeKurssit(0, "");
    		return null;
    	} catch (SailoException ex) {
    		haeKurssit(0, "");
    		String virhe = ex.getMessage();
    		if (virhe != null) Dialogs.showMessageDialog(virhe);
    		return virhe;
    	}
    }
    
    
    /**
     * Tallentaa muutetut tiedot.
     * @return virhe, mik�li ep�onnistuu
     */
    protected String tallenna() {
    	try {
    		openapu.tallenna();
    		return null;
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Tallentaminen ep�onnistui: " + ex.getMessage());
    		return ex.getMessage();
    	}
    }
    
    
    /**
     * Tulostaa valinnan kohdalla olevan kurssin tiedot tekstikenttiin, jos onnistuu.
     */
    protected void naytaKurssi() {
        kurssiKohdalla = chooserKurssit.getSelectedObject();
        
        if (kurssiKohdalla == null) return;
        
        KurssiDialogGUIController.naytaKurssi(edits, kurssiKohdalla);

        chooserOppilaat.clear();
        int index2 = 0;
        Collection<Oppilas> oppilaatColl;
        oppilaatColl = openapu.annaOppilaat(kurssiKohdalla);
        int i = 0;
        for (Oppilas oppilas: oppilaatColl) {
            if (oppilas == null) continue;
            if (oppilas.getID() == 0) index2 = i;
            chooserOppilaat.add(oppilas.toString(), oppilas);
            i++;
        }
        
        chooserOppilaat.setSelectedIndex(index2);
    }
 
    
    /**
     * N�ytt�� oppilaan kurssikohtaiset tiedot kentiss�.
     */
    protected void naytaKurssikohtTiedot() {
    	kurssiKohdalla = chooserKurssit.getSelectedObject();
    	oppilasKohdalla = chooserOppilaat.getSelectedObject();
    	
    	if (kurssiKohdalla == null || oppilasKohdalla == null) return;
    	
    	KurssikohtTieto kktieto = openapu.haeKktieto(kurssiKohdalla.getID(), oppilasKohdalla.getID());
    	
    	editPoissaolot.setText(""+kktieto.getPoissaolot());
    	editKoetulokset.setText(""+kktieto.getArvosana());
    	editMuutMerkinnat.setText(kktieto.getMerkinnat());
    }
    
    
    /**
     * Avaa dialogin uuden kurssin tietojen sy�tt�mist� varten.
     */
    protected void uusiKurssi() {
        Kurssi kurssi;
        Kurssi oletus = new Kurssi(); // Tyhjill� tiedoilla.
        kurssi = KurssiDialogGUIController.kysyKurssi(null, oletus.clone());
        if (kurssi == null) return;
        kurssi.rekisteroi();
        openapu.korvaaTaiLisaa(kurssi);
        haeKurssit(kurssi.getID(), "");
    }
    
    
    /**
     * K�sittelee kurssin tietojen muokkaamisen ja tallentamisen.
     */
    public void muokkaaKurssia() {
    	if (kurssiKohdalla == null) return;
    	Kurssi kurssi;
        kurssi = KurssiDialogGUIController.kysyKurssi(null, kurssiKohdalla.clone());
        if (kurssi == null) return;
        openapu.korvaaTaiLisaa(kurssi);
        haeKurssit(kurssi.getID(), "");
    }
    
    
    /**
     * K�sittelee kurssin oppilaiden hallinnoimisdialogin.
     */
    private void kurssinOppilaat() {
    	Openapu uusi = null;
		if (kurssiKohdalla != null) uusi = KurssinOppilaatGUIController.kysyTiedot(null, openapu, kurssiKohdalla.clone());
        if (uusi == null) return;
        setOpenapu(uusi);
        naytaKurssi();
	}
    
    
    /**
     * N�ytt�� tietorakenteen oppilaat.
     */
    private void naytaOppilaat() {
    	Openapu uusi = OppilaatGUIController.kysyOppilaat(null, openapu);
    	if (uusi == null) return;
    	setOpenapu(uusi);
	}

    
    
    /**
     * P�ivitt�� kurssien tiedot listaan. Valitsee listasta kurssin ID:n perusteella. Oletuksena valitaan ensimm�inen. K�ytt�� hakukentt�� avukseen kurssien haussa.
     * @param knro kurssin numero, joka aktivoidaan
     * @param hakuehto nimi jota etsit��n
     */
    protected void haeKurssit(int knro, String hakuehto) {
    	chooserKurssit.clear();
    	String ehto = "";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
    	int index = 0;
    	Collection<Kurssi> kurssitColl;
    	try {
	    	kurssitColl = openapu.etsiKurssit(ehto);
			int i = 0;
			for (Kurssi kurssi: kurssitColl) {
				if (kurssi == null) continue;
			    if (kurssi.getID() == knro) index = i;
			    chooserKurssit.add(kurssi.getNimi(), kurssi);
			    i++;
			}
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Ongelma kursseja haettaessa: " + ex.getMessage());
    	}
        chooserKurssit.setSelectedIndex(index);
        kurssiKohdalla = chooserKurssit.getSelectedObject();
    }
   
    
    /**
     * Poistaa valitun kurssin listasta ja tietokannasta.
     */
    protected void poistaKurssi() {
    	if (kurssiKohdalla == null) return;
    	if (!Dialogs.showQuestionDialog("Vahvistus", "Haluatko varmasti poistaa kurssin " + kurssiKohdalla.getNimi() + "?", "Kyll�", "Ei"))
        return;
        openapu.poista(kurssiKohdalla);
        kurssiKohdalla = null;
        int index = chooserKurssit.getSelectedIndex();
        chooserOppilaat.clear();
        haeKurssit(0, null);
        chooserKurssit.setSelectedIndex(index);
        
    }
    
    
    /**
     * Asettaa openapu olion jota k�ytet��n t�ss�.
     * @param openapu Openapu jota t�ss� k�ytet��n.
     */
    public void setOpenapu(Openapu openapu) {
        this.openapu = openapu;
    }
    
    
    /**
     * Lis�� oppilaan k�ytt�liittym��n. Toistaiseksi valmiit arvotut tiedot.
     */
    protected void lisaaOppilas() {

        if (kurssiKohdalla == null) return;
        Oppilas oppilas = new Oppilas();
        oppilas.rekisteroi();
        oppilas.taytaOlliOppilas();
        try {
            openapu.lisaa(oppilas);
            openapu.liitaKurssille(kurssiKohdalla, oppilas);
            naytaKurssi();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Aliohjelma tiedostojen avaamiselle.
     * @return true jos onnistuu, false jos ei
     */
    public boolean avaa() {
        String uusikurssit = kurssit;
        String uusioppilaat = oppilaat;
        String uusikktiedot = kktiedot;
        if (uusikurssit == null || uusioppilaat == null || uusikktiedot == null) return false;
        lueTiedosto(uusikurssit, uusioppilaat, uusikktiedot);
        return true;
    }
    
    
    /**
     * N�ytet��n ohjelman suunnitelma ja k�ytt�ohjeet selaimessa.
     */
    private void avustus() {
        Desktop dt = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2018k/ht/openapu");
            dt.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
    
    
    /**
     * N�ytet��n oppilaan kurssikohtaiset tiedot.
     */
    private void oppilaanTiedot(){
        KurssikohtTieto kktieto = null;
        if ( oppilasKohdalla == null || kurssiKohdalla == null ) return;
        kktieto = OppilaanTiedotGUIController.kysyKurssikohtTieto(null, openapu.haeKktieto(kurssiKohdalla.getID(), oppilasKohdalla.getID()).clone());
        if (kktieto == null) return;
        openapu.korvaaTaiLisaa(kktieto);
        haeKurssit(kurssiKohdalla.getID(), null);
    }

    
    /**
     * Tutkii, saako sulkea, eli ett� onko tallennettu.
     * @return true mik�li tiedot tallennettu, false mik�li ei tallennettu
     */
    public boolean voikoSulkea() {
        return !openapu.onkoMuutettu();
    }
}

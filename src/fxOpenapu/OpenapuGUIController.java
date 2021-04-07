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
 * Tapahtumankäsittelijä käyttöliittymälle.
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
     * Käsittelee uuden kurssin lisäämisen.
     */
    @FXML private void handleUusiKurssi() {
        uusiKurssi();
    }
    
    
    /**
     * Käsittelee olemassaolevan kurssin muokkaamisen.
     */
    @FXML private void handleMuokkaaKurssia() {
        muokkaaKurssia();
    }
    
    
    /**
     * Käsittelee olemassaolevan kurssin poistamisen tietokannasta.
     */
    @FXML private void handlePoistaKurssi() {
        poistaKurssi();
    }
    
    
    /**
     * Avaa dialogin, jossa kaikki oppilaat. Uuden lisääminen tapahtuu tämän dialogin kautta.
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
     * Käsittelee olemassaolevan oppilaan tietojen poistamisen. Todellisuudessa avaa dialogin jossa kaikki oppilaat, jonka kautta poistaminen tapahtuu.
     */
    @FXML private void handlePoistaOppilas() {
        naytaOppilaat();
    }
    
    
    /**
     * Käsittelee kurssin oppilaiden hallinnointinäkymän avaamisen.
     */
    @FXML private void handleKurssinOppilaat() {
        kurssinOppilaat();
    }
        

	/**
     * Käsittelee oppilaan tietonäkymän avaamisen.
     */
    @FXML private void handleOppilaanTiedot() {
        oppilaanTiedot();
    }
    

    /**
     * Käsittelee dialogin, jossa näkyy kaikki oppilaat.
     */
    @FXML private void handleNaytaOppilaat(){
    	naytaOppilaat();
    }
    

	/**
     * Käsittelee muokattujen tietojen tallentamisen.
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    

    /**
     * Käsittelee tiedoston avaamisen.
     */
    @FXML private void handleAvaa() {
        avaa();
    }
    
    
    /**
     * Käsittelee avustuksen näyttämisen.
     */
    @FXML private void handleApua() {
        avustus();
    }
    
   
    @FXML private void handleLopeta() {
        if ( !this.voikoSulkea() ) {
            boolean vastaus = Dialogs.showQuestionDialog("Sulkeminen",
                       "Tietoja ei tallennettu. Tallennetaanko ennen sulkemista?", "Kyllä", "Ei");
            if (vastaus) {this.tallenna();}
        }
        ModalController.closeStage(chooserKurssit);
    }
    
    
    /**
     * Käsittelee tietojen näyttämisen.
     */
    @FXML private void handleTietoja() {
        ModalController.showModal(OpenapuGUIController.class.getResource("AboutView.fxml"), "Openapu", null, "");
    }
    
    /* ======== Tästä eteenpäin ei käyttöliittymään liittyvää koodia. */
    
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
     * @return null jos lukeminen onnistuu, muuten virhe tekstinä
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
     * @return virhe, mikäli epäonnistuu
     */
    protected String tallenna() {
    	try {
    		openapu.tallenna();
    		return null;
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Tallentaminen epäonnistui: " + ex.getMessage());
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
     * Näyttää oppilaan kurssikohtaiset tiedot kentissä.
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
     * Avaa dialogin uuden kurssin tietojen syöttämistä varten.
     */
    protected void uusiKurssi() {
        Kurssi kurssi;
        Kurssi oletus = new Kurssi(); // Tyhjillä tiedoilla.
        kurssi = KurssiDialogGUIController.kysyKurssi(null, oletus.clone());
        if (kurssi == null) return;
        kurssi.rekisteroi();
        openapu.korvaaTaiLisaa(kurssi);
        haeKurssit(kurssi.getID(), "");
    }
    
    
    /**
     * Käsittelee kurssin tietojen muokkaamisen ja tallentamisen.
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
     * Käsittelee kurssin oppilaiden hallinnoimisdialogin.
     */
    private void kurssinOppilaat() {
    	Openapu uusi;
		uusi = KurssinOppilaatGUIController.kysyTiedot(null, openapu, kurssiKohdalla.clone());
        if (uusi == null) return;
        setOpenapu(uusi);
        naytaKurssi();
	}
    
    
    /**
     * Näyttää tietorakenteen oppilaat.
     */
    private void naytaOppilaat() {
    	Openapu uusi = OppilaatGUIController.kysyOppilaat(null, openapu);
    	if (uusi == null) return;
    	setOpenapu(uusi);
	}

    
    
    /**
     * Päivittää kurssien tiedot listaan. Valitsee listasta kurssin ID:n perusteella. Oletuksena valitaan ensimmäinen. Käyttää hakukenttää avukseen kurssien haussa.
     * @param knro kurssin numero, joka aktivoidaan
     * @param hakuehto nimi jota etsitään
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
    }
   
    
    /**
     * Poistaa valitun kurssin listasta ja tietokannasta.
     */
    protected void poistaKurssi() {
    	if (kurssiKohdalla == null) return;
    	if (!Dialogs.showQuestionDialog("Vahvistus", "Haluatko varmasti poistaa kurssin " + kurssiKohdalla.getNimi() + "?", "Kyllä", "Ei"))
        return;
        openapu.poista(kurssiKohdalla);
        int index = chooserKurssit.getSelectedIndex();
        chooserOppilaat.clear();
        haeKurssit(0, null);
        chooserKurssit.setSelectedIndex(index);
        
    }
    
    
    /**
     * Asettaa openapu olion jota käytetään tässä.
     * @param openapu Openapu jota tässä käytetään.
     */
    public void setOpenapu(Openapu openapu) {
        this.openapu = openapu;
    }
    
    
    /**
     * Lisää oppilaan käyttöliittymään. Toistaiseksi valmiit arvotut tiedot.
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
     * Näytetään ohjelman suunnitelma ja käyttöohjeet selaimessa.
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
     * Näytetään oppilaan kurssikohtaiset tiedot.
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
     * Tutkii, saako sulkea, eli että onko tallennettu.
     * @return true mikäli tiedot tallennettu, false mikäli ei tallennettu
     */
    public boolean voikoSulkea() {
        return !openapu.onkoMuutettu();
    }
}

package openapu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Openapu-luokka, joka huolehtii kursseista ja oppilaista. P‰‰osin kaikki metodit v‰litt‰j‰metodeja.
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @version 20.4.2018
 */
public class Openapu {
    private Kurssit kurssit = new Kurssit();
    private Oppilaat oppilaat = new Oppilaat();
    private KurssikohtTiedot kktiedot = new KurssikohtTiedot();
    
    
    /**
     * Palauttaa kurssien lukum‰‰r‰n
     * @return kurssien lkm
     */
    public int getKursseja() {
        return kurssit.getLkm();
    }
    
    
    /**
     * Palauttaa oppilaiden lukum‰‰r‰n.
     * @return oppilaiden lkm
     */
    public int getOppilaita() {
        return oppilaat.getLkm();
    }
    
    
    /**
     * Lis‰‰ ohjelmaan uuden kurssin.
     * @param kurssi lis‰tt‰v‰ kurssi
     * @throws SailoException jos lis‰yst‰ ei voida tehd‰
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Openapu openapu = new Openapu();
     * Kurssi kem1 = new Kurssi(), kem2 = new Kurssi();
     * kem1.rekisteroi(); kem2.rekisteroi();
     * openapu.getKursseja() === 0;
     * openapu.lisaa(kem1); openapu.getKursseja() === 1;
     * openapu.lisaa(kem2); openapu.getKursseja() === 2;
     * openapu.lisaa(kem1); openapu.getKursseja() === 3;
     * openapu.getOppilaita() === 0;
     * openapu.annaKurssi(0) === null;
     * openapu.annaKurssi(4) === kem1; //muita kursseja testeiss‰, 4 seuraava
     * openapu.annaKurssi(5) === kem2;
     * openapu.annaKurssi(2) === null;
     * openapu.lisaa(kem1);
     * openapu.lisaa(kem1); openapu.getKursseja() === 5;
     * openapu.lisaa(kem1);
     * </pre>
     */
    public void lisaa(Kurssi kurssi) throws SailoException {
        kurssit.lisaa(kurssi);
    }
    
    
    /**
     * Lis‰‰ ophjelmaan uuden oppilaan.
     * @param oppilas lis‰tt‰v‰ oppilas
     * @throws SailoException jos lis‰iyst‰ ei voida tehd‰
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Openapu openapu = new Openapu();
     * Oppilas olli1 = new Oppilas(), olli2 = new Oppilas();
     * olli1.rekisteroi(); olli2.rekisteroi();
     * openapu.getOppilaita() === 0;
     * openapu.lisaa(olli1); openapu.getOppilaita() === 1;
     * openapu.lisaa(olli2); openapu.getOppilaita() === 2;
     * openapu.lisaa(olli1); openapu.getOppilaita() === 3;
     * openapu.getKursseja() === 0;
     * openapu.annaOppilasNro(0) === olli1;
     * openapu.annaOppilasNro(1) === olli2;
     * openapu.annaOppilasNro(2) === olli1;
     * openapu.annaOppilasNro(4) === null;
     * openapu.lisaa(olli1);
     * openapu.lisaa(olli1); openapu.getOppilaita() === 5;
     * </pre>
     */ 
    public void lisaa(Oppilas oppilas) throws SailoException {
        oppilaat.lisaa(oppilas);
    }
    
    
    /**
     * Poistaa oppilaan tietorakenteesta. 
     * @param oppilas joka poistetaan
     */
    public void poista(Oppilas oppilas) {
        oppilaat.poista(oppilas.getID());
    }
    
    
    /**
     * Lukee tiedostoista tiedot.
     * @param kurssitTied kurssitiedoston nimi
     * @param oppilaatTied oppilastiedoston nimi
     * @param kkTied kurssikohtaisten tietojen tiedoston nimi
     * @throws SailoException mik‰li ei onnistu
     */
    public void lueTiedostosta(String kurssitTied, String oppilaatTied, String kkTied) throws SailoException {
        kurssit.lueTiedostosta(kurssitTied);
        oppilaat.lueTiedostosta(oppilaatTied);
        kktiedot.lueTiedostosta(kkTied);    		
    }
    
    
    /**
     * Palauttaa kurssin jonka ID on i.
     * @param i kurssin indeksi kurssit-luokassa
     * @return i:s kurssi
     */
    public Kurssi annaKurssi(int i) {
        return kurssit.anna(i);
    }
    
    
    /**
     * Palauttaa oppilaan, jonka id on i;
     * @param id oppilaan ID
     * @return oppilas jonka ID on id
     */
    public Oppilas annaOppilas(int id) {
        return oppilaat.anna(id);
    }
    
    
    /**
     * Etsii tietyt kurssit kurssit-oliosta
     * @param hakuehto nimi, jota etsit‰‰n
     * @return lista kursseista
     * @throws SailoException jos ep‰onnistuu
     */
    public Collection<Kurssi> etsiKurssit(String hakuehto) throws SailoException {
    	return kurssit.etsi(hakuehto);
    }
    
    /**
     * Hakee oppilaat listaan.
     * @return lista oppilaista
     * @throws SailoException jos ep‰onnistuu
     */
    public Collection<Oppilas> etsiOppilaat() throws SailoException {
        return oppilaat.etsi();
    }
    
    
    /**
     * Palauttaa alkiot taulukosta i:nnen oppilaan. 0 on ensimm‰inen.
     * @param i lis‰ysj‰rjestysnumero
     * @return i:s oppilas lis‰ysj‰rjestyksess‰
     */
    public Oppilas annaOppilasNro(int i) {
        return oppilaat.annaAlkio(i);
    }
    
    
    /**
     * Tallettaa kurssien tiedot ja oppilaiden tiedot.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
    	try {
        	oppilaat.tallenna();
        } catch (Exception e) { virhe += e.getMessage();}
    	try {
    		kurssit.tallenna();
    	} catch (Exception e) { virhe += e.getMessage();}
    	try {
    		kktiedot.tallenna();
    	} catch (Exception e) { virhe += e.getMessage();}
    	
    	if (!virhe.equals("")) throw new SailoException("Tallentaminen ep‰onnistui: " + virhe);
    }
    
    
    /**
     * Tutkii, onko kurssilla oppilas vai ei.
     * @param kurssi kurssi, jolta etsit‰‰n
     * @param oppilas oppilas, jota etsit‰‰n
     * @return onko kurssilla oppilas vai ei
     */
    public boolean onkoKurssilla(Kurssi kurssi, Oppilas oppilas) {
        return kktiedot.onkoKurssilla(kurssi.getID(), oppilas.getID());
    }
    
    
    /**
     * Lis‰‰ oppilaan kurssille luomalla uuden kurssikohtainen-olion. Ei lis‰‰, mik‰li oppilas on jo kurssilla.
     * @param oppilas oppilas joka lis‰t‰‰n
     * @param kurssi kurssi jolle oppilas lis‰t‰‰n
     * @throws SailoException mik‰li lis‰‰minen ei onnistu
     */
    public void liitaKurssille(Kurssi kurssi, Oppilas oppilas) throws SailoException {
    	if (onkoKurssilla(kurssi,oppilas)) return;
        KurssikohtTieto kktieto = new KurssikohtTieto(kurssi.getID(), oppilas.getID());
        kktiedot.lisaa(kktieto);
    }
    
    
    /**
     * Poistaa oppilaan kurssilta poistamalla kurssikohtainen-olion.
     * @param kurssi jolta oppilas poistetaan
     * @param oppilas joka poistetaan
     */
    public void poistaKurssilta(Kurssi kurssi, Oppilas oppilas) {
        kktiedot.poista(kurssi.getID(), oppilas.getID());
    }
    
    
    /**
     * Poistaa kurssin tietoj‰rjestelm‰st‰
     * @param kurssi poistettavan kurssin viite.
     */
    public void poista(Kurssi kurssi) {
    	kurssit.poista(kurssi.getID());
    }
    
    
    /**
     * Palauttaa listana kurssin oppilaiden ID:t.
     * @param kurssiID kurssin ID
     * @return lista oppilaiden ID:st‰
     */
    public List<Integer> annaOppilasIDTiedot(int kurssiID) {
        return kktiedot.annaOppilasIDTiedot(kurssiID);
    }
    
    
    /**
     * Palauttaa listan oppilaista, jotka ovat kurssilla.
     * @param kurssi jolta halutaan oppilaat
     * @return lista oppilaista, jotka kurssilla
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.List;
     * #import java.util.ArrayList;
     * Openapu openapu2 = new Openapu();
     * Oppilas oskari1 = new Oppilas(); oskari1.rekisteroi();
     * Oppilas oskari2 = new Oppilas(); oskari2.rekisteroi();
     * Oppilas oskari3 = new Oppilas(); oskari3.rekisteroi();
     * Kurssi mat1 = new Kurssi(); mat1.rekisteroi();
     * Kurssi mat2 = new Kurssi(); mat2.rekisteroi();
     * Kurssi mat3 = new Kurssi(); mat3.rekisteroi();
     * 
     * openapu2.lisaa(oskari1);openapu2.lisaa(oskari2);openapu2.lisaa(oskari3);
     * openapu2.lisaa(mat1); openapu2.lisaa(mat2); openapu2.lisaa(mat3);
     * openapu2.liitaKurssille(mat1, oskari2);
     * openapu2.liitaKurssille(mat1, oskari3);
     * openapu2.liitaKurssille(mat3, oskari2);
     * 
     * List<Oppilas> oppilaatLista = openapu2.annaOppilaat(mat1);
     * 
     * oppilaatLista.contains(oskari2) === true;
     * oppilaatLista.contains(oskari3) === true;
     * oppilaatLista.contains(oskari1) === false;
     * 
     * oppilaatLista = openapu2.annaOppilaat(mat3);
     * 
     * oppilaatLista.contains(oskari2) === true;
     * oppilaatLista.contains(oskari3) === false;
     * </pre>
     */
    public List<Oppilas> annaOppilaat(Kurssi kurssi){
        List<Oppilas> oppilaatList = new ArrayList<Oppilas>();
    	if (kurssi == null) return oppilaatList;
        List<Integer> oppilaatID = kktiedot.annaOppilasIDTiedot(kurssi.getID());
        
        for (Integer id : oppilaatID) {
            oppilaatList.add(this.annaOppilas(id));
        }
        return oppilaatList;
    }
    
    
    /**
     * Muuttaa kurssin tietoja tietorakenteessa.
     * Etsit‰‰n kurssi vastaavalla ID:ll‰, jos ei lˆydy niin lis‰t‰‰n uutena.
     * @param kurssi kurssin viite, joka halutaan korvata. Tietorakenne muuttuu omistajaksi
     */
    public void korvaaTaiLisaa(Kurssi kurssi) {
    	kurssit.korvaaTaiLisaa(kurssi);
    }
    
    
    /**
     * Muuttaa oppilaan tietoja tietorakenteessa.
     * Etsit‰‰n kurssi vastaavalla ID:ll‰, jos ei lˆydy niin lis‰t‰‰n uutena.
     * @param oppilas joka korvataan tai lis‰t‰‰n.
     */
    public void korvaaTaiLisaa(Oppilas oppilas) {
        oppilaat.korvaaTaiLisaa(oppilas);
    }
    
    
    /**
     * Muuttaa kurssikohtaista tietoa tietorakenteessa.
     * @param kktieto joka korvataan tai lis‰t‰‰n
     */
    public void korvaaTaiLisaa(KurssikohtTieto kktieto) {
        kktiedot.korvaaTaiLisaa(kktieto);
    }
    
    
    /**
     * Hakee tietty‰ Kurssi ID:t‰ ja oppilas ID:t‰ vastaavan kursskohtaisen tiedon
     * @param KID kurssin id
     * @param OID oppilaan id
     * @return kurssikohtainen tieto
     */
    public KurssikohtTieto haeKktieto(int KID, int OID) {
    	return kktiedot.haeKktieto(KID, OID);
    }

    
    /**
     * Palauttaa tiedon, onko tietorakennetta muutettu.
     * @return true, mik‰li muutettu ja false mik‰li ei muutettu
     */
    public boolean onkoMuutettu() {
        return oppilaat.onkoMuutettu() || kurssit.onkoMuutettu() || kktiedot.onkoMuutettu();
    }

        
    /**
     * Testip‰‰ohjelma
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Openapu openapu = new Openapu();
        
        Oppilas olli = new Oppilas(), olli2 = new Oppilas();
        olli.parse("0|Olli|Oppilas|050595");
        olli.rekisteroi();
        olli2.parse("0|Kalle|Oppilas|190585");
        olli2.rekisteroi();
        
        Kurssi kurssi1 = new Kurssi(), kurssi2 = new Kurssi();         
        kurssi1.rekisteroi();
        kurssi1.parse("1|Fysiikka|FYSA200|Fysiikan perusteet|02.02.1995|20.12.2018");
        kurssi2.rekisteroi();
        kurssi2.taytaTiedotMata200();
        
        try {                     
            openapu.lisaa(olli);
            openapu.lisaa(olli2);
            openapu.lisaa(kurssi1);
            openapu.lisaa(kurssi2);
            
            openapu.liitaKurssille(kurssi1,olli);
            openapu.liitaKurssille(kurssi1,olli2);
            openapu.liitaKurssille(kurssi2,olli);
            System.out.println("==================== Openapu-testi ====================");
            
            System.out.println("==========================");            
            System.out.println("Kurssilla " + kurssi1.getNimi() + " on oppilaat " + openapu.annaOppilaat(kurssi1));
            System.out.println("Kurssilla " + kurssi2.getNimi() + " on oppilaat " + openapu.annaOppilaat(kurssi2));
            
            System.out.println("==========================");
            System.out.println("Kurssilla " +  kurssi1.getNimi() + " on oppilas " + olli + " : " + openapu.onkoKurssilla(kurssi1, olli) );
            System.out.println("Kurssilla " +  kurssi2.getNimi() + " on oppilas " + olli2 + " : " + openapu.onkoKurssilla(kurssi2, olli2) );
            
         } catch (SailoException e) {
           System.out.println(e.getMessage());
        }
   }

}

package openapu;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Pvm;
import kanta.Random;

/**
 * Luokka kurssille.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 *
 */
public class Kurssi implements Cloneable{
    private int     kurssiID;
    private String  oppiaine = "";
    private String  kurssiKoodi = "";
    private String  nimi = "";
    
    private Pvm     aloitusPvm = new Pvm();
    private Pvm     lopetusPvm = new Pvm();
    
    private static int seuraavaKurssiID = 1; // Kaikille Kursseille sama. Mahdollistaa kurssin rekisteröimisen tietokantaan, ks rekisteroi().

    
    /**
     * Muodostaja kurssille, kun päivämäärät syötetään merkkijonona. Mikäli ongelmia päivämäärän syötössä, asetetaan nykypäivämäärä.
     * @param oppiAine oppiaine
     * @param kurssiKoodi kurssikoodi
     * @param nimi kurssin nimi
     * @param aloitusPvm kurssin aloituspäivämäärä
     * @param lopetusPvm kurssin lopetuspäivämäärä
     */
    public Kurssi(String oppiAine, String kurssiKoodi, String nimi, String aloitusPvm, String lopetusPvm) {
        this.oppiaine = oppiAine;
        this.kurssiKoodi = kurssiKoodi;
        this.nimi = nimi;
        
        this.aloitusPvm.parse(aloitusPvm);
        this.lopetusPvm.parse(lopetusPvm);
    }
    
    
    /**
     * Muodostaja kurssille, kun päivämäärät syötetään Pvm-olioina. Päivämäärät oletuksena nykyinen päivä.
     * @param oppiAine oppiaine
     * @param kurssiKoodi kurssikoodi
     * @param nimi kurssin nimi
     * @param aloitusPvm kurssin aloituspäivämäärä
     * @param lopetusPvm kurssin lopetuspäivämäärä
     */
    public Kurssi(String oppiAine, String kurssiKoodi, String nimi, Pvm aloitusPvm, Pvm lopetusPvm) {
        this.oppiaine = oppiAine;
        this.kurssiKoodi = kurssiKoodi;
        this.nimi = nimi;
        
        this.aloitusPvm = aloitusPvm;
        this.lopetusPvm = lopetusPvm;
    }

        
    /**
     * Alustaa tyhjän kurssiluokan.
     * @example
     * <pre name="test">
     * Kurssi k1 = new Kurssi();
     * k1.getID() === 0;
     * k1.getNimi() === "";
     * k1.taytaTiedotMata200();
     * k1.getID() === 0;
     * k1.getNimi() === "Johdatus johdatteluun";
     * k1.getAloitusPvm() === "2.2.2018";
     * k1.rekisteroi();
     * k1.getID() === 1;
     * </pre>
     */
    public Kurssi() {
        // Attribuuttien oma alustus riittää.
    }
    
    
    /**
     * Asettaa kurssille seuraavan tunnusnumeron (ID).
     * @return asetettu tunnusnumero
     * @example
     * <pre name="test">
     * Kurssi kurssi1 = new Kurssi(); Kurssi kurssi2 = new Kurssi();
     * kurssi1.rekisteroi();
     * kurssi2.rekisteroi();
     * int i1 = kurssi1.getID();
     * int i2 = kurssi2.getID();
     * i2 - i1 === 1;
     * </pre>
     */
    public int rekisteroi() {
        this.kurssiID = seuraavaKurssiID;
        seuraavaKurssiID++;
        return this.kurssiID;
    }
    
    
    /**
     * Metodi muuttaa kurssin tiedot tolpilla erotetuksi merkkijonoksi. 
     * @return Tolpilla erotetut kurssin tiedot
     */
    public String merkkijonoksi() {
    	return kurssiID + "|" + oppiaine + "|" + kurssiKoodi + "|" + nimi + "|" + aloitusPvm + "|" + lopetusPvm;
    }
    
    
    /**
     * Parsii tiedot merkkijonosta, jossa tiedot erotettu tolpilla
     * @param s parsittava merkkijono
     * @example
     * <pre name="test">
     * Kurssi kurssi = new Kurssi();
     * kurssi.parse("1|Matematiikka    |MAA100  |Johdatus Matematiikkaan  |20.3.2017 |2.5.2017|");
     * kurssi.getID() === 1;
     * kurssi.getOppiaine() === "Matematiikka";
     * kurssi.getKurssiKoodi() === "MAA100";
     * kurssi.getNimi() === "Johdatus Matematiikkaan";
     * kurssi.getAloitusPvm() === "20.3.2017";
     * kurssi.getLopetusPvm() === "2.5.2017";
     * </pre>
     */
    public void parse(String s) {
    	StringBuilder sb = new StringBuilder(s.trim());
    	setID(Integer.parseInt(Mjonot.erota(sb, '|')));
    	oppiaine = Mjonot.erota(sb, '|', oppiaine);
    	kurssiKoodi = Mjonot.erota(sb, '|', kurssiKoodi);
    	nimi = Mjonot.erota(sb, '|', nimi);
    	aloitusPvm.parse(Mjonot.erota(sb, '|', aloitusPvm.toString()));
    	lopetusPvm.parse(Mjonot.erota(sb, '|', lopetusPvm.toString()));
    }
      
    
    /**
     * Apumetodi, jolla saadaan testiarvot kurssille. Testit tyhjän muodostajan yhteydessä.
     */
    public void taytaTiedotMata200() {
        oppiaine = "Matematiikka";
        kurssiKoodi = "MATA" +  + Random.rand(1000,9999);
        nimi = "Johdatus johdatteluun";
        
        aloitusPvm.parse("02.02.2018");
        lopetusPvm.parse("04.04.2018");
    }        

    
    /**
     * Palauttaa kurssin rekisteröintinumeron (id)
     * @return kokonaislukuna ID
     * @example
     */
    public int getID() {
        return this.kurssiID;
    }
    
    
    /**
     * Palauttaa kurssin nimen merkkijonona 
     * @return nimi merkkijonona
     */
    public String getNimi(){
    	return this.nimi;
    }
    
    
    /**
     * Get-metodi oppiaineen hakemiseksi
     * @return oppiaine String-oliona.
     */
    public String getOppiaine() {
    	return this.oppiaine;
    }
    
    
    /**
     * Palauttaa kurssin kurssikoodin merkkijonona
     * @return kurssikoodi merkkijonona
     */
    public String getKurssiKoodi(){
    	return this.kurssiKoodi;
    }
    
    
	/**
	 * Asettaa ID:n ja varmistaa että seuraava numero on aina suurempi,
	 * kuin tähän mennessä suurin.
	 * @param nr asetettava ID
	 */
	private void setID(int nr) {
		kurssiID = nr;
		if (kurssiID >= seuraavaKurssiID) seuraavaKurssiID = kurssiID + 1; 
	}
    
    
    /**
     * Tulostetaan kurssin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", kurssiID) + " " + oppiaine + " " + kurssiKoodi);
        out.println(" " + nimi);
        out.println(aloitusPvm.toString() + "-" + lopetusPvm.toString());
        out.println();
     }
    
    
    /**
     * Asettaa kurssille nimen
     * @param nimi kurssin nimi merkkijonona
     * @return virhe, mikäli ongelmia
     */
    public String setNimi(String nimi) {
    	String virhe = null;
    	if (nimi.equals("")) virhe = "Nimi ei saa olla tyhjä!";
        this.nimi = nimi;
        return virhe;
    }
    
    
    /**
     * Asettaa kurssille kurssikoodin
     * @param kurssikoodi kurssin kurssikoodi merkkijonona
     * @return virhe, mikäli ongelmia
     */
    public String setKurssiKoodi(String kurssikoodi) {
    	String virhe = null;
    	if (kurssikoodi.equals("")) virhe = "Kurssikoodi ei saa olla tyhjä!";
        this.kurssiKoodi = kurssikoodi;
        return virhe;
    }
    
    
    /**
     * Asettaa kurssille oppiaineen
     * @param oppiaine kurssin oppiaine merkkijonona
     * @return virhe, mikäli ongelmia
     */
    public String setOppiaine(String oppiaine) {
    	String virhe = null;
    	if (oppiaine.equals("")) virhe = "Oppiaine ei saa olla tyhjä!";
        this.oppiaine = oppiaine;
        return virhe;
    }
    
    
    /**
     * Asettaa kurssille aloituspäivämäärän.
     * @param pvm aloituspäivämäärä merkkijonona
     * @return virhe, mikäli ongelmia
     */
    public String setAloitusPvm(String pvm) {
        aloitusPvm.parse(pvm);
       if (aloitusPvm.compareTo(lopetusPvm) == 1) return "Aloituspäivämäärän tulee olla lopetuspäivämäärää ennen!";
       return null;
    }


    /**
     * Asettaa kurssille lopetuspäivämäärän.
     * @param pvm lopetuspäivämäärä merkkijonona
     * @return virhe, mikäli ongelmia
     */
    public String setLopetusPvm(String pvm) {
       lopetusPvm.parse(pvm);
       if (aloitusPvm.compareTo(lopetusPvm) == 1) return "Aloituspäivämäärän tulee olla lopetuspäivämäärää ennen!";
       return null;
    }

    
    
    /**
     * Hakee aloituspäivämäärän.
     * @return päivämäärä merkkijonona
     * @example
     * <pre name="test">
     * Kurssi mata200 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.1995","04.02.1995");
     * Kurssi mata201 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.2019","04.02.2019");
     * Kurssi mata202 = new Kurssi();
     * mata200.getAloitusPvm() === "2.2.1995";
     * mata201.getAloitusPvm() === "2.2.2019";
     * mata202.getAloitusPvm() === new Pvm().toString(); // Testataan meneekö nykypäiväksi. Luotto on nyt Pvm-luokassa.
     * </pre>
     */
    public String getAloitusPvm() {
        return aloitusPvm.toString();
    }
    
    
    /**
     * Hakee lopetuspäivämäärän.
     * @return päivämäärä merkkijonon
     * @example
     * <pre name="test">
     * #import kanta.Pvm;
     * Kurssi mata200 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.1995","04.02.1995");
     * Kurssi mata201 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.2019","04.02.2019");
     * Kurssi mata202 = new Kurssi();
     * mata200.getLopetusPvm() === "4.2.1995";
     * mata201.getLopetusPvm() === "4.2.2019";
     * mata202.getLopetusPvm() === new Pvm().toString(); // Testataan meneekö nykypäiväksi. Luotto on nyt Pvm-luokassa.
     * </pre>
     */
    public String getLopetusPvm() {
        return lopetusPvm.toString();
    }
    
    
    /**
     * Tulostetaan kurssin tiedot.
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        this.tulosta(new PrintStream(os));
    }
    
    
    /**
     * Luo kloonin kurssista.
     * @example
     * <pre name="test">
     * Kurssi k1 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.1995","04.02.1995");
     * Kurssi k2 = k1.clone();
     * k1 == k2 === false;
     * k1.getNimi() === k2.getNimi();
     * k1.getID() === k2.getID();
     * k1.getAloitusPvm() == k2.getAloitusPvm() === false;
     * k1.getAloitusPvm().toString() === k2.getAloitusPvm().toString();
     * </pre>
     */
    @Override
    public Kurssi clone() {
    	Kurssi uusi = new Kurssi();
    	uusi.parse(this.merkkijonoksi());
    	return uusi;
    }

    
    /**
     * Testipääohjelma.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kurssi kurssiTest = new Kurssi();
        kurssiTest.tulosta(System.out); //Tulostaa kurssin ilman rekisteröintiä.
        kurssiTest.rekisteroi();
        Kurssi kurssiTest2 = new Kurssi();
        kurssiTest2.rekisteroi();
        
        kurssiTest.tulosta(System.out); //Tulostaa kurssit ilman tietoja.
        kurssiTest2.tulosta(System.out);//Päivämäärinä oletuspäivämäärät.
        
        kurssiTest.taytaTiedotMata200();
        kurssiTest2.taytaTiedotMata200();
        
        kurssiTest.tulosta(System.out);
        kurssiTest2.tulosta(System.out);
    }
}

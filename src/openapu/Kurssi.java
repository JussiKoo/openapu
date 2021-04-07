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
    
    private static int seuraavaKurssiID = 1; // Kaikille Kursseille sama. Mahdollistaa kurssin rekisterˆimisen tietokantaan, ks rekisteroi().

    
    /**
     * Muodostaja kurssille, kun p‰iv‰m‰‰r‰t syˆtet‰‰n merkkijonona. Mik‰li ongelmia p‰iv‰m‰‰r‰n syˆtˆss‰, asetetaan nykyp‰iv‰m‰‰r‰.
     * @param oppiAine oppiaine
     * @param kurssiKoodi kurssikoodi
     * @param nimi kurssin nimi
     * @param aloitusPvm kurssin aloitusp‰iv‰m‰‰r‰
     * @param lopetusPvm kurssin lopetusp‰iv‰m‰‰r‰
     */
    public Kurssi(String oppiAine, String kurssiKoodi, String nimi, String aloitusPvm, String lopetusPvm) {
        this.oppiaine = oppiAine;
        this.kurssiKoodi = kurssiKoodi;
        this.nimi = nimi;
        
        this.aloitusPvm.parse(aloitusPvm);
        this.lopetusPvm.parse(lopetusPvm);
    }
    
    
    /**
     * Muodostaja kurssille, kun p‰iv‰m‰‰r‰t syˆtet‰‰n Pvm-olioina. P‰iv‰m‰‰r‰t oletuksena nykyinen p‰iv‰.
     * @param oppiAine oppiaine
     * @param kurssiKoodi kurssikoodi
     * @param nimi kurssin nimi
     * @param aloitusPvm kurssin aloitusp‰iv‰m‰‰r‰
     * @param lopetusPvm kurssin lopetusp‰iv‰m‰‰r‰
     */
    public Kurssi(String oppiAine, String kurssiKoodi, String nimi, Pvm aloitusPvm, Pvm lopetusPvm) {
        this.oppiaine = oppiAine;
        this.kurssiKoodi = kurssiKoodi;
        this.nimi = nimi;
        
        this.aloitusPvm = aloitusPvm;
        this.lopetusPvm = lopetusPvm;
    }

        
    /**
     * Alustaa tyhj‰n kurssiluokan.
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
        // Attribuuttien oma alustus riitt‰‰.
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
     * Apumetodi, jolla saadaan testiarvot kurssille. Testit tyhj‰n muodostajan yhteydess‰.
     */
    public void taytaTiedotMata200() {
        oppiaine = "Matematiikka";
        kurssiKoodi = "MATA" +  + Random.rand(1000,9999);
        nimi = "Johdatus johdatteluun";
        
        aloitusPvm.parse("02.02.2018");
        lopetusPvm.parse("04.04.2018");
    }        

    
    /**
     * Palauttaa kurssin rekisterˆintinumeron (id)
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
	 * Asettaa ID:n ja varmistaa ett‰ seuraava numero on aina suurempi,
	 * kuin t‰h‰n menness‰ suurin.
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
     * @return virhe, mik‰li ongelmia
     */
    public String setNimi(String nimi) {
    	String virhe = null;
    	if (nimi.equals("")) virhe = "Nimi ei saa olla tyhj‰!";
        this.nimi = nimi;
        return virhe;
    }
    
    
    /**
     * Asettaa kurssille kurssikoodin
     * @param kurssikoodi kurssin kurssikoodi merkkijonona
     * @return virhe, mik‰li ongelmia
     */
    public String setKurssiKoodi(String kurssikoodi) {
    	String virhe = null;
    	if (kurssikoodi.equals("")) virhe = "Kurssikoodi ei saa olla tyhj‰!";
        this.kurssiKoodi = kurssikoodi;
        return virhe;
    }
    
    
    /**
     * Asettaa kurssille oppiaineen
     * @param oppiaine kurssin oppiaine merkkijonona
     * @return virhe, mik‰li ongelmia
     */
    public String setOppiaine(String oppiaine) {
    	String virhe = null;
    	if (oppiaine.equals("")) virhe = "Oppiaine ei saa olla tyhj‰!";
        this.oppiaine = oppiaine;
        return virhe;
    }
    
    
    /**
     * Asettaa kurssille aloitusp‰iv‰m‰‰r‰n.
     * @param pvm aloitusp‰iv‰m‰‰r‰ merkkijonona
     * @return virhe, mik‰li ongelmia
     */
    public String setAloitusPvm(String pvm) {
        aloitusPvm.parse(pvm);
       if (aloitusPvm.compareTo(lopetusPvm) == 1) return "Aloitusp‰iv‰m‰‰r‰n tulee olla lopetusp‰iv‰m‰‰r‰‰ ennen!";
       return null;
    }


    /**
     * Asettaa kurssille lopetusp‰iv‰m‰‰r‰n.
     * @param pvm lopetusp‰iv‰m‰‰r‰ merkkijonona
     * @return virhe, mik‰li ongelmia
     */
    public String setLopetusPvm(String pvm) {
       lopetusPvm.parse(pvm);
       if (aloitusPvm.compareTo(lopetusPvm) == 1) return "Aloitusp‰iv‰m‰‰r‰n tulee olla lopetusp‰iv‰m‰‰r‰‰ ennen!";
       return null;
    }

    
    
    /**
     * Hakee aloitusp‰iv‰m‰‰r‰n.
     * @return p‰iv‰m‰‰r‰ merkkijonona
     * @example
     * <pre name="test">
     * Kurssi mata200 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.1995","04.02.1995");
     * Kurssi mata201 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.2019","04.02.2019");
     * Kurssi mata202 = new Kurssi();
     * mata200.getAloitusPvm() === "2.2.1995";
     * mata201.getAloitusPvm() === "2.2.2019";
     * mata202.getAloitusPvm() === new Pvm().toString(); // Testataan meneekˆ nykyp‰iv‰ksi. Luotto on nyt Pvm-luokassa.
     * </pre>
     */
    public String getAloitusPvm() {
        return aloitusPvm.toString();
    }
    
    
    /**
     * Hakee lopetusp‰iv‰m‰‰r‰n.
     * @return p‰iv‰m‰‰r‰ merkkijonon
     * @example
     * <pre name="test">
     * #import kanta.Pvm;
     * Kurssi mata200 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.1995","04.02.1995");
     * Kurssi mata201 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.2019","04.02.2019");
     * Kurssi mata202 = new Kurssi();
     * mata200.getLopetusPvm() === "4.2.1995";
     * mata201.getLopetusPvm() === "4.2.2019";
     * mata202.getLopetusPvm() === new Pvm().toString(); // Testataan meneekˆ nykyp‰iv‰ksi. Luotto on nyt Pvm-luokassa.
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
     * Testip‰‰ohjelma.
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Kurssi kurssiTest = new Kurssi();
        kurssiTest.tulosta(System.out); //Tulostaa kurssin ilman rekisterˆinti‰.
        kurssiTest.rekisteroi();
        Kurssi kurssiTest2 = new Kurssi();
        kurssiTest2.rekisteroi();
        
        kurssiTest.tulosta(System.out); //Tulostaa kurssit ilman tietoja.
        kurssiTest2.tulosta(System.out);//P‰iv‰m‰‰rin‰ oletusp‰iv‰m‰‰r‰t.
        
        kurssiTest.taytaTiedotMata200();
        kurssiTest2.taytaTiedotMata200();
        
        kurssiTest.tulosta(System.out);
        kurssiTest2.tulosta(System.out);
    }
}

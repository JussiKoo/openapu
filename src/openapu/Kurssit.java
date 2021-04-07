package openapu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Kurssi-alkioiden käsittelijäluokka. Käsittelee kurssin lisäämisen.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 *
 */
public class Kurssit {
    
    private static int          maxKursseja     = 5;
    private int                 lkm             = 0;
    private Kurssi[]            alkiot          = new Kurssi[maxKursseja];
    private boolean muutettu;
    private String defaultNimi = "kurssit";
    
    private String              tiedostonNimi   = "tiednimi.txt";

    
    /**
     * Tyhjä muodostaja Kurssit-luokalle.
     */
    public Kurssit() {
        // Attribuuttien oma alustus riittää.
    }
    
    
    /**
     * Lisää kurssin kurssit-taulukkoon ja kasvattaa lkm-muuttujaa.
     * @param kurssi lisättävä kurssi
     * @example
     * <pre name="test">
     * Kurssit kurssit = new Kurssit();
     * Kurssi kurssi1 = new Kurssi(), kurssi2 = new Kurssi();
     * 
     * kurssit.getLkm() === 0;
     * kurssit.lisaa(kurssi1); kurssit.getLkm() === 1;
     * kurssit.lisaa(kurssi2); kurssit.getLkm() === 2;
     * kurssit.lisaa(kurssi1); kurssit.getLkm() === 3;
     * kurssit.lisaa(kurssi1);
     * kurssit.lisaa(kurssi2); kurssit.getLkm() === 5;
     * kurssit.lisaa(kurssi1);
     * 
     * kurssit.annaAlkio(0) === kurssi1;
     * kurssit.annaAlkio(1) === kurssi2;
     * kurssit.annaAlkio(2) === kurssi1;
     * kurssit.annaAlkio(0) == kurssit.annaAlkio(2) === true;
     * kurssit.annaAlkio(0) == kurssit.annaAlkio(1) === false;
     * kurssit.annaAlkio(5) === kurssi1;
     * 
     * Kurssi kurssi3 = new Kurssi();
     * kurssit.lisaa(kurssi3); kurssit.getLkm() === 7;
     * kurssit.annaAlkio(6) === kurssi3;
     * </pre>
     */
    public void lisaa(Kurssi kurssi) {
    	muutettu = true;
        if (lkm >= alkiot.length) {
            maxKursseja += 30;
            Kurssi[] alkiotBak = alkiot;
            alkiot = new Kurssi[maxKursseja];
            for (int i = 0; i < alkiotBak.length; i++) {
                alkiot[i] = alkiotBak[i];
            }
        }
        alkiot[lkm++] = kurssi;
    }
    
    
    /**
     * Palauttaa viitteen i:nteen kurssiin listassa. Testataan mm. lisaa-metodin yhteydessä. Käytetään testeihin.
     * @param i monesko kurssi alkiot-taulukossa
     * @return viite i:nteen kurssiin
     * @throws IndexOutOfBoundsException jos i ei ole sallittu
     */
    public Kurssi annaAlkio(int i)throws IndexOutOfBoundsException {
        if (i<0 || lkm <= i) throw new IndexOutOfBoundsException("Virheellinen indeksi: " + i);
        return alkiot[i];
    }
    

    /**
     * Palauttaa kurssien lukumäärän. Testataan mm. lisaa-metodin yhteydessä.
     * @return kurssien lukumäärä
     */
    public int getLkm() {
        return this.lkm;
    }
    
    
    /**
     * Get-metodi jolla haetaan käytettävä tiedoston nimi.
     * @return tiedoston nimi String-oliona.
     */
    public String getTiedostonNimi() {
    	return this.tiedostonNimi + ".dat";
    }
    
    
    /**
     * Lukee kurssit tiedostosta.
     * @param tiedosto tiedoston nimi
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException, IOException
     * #import java.io.File;
     * #import java.io.IOException;
     * #import fi.jyu.mit.ohj2.VertaaTiedosto;
     * 
     * VertaaTiedosto.kirjoitaTiedosto("kurssitesti.dat", "2|Matematiikka|MATO11|Geometria|21.02.2012|27.03.2012\n"+
     *                                 "5|Kemia|KEM4|Fysikaalinen kemia|01.01.1999|01.01.2000\n"+
     *                                 "21|Äidinkieli|AI4|Kielioppi|12.04.2012|22.03.2013\n");
     * Kurssit kurssit = new Kurssit();
     * kurssit.lueTiedostosta("kurssitesti");
     * kurssit.anna(2).getOppiaine() === "Matematiikka";
     * kurssit.anna(2).getKurssiKoodi() === "MATO11";
     * kurssit.anna(5).getAloitusPvm() === "1.1.1999";
     * kurssit.anna(5).getNimi() === "Fysikaalinen kemia";
     * kurssit.anna(21).getLopetusPvm() === "22.3.2013";
     * kurssit.anna(21).getOppiaine() === "Äidinkieli";
     * Kurssi k1 = new Kurssi();
     * k1.rekisteroi();
     * k1.getID() - kurssit.anna(21).getID() === 1;
     * k1.parse("7|Biologia|BIOA211|Solubiologia|04.09.1891|13.10.1891");
     * k1.getID() === 7;
     * kurssit.lisaa(k1);
     * kurssit.tallenna();
     * 
     * kurssit = new Kurssit();
     * kurssit.lueTiedostosta("kurssitesti");
     * kurssit.anna(5).getNimi() === "Fysikaalinen kemia";
     * kurssit.anna(21).getOppiaine() === "Äidinkieli";
     * kurssit.anna(7).getAloitusPvm() === "4.9.1891";
     * VertaaTiedosto.tuhoaTiedosto("kurssitesti.dat");
     * </pre>
     */
    public void lueTiedostosta(String tiedosto) throws SailoException {
        setTiedostonNimi(tiedosto);
        try ( Scanner fi = new Scanner(new FileReader(getTiedostonNimi()))) {  
        	while ( fi.hasNext() ) {
        		String rivi = fi.nextLine();
        		if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
        		Kurssi kurssi = new Kurssi();
        		kurssi.parse(rivi);
        		lisaa(kurssi);
        	}
        	muutettu = false;
        } catch ( FileNotFoundException e ) {
        	throw new SailoException("Tiedosto " + tiedostonNimi + " ei aukea.");
        }
    }
    
    
	/**
     * Tallentaa kurssit tiedostoon, jos on muutoksia.
     * Tiedoston muoto:
     * <pre>
     * Openapu kurssit
     * 20
     * ; kommenttirivi
     * 2|Fysiikka|FYSP105|Lennon-Fysiikka|10.10.2019|12.12.2019
     * 3|Matematiikka|MATS202|Topologia|12.4.2011|22.5.2011
     * </pre>
     * @throws SailoException mikäli tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
    	if (!muutettu) return;
    	
    	File fbak = new File(getBakNimi());
    	File ftied = new File(getTiedostonNimi());
    	
    	try (PrintStream fo = new PrintStream(new FileOutputStream(getTiedostonNimi()))) {
    		fbak.delete();
    		ftied.renameTo(fbak);
    		for (Kurssi kurssi: alkiot) {
    			if (kurssi == null) continue;
    			fo.println(kurssi.merkkijonoksi());
    		}
    	} catch (FileNotFoundException e) {
    		throw new SailoException("Tiedosto ei aukea: " + e.getMessage());
    	}
    	muutettu = false;
    }
    
    
    /**
     * Korvaa kurssin tietorakenteessa ja ottaa kurssin omistukseensa.
     * Etsitään kurssi vastaavalla ID:llä. Jos ei löydy lisätään uutena kurssina.
     * @param kurssi lisättävän kurssin viite. Tietorakenne muuttuu omistajaksi
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     * Kurssit kurssit = new Kurssit();
     * Kurssi k1 = new Kurssi(); Kurssi k2 = new Kurssi();
     * k1.rekisteroi(); k2.rekisteroi();
     * kurssit.getLkm() === 0;
     * kurssit.korvaaTaiLisaa(k1); kurssit.getLkm() === 1;
     * kurssit.korvaaTaiLisaa(k2); kurssit.getLkm() === 2;
     * Kurssi k3 = k1.clone();
     * kurssit.korvaaTaiLisaa(k3); kurssit.getLkm() === 2;
     * </pre>
     */
    public void korvaaTaiLisaa(Kurssi kurssi) {
    	int id = kurssi.getID();
    	for (int i = 0; i < lkm; i++) {
    	    if (alkiot[i] == null) continue;
    		if (alkiot[i].getID() == id) {
    			alkiot[i] = kurssi;
    			muutettu = true;
    			return;
    		}
    	}
    	lisaa(kurssi);
    }
    
    
    /**
     * Poistaa kurssin tietojärjestelmästä
     * @param id poistettavan kurssin ID
     * @example
     * <pre name="test">
     * Kurssit kt12 = new Kurssit();
     * Kurssi k1 = new Kurssi();
     * k1.parse("44|Matiikka|MAA|matiikka|12.3.1212|12.4.1212");
     * Kurssi k2 = new Kurssi();
     * k2.parse("67|Matiikka|MAA|matiikka|12.3.1212|12.4.1212");
     * Kurssi k3 = new Kurssi();
     * k3.parse("654|Matiikka|MAA|matiikka|12.3.1212|12.4.1212");
     * kt12.lisaa(k1);
     * kt12.lisaa(k2);
     * kt12.lisaa(k3);
     * kt12.etsiID(44) === 0;
     * kt12.poista(44);
     * kt12.etsiID(44) === -1;
     * kt12.etsiID(67) === 0;
     * kt12.getLkm() === 2;
     * </pre>
     */
    public void poista(int id) {   	
    	int ind = etsiID(id);
    	if (ind == -1) return;
    	lkm--;
    	for (int i = ind; i < lkm; i++) 
    		alkiot[i] = alkiot[i+1];
        alkiot[lkm] = null;
        muutettu = true;
    }
    
    
    /**
     * Etsii alkiot-taulukosta id:tä vastaavan kurssin
     * @param id jota etsitään
     * @return indeksi alkiot taulukossa, -1 jos ei löydy
     * @example
     * <pre name="test">
     * Kurssit kt44 = new Kurssit();
     * Kurssi k1 = new Kurssi();
     * k1.parse("121|Matiikka|MAA|matiikka|12.3.1212|12.4.1212");
     * Kurssi k2 = new Kurssi();
     * k2.parse("1213|Matiikka|MAA|matiikka|12.3.1212|12.4.1212");
     * Kurssi k3 = new Kurssi();
     * k3.parse("111|Matiikka|MAA|matiikka|12.3.1212|12.4.1212");
     * kt44.lisaa(k1);
     * kt44.lisaa(k2);
     * kt44.lisaa(k3);
     * kt44.etsiID(121) === 0;
     * kt44.etsiID(1213) === 1;
     * kt44.etsiID(111) === 2;
     * Kurssi k4 = new Kurssi();
     * k4.parse("17|Matiikka|MAA|matiikka|12.3.1212|12.4.1212");
     * kt44.lisaa(k4);
     * kt44.etsiID(16) === -1;
     * kt44.etsiID(17) === 3; 
     * </pre>
     */
    public int etsiID(int id) {
    	for (int i = 0; i < lkm; i++)
    		if (alkiot[i].getID() == id) return i;
    	return -1;
   
    }
    
    
    /**
     * Palauttaa viitteen kurssiin, jonka id on i.
     * Testissä ID:t on vähän "oudossa" paikassa muiden testien takia.
     * @param id kurssin id
     * @return viite kurssiin, jonka id on i. null, mikäli ei olemassa kyseisen ID:n edustajaa.
     * @example
     * <pre name="test">
     * Kurssit kurssit2 = new Kurssit();
     * Kurssi k1 = new Kurssi();
     * k1.parse("101|||||");
     * Kurssi k2 = new Kurssi();
     * k2.parse("104|||||");
     * Kurssi k3 = new Kurssi();
     * k3.parse("109|||||");
     * kurssit2.lisaa(k1);
     * kurssit2.lisaa(k2);
     * kurssit2.lisaa(k3);
     * kurssit2.anna(101) === k1;
     * kurssit2.anna(104) === k2;
     * kurssit2.anna(109) === k3;
     * kurssit2.anna(4) === null;
     * </pre>
     */
    public Kurssi anna(int id){
        for (Kurssi k : alkiot) {
       	 if (k == null) continue; //alkiot taulukossa on myös tyhjää tilaa.
            if (k.getID() == id) return k;             
        }
        return null;
    }
    
    
    /**
     * Lisää tietyt kurssit listaan. Hakuehto hakee kurssia nimen perusteella. Hakuehto saa esiintyä missä tahansa kurssin nimessä, jotta päätyy listaan.
     * @param hakuehto nimi jota etsitään listasta
     * @return lista kursseista, joiden nimi ehdossa. Kaikki kurssit, mikäli ehto on tyhjä.
     * @example
     * <pre name="test">
     * #import java.util.Collection;
     * Kurssit kurssit = new Kurssit();
     * Kurssi ku1 = new Kurssi("Matematiikka","MATA200","Johdatus johdatteluun","02.02.1995","04.02.1995"); 
     * Kurssi ku2 = new Kurssi("Matematiikka","MATA200","Johdatus odotteluun","02.02.2019","04.02.2019"); 
     * Kurssi ku3 = new Kurssi("Matematiikka","MATA200","Olemassaoloa johtamassa","02.02.1995","04.02.1995");
     * kurssit.lisaa(ku1); kurssit.lisaa(ku2); kurssit.lisaa(ku3);
     * Collection<Kurssi> etsittavat1 = kurssit.etsi("Johdatus");
     * etsittavat1.contains(ku1) === true;
     * etsittavat1.contains(ku2) === true;
     * etsittavat1.contains(ku3) === false;
     * etsittavat1.size() === 2;
     * etsittavat1 = kurssit.etsi("Oliot");
     * etsittavat1.contains(ku1) === false;
     * etsittavat1.contains(ku2) === false;
     * etsittavat1.contains(ku3) === false;
     * etsittavat1.size() === 0;
     * </pre>
     */
    public Collection<Kurssi> etsi(String hakuehto) {
    	String ehto = "*";  	
    	if ( hakuehto != null && hakuehto.length() > 0 ) ehto = "*" + hakuehto + "*";
    	Collection<Kurssi> loytyneet = new ArrayList<Kurssi>();
    	
    	for (Kurssi kurssi: alkiot) {
    		if (kurssi == null) continue;
    		if (WildChars.onkoSamat(kurssi.getNimi(), ehto)) loytyneet.add(kurssi);
    	}
    	return loytyneet;
    }
            
    
    /**
     * Asettaa tiedoston nimen halutuksi
     * @param tiedosto tiedoston nimi
     */
    private void setTiedostonNimi(String tiedosto) {
		this.tiedostonNimi = tiedosto;		
	}

    
    /**
     * Get-metodi, default-tiedoston nimelle
     * @return default-nimi tiedostolle
     */
    private String getBakNimi() {
		return defaultNimi + ".bak";
	}
    
    
    /**
     * Palauttaa tiedon, onko muutettu. 
     * @return true, mikäli muutettu
     */
    public boolean onkoMuutettu() {
        return muutettu;
    }


	/**
     * Testipääohjelma Kurssit-luokalle
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kurssit kurssit = new Kurssit();
        Kurssi kurssi = new Kurssi(), kurssi2 = new Kurssi();
        
        kurssi.rekisteroi();
        kurssi.taytaTiedotMata200();
        kurssi2.rekisteroi();
        kurssi2.taytaTiedotMata200();
        
        try {
            kurssit.lisaa(kurssi);
            kurssit.lisaa(kurssi2);
        } catch (Exception e) {
            e.printStackTrace();
        }
                
        System.out.println("======= Kurssit testi =========");
        
        for(int i = 0; i < kurssit.getLkm(); i++) {
            Kurssi kurssiTmp = kurssit.anna(i);
            System.out.println("Kurssi nro: " + i);
            if ( kurssiTmp == null) continue;
            kurssiTmp.tulosta(System.out);
        }
    }
}

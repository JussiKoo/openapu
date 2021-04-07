package openapu;

import openapu.SailoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Luokka kurssikohtaisten tietojen k‰sittelij‰lle.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 *
 */
public class KurssikohtTiedot {
	
	private String tiedostonNimi = "";
	
	private final ArrayList<KurssikohtTieto> kkTiedot = new ArrayList<KurssikohtTieto>();
	private boolean muutettu = false;
	
	/**
	 * Oppilaan kurssikohtaisten tietojen alustaminen
	 */
	public KurssikohtTiedot() {
		// ei tee toistaiseksi mit‰‰n
	}
	
	
	/**
	 * Lis‰‰ uudet kurssikohtaiset tiedot tietorakenteeseen
	 * @param kkTieto kurssikohtainen tieto
	 */
	public void lisaa(KurssikohtTieto kkTieto ) {
		muutettu = true;
		kkTiedot.add(kkTieto);
	}

	
    /**
     * Korvataan tai lis‰t‰‰n kurssikohtainen tieto.
     * @param kktieto joka korvataan tai lis‰t‰‰n
     * @example
     * <pre name="test">
     * KurssikohtTieto kkt1 = new KurssikohtTieto(), kkt2 = new KurssikohtTieto(), kkt3 = new KurssikohtTieto(), kkt4 = new KurssikohtTieto();
     * kkt1.parse("1|3|5|10|apua");
     * kkt2.parse("1|4|5|10|help"); 
     * kkt3.parse("2|3|10|15|halp");
     * 
     * KurssikohtTiedot kktd = new KurssikohtTiedot(); kktd.korvaaTaiLisaa(kkt1); kktd.korvaaTaiLisaa(kkt2); kktd.korvaaTaiLisaa(kkt3);
     * kktd.haeKktieto(1,3) === kkt1;
     * kktd.haeKktieto(1,4) === kkt2;
     * kktd.haeKktieto(2,3) === kkt3;
     * kkt4.parse("1|4|10|10|hulp");
     * kktd.korvaaTaiLisaa(kkt4);
     * 
     * kktd.haeKktieto(1,4) === kkt4;
     * kkt2.parse("2|3|10|15|hulp");
     * kktd.korvaaTaiLisaa(kkt2);
     * kktd.haeKktieto(2,3).getMerkinnat() === "hulp";
     * </pre>
     */
    public void korvaaTaiLisaa(KurssikohtTieto kktieto) {
        int kid = kktieto.getKurssiID();
        int oid = kktieto.getOppilasID();
        for (int i = 0; i < getLkm(); i++) {
            if (kkTiedot.get(i).getOppilasID() == oid && kkTiedot.get(i).getKurssiID() == kid) {
                kkTiedot.set(i, kktieto);
                muutettu = true;
                return;
            }
        }
        lisaa(kktieto);
    }

	
	/**
	 * Palauttaa tietorakenteen alkioiden lukum‰‰r‰n.
	 * @return kurssikohtaisten tietojen lukum‰‰r‰
	 */
	private int getLkm() {
        return kkTiedot.size();
    }


    /**
	 * lukee kurssikohtaiset tiedot tiedostosta.
	 * @param hakemisto tiedoston nimi
	 * @throws SailoException jos lukeminen ep‰onnistuu
     * <pre name="test">
     * #THROWS SailoException, IOException
     * #import fi.jyu.mit.ohj2.VertaaTiedosto;
     * #import java.io.*;
     * VertaaTiedosto.kirjoitaTiedosto("kktiedotkoe.dat",
     *      "5|3|10|8|Ht ok\n" + 
     *      "15|9|3|9|\n" +
     *      "22|3|5|5|ryhti‰"
     *      );
     * KurssikohtTiedot kktiedot1 = new KurssikohtTiedot();
     * kktiedot1.lueTiedostosta("kktiedotkoe");
     * kktiedot1.onkoKurssilla(5,3) === true;
     * kktiedot1.onkoKurssilla(15,3) === false;
     * kktiedot1.onkoKurssilla(3,22) === false;
     * kktiedot1.lisaa(new KurssikohtTieto(15,3));
     * kktiedot1.onkoKurssilla(15,3) === true;
     * kktiedot1.tallenna();
     * 
     * kktiedot1 = new KurssikohtTiedot(); // tyhjennet‰‰n tiedot
     * kktiedot1.lueTiedostosta("kktiedotkoe");
     * kktiedot1.onkoKurssilla(15,3) === true;
     * VertaaTiedosto.tuhoaTiedosto("kktiedotkoe.dat");
     * </pre>
	 */
	public void lueTiedostosta(String hakemisto) throws SailoException {
    	setTiedostonNimi(hakemisto);
        try ( Scanner fi = new Scanner(new FileReader(getTiedostonNimi()))){
        	while ( fi.hasNext() ) {
        		String rivi = fi.nextLine();
        		rivi = rivi.trim();
        		if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
        		
        		KurssikohtTieto kktieto = new KurssikohtTieto();
        		kktieto.parse(rivi);
        		lisaa(kktieto);
        	}
        	muutettu = false;
        } catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei avaudu");
		} 
	}
	
	
	/**
	 * Asettaa tiedoston nimen.
	 * @param hakemisto uusi tiedoston nimi
	 */
    private void setTiedostonNimi(String hakemisto) {
		this.tiedostonNimi = hakemisto;
	}


	/**
     * Tallentaa kurssikohtaiset tiedot tiedostoon  
     * @throws SailoException jos talletus ep‰onnistuu
     */
	public void tallenna() throws SailoException {
		if ( !muutettu) return;
		
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());

		try (PrintStream fo = new PrintStream(new FileOutputStream(getTiedostonNimi()))){
			fbak.delete();
			ftied.renameTo(fbak);
			for (KurssikohtTieto kkt : kkTiedot) {
			    if (kkt == null) continue; 
				fo.println(kkt.merkkijonoksi());
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto ei aukea: " + e.getMessage());
		}
		muutettu = false;
	}
         
	
    /**
     * Palauttaa tiedoston nimen.
     * @return
     */
    private String getTiedostonNimi() {
		return tiedostonNimi + ".dat";
	}

    
    /**
     * Palauttaa bak-tiedoston nimen.
     * @return bak-tiedoston nimi
     */
	private String getBakNimi() {
		return tiedostonNimi + ".bak";
	}


	/**
     * Metodi palauttaa kurssilla olevien oppilaiden ID:t listana.
     * @param kurssiID kurssin id
     * @return Listana oppilaiden ID:t
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * KurssikohtTiedot kkTiedot = new KurssikohtTiedot();
     * KurssikohtTieto tieto11 = new KurssikohtTieto(1,1); kkTiedot.lisaa(tieto11);
     * KurssikohtTieto tieto12 = new KurssikohtTieto(1,2); kkTiedot.lisaa(tieto12);
     * KurssikohtTieto tieto21 = new KurssikohtTieto(2,1); kkTiedot.lisaa(tieto21);
     * KurssikohtTieto tieto22 = new KurssikohtTieto(2,2); kkTiedot.lisaa(tieto22);
     * KurssikohtTieto tieto23 = new KurssikohtTieto(2,3); kkTiedot.lisaa(tieto23);
     * KurssikohtTieto tieto14 = new KurssikohtTieto(1,4); kkTiedot.lisaa(tieto14);
     * KurssikohtTieto tieto24 = new KurssikohtTieto(2,4); kkTiedot.lisaa(tieto24);
     * KurssikohtTieto tieto31 = new KurssikohtTieto(3,1); kkTiedot.lisaa(tieto31);
     * 
     * List<Integer> loytyneet;
     * loytyneet = kkTiedot.annaOppilasIDTiedot(1);
     * loytyneet.size() === 3;
     * loytyneet.get(0) == tieto11.getOppilasID() === true;
     * loytyneet.get(1) == tieto12.getOppilasID() === true;
     * loytyneet = kkTiedot.annaOppilasIDTiedot(4);
     * loytyneet.size() === 0;
     * loytyneet = kkTiedot.annaOppilasIDTiedot(4);
     * loytyneet = kkTiedot.annaOppilasIDTiedot(2);
     * loytyneet.size() === 4;
     * loytyneet.get(1) == tieto22.getOppilasID() === true;
     * </pre>
     */
    public List<Integer> annaOppilasIDTiedot(int kurssiID){
    	
    	List<Integer> loydetyt = new ArrayList<Integer>();
    	for (KurssikohtTieto tieto : kkTiedot)
    		if (tieto.getKurssiID() == kurssiID) loydetyt.add(tieto.getOppilasID());
    	return loydetyt;
    }
    
    
    /**
     * Hakee Kurssin id:t‰ ja oppilaan id:t‰ vastaavan kurssikohtasen tiedon.
     * @param kid kurssin id
     * @param oid oppilaan id
     * @return viite haluttuun kurssikohtaiseen tietoon, jos ei lˆydy niin null viite.
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * KurssikohtTiedot k = new KurssikohtTiedot();
     * KurssikohtTieto kt1 = new KurssikohtTieto(1,3);
     * KurssikohtTieto kt2 = new KurssikohtTieto(2,3);
     * KurssikohtTieto kt3 = new KurssikohtTieto(4,2);
     * k.lisaa(kt1);
     * k.lisaa(kt2);
     * k.lisaa(kt3);
     * k.haeKktieto(1,2) === null;
     * k.haeKktieto(1,3) === kt1;
     * k.haeKktieto(2,3) === kt2;
     * k.haeKktieto(4,2) === kt3;
     * 
     * </pre>
     */
    public KurssikohtTieto haeKktieto(int kid, int oid) {
	    for (KurssikohtTieto k: kkTiedot) {
	    	if (k.getKurssiID() == kid && k.getOppilasID() == oid) return k;
	    	}
	    return null;    	
    }

    
    /**
     * Tutkii onko oppilas kurssilla ID numeroiden perusteella.
     * @param kurssiID kurssin ID jolta oppilasta etsit‰‰n
     * @param oppilasID oppilaan ID jota etsit‰‰n
     * @return totuusarvo, onko kurssilla vai ei
     */
	public boolean onkoKurssilla(int kurssiID, int oppilasID) {
		List<Integer> oppilaatID = annaOppilasIDTiedot(kurssiID);
        return oppilaatID.contains(oppilasID);
	}
	
	
	/**
	 * Poistaa kurssikohtaisen tiedon, jonka ID:t vastaavat parametreja.
	 * @param kurssiID kurssin ID, jolta halutaan poistaa
	 * @param oppilasID oppilaan ID, joka halutaan poistaa
	 * @example
	 * <pre name="test">
     * #THROWS SailoException
     * KurssikohtTiedot k = new KurssikohtTiedot();
     * KurssikohtTieto kt1 = new KurssikohtTieto(1,3);
     * KurssikohtTieto kt2 = new KurssikohtTieto(2,3);
     * KurssikohtTieto kt3 = new KurssikohtTieto(4,2);
     * k.lisaa(kt1);
     * k.lisaa(kt2);
     * k.lisaa(kt3);
     * k.contains(kt1) === true;
     * k.poista(1,3);
     * k.contains(kt1) === false;
     * k.contains(kt2) === true;
     * k.poista(2,4);
     * k.contains(kt2) && k.contains(kt3) === true;
     * k.poista(4,2);
     * k.contains(kt3) === false;
  	 * </pre>
	 */
	public void poista(int kurssiID, int oppilasID) {
	    KurssikohtTieto poistettava = new KurssikohtTieto();
	    for (KurssikohtTieto k : kkTiedot) {
	        if (k.getKurssiID() == kurssiID && k.getOppilasID() == oppilasID) poistettava = k;
	    }
	    muutettu = true;
	    kkTiedot.remove(poistettava);
	}
	
	
	/**
	 * Tutkii sis‰ltyykˆ kurssikohtainen tieto rakenteeseen. Testaus poista-metodissa.
	 * @param kktieto jota tutkitaan
	 * @return true, mik‰li sis‰ltyy, false muutoin.
	 */
	public boolean contains(KurssikohtTieto kktieto) {
	    return kkTiedot.contains(kktieto);
	}
	
	
    /**
     * Palauttaa tiedon, onko muutettu. 
     * @return true, mik‰li muutettu
     */
    public boolean onkoMuutettu() {
        return muutettu;
    }



}

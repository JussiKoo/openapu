package openapu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Luokka oppilaiden k‰sittelemist‰ varten.
 * Osaa esimerkiksi lis‰t‰ j‰seni‰.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 */
public class Oppilaat {
	
	private int maxOppilaat = 6;
	private Oppilas[] alkiot = new Oppilas[maxOppilaat];
	private int lkm = 0;
	private boolean muutettu = false;
	
	private String tiedostonNimi = "oppilaat";
	
	
	/**
	 * Muodostaja tyhj‰lle luokalle.
	 */
	public Oppilaat() {
		// Attribuuttien oma alustus riitt‰‰.
	}
	

    /**
     * Lukee oppilaat tiedostosta ja lis‰‰ ne tietorakenteeseen.
     * @param tiedosto tiedoston nimi
     * @throws SailoException jos lukeminen ep‰onnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException, IOException
     * #import fi.jyu.mit.ohj2.VertaaTiedosto;
     * #import java.io.*;
     * VertaaTiedosto.kirjoitaTiedosto("oppilaatkoe.dat",
     *      "52|Kalle|Koskenlaskija|029566\n" + 
     *      "31|Olli|Urvelo|051213\n" +
     *      "79|Matti|Vanhanen|231266"
     *      );
     * Oppilaat oppilaat1 = new Oppilaat();
     * oppilaat1.lueTiedostosta("oppilaatkoe");
     * oppilaat1.anna(52).getEtunimi() === "Kalle";
     * oppilaat1.anna(52).getHetu() === "029566";
     * oppilaat1.anna(79).getSukunimi() === "Vanhanen";
     * Oppilas oiva = new Oppilas();
     * oiva.rekisteroi();
     * oiva.getID() === oppilaat1.anna(79).getID() + 1;
     * oiva.parse("54|Oiva|Mattila|180999");
     * oiva.getID() === 54;
     * oppilaat1.lisaa(oiva);
     * oppilaat1.tallenna();
     * 
     * oppilaat1 = new Oppilaat(); //Tyhjennet‰‰n tietorakenne
     * oppilaat1.lueTiedostosta("oppilaatkoe"); //Ladataan tiedot takaisin.
     * 
     * oppilaat1.anna(31).getEtunimi() === "Olli";
     * oppilaat1.anna(54).getSukunimi() === "Mattila";
     * VertaaTiedosto.tuhoaTiedosto("oppilaatkoe.dat");
     * </pre>
     */
    public void lueTiedostosta(String tiedosto) throws SailoException {
    	setTiedostonNimi(tiedosto);
        try ( Scanner fi = new Scanner(new FileReader(getTiedostonNimi()))){
        	while ( fi.hasNext() ) {
        		String rivi = fi.nextLine();
        		rivi = rivi.trim();
        		if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
        		Oppilas oppilas = new Oppilas();
        		oppilas.parse(rivi);
        		lisaa(oppilas);
        	}
        	muutettu = false;
        } catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei avaudu");
		} 
    }
    
    /**
     * Tallentaa oppilaat tiedostoon.
     * Tiedoston muoto:
     * <pre>
     * ; kommenttirivi
     * 1|Olli|Oppilas|020495
     * 3|Kalle|Koijari|051299
     * 4|Ville|Valo|270200
     * </pre>
     * @throws SailoException mik‰li talletus ei onnistu
     */
	public void tallenna() throws SailoException {
		if ( !muutettu) return;
		
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());

		try (PrintStream fo = new PrintStream(new FileOutputStream(getTiedostonNimi()))){
			fbak.delete();
			ftied.renameTo(fbak);
			for (Oppilas oppilas : alkiot) {
			    if (oppilas == null) continue; 
				fo.println(oppilas.merkkijonoksi());
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto ei aukea: " + e.getMessage());
		}
		muutettu = false;
	}
         

	/**
     * Palauttaa alkiotaulukon i:nnen alkion. Testausta varten.
     * @param i monesko alkio
     * @return i:s alkio
     */
    public Oppilas annaAlkio(int i){
        return alkiot[i];
    }
    
    	
     /**
      * Palauttaa viitteen oppilaaseen, jonka id on i. Testaus tiedoston lukemisen yhteydess‰.
      * @param id oppilaan id
      * @return viite oppilaaseen, jonka id on i. null, mik‰li ei olemassa kyseisen ID:n edustajaa.
      */
     public Oppilas anna(int id){
         for (Oppilas o : alkiot) {
        	 if (o == null) continue; //alkiot taulukossa on myˆs tyhj‰‰ tilaa.
             if (o.getID() == id) return o;             
         }
         return null;
     }
     
     
     /**
      * Lis‰‰ uuden oppilaan tietorakenteeseen.
      * @param oppilas lis‰tt‰v‰n oppilaan viite
      * @example
      * <pre name="test">
      * Oppilaat oppilaat = new Oppilaat();
      * Oppilas olli1 = new Oppilas(), olli2 = new Oppilas();
      * oppilaat.lisaa(olli1); oppilaat.getLkm() === 1;
      * oppilaat.lisaa(olli2); oppilaat.getLkm() === 2;
      * oppilaat.lisaa(olli1); oppilaat.getLkm() === 3;
      * oppilaat.annaAlkio(0) === olli1;
      * oppilaat.annaAlkio(1) === olli2;
      * oppilaat.annaAlkio(2) === olli1;
      * oppilaat.annaAlkio(1) == olli1 === false;
      * oppilaat.annaAlkio(1) == olli2 === true;
      * oppilaat.annaAlkio(3) === null; 
      * oppilaat.lisaa(olli2); oppilaat.getLkm() === 4;
      * oppilaat.lisaa(olli1); oppilaat.getLkm() === 5;
      * oppilaat.lisaa(olli1); oppilaat.getLkm() === 6;
      * </pre>
      */
     public void lisaa(Oppilas oppilas) {
         muutettu = true;
         if (lkm >= alkiot.length) {
             maxOppilaat += 30;
             Oppilas[] alkiotBak = alkiot;
             alkiot = new Oppilas[maxOppilaat];
             for (int i = 0; i < alkiotBak.length; i++) {
                 alkiot[i] = alkiotBak[i];
             }
         }
         alkiot[lkm++] = oppilas;
     }
     
     /**
      * Lis‰‰ oppilaat listaan.
      * hakeminen ei viel‰ toimi
      * @return lista oppilaista
      * @example
      * <pre name="test">
      * #import java.util.*;
      * Oppilaat op2 = new Oppilaat();
      * Oppilas o1 = new Oppilas();
      * Oppilas o2 = new Oppilas();
      * Oppilas o3 = new Oppilas();
      * op2.lisaa(o1);
      * op2.lisaa(o2);
      * op2.lisaa(o3);
      * op2.etsi().size() === 3;
      * </pre>
      */
     public Collection<Oppilas> etsi() {
         Collection<Oppilas> loytyneet = new ArrayList<Oppilas>();
         for (Oppilas oppilas: alkiot) {
        	 if (oppilas == null) continue;
             loytyneet.add(oppilas);
         }
         return loytyneet;
     }
     
     
     /**
      * Metodi palauttaa oppilaiden lukum‰‰r‰n
      * @return oppilaiden lukum‰‰r‰
      */
     public int getLkm() {
    	 return this.lkm;
     }
     
     
     /**
      * Palauttaa tiedoston nimen.
      * @return tiedoston nimi
      */
     public String getTiedostonNimi(){
    	 return tiedostonNimi + ".dat";
     }
     
     
     /**
      * Asettaa tiedoston nimi
      * @param nimi joka asetetaan tiedostolle
      */
     public void setTiedostonNimi(String nimi){
    	 this.tiedostonNimi = nimi;
     }

     
     /**
      * Palauttaa varatiedoston nimen.
      * @return varatiedoston nimi
      */
     public String getBakNimi() {
 		return tiedostonNimi + ".bak";
 	 }

     
     /**
      * Korvaa tai lis‰‰ oppilaan tietorakenteeseen, riippuen esiintyykˆ oppilaan ID.
      * @param oppilas joka korvataan tai lis‰t‰‰n.
      * @example
      * <pre name="test">
      * #THROWS CloneNotSupportedException
      * Oppilaat oppilaat = new Oppilaat();
      * Oppilas k1 = new Oppilas(); Oppilas k2 = new Oppilas();
      * k1.rekisteroi(); k2.rekisteroi();
      * oppilaat.getLkm() === 0;
      * oppilaat.korvaaTaiLisaa(k1); oppilaat.getLkm() === 1;
      * oppilaat.korvaaTaiLisaa(k2); oppilaat.getLkm() === 2;
      * Oppilas k3 = k1.clone();
      * oppilaat.korvaaTaiLisaa(k3); oppilaat.getLkm() === 2;
      * </pre>
      */
     public void korvaaTaiLisaa(Oppilas oppilas) {
     	int id = oppilas.getID();
     	for (int i = 0; i < lkm; i++) {
     	    if (alkiot[i] == null) continue;
     		if (alkiot[i].getID() == id) {
     			alkiot[i] = oppilas;
     			muutettu = true;
     			return;
     		}
     	}
     	lisaa(oppilas);
     }
     
     
     /**
      * Poistaa oppilaan tietorakenteesta, jolla on sama ID kuin parametrina tuotu.
      * @param id jonka oppilas poistetaan
      * @return 1 mik‰li onnistuu, 0 mik‰li ei onnistu. 
      * @example
      * <pre name="test">
      * #import java.util.Collection;
      * Oppilaat oppilaat = new Oppilaat();
      * Oppilas o1 = new Oppilas(); Oppilas o2 = new Oppilas(); Oppilas o3 = new Oppilas();
      * o1.rekisteroi(); o2.rekisteroi(); o3.rekisteroi();
      * oppilaat.korvaaTaiLisaa(o1); oppilaat.korvaaTaiLisaa(o2); oppilaat.korvaaTaiLisaa(o3);
      * 
      * Collection<Oppilas> oppilaatColl = oppilaat.etsi();
      * oppilaatColl.contains(o1) === true;
      * 
      * oppilaat.getLkm() === 3;
      * oppilaat.poista(o1.getID()) === 1;
      * oppilaat.getLkm() === 2;
      * oppilaatColl = oppilaat.etsi();
      * oppilaatColl.contains(o1) === false;
      * 
      * oppilaat.poista(o2.getID()) === 1;
      * oppilaat.korvaaTaiLisaa(o1);
      * oppilaatColl = oppilaat.etsi();
      * oppilaatColl.contains(o1) === true;
      * oppilaatColl.contains(o2) === false;
      * 
      * oppilaat.poista(o3.getID()+1) === 0;
      * oppilaat.getLkm() === 2;
      * </pre>
      */
     public int poista(int id) {
         int indeksi = etsiID(id);
         if (indeksi < 0) return 0;
         lkm--;
         for (int i = indeksi; i < lkm; i++) 
             alkiot[i] = alkiot[i+1];
         alkiot[lkm] = null;
         muutettu = true;
         return 1;
     }
     
     
     /**
      * Etsii oppilaan ID:n perusteella.
      * @param id jota etsit‰‰n
      * @return oppilaan indeksi alkiot-taulukossa. -1 mik‰li ei lˆydy.
      * Oppilas o1 = new Oppilas(); Oppilas o2 = new Oppilas(); Oppilas o3 = new Oppilas();
      * o1.rekisteroi(); o2.rekisteroi(); o3.rekisteroi();
      * oppilaat.korvaaTaiLisaa(o1); oppilaat.korvaaTaiLisaa(o2); oppilaat.korvaaTaiLisaa(o3);
      * 
      * oppilaat.etsiID(o1.getID()+1) == o2.getID();
      * oppilaat.etsiID(o2.getID()+1) == o3.getID();
      * oppilaat.etsiID(o3.getID()+10) == -1;
      */
     public int etsiID(int id) {
         for (int i = 0; i<lkm ; i++)
             if (id == alkiot[i].getID()) return i;
         return -1;
     }
     
     
     /**
      * Palauttaa tiedon, onko muutettu. 
      * @return true, mik‰li muutettu
      */
     public boolean onkoMuutettu() {
         return muutettu;
     }
     
     /**
 	 * Testip‰‰ohjelma testaamaan oppilaskantaa
 	 * @param args ei k‰ytˆss‰
 	 */
     public static void main(String[] args) {
          Oppilaat oppilaat = new Oppilaat();
          Oppilas olli = new Oppilas(), olli2 = new Oppilas();
          olli.rekisteroi();
          olli.taytaOlliOppilas();
          olli2.rekisteroi();
          olli2.taytaOlliOppilas();
          
          try {
 		      oppilaat.lisaa(olli);
 	          oppilaat.lisaa(olli2);
 	          
 	          Collection<Oppilas> oppilaatColl = oppilaat.etsi();
              System.out.println("==================== Oppilaat-testi ====================");
          
              for (Oppilas o : oppilaatColl) {
                  System.out.println("Oppilaan id: " + o.getID());
         	      o.tulosta(System.out);
              }
              
              System.out.println("Poistetaan oppilas...");
              oppilaat.poista(olli.getID());
              oppilaatColl = oppilaat.etsi();
              for (Oppilas o : oppilaatColl) {
                  System.out.println("Oppilaan id: " + o.getID());
                  o.tulosta(System.out);
              }
             
           } catch (Exception e) {
  			 System.out.println(e.getMessage());
  		  }
     }
}

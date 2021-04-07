package openapu;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Random;

/**
 * Luokka joka tiet�� oppilaan tiedot ja osaa huolehtia tunnusnumeroista
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 */
public class Oppilas implements Cloneable {
	private int oppilasID;
	private String etunimi = "";
	private String sukunimi = "";
	private String hetu = "";
	
	private static int seuraavaNro = 1;
		
	
	/**
	 * P��ohjelma jolla testataan luokan toimintaa
	 * @param args ei k�yt�ss�
	 */
    public static void main(String[] args) {
    	Oppilas olli = new Oppilas(), olli2 = new Oppilas();
    	olli.rekisteroi();
    	olli2.rekisteroi();
    	olli.tulosta(System.out);
    	olli.taytaOlliOppilas();
    	olli.tulosta(System.out);
    	
    	olli2.taytaOlliOppilas();
    	olli2.tulosta(System.out);
    	
    	olli2.taytaOlliOppilas();
    	olli2.tulosta(System.out);	
    }
    
    
	/**
	 * Tyhj� muodostaja
	 */
	public Oppilas() {
		
	}
	
	
	/**
	 * Muodostaja oppilaalle, jolle annetaan parametrej�
	 * @param etunimi oppilaan etunimi
	 * @param sukunimi oppilaan sukunimi
	 * @param hetu oppilaan henkil�tunnuksen alkuosa
	 */
	public Oppilas(String etunimi, String sukunimi, String hetu) {
		this.etunimi = etunimi;
		this.sukunimi = sukunimi;
		this.hetu = hetu;		
	}
	
	
	/**
	 * Metodi joka t�ytt�� oppilaalle tiedot testaamista varten
	 * @param apuhetu henkil�tunnus oppilaalle
	 */
	public void taytaOlliOppilas(String apuhetu) {
		this.etunimi = "Olli";
		this.sukunimi = "Oppilas" + " " + Random.rand(1000, 9999);
		this.hetu = apuhetu;
	}
	
	
	/**
	 * Metodi joka t�ytt�� oppilaalle tiedot testaamista varten.
	 * Hl�tunnus arvotaan, jottei tule samoja
	 */
	public void taytaOlliOppilas() {
		String apuhetu = Random.arvoHetu();
        taytaOlliOppilas(apuhetu);
	}
	
	
	/**
	 * Antaa oppilaalle uniikin ID:n
	 * @return oppilaalle annettu ID
	 * @example
	 * <pre name="test">
	 * Oppilas olli = new Oppilas();
	 * olli.getID() === 0;
	 * olli.rekisteroi();
	 * Oppilas alma = new Oppilas();
	 * alma.getID();
	 * alma.rekisteroi();
	 * int o1 = olli.getID();
	 * int o2 = alma.getID();
	 * o2 - o1 === 1;
	 * </pre>
	 */
	public int rekisteroi() {
		this.oppilasID = seuraavaNro;
		seuraavaNro++;
		return oppilasID;
	}
	
	
	/**
	 * Palauttaa oppilaan ID:n
	 * @return oppilaan ID
	 */
	public int getID() {
		return this.oppilasID;
	}
	
	
	/**
	 * Asettaa ID:n ja varmistaa ett� seuraava numero on aina suurempi,
	 * kuin t�h�n menness� suurin.
	 * @param nr asetettava ID
	 */
	private void setID(int nr) {
		oppilasID = nr;
		if (oppilasID >= seuraavaNro) seuraavaNro = oppilasID + 1; 
	}
	
	
	
	/**
	 * Metodi tulostamista varten
	 * @param out tietovirta, johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("%03d", oppilasID) + " " + etunimi + " " + sukunimi + " " + hetu);
	}
	
	
    /**
     * Tulostetaan oppilaan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
	

	/**
	 * Tiedoston lukua varten. Lukee merkkijonosta tiedot uuden kurssikohtaisen tiedon luomiseksi.
	 * Erottimena |. 
	 * @param string parsittava merkkijono
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Oppilas oppilas1 = new Oppilas("Olli", "Oppilas", "050595");
	 * oppilas1.getID() === 0;
	 * oppilas1.parse("7		|Kalle			|   Valtteri    |   050595");
	 * oppilas1.getID() === 7;
	 * oppilas1.getEtunimi() === "Kalle";
	 * oppilas1.getSukunimi() === "Valtteri";
	 * oppilas1.getHetu() === "050595";
	 * oppilas1.merkkijonoksi() === "7|Kalle|Valtteri|050595";
	 * Oppilas oppilas2 = new Oppilas();
	 * oppilas2.rekisteroi();
	 * oppilas2.getID() === oppilas1.getID() + 1; 
	 * </pre>
	 */
	public void parse(String string){
		StringBuilder muokattava = new StringBuilder(string);
		setID(Mjonot.erota(muokattava, '|', oppilasID));
		etunimi 	 = Mjonot.erota(muokattava, '|', etunimi);
		sukunimi 	 = Mjonot.erota(muokattava, '|', sukunimi);
		hetu    	 = Mjonot.erota(muokattava, '|', hetu);
	}
	
	/**
	 * Muuttaa oppilaan merkkijonoksi muotoon ID|etunimi|sukunimi|hetu
	 * @return oppilaan tiedot merkkijonona
	 */
	public String merkkijonoksi(){
		return oppilasID + "|" + etunimi + "|" + sukunimi + "|" + hetu;
	}

	
	/**
	 * Palauttaa oppilaan etunimen.
	 * @return oppilaan etunimi
	 */
	public String getEtunimi() {
		return this.etunimi;
	}
	
	
	/**
	 * Palauttaa oppilaan sukunimen.
	 * @return oppilaan sukunimi
	 */
	public String getSukunimi() {
		return this.sukunimi;
	}
	
	
	/**
	 * Palauttaa oppilaan hetun.
	 * @return oppilaan hetu
	 */
	public String getHetu() {
		return this.hetu;
	}
	
	
	/**
	 * Asettaa oppilaalle etunimen.
	 * @param etunimi merkkijonona
	 * @return virhe, mik�li ongelmia
	 */
	public String setEtunimi(String etunimi) {
	    this.etunimi = etunimi;
	    return null;
	}
	   

	   /**
     * Asettaa oppilaalle sukunimen.
     * @param sukunimi merkkijonona
     * @return virhe, mik�li ongelmia
     */
    public String setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
        return null;
    }

    
    /**
     * Asettaa oppilaalle hetun.
     * @param hetu merkkijonona
     * @return virhe, mik�li ongelmia
     */
    public String setHetu(String hetu) {
        this.hetu = hetu;
        return null;
    }

	
	@Override
	public String toString() {
	    return etunimi + " " + sukunimi + " " + hetu;
	}
	
	
    /**
     * Luo kloonin oppilaasta.
     */
    @Override
    public Oppilas clone() throws CloneNotSupportedException {
        Oppilas uusi;
        uusi = (Oppilas) super.clone();
        return uusi;
    }
}

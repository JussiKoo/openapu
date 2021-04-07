package openapu;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Random;

/**
 * Luokka, joka tiet‰‰ kurssikohtaiset tiedot tietylle kurssille tietylle oppilaalle.
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @version 20.4.2018
 */
public class KurssikohtTieto {
	private int kurssiID = 0;
	private int oppilasID = 0;
	private String poissaolot = "";
	private String koetulokset = "";
	private String merkinnat = "";
	
	
	/**
	 * Muodostaja kurssikohtaiselle tiedolle
	 * @param kurssiID kurssin ID
	 * @param oppilasID oppilaan ID
	 * @throws SailoException poikkeus jos laitetaan ep‰validi ID
	 */
	public KurssikohtTieto(int kurssiID, int oppilasID) throws SailoException {
		if (kurssiID < 0 || oppilasID < 0) throw new SailoException("kurssin tai oppilaan ID ei ole validi.");
		this.kurssiID = kurssiID;
		this.oppilasID = oppilasID;
	}
	
	
	/**
	 * Tyhj‰ muodostaja.
	 */
	public KurssikohtTieto() {
		// oma alustus riitt‰‰
	}
	

	/**
	 * Tiedoston lukua varten. Lukee merkkijonosta tiedot uuden kurssikohtaisen tiedon luomiseksi.
	 * Erottimena |. 
	 * @param string parsittava merkkijono
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * KurssikohtTieto kktieto1 = new KurssikohtTieto(1,2);
	 * kktieto1.getKurssiID() === 1;
	 * kktieto1.getOppilasID() === 2;
	 * kktieto1.parse("1   |3|5|   10|   apua");
	 * kktieto1.getKurssiID() === 1;
	 * kktieto1.getOppilasID() === 3;
	 * kktieto1.getPoissaolot() === "5";
	 * kktieto1.getArvosana() === "10";
	 * kktieto1.getMerkinnat() === "apua";
	 * kktieto1.merkkijonoksi() === "1|3|5|10|apua"; 
	 * </pre>
	 */
	public void parse(String string){
		StringBuilder muokattava = new StringBuilder(string);
		kurssiID	 = Mjonot.erota(muokattava,'|', kurssiID);
		oppilasID	 = Mjonot.erota(muokattava,'|', oppilasID);
		poissaolot	 = Mjonot.erota(muokattava,'|', poissaolot);
		koetulokset	 = Mjonot.erota(muokattava,'|', koetulokset);
		merkinnat	 = Mjonot.erota(muokattava,'|', merkinnat);
	}
	
	
	/**
	 * Tiedoston tallentamista varten. Muuttaa kktiedon merkkijonoksi.
	 * @return merkkijono muotoa kurssiID|oppilasID|poissaolot|arvosana|merkinnat
	 */
	public String merkkijonoksi(){
		return kurssiID + "|" + oppilasID  + "|" + poissaolot + "|" + koetulokset  + "|" + merkinnat; 
	}
	
	
	/**
	 * Hakee poissaolojen lukum‰‰r‰n
	 * @return poissaolojen lukum‰‰r‰
	 */
	public String getPoissaolot() {
		return this.poissaolot;
	}
	
	
	/**
	 * Hakee arvosanan
	 * @return arvosana reaalilukuna
	 */
	public String getArvosana() {
		return this.koetulokset;
	}
	
	
    
    /**
     * hakee kurssin ID:n
     * @return kurssin ID
     */
    public int getKurssiID() {
    	return this.kurssiID;
    }
    
    
    /**
     * hakee oppilaan ID:n
     * @return oppilaann ID
     */
    public int getOppilasID() {
    	return this.oppilasID;
    }    
	
	
	/**
	 * Hakee kurssikohtaiset merkinn‰t
	 * @return merkint‰ String-oliona
	 */
	public String getMerkinnat() {
		return this.merkinnat;
	}
	
	
	/**
	 * Asettaa poissaolot.
	 * @param poissaolot merkkijonona
	 * @return virhe, mik‰li ongelmia
	 */
	public String setPoissaolot(String poissaolot) {
	    this.poissaolot = poissaolot;
        return null;
	}
	
	/**
	 * Asettaa merkinn‰t.
	 * @param merkinnat merkkijonona
	 * @return virhe, mik‰li ongelmia
	 */
	public String setMerkinnat(String merkinnat) {
	    this.merkinnat = merkinnat;
        return null;
	    
	}
	
	/**
	 * Asettaa arvosanat.
	 * @param arvosana merkkijonona
	 * @return virhe, mik‰li ongelmia
	 */
	public String setArvosana(String arvosana) {
        this.koetulokset = arvosana;
        return null;
	}
	
	
	/**
	 * T‰ytt‰‰ kurssikohtaiset tiedot valmiiksi
	 */
	public void taytaTiedot() {
		this.oppilasID = Random.rand(1,5);
		this.kurssiID = Random.rand(1,5);
		this.poissaolot = "3";
		this.koetulokset = "8.5";
		this.merkinnat = "Ei tehnyt kotil‰ksyj‰";
		
	}
	
	
	/**
	 * Kloonaa uuden kurssikohtaisen tiedon. Object-cloonitapa ei toiminut tuntemattomasta syyst‰.
	 * @example
	 * <pre name="test">
	 * KurssikohtTieto kkt1 = new KurssikohtTieto();
	 * kkt1.parse("1|3|5|10|apua");
	 * KurssikohtTieto kkt2 = kkt1.clone();
	 * kkt1 == kkt2 === false;
	 * kkt1.getOppilasID() === kkt2.getOppilasID();
	 * </pre>
	 */
	@Override
    public KurssikohtTieto clone() {
	    KurssikohtTieto uusi = new KurssikohtTieto();
	    uusi.parse(this.merkkijonoksi());
	    return uusi;
	}
	
}

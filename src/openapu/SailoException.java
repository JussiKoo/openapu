package openapu;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
 * @author Akseli Rauhansalo a.rauhansalo@gmail.com
 * @author Jussi Kauppinen jussi.m.o.kauppinen@student.jyu.fi
 * @version 19.2.2018
 *
 */
public class SailoException extends Exception{
    private static final long serialVersionUID = 1L;
    
    /**
     * Poikkeuksen muodostaja
     * @param viesti poikkeuksen viesti muodostajalle
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}

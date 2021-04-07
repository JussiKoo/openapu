package kanta;

/**
 * Luokka satunnaisuutta luoville aliohjelmille.
 * @author Akseli Rauhansalo
 * @author Jussi Kauppinen
 * @version 21.2.2018
 *
 */
public class Random {
    /**
     * Arvotaan satunnainen kokonaisluku v�lille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yl�raja
     * @return satunnainen luku v�lilt� [ala,yla]
     */
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
    }
    
    
    /**
     * Arvotaan satunnainen henkil�tunnuksen alkuosa, joka t�ytt�� hetun ehdot    
     * @return satunnainen laillinen loppuosaton henkil�tunnus
     */
    public static String arvoHetu() {
        String apuhetu = String.format("%02d",Random.rand(1,28))   +
        String.format("%02d",Random.rand(1,12))   +
        String.format("%02d",Random.rand(1,99));
        return apuhetu;    
    }
}

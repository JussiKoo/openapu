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
     * Arvotaan satunnainen kokonaisluku välille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yläraja
     * @return satunnainen luku väliltä [ala,yla]
     */
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
    }
    
    
    /**
     * Arvotaan satunnainen henkilötunnuksen alkuosa, joka täyttää hetun ehdot    
     * @return satunnainen laillinen loppuosaton henkilötunnus
     */
    public static String arvoHetu() {
        String apuhetu = String.format("%02d",Random.rand(1,28))   +
        String.format("%02d",Random.rand(1,12))   +
        String.format("%02d",Random.rand(1,99));
        return apuhetu;    
    }
}

package kanta;

/**
 * Ohjelmalla testataan p‰iv‰m‰‰r‰n lis‰yst‰.
 * P‰iv‰m‰‰r‰ on talukossa muodossa pp,kk,vv
 * Taulukko ei ole hyv‰ ratkaisu, parempi on kunnon
 * Pvm-luokka.  Tulee myˆhemmiss‰ demoissa.
 * @author Vesa Lappalainen
 * @version 1.0, 17.01.2002
 * @since 1.4
 */

public class LisaaPvm {

    private final static int PP = 0, KK = 1, VV = 2;

    /** Taulukko kuukausien pituuksista. Oma rivi  karkausvuosille */
    public static final int KPITUUDET[][] = {
            // 1  2  3  4  5  6  7  8  9 10 11 12
            { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
            { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }
    };


    /**
     * Palautetaan tieto siit‰ onko tutkittava vuosi karkausvuosi vai ei
     * @param vv tutkittava vuosi
     * @return 1 jos on karkausvuosi ja 0 jos ei ole
     * @example
     * <pre name="test">
     *   karkausvuosi(1900) === 0
     *   karkausvuosi(1900) === 0
     *   karkausvuosi(1901) === 0
     *   karkausvuosi(1996) === 1
     *   karkausvuosi(2000) === 1
     *   karkausvuosi(2001) === 0
     *   karkausvuosi(2004) === 1
     * </pre>
     */
    public static int karkausvuosi(int vv) {
        if ( vv % 400 == 0 ) return 1;
        if ( vv % 100 == 0 ) return 0;
        if ( vv % 4 == 0 ) return 1;
        return 0;
    }


    /**
     * Aliohjelmalla lis‰t‰‰n taulukossa olevaa p‰iv‰m‰‰r‰‰ yhdell‰ 
     * @param pvm lis‰tt‰v‰ p‰iv‰m‰‰r‰
     * @example
     * <pre name="test">
     * int [] pv; 
     * pv = new int[]{$pp1,$kk1,$vv1}; lisaaPvm(pv); pv[0] === $pp2; pv[1] === $kk2; pv[2] === $vv2;
     * 
     * | $pp1 | $kk1 | $vv1 |   $pp2 | $kk2 | $vv2 |
     * ----------------------------------------------
     * |   1  |   1  | 1999 |    2   |  1   | 1999 |    
     * |  31  |   1  | 1999 |    1   |  2   | 1999 |    
     * |  27  |   2  | 1999 |   28   |  2   | 1999 |    
     * |      |      |  --- |    1   |  3   | 1999 |    
     * |      |      |  --- |    2   |  3   | 1999 |    
     * |  29  |   4  | 1999 |   30   |  4   | 1999 |    
     * |      |      |  --- |    1   |  5   | 1999 |    
     * |  30  |  12  | 1999 |   31   | 12   | 1999 |    
     * |      |      |  --- |    1   |  1   | 2000 |    
     * |      |      |  --- |    2   |  1   | 2000 |    
     * |  27  |   2  | 2004 |   28   |  2   | 2004 |    
     * |      |      |  --- |   29   |  2   | 2004 |    
     * |      |      |  --- |    1   |  3   | 2004 |    
     * |  27  |  14  | 2004 |   28   |  2   | 2004 | #THROWS ArrayIndexOutOfBoundsException    
     * </pre>
     */
    public static void lisaaPvm(int pvm[]) {
        ++pvm[PP];
        int kv = karkausvuosi(pvm[VV]); //palauttaa 0 jos ei, 1 jos on
        int pv_lkm = KPITUUDET[kv][pvm[KK] - 1];
        if ( pvm[PP] > pv_lkm ) {
            pvm[PP] = 1;
            ++pvm[KK];
        }
        if ( pvm[KK] > 12 ) {
            pvm[KK] = 1;
            ++pvm[VV];
        }
    }


    private static void testaa(int pvm[], int kpl) {
        System.out.println("Aluksi: " + pvm[PP] + "." + pvm[KK] + "." + pvm[VV]);
        for (int i = 0; i < kpl; i++) {
            lisaaPvm(pvm);
            System.out.println(" +1 =>  " + pvm[PP] + "." + pvm[KK] + "." + pvm[VV]);
        }
    }


    /**
     * Testataan lisaaPvm aliohjelmaa
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        int pvm[] = { 27, 2, 1999 };
        testaa(pvm, 3);
        int pvm2[] = { 27, 2, 2000 };
        testaa(pvm2, 3);
        int pvm3[] = { 31, 12, 2002 };
        testaa(pvm3, 2);
    }
}
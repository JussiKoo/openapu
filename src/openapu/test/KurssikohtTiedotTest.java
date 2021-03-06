package openapu.test;
// Generated by ComTest BEGIN
import fi.jyu.mit.ohj2.VertaaTiedosto;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
import openapu.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2021.04.08 01:18:39 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class KurssikohtTiedotTest {


  // Generated by ComTest BEGIN
  /** testKorvaaTaiLisaa50 */
  @Test
  public void testKorvaaTaiLisaa50() {    // KurssikohtTiedot: 50
    KurssikohtTieto kkt1 = new KurssikohtTieto(), kkt2 = new KurssikohtTieto(), kkt3 = new KurssikohtTieto(), kkt4 = new KurssikohtTieto(); 
    kkt1.parse("1|3|5|10|apua"); 
    kkt2.parse("1|4|5|10|help"); 
    kkt3.parse("2|3|10|15|halp"); 
    KurssikohtTiedot kktd = new KurssikohtTiedot(); kktd.korvaaTaiLisaa(kkt1); kktd.korvaaTaiLisaa(kkt2); kktd.korvaaTaiLisaa(kkt3); 
    assertEquals("From: KurssikohtTiedot line: 57", kkt1, kktd.haeKktieto(1,3)); 
    assertEquals("From: KurssikohtTiedot line: 58", kkt2, kktd.haeKktieto(1,4)); 
    assertEquals("From: KurssikohtTiedot line: 59", kkt3, kktd.haeKktieto(2,3)); 
    kkt4.parse("1|4|10|10|hulp"); 
    kktd.korvaaTaiLisaa(kkt4); 
    assertEquals("From: KurssikohtTiedot line: 63", kkt4, kktd.haeKktieto(1,4)); 
    kkt2.parse("2|3|10|15|hulp"); 
    kktd.korvaaTaiLisaa(kkt2); 
    assertEquals("From: KurssikohtTiedot line: 66", "hulp", kktd.haeKktieto(2,3).getMerkinnat()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta96 
   * @throws SailoException when error
   * @throws IOException when error
   */
  @Test
  public void testLueTiedostosta96() throws SailoException, IOException {    // KurssikohtTiedot: 96
    VertaaTiedosto.kirjoitaTiedosto("kktiedotkoe.dat",
    "5|3|10|8|Ht ok\n" +
    "15|9|3|9|\n" +
    "22|3|5|5|ryhti?"
    ); 
    KurssikohtTiedot kktiedot1 = new KurssikohtTiedot(); 
    kktiedot1.lueTiedostosta("kktiedotkoe"); 
    assertEquals("From: KurssikohtTiedot line: 107", true, kktiedot1.onkoKurssilla(5,3)); 
    assertEquals("From: KurssikohtTiedot line: 108", false, kktiedot1.onkoKurssilla(15,3)); 
    assertEquals("From: KurssikohtTiedot line: 109", false, kktiedot1.onkoKurssilla(3,22)); 
    kktiedot1.lisaa(new KurssikohtTieto(15,3)); 
    assertEquals("From: KurssikohtTiedot line: 111", true, kktiedot1.onkoKurssilla(15,3)); 
    kktiedot1.tallenna(); 
    kktiedot1 = new KurssikohtTiedot();  // tyhjennet??n tiedot
    kktiedot1.lueTiedostosta("kktiedotkoe"); 
    assertEquals("From: KurssikohtTiedot line: 116", true, kktiedot1.onkoKurssilla(15,3)); 
    VertaaTiedosto.tuhoaTiedosto("kktiedotkoe.dat"); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testAnnaOppilasIDTiedot195 
   * @throws SailoException when error
   */
  @Test
  public void testAnnaOppilasIDTiedot195() throws SailoException {    // KurssikohtTiedot: 195
    KurssikohtTiedot kkTiedot = new KurssikohtTiedot(); 
    KurssikohtTieto tieto11 = new KurssikohtTieto(1,1); kkTiedot.lisaa(tieto11); 
    KurssikohtTieto tieto12 = new KurssikohtTieto(1,2); kkTiedot.lisaa(tieto12); 
    KurssikohtTieto tieto21 = new KurssikohtTieto(2,1); kkTiedot.lisaa(tieto21); 
    KurssikohtTieto tieto22 = new KurssikohtTieto(2,2); kkTiedot.lisaa(tieto22); 
    KurssikohtTieto tieto23 = new KurssikohtTieto(2,3); kkTiedot.lisaa(tieto23); 
    KurssikohtTieto tieto14 = new KurssikohtTieto(1,4); kkTiedot.lisaa(tieto14); 
    KurssikohtTieto tieto24 = new KurssikohtTieto(2,4); kkTiedot.lisaa(tieto24); 
    KurssikohtTieto tieto31 = new KurssikohtTieto(3,1); kkTiedot.lisaa(tieto31); 
    List<Integer> loytyneet; 
    loytyneet = kkTiedot.annaOppilasIDTiedot(1); 
    assertEquals("From: KurssikohtTiedot line: 210", 3, loytyneet.size()); 
    assertEquals("From: KurssikohtTiedot line: 211", true, loytyneet.get(0) == tieto11.getOppilasID()); 
    assertEquals("From: KurssikohtTiedot line: 212", true, loytyneet.get(1) == tieto12.getOppilasID()); 
    loytyneet = kkTiedot.annaOppilasIDTiedot(4); 
    assertEquals("From: KurssikohtTiedot line: 214", 0, loytyneet.size()); 
    loytyneet = kkTiedot.annaOppilasIDTiedot(4); 
    loytyneet = kkTiedot.annaOppilasIDTiedot(2); 
    assertEquals("From: KurssikohtTiedot line: 217", 4, loytyneet.size()); 
    assertEquals("From: KurssikohtTiedot line: 218", true, loytyneet.get(1) == tieto22.getOppilasID()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testHaeKktieto236 
   * @throws SailoException when error
   */
  @Test
  public void testHaeKktieto236() throws SailoException {    // KurssikohtTiedot: 236
    KurssikohtTiedot k = new KurssikohtTiedot(); 
    KurssikohtTieto kt1 = new KurssikohtTieto(1,3); 
    KurssikohtTieto kt2 = new KurssikohtTieto(2,3); 
    KurssikohtTieto kt3 = new KurssikohtTieto(4,2); 
    k.lisaa(kt1); 
    k.lisaa(kt2); 
    k.lisaa(kt3); 
    assertEquals("From: KurssikohtTiedot line: 245", null, k.haeKktieto(1,2)); 
    assertEquals("From: KurssikohtTiedot line: 246", kt1, k.haeKktieto(1,3)); 
    assertEquals("From: KurssikohtTiedot line: 247", kt2, k.haeKktieto(2,3)); 
    assertEquals("From: KurssikohtTiedot line: 248", kt3, k.haeKktieto(4,2)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoista277 
   * @throws SailoException when error
   */
  @Test
  public void testPoista277() throws SailoException {    // KurssikohtTiedot: 277
    KurssikohtTiedot k = new KurssikohtTiedot(); 
    KurssikohtTieto kt1 = new KurssikohtTieto(1,3); 
    KurssikohtTieto kt2 = new KurssikohtTieto(2,3); 
    KurssikohtTieto kt3 = new KurssikohtTieto(4,2); 
    k.lisaa(kt1); 
    k.lisaa(kt2); 
    k.lisaa(kt3); 
    assertEquals("From: KurssikohtTiedot line: 286", true, k.contains(kt1)); 
    k.poista(1,3); 
    assertEquals("From: KurssikohtTiedot line: 288", false, k.contains(kt1)); 
    assertEquals("From: KurssikohtTiedot line: 289", true, k.contains(kt2)); 
    k.poista(2,4); 
    assertEquals("From: KurssikohtTiedot line: 291", true, k.contains(kt2) && k.contains(kt3)); 
    k.poista(4,2); 
    assertEquals("From: KurssikohtTiedot line: 293", false, k.contains(kt3)); 
  } // Generated by ComTest END
}
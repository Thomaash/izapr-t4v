package izapr.banka;

import java.util.HashMap;
import java.util.Map.Entry;

/**
* Naše super bezpečná banka. Vaše peníze nikdo neukradne*!!!!
*
* * Poplatky se za krádež NEPOVAŽUJÍ!
*/
public class Banka {

/**
* Všechny naše účty maj stejnej úrok (bez jednotek, aby se dalo rovnou
* násobit).
*/
public static double urok = 0.003;

/**
* Měsíční poplatek za vedení účtu u naší banky.
*/
public static double mpzvuunb = 300;

/**
* Účty všechn našich klientů.
*/
private static final HashMap<Long, Ucet> Ucty = new HashMap<>();

/**
* Nová banka.
*
* @param mpzvuunb Měsíční poplatek za vedení účtu u naší banky.
* @param urok Úrok na našich účtech (v procentech).
*/
Banka(long mpzvuunb, float urok) {
Banka.mpzvuunb = mpzvuunb;
Banka.urok = urok;
}

/**
* Nový účet s číslem na přání (pokud je volný).
*
* @param jap Jméno a příjmení klienta.
* @param v Prvotní vklad na účet.
* @param x Požadovaný číslo účtu.
* @return Vytvořený účet.
* @throws izapr.banka.Banka.CisloJeObsazeny Pokud je číslo už obsazený.
*/
public static Ucet zalozUcetSCislemNaPrani(String jap, long v, long x) throws CisloJeObsazeny {
Ucet u = new Ucet(x, jap, v);
Banka.Ucty.put(u.cislo, u);
return u;
}

/**
* Nový účet.
*
* @param jap Jméno a příjmení klienta.
* @param v Prvotní vklad na účet.
* @return Vytvořený účet.
*/
public static Ucet zalozUcet(String jap, long v) {
Ucet u = new Ucet(jap, v);
Banka.Ucty.put(u.cislo, u);
return u;
}

/**
* Zjisti zůstatek na účtu.
*
* @param cisloUctu Číslo účtu klienta.
*/
public static void zjistiZustatek(long cisloUctu) {
System.out.println("Zůstatek je: " + Banka.Ucty.get(cisloUctu).zjistiZustatek());
}

/**
* Zrušení účtu.
*
* @param cisloUctu Číslo účtu klienta.
* @return Zrušený účet.
*/
public static Ucet zrusUcet(long cisloUctu) {
Ucet u = Banka.Ucty.get(cisloUctu);
Banka.Ucty.remove(u);
return u;
}

/**
* Strhne měsíční poplatky na všech účtech.
*
* @return Seznam účtů, ze kterých nešlo strhnout poplatky kvůli
* nedostatečnýmu zůstatku.
*/
public static HashMap<Long, Ucet> strhniMesicniPoplatky() {
for (long ucet : Banka.Ucty.keySet()) {
Ucet ucet2 = Banka.Ucty.get(ucet);
ucet2.zustatek -= Banka.mpzvuunb;
}

return Banka.Ucty;
}

/**
* Pričte úroky na všechny účty.
*
* @return Seznam účtů, ze kterých nešlo strhnout poplatky kvůli
* nedostatečnýmu zůstatku.
*/
public static HashMap<Long, Ucet> prictiUroky() {
for (long ucet : Banka.Ucty.keySet()) {
Ucet ucet2 = Banka.Ucty.get(ucet);
ucet2.zustatek += ucet2.zustatek * (Banka.urok);
}

return Banka.Ucty;
}

/**
* Zjisti disponibilní zůstatek na účtu.
*
* @param cislo1 Číslo účtu klienta.
* @return Zůstatek - blokovaná částka.
*/
public static String zjistiDisponibilniZustatek(long cislo1) {
long cislo3 = 0;
for (Entry<Long, Ucet> ucty : Banka.Ucty.entrySet()) {
if (ucty.getKey() == cislo1) {
cislo3 = cislo1;
}
}

return Banka.Ucty.get(cislo3).zjistiDisponibilniZustatek();
}

public class CisloJeObsazeny extends Exception {
}

public class UcetNeexistuje extends Exception {
}

}

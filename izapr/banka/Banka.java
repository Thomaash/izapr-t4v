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
    public double urok = 0.003;

    /**
     * Měsíční poplatek za vedení účtu u naší banky.
     */
    public double mpzvuunb = 300;

    /**
     * Účty všechn našich klientů.
     */
    private final HashMap<Long, Ucet> Ucty = new HashMap<>();

    /**
     * Nová banka.
     *
     * @param mpzvuunb Měsíční poplatek za vedení účtu u naší banky.
     * @param urok Úrok na našich účtech (v procentech).
     */
    Banka(long mpzvuunb, float urok) {
        this.mpzvuunb = mpzvuunb;
        this.urok = urok;
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
    public Ucet zalozUcetSCislemNaPrani(String jap, long v, long x) throws CisloJeObsazeny {
        Ucet u = new Ucet(x, jap, v);
        this.Ucty.put(u.cislo, u);
        return u;
    }

    /**
     * Nový účet.
     *
     * @param jap Jméno a příjmení klienta.
     * @param v Prvotní vklad na účet.
     * @return Vytvořený účet.
     */
    public Ucet zalozUcet(String jap, long v) {
        Ucet u = new Ucet(jap, v);
        this.Ucty.put(u.cislo, u);
        return u;
    }

    /**
     * Zjisti zůstatek na účtu.
     *
     * @param cisloUctu Číslo účtu klienta.
     * @throws izapr.banka.Banka.UcetNeexistuje Pokud žádný účet s timdle číslem u týdle banky veden neni.
     */
    public void zjistiZustatek(long cisloUctu) throws UcetNeexistuje {
        final Ucet ucet = this.Ucty.get(cisloUctu);
        if (ucet == null) {
            throw new UcetNeexistuje("Účet s číslem " + cisloUctu + " nenalezen.");
        } else {
            System.out.println("Zůstatek je: " + this.Ucty.get(cisloUctu).zjistiZustatek());
        }
    }

    /**
     * Zrušení účtu.
     *
     * @param cisloUctu Číslo účtu klienta.
     * @return Zrušený účet.
     */
    public Ucet zrusUcet(long cisloUctu) {
        Ucet u = this.Ucty.get(cisloUctu);
        this.Ucty.remove(u);
        return u;
    }

    /**
     * Strhne měsíční poplatky na všech účtech.
     *
     * @return Seznam účtů, ze kterých nešlo strhnout poplatky kvůli
     * nedostatečnýmu zůstatku.
     */
    public HashMap<Long, Ucet> strhniMesicniPoplatky() {
        for (long ucet : this.Ucty.keySet()) {
            Ucet ucet2 = this.Ucty.get(ucet);
            ucet2.zustatek -= this.mpzvuunb;
        }

        return this.Ucty;
    }

    /**
     * Pričte úroky na všechny účty.
     *
     * @return Seznam účtů, ze kterých nešlo strhnout poplatky kvůli
     * nedostatečnýmu zůstatku.
     */
    public HashMap<Long, Ucet> prictiUroky() {
        for (long ucet : this.Ucty.keySet()) {
            Ucet ucet2 = this.Ucty.get(ucet);
            ucet2.zustatek += ucet2.zustatek * (this.urok);
        }

        return this.Ucty;
    }

    /**
     * Zjisti disponibilní zůstatek na účtu.
     *
     * @param cislo1 Číslo účtu klienta.
     * @return Zůstatek - blokovaná částka.
     */
    public long zjistiDisponibilniZustatek(long cislo1) {
        long cislo3 = 0;
        for (Entry<Long, Ucet> ucty : this.Ucty.entrySet()) {
            if (ucty.getKey() == cislo1) {
                cislo3 = cislo1;
            }
        }

        return this.Ucty.get(cislo3).zjistiDisponibilniZustatek();
    }

    public class CisloJeObsazeny extends Exception {
    }

    public class UcetNeexistuje extends Exception {

        public UcetNeexistuje() {
            super();
        }

        public UcetNeexistuje(final String text) {
            super(text);
        }

    }

}

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
    private double urok = 0.003;

    /**
     * Měsíční poplatek za vedení účtu u naší banky.
     */
    private long mesicniPoplatekZaVedeniUctu = 300;

    /**
     * Účty všechn našich klientů.
     */
    private final HashMap<Long, Ucet> ucty = new HashMap<>();

    /**
     * Nová banka.
     *
     * @param mesicniPoplatekZaVedeniUctu Měsíční poplatek za vedení účtu u naší
     * banky.
     * @param urok Úrok na našich účtech (v procentech).
     */
    Banka(long mesicniPoplatekZaVedeniUctu, float urok) {
        this.mesicniPoplatekZaVedeniUctu = mesicniPoplatekZaVedeniUctu;
        this.urok = urok;
    }

    /**
     * Nový účet s číslem na přání (pokud je volný).
     *
     * @param jmenoAPrijmeniKlienta Jméno a příjmení klienta.
     * @param prvotniVklad Prvotní vklad na účet.
     * @param cisloUctu Požadovaný číslo účtu.
     * @return Číslo nově vytvořenýho účtu.
     * @throws izapr.banka.Banka.CisloJeObsazeny Pokud je číslo už obsazený.
     */
    public long zalozUcetSCislemNaPrani(String jmenoAPrijmeniKlienta, long prvotniVklad, long cisloUctu) throws CisloJeObsazeny {
        Ucet ucet = new Ucet(cisloUctu, jmenoAPrijmeniKlienta, prvotniVklad);
        this.ucty.put(ucet.zjistiCislo(), ucet);

        return cisloUctu;
    }

    /**
     * Nový účet.
     *
     * @param jmenoAPrijmeniKlienta Jméno a příjmení klienta.
     * @param prvotniVklad Prvotní vklad na účet.
     * @return Číslo nově vytvořenýho účtu.
     */
    public long zalozUcet(String jmenoAPrijmeniKlienta, long prvotniVklad) {
        Ucet ucet = new Ucet(jmenoAPrijmeniKlienta, prvotniVklad);
        this.ucty.put(ucet.zjistiCislo(), ucet);

        return ucet.zjistiCislo();
    }

    /**
     * Zjisti zůstatek na účtu.
     *
     * @param cisloUctu Číslo účtu klienta.
     * @return Zůstatek na požadovan účtu.
     * @throws izapr.banka.Banka.UcetNeexistuje Pokud žádný účet s timdle číslem
     * u týdle banky veden neni.
     */
    public long zjistiZustatek(long cisloUctu) throws UcetNeexistuje {
        final Ucet ucet = this.ucty.get(cisloUctu);
        if (ucet == null) {
            throw new UcetNeexistuje("Účet s číslem " + cisloUctu + " nenalezen.");
        } else {
            return this.ucty.get(cisloUctu).zjistiZustatek();
        }
    }

    void nastavUrok(double urok) {
        this.urok = urok;
    }

    void nastavMesicniPoplatek(long mesicniPoplatek) {
        this.mesicniPoplatekZaVedeniUctu = mesicniPoplatek;
    }

    /**
     * Zrušení účtu.
     *
     * @param cisloUctu Číslo účtu klienta.
     * @throws izapr.banka.Banka.UcetNeexistuje Pokud účet s danym číslem u
     * týdle banky neni.
     */
    public void zrusUcet(long cisloUctu) throws UcetNeexistuje {
        if (this.ucty.containsKey(cisloUctu)) {
            this.ucty.remove(cisloUctu);
        } else {
            throw new UcetNeexistuje("Účet s číslem " + cisloUctu + " neexistuje.");
        }
    }

    /**
     * Strhne měsíční poplatky na všech účtech.
     *
     * @return Seznam účtů, ze kterých nešlo strhnout poplatky kvůli
     * nedostatečnýmu zůstatku.
     */
    HashMap<Long, Ucet> strhniMesicniPoplatky() {
        final HashMap<Long, Ucet> uctyVMinusu = new HashMap<>();

        for (Ucet ucet : this.ucty.values()) {
            if (!ucet.vnitrniVyber(this.mesicniPoplatekZaVedeniUctu)) {
                uctyVMinusu.put(ucet.zjistiCislo(), ucet);
            }
        }

        return uctyVMinusu;
    }

    /**
     * Pričte úroky na všechny účty.
     */
    void prictiUroky() {
        for (Ucet ucet : this.ucty.values()) {
            if (ucet.zjistiZustatek() > 0) {
                ucet.vloz((long) (ucet.zjistiZustatek() * this.urok));
            }
        }
    }

    /**
     * Zjisti disponibilní zůstatek na účtu.
     *
     * @param cisloUctu Číslo účtu klienta.
     * @return Zůstatek - blokovaná částka.
     * @throws izapr.banka.Banka.UcetNeexistuje Pokud požadovanej účet u týdle
     * banky neexistuje.
     */
    public long zjistiDisponibilniZustatek(long cisloUctu) throws UcetNeexistuje {
        if (this.ucty.containsKey(cisloUctu)) {
            return this.ucty.get(cisloUctu).zjistiDisponibilniZustatek();
        } else {
            throw new UcetNeexistuje("Účet s číslem " + cisloUctu + " neexistuje.");
        }
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

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
    private double mesicniPoplatekZaVedeniUctu = 300;

    /**
     * Účty všechn našich klientů.
     */
    private final HashMap<Long, Ucet> ucty = new HashMap<>();

    /**
     * Nová banka.
     *
     * @param mesicniPoplatekZaVedeniUctu Měsíční poplatek za vedení účtu u naší banky.
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
     * @return Vytvořený účet.
     * @throws izapr.banka.Banka.CisloJeObsazeny Pokud je číslo už obsazený.
     */
    public Ucet zalozUcetSCislemNaPrani(String jmenoAPrijmeniKlienta, long prvotniVklad, long cisloUctu) throws CisloJeObsazeny {
        Ucet ucet = new Ucet(cisloUctu, jmenoAPrijmeniKlienta, prvotniVklad);
        this.ucty.put(ucet.zjistiCislo(), ucet);
        return ucet;
    }

    /**
     * Nový účet.
     *
     * @param jmenoAPrijmeniKlienta Jméno a příjmení klienta.
     * @param prvotniVklad Prvotní vklad na účet.
     * @return Vytvořený účet.
     */
    public Ucet zalozUcet(String jmenoAPrijmeniKlienta, long prvotniVklad) {
        Ucet ucet = new Ucet(jmenoAPrijmeniKlienta, prvotniVklad);
        this.ucty.put(ucet.zjistiCislo(), ucet);
        return ucet;
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

    void nastavMesicniPoplatek(double mesicniPoplatek) {
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
        for (long ucet : this.ucty.keySet()) {
            Ucet ucet2 = this.ucty.get(ucet);
            ucet2.vnitrniVyber((long) this.mesicniPoplatekZaVedeniUctu);
        }

        return this.ucty;
    }

    /**
     * Pričte úroky na všechny účty.
     *
     * @return Seznam účtů, ze kterých nešlo strhnout poplatky kvůli
     * nedostatečnýmu zůstatku.
     */
    HashMap<Long, Ucet> prictiUroky() {
        for (long ucet : this.ucty.keySet()) {
            Ucet ucet2 = this.ucty.get(ucet);
            ucet2.vloz((long) (ucet2.zjistiZustatek() * (this.urok)));
        }

        return this.ucty;
    }

    /**
     * Zjisti disponibilní zůstatek na účtu.
     *
     * @param cislo1 Číslo účtu klienta.
     * @return Zůstatek - blokovaná částka.
     */
    public long zjistiDisponibilniZustatek(long cislo1) {
        long cislo3 = 0;
        for (Entry<Long, Ucet> ucty : this.ucty.entrySet()) {
            if (ucty.getKey() == cislo1) {
                cislo3 = cislo1;
            }
        }

        return this.ucty.get(cislo3).zjistiDisponibilniZustatek();
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

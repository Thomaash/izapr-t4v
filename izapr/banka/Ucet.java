package izapr.banka;

import java.util.HashSet;

/**
 * Naše vnitřní třída pro údaje o účtech.
 */
public class Ucet {

    /**
     * Sem se vždy při vytvoření účtu uloží jeho číslo aby se zamezilo situacím,
     * kdy má víc různejch účtů (třeba i u různejch bank) stejný číslo.
     */
    private static HashSet<Long> pouzityCisla = new HashSet<>();

    /**
     * Číslo účtu.
     */
    private long cislo;

    /**
     * Vlastník účtu.
     */
    private String vlastnik;

    /**
     * Zůstatek na účtu (součet peněz k dispozici + blokovaných prostředků).
     */
    private long zustatek = 0;

    /**
     * Blokovaná částka na účtu. To jsou platby kartou atp. co se vyplatí teprv
     * v budoucnu.
     */
    private long blokovano = 0;

    /**
     * Vytvoření účtu se zadanym číslem.
     *
     * @param c Číslo účtu (nelze vytvořit účet, pokud už je daný číslo použitý
     * u jinýho účtu).
     * @param v Jméno a příjmení vlastníka účtu.
     * @param o Prvotní vklad na účet.
     */
    public Ucet(long c, String v, long o) {
        if (v != null && v != "" && c > 0 && o > 0) {
            this.zustatek = o;
            this.cislo = c;
            this.vlastnik = v;
        } else {
            System.out.println("Účet musí být nějaký jméno.");
            System.exit(0);
        }
    }

    /**
     * Vytvoření účtu pro novýho klienta.
     *
     * @param v Jméno a příjmení vlastníka účtu.
     * @param o Prvotní vklad na účet.
     */
    public Ucet(String v, long o) {
        this((long) (Math.random() * 100), v, o);
    }

    /**
     * Vložení částky na účet.
     *
     * @param castka Množství korun, co se přičte k zůstatku.
     */
    public void vloz(long castka) {
        this.zustatek += castka;
    }

    /**
     * Výběr z účtu
     *
     * @param castka Množství korun, co se odečte od zůstatku.
     * @return True pokud výběr proběhl, false pokud na účtu neni k dispozici
     * dost peněz.
     */
    public boolean vyber(long castka) {
        if (castka > this.zustatek) {
            this.zustatek -= castka;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Najití nepoužitýho čísla účtu.
     *
     * @return Nepoužitý číslo účtu.
     */
    public static long najdiNCU() {
        long i = 0;

        for (;; ++i) {
            if (Ucet.pouzityCisla.contains(i)) {
                continue;
            } else {
                break;
            }
        }

        return i;
    }

    /**
     * Výběr z účtu pro vnitřní použití uvnitř banky. Při výběru například
     * poplatků se může, narozdíl třeba od výběru z bankomatu, účet dostat do
     * záporu.
     *
     * @param poplatek Množství korun, co se odečte od zůstatku.
     * @return True pokud výběr proběhl, false pokud na účtu neni k dispozici
     * dost peněz.
     */
    public boolean vnitrniVyber(long poplatek) {
        this.zustatek -= poplatek;
        return true;
    }

    void blokujCastku(long blokovanaCastka) throws NedostatekProstredku {
        if (this.blokovano + blokovanaCastka <= this.zustatek) {
            this.blokovano += blokovanaCastka;
        } else {
            throw new NedostatekProstredku();
        }
    }

    public long zjistiCislo() {
        return this.cislo;
    }

    public String zjistiVlastnika() {
        return this.vlastnik;
    }

    /**
     * Zjištění zůstatku na účtu.
     *
     * @return Kolik je na účtu korun.
     */
    public long zjistiZustatek() {
        return this.zustatek;
    }

    /**
     * Zjištění zůstatku na účtu.
     *
     * @return Kolik je na účtu korun.
     */
    public long zjistiDisponibilniZustatek() {
        return (this.zustatek - this.blokovano);
    }

    public class NedostatekProstredku extends Exception {

        public NedostatekProstredku() {
            super("Na účtu " + cislo + " neni dostatek finančních prostředků.");
        }

    }
}

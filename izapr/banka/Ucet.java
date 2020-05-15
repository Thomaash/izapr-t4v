package izapr.banka;

import java.util.HashSet;

/**
 * Naše vnitřní třída pro údaje o účtech.
 */
class Ucet {

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
     * @param cisloUctu Číslo účtu (nelze vytvořit účet, pokud už je daný číslo
     * použitý u jinýho účtu).
     * @param jmenoAPrijmeniVlastnika Jméno a příjmení vlastníka účtu.
     * @param prvotniVklad Prvotní vklad na účet.
     */
    Ucet(long cisloUctu, String jmenoAPrijmeniVlastnika, long prvotniVklad) {
        if (jmenoAPrijmeniVlastnika != null && !jmenoAPrijmeniVlastnika.equals("") && cisloUctu > 0 && prvotniVklad > 0) {
            this.zustatek = prvotniVklad;
            this.cislo = cisloUctu;
            this.vlastnik = jmenoAPrijmeniVlastnika;
        } else {
            System.out.println("Účet musí být nějaký jméno.");
            System.exit(0);
        }
    }

    /**
     * Vytvoření účtu pro novýho klienta.
     *
     * @param jmenoAPrijmeniVlastnika Jméno a příjmení vlastníka účtu.
     * @param prvotniVklad Prvotní vklad na účet.
     */
    Ucet(String jmenoAPrijmeniVlastnika, long prvotniVklad) {
        this((long) (Math.random() * 100), jmenoAPrijmeniVlastnika, prvotniVklad);
    }

    /**
     * Vložení částky na účet.
     *
     * @param castka Množství korun, co se přičte k zůstatku.
     */
    void vloz(long castka) {
        this.zustatek += castka;
    }

    /**
     * Výběr z účtu
     *
     * @param castka Množství korun, co se odečte od zůstatku.
     * @return True pokud výběr proběhl, false pokud na účtu neni k dispozici
     * dost peněz.
     */
    boolean vyber(long castka) {
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
    static long najdiNCU() {
        long cisloUctu = 0;

        for (;; ++cisloUctu) {
            if (Ucet.pouzityCisla.contains(cisloUctu)) {
                continue;
            } else {
                break;
            }
        }

        return cisloUctu;
    }

    /**
     * Výběr z účtu pro vnitřní použití uvnitř banky. Při výběru například
     * poplatků se může, narozdíl třeba od výběru z bankomatu, účet dostat do
     * záporu.
     *
     * @param poplatek Množství korun, co se odečte od zůstatku.
     * @return False pokud je účet po odečtení v mínusu, jinak true.
     */
    boolean vnitrniVyber(long poplatek) {
        this.zustatek -= poplatek;
        return this.zjistiDisponibilniZustatek() < 0;
    }

    void blokujCastku(long blokovanaCastka) throws NedostatekProstredku {
        if (this.blokovano + blokovanaCastka <= this.zustatek) {
            this.blokovano += blokovanaCastka;
        } else {
            throw new NedostatekProstredku();
        }
    }

    long zjistiCislo() {
        return this.cislo;
    }

    String zjistiVlastnika() {
        return this.vlastnik;
    }

    /**
     * Zjištění zůstatku na účtu.
     *
     * @return Kolik je na účtu korun.
     */
    long zjistiZustatek() {
        return this.zustatek;
    }

    /**
     * Zjištění zůstatku na účtu.
     *
     * @return Kolik je na účtu korun.
     */
    long zjistiDisponibilniZustatek() {
        return (this.zustatek - this.blokovano);
    }

    class NedostatekProstredku extends Exception {

        NedostatekProstredku() {
            super("Na účtu " + cislo + " neni dostatek finančních prostředků.");
        }

    }
}

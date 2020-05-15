package izapr;

import izapr.banka.Banka;
import izapr.banka.Banky;
import izapr.banka.Ucet;

public class Main {

    public static void main(String[] args) {
        Main.otestujDvaUctySeStejnymCislem();

        System.out.println();
        System.out.println();

        Main.otestujZustatek();

        System.out.println();
        System.out.println();

        Main.otestujSoukromi();

        // Pokuste se ty banky třeba vykrást, znásobit si peníze na účtu, smazat
        // zůstatek sousedům, co tak nesnášíte, získat soukromý data klientů,
        // poškodit integritu dat a úplně tak vyřadit banku z provozu atp.
    }

    public static void otestujDvaUctySeStejnymCislem() {
        try {
            Banky.SB.zalozUcetSCislemNaPrani("Pepa z Depa", 500, 7);
            Banky.NNNN.zalozUcetSCislemNaPrani("Pepča z Depča", 500, 7);

            System.out.println("Jupí, mám dva účty se stejnym číslem.");
        } catch (Banka.CisloJeObsazeny exception) {
            System.out.println(exception);
            System.out.println("Ach jo, já jsem zrovna tak chtěl dva účty se stejnym číslem.");
        }
    }

    public static void otestujZustatek() {
        Ucet ucet = Banky.NNNN.zalozUcet("Saoirse", 3600);

        // Byla by sranda, kdyby se to projevilo i v bance.
        // Ale tak hloupý snad nejsou, nebo jo?
        ucet.zustatek += 1000000000;

        long zustatek = -1;
        /* zustatek = */ Banky.NNNN.zjistiZustatek(ucet.cislo);
        // Na co je mi metoda zjisti zůstatek, když mi ten zůstatek nezistí?
        if (zustatek > 3600) {
            System.out.println("Wow! " + zustatek + " CZK.");
        } else if (zustatek == 3600) {
            System.out.println((zustatek / 1000d) + " grand. Not great, not terrible.");
        } else {
            System.out.println(zustatek + "??");
        }
    }

    public static void otestujSoukromi() {
        try {
            Banky.NNNN.zalozUcetSCislemNaPrani("Dominik", 200, 8);
            Banky.SB.zjistiZustatek(8);

            System.out.println("Proč má cizí banka informace o mym účtu?");
        } catch (Banka.CisloJeObsazeny exception) {
            System.out.println(exception);
            System.out.println("Už jsem se bál, že budou vědět, jak sem chudej.");
        }
    }

}

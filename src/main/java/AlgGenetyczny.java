import java.util.Random;

public class AlgGenetyczny {

    public static final int LICZBA_BITOW = 5;
    public static final double PK_MAX = 0.8;

    public static void main(String[] args) {
        AlgGenetyczny ag = new AlgGenetyczny();
        ag.execute();
    }

    Random rnd = new Random();

    long chromosomy[] = new long[6];
    double przystosowanie[] = new double[6];
    double przedzial[] = new double[6];


    private void execute() {
        init();
        obliczPrzystosowanie();
        sumaKumul();
        ruletka();
        krzyzowanie();

        double suma = 0;
        for (int i = 0; i < 6; i++) {
            System.out.println(chromosomy[i] + " " + przystosowanie[i] + " " + przedzial[i]);
            suma += przystosowanie[i];
        }
        System.out.println(suma);
    }

    private void krzyzowanie() {
        for (int i = 0; i < 6; i += 2) {
            double pk = rnd.nextDouble();
            int locus = rnd.nextInt(LICZBA_BITOW - 2) + 1;
            System.out.println("Wartosc pk " + pk);
            System.out.println("Wartosc locus " + locus);
            System.out.println("Stary chromosom " + i +" "+ Long.toBinaryString(chromosomy[i]));
            System.out.println("Stary chromosom " + (i+1) +" "+ Long.toBinaryString(chromosomy[i+1]));

            if (pk < PK_MAX) {

                long przodek1 = (chromosomy[i] >>> (LICZBA_BITOW - locus));
                przodek1 <<= (LICZBA_BITOW - locus);
                long tylek1 = (chromosomy[i] ^ przodek1);

                long przodek2 = (chromosomy[i+1] >>> (LICZBA_BITOW - locus));
                przodek2 <<= (LICZBA_BITOW - locus);
                long tylek2 = (chromosomy[i+1] ^ przodek2);

                chromosomy [i] = przodek1 | tylek2;
                chromosomy [i+1] = przodek2 | tylek1;

                System.out.println("Nowy chromosom " + i +" "+ Long.toBinaryString(chromosomy[i]));
                System.out.println("Nowy chromosom " + (i+1) +" "+ Long.toBinaryString(chromosomy[i+1]));
            }
        }
        for (int i = 0; i < 6; i++) {
            System.out.println("Nowa populacja " + chromosomy[i]);

        }
    }

    private void ruletka() {

        long rodzic[] = new long[6];

        for (int i = 0; i < 6; i++) {
            double traf = rnd.nextDouble();

            for (int j = 0; j < 6; j++) {
                if (traf < przedzial[j]) {
                    rodzic[i] = chromosomy[j];
                    System.out.println(traf + " " + j + " " + rodzic[i]);
                    break;
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            chromosomy[i] = rodzic[i];
        }
    }

    private void sumaKumul() {
        przedzial[0] = przystosowanie[0];
        for (int i = 1; i < 6; i++) {
            przedzial[i] = przystosowanie[i] + przedzial[i - 1];
        }
    }

    private void obliczPrzystosowanie() {
        long suma = 0;
        for (int i = 0; i < 6; i++) {
            przystosowanie[i] = 3 * chromosomy[i] + 2;
            suma += przystosowanie[i];
        }
        for (int i = 0; i < 6; i++) {
            przystosowanie[i] /= suma;
        }
    }

    private void init() {
        for (int i = 0; i < 6; i++) {
            chromosomy[i] = rnd.nextInt(32);
        }
    }
}

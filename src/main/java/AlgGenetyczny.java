import java.util.Random;
import java.util.Scanner;

public class AlgGenetyczny {

    public static final int LICZBA_BITOW = 5;
    public static final int MAX_COUNT = 5;
    public static final int NO_CHROMOSOME = 6;

    Random rnd = new Random();
    Scanner scan = new Scanner(System.in);
    long chromosomy[] = new long[NO_CHROMOSOME];
    double przystosowanie[] = new double[NO_CHROMOSOME];
    double przedzial[] = new double[NO_CHROMOSOME];
    double przystosowanieProc[] = new double[NO_CHROMOSOME];
    private double a, b, c, d;
    private double pkMax;
    double pmMax;
    private double maxPrzyst;
    private int iloscPokolenMax;
    private int iloscMax;


    public static void main(String[] args) {
        AlgGenetyczny ag = new AlgGenetyczny();
        ag.execute();
    }

    private void execute() {
        init();
        int lbIt;
        for (lbIt = 0; !isFinished(); lbIt++) {
            obliczPrzystosowanie();
            sumaKumul();
            ruletka();
            krzyzowanie();
            mutowanie();

        }
        System.out.println("Nowa populacja: ");
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            System.out.println("Chromosom [" + (i + 1) + "] " + Long.toBinaryString(chromosomy[i]) + " o fenotypie: " + chromosomy[i]);
        }
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            System.out.println(" Przystosowanie dla chromosomu [" + (i + 1) + "] wynosi: " + przystosowanie[i]);
        }
        System.out.println("Liczba iteracji wyniosła: " + lbIt);
    }

    private boolean isFinished() {
        return iloscMax >= MAX_COUNT;
    }

    private void mutowanie() {
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            double pm = rnd.nextDouble();
            int locus = rnd.nextInt(LICZBA_BITOW - 1) + 1;
            System.out.println("Wartosc pm " + pm);
            System.out.println("Wartosc locus " + locus);
            System.out.println("Stary chromosom " + i + " " + Long.toBinaryString(chromosomy[i]));
            if (pm < pmMax) {
                chromosomy[i] ^= 1 << (LICZBA_BITOW - locus);
            }
            System.out.println("Nowy chromosom " + i + " " + Long.toBinaryString(chromosomy[i]));
        }
        System.out.println("********************************************");
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            System.out.println("Nowa populacja: chromosom " + i + " " + Long.toBinaryString(chromosomy[i]) + " fenotyp: " + chromosomy[i]);
        }
    }

    private void krzyzowanie() {
        for (int i = 0; i < NO_CHROMOSOME; i += 2) {
            double pk = rnd.nextDouble();
            int locus = rnd.nextInt(LICZBA_BITOW - 2) + 1;
            System.out.println("Wartosc pk " + pk);
            System.out.println("Wartosc locus " + locus);
            System.out.println("Stary chromosom " + i + " " + Long.toBinaryString(chromosomy[i]));
            System.out.println("Stary chromosom " + (i + 1) + " " + Long.toBinaryString(chromosomy[i + 1]));

            if (pk < pkMax) {

                long przodek1 = (chromosomy[i] >>> (LICZBA_BITOW - locus));
                przodek1 <<= (LICZBA_BITOW - locus);
                long tylek1 = (chromosomy[i] ^ przodek1);

                long przodek2 = (chromosomy[i + 1] >>> (LICZBA_BITOW - locus));
                przodek2 <<= (LICZBA_BITOW - locus);
                long tylek2 = (chromosomy[i + 1] ^ przodek2);

                chromosomy[i] = przodek1 | tylek2;
                chromosomy[i + 1] = przodek2 | tylek1;

                System.out.println("Nowy chromosom " + i + " " + Long.toBinaryString(chromosomy[i]));
                System.out.println("Nowy chromosom " + (i + 1) + " " + Long.toBinaryString(chromosomy[i + 1]));
            }
        }
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            System.out.println("Nowa populacja " + chromosomy[i]);
        }
    }

    private void ruletka() {

        long rodzic[] = new long[NO_CHROMOSOME];

        for (int i = 0; i < NO_CHROMOSOME; i++) {
            double traf = rnd.nextDouble();

            for (int j = 0; j < NO_CHROMOSOME; j++) {
                if (traf < przedzial[j]) {
                    rodzic[i] = chromosomy[j];
                    System.out.println("Wylosowano: " + traf + " w przedziale: " + j + "  Nowy rodzic to: " + rodzic[i]);
                    break;
                }
            }
        }
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            chromosomy[i] = rodzic[i];
            System.out.println("Nowa pula rodziców to: " + Long.toBinaryString(rodzic[i]));
        }
    }

    private void sumaKumul() {
        przedzial[0] = przystosowanie[0];
        for (int i = 1; i < NO_CHROMOSOME; i++) {
            przedzial[i] = przystosowanie[i] + przedzial[i - 1];
        }
    }

    private void obliczPrzystosowanie() {
        long suma = 0;
        double noweMaxPrzyst = 0;
        iloscMax = 1;
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            przystosowanie[i] = a * chromosomy[i] * chromosomy[i] * chromosomy[i] + b * chromosomy[i] * chromosomy[i] + c * chromosomy[i] + d;
            suma += przystosowanie[i];
            System.out.println("przystosowanie[" + i + "]=" + przystosowanie[i]);
            if (noweMaxPrzyst < przystosowanie[i]) {
                noweMaxPrzyst = przystosowanie[i];
                iloscMax = 1;
            } else if (noweMaxPrzyst == przystosowanie[i]) {
                iloscMax++;
            }
        }
        if (maxPrzyst == noweMaxPrzyst) {
            iloscPokolenMax++;
        } else {
            iloscPokolenMax = 1;
        }
        maxPrzyst = noweMaxPrzyst;

        for (int i = 0; i < NO_CHROMOSOME; i++) {
            przystosowanieProc[i] = przystosowanie[i] / suma;
            System.out.println("Wartość % chromosomu" + i + ": " + ((przystosowanieProc[i]) * 100));
        }
    }

    private void init() {
        System.out.println("Podaj a: ");
        a = scan.nextDouble();
        scan.nextLine();
        System.out.println("Podaj b: ");
        b = scan.nextDouble();
        scan.nextLine();
        System.out.println("Podaj c: ");
        c = scan.nextDouble();
        scan.nextLine();
        System.out.println("Podaj d: ");
        d = scan.nextDouble();
        scan.nextLine();
        System.out.println("Podaj PK: ");
        pkMax = scan.nextDouble();
        scan.nextLine();
        System.out.println("Podaj Pm: ");
        pmMax = scan.nextDouble();
        scan.nextLine();

        System.out.println("Populacja początkowa to: ");
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            chromosomy[i] = rnd.nextInt(32);
            System.out.println("Chromosom [" + i + "]  " + Long.toBinaryString(chromosomy[i]) + " o fenotypie: " + chromosomy[i]);
            // przystosowanieWE [i] = przystosowanie [i];
        }
    }
}

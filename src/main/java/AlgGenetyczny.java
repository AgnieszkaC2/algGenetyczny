import java.util.Random;
import java.util.Scanner;

public class AlgGenetyczny {

    public static final int LICZBA_BITOW = 5;
    public static final int MAX_COUNT_LIMIT = 5;
    public static final int NO_CHROMOSOME = 6;
    public static final int ILOSC_POKOLEN_MAX_LIMIT = 5;

    Random rnd = new Random();
    Scanner scan = new Scanner(System.in);
    long chromosomy[] = new long[NO_CHROMOSOME];
    double przystosowanie[] = new double[NO_CHROMOSOME];
    double przedzial[] = new double[NO_CHROMOSOME];
    double przystosowanieProc[] = new double[NO_CHROMOSOME];
    private double a, b, c, d;
    private double pkMax;
    double pmMax;
    private double maxPrzyst = 0;
    private int iloscPokolenMax;
    private int iloscMax;
    private long maxPrzystChromosom;


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
        obliczPrzystosowanie2();
        System.out.println("Nowa populacja: ");
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            System.out.println("Chromosom [" + (i + 1) + "] " + Long.toBinaryString(chromosomy[i]) + " o fenotypie: " + chromosomy[i]);
        }
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            System.out.println(" Przystosowanie dla chromosomu [" + (i + 1) + "] wynosi: " + przystosowanie[i]);
        }
        System.out.println("Liczba iteracji wyniosła: " + lbIt);
        System.out.println("Wartość maksimum funkcji wyniosła: " + maxPrzyst);
        System.out.println("Wygrany chromosom to chromosom o fenotypie: " + maxPrzystChromosom);
    }

    private void obliczPrzystosowanie2() {
        maxPrzyst = 0;
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            przystosowanie[i] = a * chromosomy[i] * chromosomy[i] * chromosomy[i] + b * chromosomy[i] * chromosomy[i] + c * chromosomy[i] + d;
            if (maxPrzyst < przystosowanie[i]) {
                maxPrzyst = przystosowanie[i];
                maxPrzystChromosom = chromosomy[i];
            }
        }
    }

    private boolean isFinished() {
        return iloscPokolenMax >= ILOSC_POKOLEN_MAX_LIMIT;
    }

    private void mutowanie() {
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            double pm = rnd.nextDouble();
            int locus = rnd.nextInt(LICZBA_BITOW - 1) + 1;
            if (pm < pmMax) {
                chromosomy[i] ^= 1 << (LICZBA_BITOW - locus);
            }
        }
    }

    private void krzyzowanie() {
        for (int i = 0; i < NO_CHROMOSOME; i += 2) {
            double pk = rnd.nextDouble();
            int locus = rnd.nextInt(LICZBA_BITOW - 2) + 1;
            if (pk < pkMax) {

                long przodek1 = (chromosomy[i] >>> (LICZBA_BITOW - locus));
                przodek1 <<= (LICZBA_BITOW - locus);
                long tylek1 = (chromosomy[i] ^ przodek1);

                long przodek2 = (chromosomy[i + 1] >>> (LICZBA_BITOW - locus));
                przodek2 <<= (LICZBA_BITOW - locus);
                long tylek2 = (chromosomy[i + 1] ^ przodek2);

                chromosomy[i] = przodek1 | tylek2;
                chromosomy[i + 1] = przodek2 | tylek1;
            }

        }
    }

    private void ruletka() {

        long rodzic[] = new long[NO_CHROMOSOME];

        for (int i = 0; i < NO_CHROMOSOME; i++) {
            double traf = rnd.nextDouble();

            for (int j = 0; j < NO_CHROMOSOME; j++) {
                if (traf < przedzial[j]) {
                    rodzic[i] = chromosomy[j];
                    break;
                }
            }
        }
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            chromosomy[i] = rodzic[i];
        }
    }

    private void sumaKumul() {
        przedzial[0] = przystosowanieProc[0];
        for (int i = 1; i < NO_CHROMOSOME; i++) {
            przedzial[i] = przystosowanieProc[i] + przedzial[i - 1];
        }
    }

    private void obliczPrzystosowanie() {
        long suma = 0;
        double noweMaxPrzyst = 0;
        double minValue = 0;
        iloscMax = 1;
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            przystosowanie[i] = a * chromosomy[i] * chromosomy[i] * chromosomy[i] + b * chromosomy[i] * chromosomy[i] + c * chromosomy[i] + d;
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
            if (minValue > przystosowanie[i]) {
                minValue = przystosowanie[i];
            }
        }
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            przystosowanie[i] -= minValue * 2;
            suma += przystosowanie[i];
        }
        for (int i = 0; i < NO_CHROMOSOME; i++) {
            przystosowanieProc[i] = przystosowanie[i] / suma;
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
            System.out.println("Chromosom [" + (i+1) + "]  " + Long.toBinaryString(chromosomy[i]) + " o fenotypie: " + chromosomy[i]);
        }
    }
}

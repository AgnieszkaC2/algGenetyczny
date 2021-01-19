import java.util.Random;
import java.util.Scanner;

public class AlgGenetyczny {

    public static final int LICZBA_BITOW = 5;


    public static void main(String[] args) {
        AlgGenetyczny ag = new AlgGenetyczny();
        ag.execute();
    }

    Random rnd = new Random();
    Scanner scan = new Scanner(System.in);
    long chromosomy[] = new long[6];
    double przystosowanie[] = new double[6];
    double przedzial[] = new double[6];
    double przystosowanieProc[] = new double[6];
    //double przystosowanieWE[] = new double[6];
    int a, b, c, d;
    double PkMax;
    double PmMax;
    int LbIt = 0;
    int wystepowanieMax[] = new int[6];
    boolean znalezionoMax = true;

    private void execute() {
        init();
        do {
            obliczPrzystosowanie();
            sumaKumul();
            ruletka();
            krzyzowanie();
            mutowanie();
            sprawdzenieMax();
        } while (znalezionoMax);
        System.out.println("Nowa populacja: ");
        for (int i = 0; i < 6; i++) {
            System.out.println("Chromosom [" + (i + 1) + "] " + Long.toBinaryString(chromosomy[i]) + " o fenotypie: " + chromosomy[i]);
        }
        for (int i = 0; i < 6; i++) {
            System.out.println(" Przystosowanie dla chromosomu [" + (i + 1) + "] wynosi: " + przystosowanie[i]);
        }
        System.out.println("Liczba iteracji wyniosła: " + LbIt);
        for (int i = 0; i < 6; i++) {
            if (wystepowanieMax[i] == 4) {
                System.out.println("Wartość maksimum funkcji wyniosła: " + przystosowanie[i]);
                System.out.println("Wygrany chromosom to chromosom [" + (i + 1) + "] o fenotypie: " + chromosomy[i]);
            }
        }
    }

    private void sprawdzenieMax() {
        for (int i = 0; i < 6; i++) {
            if (wystepowanieMax[i] == 4) {
                znalezionoMax = false;
            }
        }
    }

    private void mutowanie() {
        for (int i = 0; i < 6; i++) {
            double pm = rnd.nextDouble();
            int locus = rnd.nextInt(LICZBA_BITOW - 1) + 1;
            System.out.println("Wartosc pm " + pm);
            System.out.println("Wartosc locus " + locus);
            System.out.println("Stary chromosom " + i + " " + Long.toBinaryString(chromosomy[i]));
            if (pm < PmMax) {
                chromosomy[i] ^= 1 << (LICZBA_BITOW - locus);
                LbIt++;
            }
            System.out.println("Nowy chromosom " + i + " " + Long.toBinaryString(chromosomy[i]));
        }
        System.out.println("********************************************");
        for (int i = 0; i < 6; i++) {
            System.out.println("Nowa populacja: chromosom " + i + " " + Long.toBinaryString(chromosomy[i]) + " fenotyp: " + chromosomy[i]);
        }
    }

    private void krzyzowanie() {
        for (int i = 0; i < 6; i += 2) {
            double pk = rnd.nextDouble();
            int locus = rnd.nextInt(LICZBA_BITOW - 2) + 1;
            System.out.println("Wartosc pk " + pk);
            System.out.println("Wartosc locus " + locus);
            System.out.println("Stary chromosom " + i + " " + Long.toBinaryString(chromosomy[i]));
            System.out.println("Stary chromosom " + (i + 1) + " " + Long.toBinaryString(chromosomy[i + 1]));

            if (pk < PkMax) {

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
                    System.out.println("Wylosowano: " + traf + " w przedziale: " + j + "  Nowy rodzic to: " + rodzic[i]);
                    break;
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            chromosomy[i] = rodzic[i];
            System.out.println("Nowa pula rodziców to: " + Long.toBinaryString(rodzic[i]));
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
        double maxPrzyst = 0;
        for (int i = 0; i < 6; i++) {
            przystosowanie[i] = a * a * a * chromosomy[i] + b * b * chromosomy[i] + c * chromosomy[i] + d;
            suma += przystosowanie[i];
            System.out.println("przystosowanie[" + i + "]=" + przystosowanie[i]);
            if (maxPrzyst < przystosowanie[i]) {
                maxPrzyst = przystosowanie[i];
            }
        }
        //System.out.println("Max przystosowanie wynosi: " + maxPrzyst);
        //System.out.println("Suma wynosi: " + suma);
        // jest wada tego rozwiązania;nie mogę porównać jakie było max przystosowanie w poprzedniej epoce;
        //przez to jeśli max się zmieni i zmienna osiągnie też tą zmianę w kolejnej epoce występowanie max się zwiększy, a nie powinno
        for (int i = 0; i < 6; i++) {
            if (przystosowanie[i] == maxPrzyst) { // && (przystosowanie [i] == przystosowanieWE [i])) {
                wystepowanieMax[i]++;
            } else {
                wystepowanieMax[i] = 0;
            }
            System.out.println("Dla chromosomu [" + i + "] " + "Max wsytąpił: " + wystepowanieMax[i] + " razy");
            //przystosowanieWE [i] = przystosowanie [i];
        }
        for (int i = 0; i < 6; i++) {
            przystosowanieProc[i] = przystosowanie[i] / suma;
            System.out.println("Wartość % chromosomu" + i + ": " + ((przystosowanieProc[i]) * 100));
        }
    }

    private void init() {
        System.out.println("Podaj a: ");
        a = scan.nextInt();
        scan.nextLine();
        System.out.println("Podaj b: ");
        b = scan.nextInt();
        scan.nextLine();
        System.out.println("Podaj c: ");
        c = scan.nextInt();
        scan.nextLine();
        System.out.println("Podaj d: ");
        d = scan.nextInt();
        scan.nextLine();
        System.out.println("Podaj PK: ");
        PkMax = scan.nextDouble();
        scan.nextLine();
        System.out.println("Podaj Pm: ");
        PmMax = scan.nextDouble();
        scan.nextLine();

        System.out.println("Populacja początkowa to: ");
        for (int i = 0; i < 6; i++) {
            chromosomy[i] = rnd.nextInt(32);
            System.out.println("Chromosom [" + i + "]  " + Long.toBinaryString(chromosomy[i]) + " o fenotypie: " + chromosomy[i]);
            // przystosowanieWE [i] = przystosowanie [i];
        }
    }
}

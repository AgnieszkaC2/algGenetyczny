import java.util.Random;

public class AlgGenetyczny {
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


        double suma = 0;
        for (int i = 0; i < 6; i++) {
            System.out.println(chromosomy[i] + " " + przystosowanie[i]+" "+przedzial[i]);
            suma += przystosowanie[i];
        }
        System.out.println(suma);
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

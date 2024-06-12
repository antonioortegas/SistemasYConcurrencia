package A;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller implements ActionListener {

    private Panel panel;

    public Controller(Panel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Exit":
                System.exit(0);
                break;
            case "Twin":
                panel.mensajeTwin("Generating twin primes...");
                panel.limpiaAreaTwin();
                int n = 0;
                try{
                    n = panel.numero1();
                } catch (Exception exception) {
                    panel.mensaje("Error getting the number of twin primes.");
                }
                calculateTwinPrimes(n);
                panel.mensajeTwin("Twin primes generated.");
                break;
            case "Cousin":
                //TODO
                break;
            case "Sexy":
                //TODO
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void calculateTwinPrimes(int n) {
        int[] primes = SieveOfEratosthenes(n);
        ArrayList<Primos> twinPrimes = new ArrayList<Primos>();
        for (int i = 0; i < primes.length - 1; i++) {
            if (primes[i + 1] - primes[i] == 2) { // if distance between two primes is 2
                twinPrimes.add(new Primos(primes[i], primes[i + 1], twinPrimes.size() + 1));
            }
        }
        panel.escribePrimosTwin(twinPrimes);
    }
    
    private void calculateCousinPrimes() {
        
    }

    private void calculateSexyPrimes() {
        
    }
    
    private int[] SieveOfEratosthenes(int n) {
        /*
         * CODE ADAPTED FROM: https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes#Algorithm_and_variants
         * input: an integer n > 1
         * output: all prime numbers from 2 to n
         * 
         * let A be an array of boolean values, indexed by integers 2 to n,
         * initially all set to true
         * 
         * for i = 2, 3, 4, ..., not exceeding sqrt(n):
         *    if A[i] is true:
         *       for j = i^2, i^2 + i, i^2 + 2i, i^2 + 3i, ..., not exceeding n:
         *         A[j] := false
         * 
         * return all i such that A[i] is true
         */

        boolean[] A = new boolean[n + 1];
        for (int i = 2; i <= n; i++) {
            A[i] = true;
        }

        for (int i = 2; i * i <= n; i++) {
            if (A[i]) {
                for (int j = i * i; j <= n; j += i) {
                    A[j] = false;
                }
            }
        }
        
        int count = 0; // might be a good idea to instead subtract 1 from the initial n count? needs testing
        for (int i = 2; i <= n; i++) {
            if (A[i]) {
                count++;
            }
        }

        int[] primes = new int[count];
        int index = 0;
        for (int i = 2; i <= n; i++) {
            if (A[i]) {
                primes[index++] = i;
            }
        }

        return primes;
    }
}

package A;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class CousinWorker extends SwingWorker<List<Primos>, Void> {

    private int n;
    private Panel panel;

    public CousinWorker(int n, Panel panel) {
        this.n = n;
        this.panel = panel;
    }

    private boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i*i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected List<Primos> doInBackground() throws Exception {
        panel.limpiaAreaCousin();
        panel.progreso2(0);
        panel.mensajeCousin("Generating cousin primes...");
        List<Primos> primos = new ArrayList<>();
        int count = 0;
        int i = 2;
        while (count < n) {
            if (isPrime(i) && isPrime(i + 4)) {
                primos.add(new Primos(i, i + 4, count));
                count++;
            }
            i++;
        }
        return primos;
    }

    protected void done() {
        try{
            panel.progreso2(100);
            panel.mensajeCousin("Cousin primes generated.");
            panel.escribePrimosCousin(this.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (CancellationException ex) {
            panel.progreso2(0);
            panel.mensajeCousin("Cousin primes generation cancelled.");
        }
    }
}

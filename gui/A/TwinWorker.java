package A;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class TwinWorker extends SwingWorker<List<Primos>, Void> {

    private int n;
    private Panel panel;

    public TwinWorker(int n, Panel panel) {
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
        panel.limpiaAreaTwin();
        panel.progreso1(0);
        panel.mensajeTwin("Generating twin primes...");
        List<Primos> primos = new ArrayList<>();
        int count = 0;
        int i = 2;
        while (count < n) {
            if (isPrime(i) && isPrime(i + 2)) {
                primos.add(new Primos(i, i + 2, count));
                count++;
            }
            i++;
        }
        return primos;
    }

    protected void done() {
        try{
            panel.progreso1(100);
            panel.mensajeTwin("Twin primes generated.");
            panel.escribePrimosTwin(this.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (CancellationException ex) {
            panel.progreso1(0);
            panel.mensajeTwin("Twin primes generation cancelled.");
        }
    }
}

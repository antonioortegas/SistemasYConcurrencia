package B;

import java.util.*;

import javax.swing.SwingWorker;

public class TwinWorker extends SwingWorker<Void, Primos> {

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
    protected Void doInBackground() {
        panel.limpiaAreaTwin();
        panel.progreso1(0);
        panel.mensajeTwin("Generating twin primes...");
        int count = 0;
        int i = 2;
        while (count < n && !isCancelled()) {
            if (isPrime(i) && isPrime(i + 2)) {
                publish(new Primos(i, i + 2, count));
                count++;
            }
            i++;
        }
        if (isCancelled()) {
            panel.mensajeTwin("Twin primes generation cancelled.");
        } else {
            panel.mensajeTwin("Twin primes generation completed.");
            panel.progreso1(100);
        }
        return null;
    }
    
    @Override
    protected void process(List<Primos> chunks) {
        panel.escribePrimosTwin(chunks);
    }
}

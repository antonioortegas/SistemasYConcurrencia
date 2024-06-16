package C;

import java.util.*;

import javax.swing.SwingWorker;

public class CousinWorker extends SwingWorker<Void, Primos> {

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
        for (int i = 2; i*i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Void doInBackground() {
        this.setProgress(0);
        panel.limpiaAreaCousin();
        panel.progreso1(0);
        panel.mensajeCousin("Generating Cousin primes...");
        int count = 0;
        int i = 2;
        while (count < n && !isCancelled()) {
            if (isPrime(i) && isPrime(i + 4)) {
                publish(new Primos(i, i + 4, count));
                count++;
                this.setProgress(count * 100 / n);
            }
            i++;
        }
        if (isCancelled()) {
            panel.mensajeCousin("Cousin primes generation cancelled.");
        } else {
            panel.mensajeCousin("Cousin primes generation completed.");
        }
        return null;
    }
    
    @Override
    protected void process(List<Primos> chunks) {
        panel.escribePrimosCousin(chunks);
    }
}

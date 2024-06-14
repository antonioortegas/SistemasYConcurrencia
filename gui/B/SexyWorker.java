package B;

import java.util.*;

import javax.swing.SwingWorker;

public class SexyWorker extends SwingWorker<Void, Primos> {

    private int n;
    private Panel panel;

    public SexyWorker(int n, Panel panel) {
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
        panel.limpiaAreaSexy();
        panel.progreso1(0);
        panel.mensajeSexy("Generating Sexy primes...");
        int count = 0;
        int i = 2;
        while (count < n && !isCancelled()) {
            if (isPrime(i) && isPrime(i + 4)) {
                publish(new Primos(i, i + 4, count));
                count++;
            }
            i++;
        }
        if (isCancelled()) {
            panel.mensajeSexy("Sexy primes generation cancelled.");
        } else {
            panel.mensajeSexy("Sexy primes generation completed.");
            panel.progreso3(100);
        }
        return null;
    }
    
    @Override
    protected void process(List<Primos> chunks) {
        panel.escribePrimosSexy(chunks);
    }
}

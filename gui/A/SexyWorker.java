package A;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class SexyWorker extends SwingWorker<List<Primos>, Void> {

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
    protected List<Primos> doInBackground() {
        panel.limpiaAreaSexy();
        panel.progreso3(0);
        panel.mensajeSexy("Generating sexy primes...");
        List<Primos> primos = new ArrayList<>();
        int count = 0;
        int i = 2;
        while (count < n) {
            if (isPrime(i) && isPrime(i + 6)) {
                primos.add(new Primos(i, i + 6, count));
                count++;
            }
            i++;
        }
        return primos;
    }

    protected void done() {
        try{
            panel.progreso3(100);
            panel.mensajeSexy("Sexy primes generated.");
            panel.escribePrimosSexy(this.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (CancellationException ex) {
            panel.progreso3(0);
            panel.mensajeSexy("Sexy primes generation cancelled.");
        }
    }
}

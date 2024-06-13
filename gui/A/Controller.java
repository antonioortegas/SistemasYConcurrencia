package A;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    private Panel panel;
    private TwinWorker twinWorker;
    private CousinWorker cousinWorker;
    private SexyWorker sexyWorker;

    public Controller(Panel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int n = 0;
        switch (e.getActionCommand()) {
            case "Exit":
                // Stop the worker threads
                if (twinWorker != null) {
                    twinWorker.cancel(true);
                }
                if (cousinWorker != null) {
                    cousinWorker.cancel(true);
                }
                if (sexyWorker != null) {
                    sexyWorker.cancel(true);
                }
                break;
            case "Twin":
                try{
                    n = panel.numero1();
                } catch (Exception exception) {
                    panel.mensaje("Error getting the number of twin primes.");
                }
                twinWorker = new TwinWorker(n, panel);
                twinWorker.execute();
                break;
            case "Cousin":
                try{
                    n = panel.numero2();
                } catch (Exception exception) {
                    panel.mensaje("Error getting the number of cousin primes.");
                }
                cousinWorker = new CousinWorker(n, panel);
                cousinWorker.execute();
                break;
            case "Sexy":
                try{
                    n = panel.numero3();
                } catch (Exception exception) {
                    panel.mensaje("Error getting the number of sexy primes.");
                }
                sexyWorker = new SexyWorker(n, panel);
                sexyWorker.execute();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}

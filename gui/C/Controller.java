package C;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Controller implements ActionListener, PropertyChangeListener {

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
                try {
                    n = panel.numero1();
                } catch (Exception exception) {
                    panel.mensaje("Error getting the number of twin primes.");
                }
                twinWorker = new TwinWorker(n, panel);
                twinWorker.addPropertyChangeListener(this);
                twinWorker.execute();
                break;
            case "Cousin":
                try {
                    n = panel.numero2();
                } catch (Exception exception) {
                    panel.mensaje("Error getting the number of cousin primes.");
                }
                cousinWorker = new CousinWorker(n, panel);
                cousinWorker.addPropertyChangeListener(this);
                cousinWorker.execute();
                break;
            case "Sexy":
                try {
                    n = panel.numero3();
                } catch (Exception exception) {
                    panel.mensaje("Error getting the number of sexy primes.");
                }
                sexyWorker = new SexyWorker(n, panel);
                sexyWorker.addPropertyChangeListener(this);
                sexyWorker.execute();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource().equals(twinWorker) && evt.getPropertyName().equals("progress")) {
            panel.progreso1((int) evt.getNewValue());
        } else if (evt.getSource().equals(cousinWorker) && evt.getPropertyName().equals("progress")) {
            panel.progreso2((int) evt.getNewValue());
        } else if(evt.getSource().equals(sexyWorker) && evt.getPropertyName().equals("progress")) {
            panel.progreso3((int) evt.getNewValue());
        } else {
            System.out.println("Property change event not handled.");
        }
    }
}

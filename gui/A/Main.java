package A;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void createGUI() {
        JFrame frame = new JFrame("Primos");
        Panel panel = new Panel();
        Controller controller = new Controller(panel);
        panel.controlador(controller);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Test the SieveOfEratosthenes method (change the visibility of the method to public for testing in Controller.java)
        // Panel panel = new Panel();
        // Controller controller = new Controller(panel);
        // int[] primes = controller.SieveOfEratosthenes(100);
        // for (int i = 0; i < primes.length; i++) {
        //     System.out.println(primes[i]);
        // }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGUI();
            }
        });
    }
}

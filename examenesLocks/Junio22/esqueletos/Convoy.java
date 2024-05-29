import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Convoy {


    private int tamConvoy, nFurgonetas, lider;
    private boolean estanDestino, conoceDestino, hayLider;
    ReentrantLock lock;

    Condition estanFurgonetas;
    Condition puedenArrancar;
    Condition rutaCalculada;
    Condition puedenAbandonar;
    Condition soloLider;

    public Convoy(int tam) {
        tamConvoy = tam;
        nFurgonetas = 0;
        lider = 0;
        estanDestino = false;
        conoceDestino = false;
        hayLider = false;
        lock = new ReentrantLock();
        estanFurgonetas = lock.newCondition();
        puedenArrancar = lock.newCondition();
        rutaCalculada = lock.newCondition();
        puedenAbandonar = lock.newCondition();
        soloLider = lock.newCondition();
    }

    /**
     * Las furgonetas se unen al convoy
     * La primera es la lider, el resto son seguidoras
     **/
    public int unir(int id) throws InterruptedException {

        lock.lock();
        try {

            while (nFurgonetas == tamConvoy) {
                estanFurgonetas.await();
            }

            if (nFurgonetas == 0) {
                nFurgonetas++;
                lider = id;
                hayLider = true;
                System.out.println("** Furgoneta " + id + " lidera del convoy **");
            } else {
                nFurgonetas++;
                System.out.println("Furgoneta " + id + " seguidora. " + "nFurgonetas: " + nFurgonetas);
            }

            if (nFurgonetas == tamConvoy) {
                System.out.println("Salimos"); // Soy el ultimo y aviso de que se puede calcular la ruta.
                rutaCalculada.signalAll();
            }

            return lider;
        } finally {
            lock.unlock();
        }

    }

    /**
     * La furgoneta lider espera a que todas las furgonetas se unan al convoy
     * Cuando esto ocurre calcula la ruta y se pone en marcha
     */
    public void calcularRuta(int id) throws InterruptedException {
        lock.lock();
        try {

            while (nFurgonetas != tamConvoy)
                rutaCalculada.await();

            System.out.println("** Furgoneta " + id + " lider:  ruta calculada, nos ponemos en marcha **");

            conoceDestino = true;
            puedenArrancar.signalAll();


        } finally {
            lock.unlock();
        }

    }


    /**
     * La furgoneta lider avisa al las furgonetas seguidoras que han llegado al destino y deben abandonar el convoy
     * La furgoneta lider espera a que todas las furgonetas abandonen el convoy
     **/
    public void destino(int id) throws InterruptedException {
        lock.lock();
        try {
            while (!conoceDestino)
                puedenArrancar.await();
            estanDestino = true;
            puedenAbandonar.signalAll();

            while (nFurgonetas != 1)
                soloLider.await();
            nFurgonetas--;
            estanDestino = false;
            conoceDestino = false;
            System.out.println("** Furgoneta " + id + " lider abandona el convoy **");


        } finally {
            lock.unlock();
        }

    }

    /**
     * Las furgonetas seguidoras hasta que la lider avisa de que han llegado al destino
     * y abandonan el convoy
     **/
    public void seguirLider(int id) throws InterruptedException {
        lock.lock();
        try {

            while (!conoceDestino)
                puedenArrancar.await();
            System.out.println("Siguiendo al lider " + id);


            while (!estanDestino)
                puedenAbandonar.await();

            nFurgonetas--;
            System.out.println("Furgoneta " + id + " abandona el convoy. nFurgonetas: " + nFurgonetas);

            if (nFurgonetas == 1)
                soloLider.signalAll();


        } finally {
            lock.unlock();
        }


    }


    /**
     * Programa principal. No modificar
     **/
    public static void main(String[] args) {
        final int NUM_FURGO = 10;
        Convoy c = new Convoy(NUM_FURGO);
        Furgoneta flota[] = new Furgoneta[NUM_FURGO];

        for (int i = 0; i < NUM_FURGO; i++)
            flota[i] = new Furgoneta(i, c);

        for (int i = 0; i < NUM_FURGO; i++)
            flota[i].start();
    }

}

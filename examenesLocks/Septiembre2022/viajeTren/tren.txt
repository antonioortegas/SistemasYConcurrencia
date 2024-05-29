package viajeTren;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Tren {

    private int pasajerosA, pasajerosB, MAX_CAPACIDAD_VAGON, MAX_CAPACIDAD_VAGON_B;
    private boolean estanBajando, viajeFinalizado;


    ReentrantLock lock;
    Condition puedenSubir, puedenBajar, puedenArrancar;

    public Tren() {
        pasajerosA = 0;
        pasajerosB = 0;
        MAX_CAPACIDAD_VAGON = 4;
        MAX_CAPACIDAD_VAGON_B = 2;
        estanBajando = false;
        viajeFinalizado = false;
        lock = new ReentrantLock();
        puedenSubir = lock.newCondition();
        puedenBajar = lock.newCondition();
        puedenArrancar = lock.newCondition();
    }

    public void viaje(int id) throws InterruptedException {
        lock.lock();
        try {
            while (pasajerosA == MAX_CAPACIDAD_VAGON && pasajerosB == MAX_CAPACIDAD_VAGON_B)
                puedenSubir.await();
            if (pasajerosA < MAX_CAPACIDAD_VAGON) {
                pasajerosA++;
                System.out.println("Pasajero " + id + " se ha subido al vagón A. Total pasajero A: " + pasajerosA);
            } else if (pasajerosB < MAX_CAPACIDAD_VAGON_B) {
                pasajerosB++;
                System.out.println("Pasajero " + id + " se ha subido al vagón B. Total pasajero B: " + pasajerosB);
            }

            if (pasajerosA == MAX_CAPACIDAD_VAGON && pasajerosB == MAX_CAPACIDAD_VAGON_B) //Soy el último en subirme
                puedenArrancar.signal();

            while (!viajeFinalizado)
                puedenBajar.await();

            if (pasajerosA > 0) {
                pasajerosA--;
                System.out.println("Pasajero " + id + " se ha bajado del vagón A. Total pasajero A: " + pasajerosA);
            } else if (pasajerosB > 0) {
                pasajerosB--;
                System.out.println("Pasajero " + id + " se ha bajado del vagón B. Total pasajero B: " + pasajerosB);
            }
            if (pasajerosA == 0 && pasajerosB == 0) {
                estanBajando = false;
                viajeFinalizado = false;
                puedenSubir.signalAll();
                System.out.println("-----------------------------------------");
            }


        } finally {
            lock.unlock();
        }


    }

    public void empiezaViaje() throws InterruptedException {
        lock.lock();

        try {
            while ((pasajerosA != MAX_CAPACIDAD_VAGON && pasajerosB != MAX_CAPACIDAD_VAGON_B) || estanBajando)
                puedenArrancar.await();
            System.out.println("        Maquinista:  empieza el viaje");
            viajeFinalizado = true;
        } finally {
            lock.unlock();
        }

    }

    public void finViaje() throws InterruptedException {
        lock.lock();

        try {
            System.out.println("        Maquinista:  fin del viaje");
            viajeFinalizado = true;
            estanBajando = true;
            puedenBajar.signalAll();
        } finally {
            lock.unlock();
        }

    }
}

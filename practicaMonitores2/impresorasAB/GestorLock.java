package impresorasAB;

import java.util.concurrent.locks.*;

/**
 * 
 * Mas justo, porque ahora primero todos estan en una cola general, que es la que se despierta cuando se libera una impresora
 * Despues se encolan en A o en B dependiendo de cual se libero. Entonces en la cola A hay impresoras tipo A y AB
 * Cuando notifico a cola A, solo notifico a una hebra, que puede ser A o AB
 * 
 */
public class GestorLock implements Gestor {

	private int numImpA, numImpB; //numero de impresoras de cada tipo

	private ReentrantLock l = new ReentrantLock();

	private Condition colaA = l.newCondition();
	private Condition colaB = l.newCondition();
	private Condition colaGeneral = l.newCondition();

	private int numProcesosColaA = 0;
	private int numProcesosColaB = 0;

	public GestorLock(int numA, int numB) {
		numImpA = numA;
		numImpB = numB;
	}

	public void qImpresoraA(int id) throws InterruptedException {
		try {
			l.lock();
			System.out.println("Cliente tipo A: " + id + "pide impresora");
			while (numImpA == 0) {
				colaGeneral.await();
			}
			while (numImpA == 0) {
				colaGeneral.signal();
				numProcesosColaA++;
				colaA.await();
			}
			numImpA--;
			System.out.println("Dada impresora de tipo A a: " + id + ". Quedan " + numImpA + " disponibles de tipo A y "
					+ numImpB + " de tipo B");
		} finally {
			l.unlock();
		}
	}

	public void qImpresoraB(int id) throws InterruptedException {
		try {
			l.lock();
			System.out.println("Cliente tipo B: " + id + "pide impresora");
			while (numImpB == 0) {
				colaGeneral.await();
			}
			while (numImpB == 0) {
				colaGeneral.signal();
				numProcesosColaB++;
				colaB.await();
			}
			numImpB--;
			System.out.println("Dada impresora de tipo B a: " + id + ". Quedan " + numImpA + " disponibles de tipo A y "
					+ numImpB + " de tipo B");
		} finally {
			l.unlock();
		}
	}

	public char qImpresoraAB(int id) throws InterruptedException {
		char valor = 'A';

		try {
			l.lock();
			System.out.println("Cliente tipo AB: " + id + "pide impresora");
			while (numImpA + numImpB == 0) {
				System.out.println("No hay impresoras disponibles");
				colaGeneral.await();
			}
			if (numImpA > 0) {
				numImpA--;
				System.out.println(
						"Dada impresora de tipo A a: " + id + ". Quedan " + numImpA + " disponibles de tipo A y "
								+ numImpB + " de tipo B");
			} else {
				valor = 'B';
				numImpB--;
				System.out.println(
						"Dada impresora de tipo B a: " + id + ". Quedan " + numImpA + " disponibles de tipo A y "
								+ numImpB + " de tipo B");
			}
		} finally {
			l.unlock();
		}

		return valor;
	}

	public void dImpresora(char tipo) {
		try {
			l.lock();
			if (tipo == 'A') {
				numImpA++;
				if (numProcesosColaA != 0) {
					numProcesosColaA--;
					colaA.signal();
				} else {
					colaGeneral.signal();
				}
				System.out.println("Devuelvo impresora de tipo A");
			} else {
				numImpB++;
				if (numProcesosColaB != 0) {
					numProcesosColaB--;
					colaB.signal();
				} else {
					colaGeneral.signal();
				}
				System.out.println("Devuelvo impresora de tipo B");
			}
			System.out.println("Quedan " + numImpA + " disponibles de tipo A y "
					+ numImpB + " de tipo B");
			colaGeneral.signal();
		} finally {
			l.unlock();
		}
	}
}
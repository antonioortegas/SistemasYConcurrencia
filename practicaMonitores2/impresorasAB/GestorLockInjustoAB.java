package impresorasAB;

import java.util.concurrent.locks.*;

/**
 * 
 * Injusto porque priorizo las colas A y B frente a la cola AB
 * 
 */
public class GestorLockInjustoAB implements Gestor {

	private int numImpA, numImpB; //numero de impresoras de cada tipo

	private ReentrantLock l = new ReentrantLock();

	private Condition colaA = l.newCondition();
	private Condition colaB = l.newCondition();
	private Condition colaAB = l.newCondition();

	private int numProcesosColaA = 0;
	private int numProcesosColaB = 0;

	public GestorLockInjustoAB(int numA, int numB) {
		numImpA = numA;
		numImpB = numB;
	}

	public void qImpresoraA(int id) throws InterruptedException {
		try {
			l.lock();
			System.out.println("Cliente tipo A: " + id + "pide impresora");
			while (numImpA == 0) {
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
				colaAB.await();
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
					colaAB.signal();
				}
				System.out.println("Devuelvo impresora de tipo A");
			} else {
				numImpB++;
				if (numProcesosColaB != 0) {
					numProcesosColaB--;
					colaB.signal();
				} else {
					colaAB.signal();
				}
				System.out.println("Devuelvo impresora de tipo B");
			}
			System.out.println("Quedan " + numImpA + " disponibles de tipo A y "
					+ numImpB + " de tipo B");
			colaAB.signal();
		} finally {
			l.unlock();
		}
	}
}
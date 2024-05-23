package impresorasAB;

import java.util.concurrent.locks.*;

/**
 * 
 * Ineficiente porque despierto a las hebras en las colas (A y AB) o (B y AB), cuando solo puede entrar una
 * 
 */
public class GestorLockIneficiente implements Gestor {

	private int numImpA, numImpB; //numero de impresoras de cada tipo

	private ReentrantLock l = new ReentrantLock();

	private Condition colaA = l.newCondition();
	private Condition colaB = l.newCondition();
	private Condition colaAB = l.newCondition();

	public GestorLockIneficiente(int numA, int numB) {
		numImpA = numA;
		numImpB = numB;
	}

	public void qImpresoraA(int id) throws InterruptedException {
		try {
			l.lock();
			System.out.println("Cliente tipo A: " + id + "pide impresora");
			while (numImpA == 0) {
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
				colaA.signal();
				System.out.println("Devuelvo impresora de tipo A");
			} else {
				numImpB++;
				colaB.signal();
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
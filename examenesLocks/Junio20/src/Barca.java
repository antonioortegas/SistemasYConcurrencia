import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barca {
	private int orilla = 1; //orilla donde esta la barca, empieza al norte (1)

	private ReentrantLock l = new ReentrantLock();

	private Condition puedenSubirCondition = l.newCondition();
	private boolean puedenSubir = true; // en alguna orilla deben empezar a subir
	private int pasajeros = 0;
	private int MAX_PASAJEROS = 3;

	private Condition puedenBajarCondition = l.newCondition();
	private boolean puedenBajar = false; // deben esperar a llegar

	private Condition puedoViajarCondition = l.newCondition();
	private boolean puedoViajar = false; // deben esperar a llegar
	


	/*
	 * El Pasajero id quiere darse una vuelta en la barca desde la orilla pos
	 */
	public void subir(int id,int pos) throws InterruptedException{
		//epero que la barca este en mi orilla y que haya asiento
		try{
			l.lock();
			while (!puedenSubir || orilla != pos) {
				puedenSubirCondition.await();
			}
			System.out.println("El pasajero " + id + " se sube en la orilla " + pos);
			//cuando me subo
			pasajeros++;
			if (pasajeros == MAX_PASAJEROS) {
				puedenSubir = false; // ya no puede subir nadie mas
				puedoViajar = true; // le digo al capitan que ya estamos
				puedoViajarCondition.signalAll();
			}
		} finally {
			l.unlock();
		}
	}
	
	/*
	 * Cuando el viaje ha terminado, el Pasajero que esta en la barca se baja
	 */
	public int bajar(int id) throws InterruptedException{
		try{
			l.lock();
			while (!puedenBajar) {
				puedenBajarCondition.await();
			}
			//cuando me bajo
			System.out.println("El pasajero " + id + " se baja en la orilla " + orilla);
			pasajeros--;
			//si soy el ultimo en bajar digo que ya se pueden subir los de la otra orilla
			if (pasajeros == 0) {
				puedenSubir = true;
				puedenBajar = false; //ya no se baja nadie mas
				System.out.println("=== SIGUIENTE TURNO ===");
				puedenSubirCondition.signalAll();
			}
			return orilla; //digo que ya estoy en la otra
		} finally {
			l.unlock();
		}
	}
	/*
	 * El Capitan espera hasta que se suben 3 pasajeros para comenzar el viaje
	 */
	public void esperoSuban() throws InterruptedException{
		try{
			l.lock();
			//si no esta llena la barca, me espero. Cuando se llene, digo que ya no se sube nadie mas
			while (!puedoViajar) {
				puedoViajarCondition.await();
			}
			puedoViajar = false;
			//cuando se llena, nos vamos
			puedenSubir = false;
			System.out.println("La barca comienza el viaje desde " + orilla);
		} finally {
			l.unlock();
		}
	}
	/*
	 * El Capitan indica a los pasajeros que el viaje ha terminado y tienen que bajarse
	 */
	public void finViaje() throws InterruptedException{
		try{
			l.lock();
			//hemos llegado
			puedenBajar = true;
			orilla = (orilla + 1) % 2; // estoy en la otra orilla
			System.out.println("La barca llega a " + orilla);
			
			puedenBajarCondition.signalAll();
		} finally {
			l.unlock();
		}
	}

}

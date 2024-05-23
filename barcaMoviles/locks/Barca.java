package locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//TODO
public class Barca extends Thread {
	private static final int C = 4; //capacidad de la barca

	//Variables booleanas y/o numericas que nos ayuden a escribir las condiciones de sincronización
	private Lock lock = new ReentrantLock();

	private Condition cBarcaLibre = lock.newCondition();
	private boolean puedeSubir = true;

	private Condition cFinViaje = lock.newCondition();
	private boolean puedeBajar = false;

	private int numAndroid = 0;
	private int numIphone = 0;

	/**
	 * Un estudiante con movil android llama a este metodo 
	 * cuando quiere cruzar el rio. Debe esperarse a montarse en la
	 * barca de forma segura y solo si hay hueco. 
	 * 
	 * El ultimo en subir los lleva a la otra orilla
	 * 
	 * Al llegar a la otra orilla del rio antes de salir del método
	 * despierta al resto de pasajeros para que se bajen
	 * 
	 * @param id del estudiante android que llama al metodo
	 * @throws InterruptedException
	 */
	public void android(int id) throws InterruptedException {
		//CS_Android: Espera mientras no pueda subir porque el coche está lleno o no sea seguro
		//subir (no pueden subir 3 android + 1 iphone o 3 iphone + 1 android
		//TO COMPLETE

		System.out.println("Android" + id + " entra en el coche ");

		//TO COMPLETE

		//El ultimo en subir los lleva a la otra orilla
		if (true) {
			//TO COMPLETE
			System.out.println("*-----------------------------------*");
			System.out.println("Coche lleno. Nos vamos de viaje!");
			System.out.println("*-----------------------------------*");
			Thread.sleep(1000);
			System.out.println("Hemos llegado a la otra orilla");
			System.out.println("*-----------------------------------*");

			//TO COMPLETE

			System.out.println("Android" + id + " baja del coche. Quedan por bajar: " + (numAndroid + numIphone));
		} else {
			//CS2_Estudiantes: Espera hasta que se pueda bajar (la barca esté llena y 
			//viajen a la otra orilla)
			//TO COMPLETE

			System.out.println("Android" + id + " baja del coche. Quedan por bajar: " + (numAndroid + numIphone));
		}

		if (true) {
			System.out.println("*-----------------------------------*");
			System.out.println("La barca esta vacia y volvemos a empezar!");
			System.out.println("*-----------------------------------*");
			//TO COMPLETE
		}
	}

	/**
	 * Un estudiante con movil iphone llama a este metodo 
	 * cuando quiere cruzar el rio. Debe esperarse a montarse en la
	 * barca de forma segura y solo si hay hueco. 
	 * 
	 * El ultimo en subir los lleva a la otra orilla
	 * 
	 * Al llegar a la otra orilla del rio antes de salir del método
	 * despierta al resto de pasajeros para que se bajen
	 * 
	 * @param id del estudiante iphone que llama al metodo
	 * @throws InterruptedException
	 */

	public void iphone(int id) throws InterruptedException {
		//CS_Iphone: Espera mientras no pueda subir porque el coche está lleno o no sea seguro
		//subir (no pueden subir 3 android + 1 iphone o 3 iphone + 1 android
		//TO COMPLETE

		System.out.println("Iphone" + id + " entra en el coche ");

		//TO COMPLETE

		if (true) {//Es el �ltimo en subir y los lleva a todos a la otra orilla
			//TO COMPLETE
			System.out.println("*-----------------------------------*");
			System.out.println("Coche lleno. Nos vamos de viaje!");
			System.out.println("*-----------------------------------*");
			Thread.sleep(1000);
			System.out.println("Hemos llegado a la otra orilla");
			System.out.println("*-----------------------------------*");

			//TO COMPLETE

			System.out.println("IPhone" + id + " baja del coche. Quedan por bajar: " + (numAndroid + numIphone));
		} else {
			//CS2_Estudiantes: Espera hasta que se pueda bajar (la barca esté llena y 
			//viajen a la otra orilla)
			//TO COMPLETE

			System.out.println("IPhone" + id + " baja del coche. Quedan por bajar: " + (numAndroid + numIphone));
		}

		if (true) {
			System.out.println("*-----------------------------------*");
			System.out.println("La barca esta vacia y volvemos a empezar!");
			System.out.println("*-----------------------------------*");
			//TO COMPLETE
		}
	}
}
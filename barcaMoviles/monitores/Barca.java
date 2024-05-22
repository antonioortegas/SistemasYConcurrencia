
public class Barca extends Thread {
	private static final int C = 4; //capacidad de la barca

	//Variables booleanas y/o numericas que nos ayuden a escribir las condiciones de sincronización
	private boolean puedeSubir = true;
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
	public synchronized void android(int id) throws InterruptedException {
		//CS_Android: Espera mientras no pueda subir porque el coche está lleno o no sea seguro
		//subir (no pueden subir 3 android + 1 iphone o 3 iphone + 1 android
		//TO COMPLETE
		while (!puedeSubir || numIphone == 3 || (numAndroid == 2 && numIphone == 1)) {
			wait();
		}

		System.out.println("Android" + id + " entra en el coche ");

		//TO COMPLETE
		numAndroid++;

		//El ultimo en subir los lleva a la otra orilla
		if (numAndroid + numIphone == C) {
			//TO COMPLETE
			puedeSubir = false;
			System.out.println("*-----------------------------------*");
			System.out.println("Coche lleno. Nos vamos de viaje!");
			System.out.println("*-----------------------------------*");
			Thread.sleep(1000);
			System.out.println("Hemos llegado a la otra orilla");
			System.out.println("*-----------------------------------*");

			//TO COMPLETE
			numAndroid--;
			puedeBajar = true;
			notifyAll();

			System.out.println("Android" + id + " baja del coche. Quedan por bajar: " + (numAndroid + numIphone));
		} else {
			//CS2_Estudiantes: Espera hasta que se pueda bajar (la barca esté llena y 
			//viajen a la otra orilla)
			//TO COMPLETE
			while (!puedeBajar) {
				wait();
			}
			numAndroid--;

			System.out.println("Android" + id + " baja del coche. Quedan por bajar: " + (numAndroid + numIphone));
		}

		// el ultimo en bajar dice que ya se puede subir
		if (numAndroid + numIphone == 0) {
			System.out.println("*-----------------------------------*");
			System.out.println("La barca esta vacia y volvemos a empezar!");
			System.out.println("*-----------------------------------*");
			//TO COMPLETE
			puedeBajar = false;
			puedeSubir = true;
			notifyAll();
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

	public synchronized void iphone(int id) throws InterruptedException {
		//CS_Iphone: Espera mientras no pueda subir porque el coche está lleno o no sea seguro
		//subir (no pueden subir 3 android + 1 iphone o 3 iphone + 1 android
		//TO COMPLETE
		while (!puedeSubir || numAndroid == 3 || (numAndroid == 1 && numIphone == 2)) {
			wait();
		}
		System.out.println("Iphone" + id + " entra en el coche ");

		//TO COMPLETE
		numIphone++;

		if (numAndroid + numIphone == C) {//Es el �ltimo en subir y los lleva a todos a la otra orilla
			//TO COMPLETE
			puedeSubir = false;
			System.out.println("*-----------------------------------*");
			System.out.println("Coche lleno. Nos vamos de viaje!");
			System.out.println("*-----------------------------------*");
			Thread.sleep(1000);
			System.out.println("Hemos llegado a la otra orilla");
			System.out.println("*-----------------------------------*");

			//TO COMPLETE
			numIphone--;
			puedeBajar = true;
			notifyAll();

			System.out.println("IPhone" + id + " baja del coche. Quedan por bajar: " + (numAndroid + numIphone));
		} else {
			//CS2_Estudiantes: Espera hasta que se pueda bajar (la barca esté llena y 
			//viajen a la otra orilla)
			//TO COMPLETE
			while (!puedeBajar) {
				wait();
			}
			numIphone--;

			System.out.println("IPhone" + id + " baja del coche. Quedan por bajar: " + (numAndroid + numIphone));
		}

		if (numAndroid + numIphone == 0) {
			System.out.println("*-----------------------------------*");
			System.out.println("La barca esta vacia y volvemos a empezar!");
			System.out.println("*-----------------------------------*");
			//TO COMPLETE
			puedeBajar = false;
			puedeSubir = true;
			notifyAll();
		}
	}
}
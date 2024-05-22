package prodconsMon;

public class Buffer {
	//Buffer
	private int N = 10; //Tamaño del buffer
	private int[] buffer = new int[N]; //Buffer
	private int[] numCons = new int[N];//Contador del numero de consumidores que faltan por leer cada elemento del buffer
	//Numero de espacios que hay en el buffer
	private int nespacios = N;
	private int ncons; //Numero de consumidores del buffer
	private int[] nelems; //Numero de elementos en el buffer para cada consumidor
	private int[] c; //posicion a partir de la que consume cada consumidor
	private int p; //posicion a partir de la que guarda el productor

	//n - numero de consumidores
	public Buffer(int n) {
		System.out.println("Tamanio del buffer " + N);
		System.out.println("Numero de consumidores " + n + "\n");

		ncons = n;
		p = 0;
		c = new int[ncons];
		nespacios = N;
		nelems = new int[ncons];
	}

	//synchronized (ex. mutua) + wait/notify (cond. sincronizaci�n)
	public synchronized void almacenar(int elem) throws InterruptedException {
		//CS-Productor: espera mientras el buffer est� lleno
		while (nespacios == 0) {
			wait();
		}
		System.out.println("Un productor guarda el elemento " + elem + " en la posicion " + p);

		//actualiza todas las variables
		buffer[p] = elem;
		for (int i = 0; i < ncons; i++) {
			nelems[i]++;
		}
		numCons[p] = ncons;
		if (p == N - 1) { // actualizacion de p de forma circular
			p = 0;
		} else {
			p++;
		}
		notifyAll();
	}

	//id- identificador del consumidor
	public synchronized int extrae(int id) throws InterruptedException {
		int v;
		//CS-Consumidor_id: espera mientras no tenga elementos que consumir
		while (nelems[id] == 0) { // wait si no tengo mas elementos para mi
			wait();
		}
		v = buffer[c[id]];
		System.out.println("Un consumidor consume el elemento " + v + " en la posicion " + "PLACEHOLDER");
		//actualiza todas las variables
		if (numCons[c[id]] == 0) {
			nespacios++;
		}
		notifyAll();
		return v;
	}
}

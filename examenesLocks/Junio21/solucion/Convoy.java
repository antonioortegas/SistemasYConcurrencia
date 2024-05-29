import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Convoy {
	
	private int NUM_FURGONES = 0;

	private ReentrantLock l = new ReentrantLock();
	private int lider = 0; //quien es el lider
	private int furgonetas = 0;
	
	//CS1-Lider no puede calcular la ruta hasta que el convoy este lleno
	//CS2-Seguidora no puede abandonar el convoy hasta que el lider no diga que hemos llegado
	//CS3-Lider no puede salir del convoy hasta que lo hayan hecho el resto

	//una forgoneta intenta unirse al convoy. Si esta lleno me espero. Si esta vacio, soy el lider, si no, seguidora
	//el lider no puede calcular la ruta sin que el convoy este lleno
	//el convoy no puede salir sin haber calculado la ruta
	//mientras que el lider no diga que hemos llegado al destino, nadie sale
	//el lider sale el ultimo
	private Condition puedoSubirCondition = l.newCondition();
	private boolean puedoSubir = true;

	private Condition estaLlenoCondition = l.newCondition();
	private boolean estaLleno = true;

	private Condition rutaCalculadaCondition = l.newCondition();
	private boolean rutaCalculada = false;

	private Condition puedeBajarSeguidorCondition = l.newCondition();
	private boolean puedeBajarSeguidor = false;

	private Condition puedeBajarLiderCondition = l.newCondition();
	private boolean puedeBajarLider = false;

	public Convoy(int tam) {
		NUM_FURGONES = tam;
	}
	
	/**
	 * Las furgonetas se unen al convoy
	 * La primera es la lider, el resto son seguidoras 
	 * @throws InterruptedException 
	 **/
	public int unir(int id) throws InterruptedException{
		l.lock();
		try{
			while(furgonetas==NUM_FURGONES){
				puedoSubirCondition.await(); // si esta lleno el convoy, me espero
			}
			//si es el primero es el lider, si no, un seguidor
			if(furgonetas == 0){
				lider = id;
				System.out.println("** Furgoneta " +id + " lidera del convoy **");
			} else {
				System.out.println("Furgoneta "+id+" seguidora");
			}
			furgonetas++;
			if(furgonetas == NUM_FURGONES){
				//Si soy el ultimo, aviso al lider para que calcule la ruta
				estaLleno = true;
				puedoSubir = false;
				estaLlenoCondition.signal();
			}
			return lider;
		}finally{
			l.unlock();
		}
	}

	/**
	 * La furgoneta lider espera a que todas las furgonetas se unan al convoy 
	 * Cuando esto ocurre calcula la ruta y se pone en marcha
	 * @throws InterruptedException 
	 * */
	public void calcularRuta(int id) throws InterruptedException{
		l.lock();
		try{
			//espero a que este lleno
			while(furgonetas < NUM_FURGONES){
				estaLlenoCondition.await();
			}
			//cuando estemos todos subidos, calculo la ruta y me pongo en marcha
			System.out.println("** Furgoneta "+id+" lider:  ruta calculada, nos ponemos en marcha **");
			rutaCalculadaCondition.signalAll(); //nos vamos todos
		} finally{
			l.unlock();
		}
	}
	
	
	/** 
	 * La furgoneta lider avisa al las furgonetas seguidoras que han llegado al destino y deben abandonar el convoy
	 * La furgoneta lider espera a que todas las furgonetas abandonen el convoy
	 * @throws InterruptedException 
	 **/
	public void destino(int id) throws InterruptedException{
		l.lock();
		try{
			//aviso de que se baje todo el mundo
			puedeBajarSeguidor = true;
			puedeBajarSeguidorCondition.signalAll();
			while(!puedeBajarLider){ // hasta que no me quede solo no me bajo
				puedeBajarLiderCondition.await();
			}
			//cuando este solo, me bajo y pueden entrar mas al convoy
			furgonetas--;
			puedeBajarLider = false;
			estaLleno = false;
			rutaCalculada = false;
			puedoSubir = true;
			puedoSubirCondition.signalAll();
			System.out.println("** Furgoneta "+id+" lider abandona el convoy **");
		} finally{
			l.unlock();
		}
	}

	/**
	 * Las furgonetas seguidoras hasta que la lider avisa de que han llegado al destino
	 * y abandonan el convoy
	 * @throws InterruptedException 
	 **/
	public void seguirLider(int id) throws InterruptedException{
		l.lock();
		try{
			//si no me dicen que hemos llegado yo me espero
			while(!puedeBajarSeguidor){
				puedeBajarSeguidorCondition.await();
			}
			//una vez me pueda bajar, me bajo
			furgonetas--;
			if(furgonetas == 1){
				//si solo queda el lider dentro, le digo que ya puede salir
				puedeBajarSeguidor = false;
				puedeBajarLider = true;
				puedeBajarLiderCondition.signal();
			}
			System.out.println("Furgoneta "+id+" abandona el convoy");
		} finally{
			l.unlock();
		}
	}

	
	
	/**
	* Programa principal. No modificar
	**/
	public static void main(String[] args) {
		final int NUM_FURGO = 10;
		Convoy c = new Convoy(NUM_FURGO);
		Furgoneta flota[ ]= new Furgoneta[NUM_FURGO];
		
		for(int i = 0; i < NUM_FURGO; i++)
			flota[i] = new Furgoneta(i,c);
		
		for(int i = 0; i < NUM_FURGO; i++)
			flota[i].start();
	}

}

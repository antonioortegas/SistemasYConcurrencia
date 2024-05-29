
//import java.util.concurrent.Semaphore;

public class Juego {
	private Urna urna;
	
	public Juego (int NBolas) {
		this.urna = new Urna();
		urna.llenoUrna(NBolas);//urna inicial
		System.out.println("Urna="+urna+"\n");
		//completar, si es necesario
	}
	
	/**
	 * 
	 * @param id del jugador que quiere sacar una bola de la urna
	 * @return el color de la bola que ha sacado
	 * @throws InterruptedException
	 * 
	 * Inicialmente,  todos los jugadores sacan una bola de la uran.
	 * A partir de la segunda jugada,
	 * "un jugador no puede participar en una nueva jugada hasta que la jugada anterior 
	 * no ha terminado completamente (todos los jugadores saben si deben continuar en el juego)". 
	 * 
	 */
	public Color sacarBola(int id) throws InterruptedException {
		
		
		System.out.print("Jugador "+id+" ha sacado una bola ");
		
		return null; //cambiar
	}
	
	/**
	 * 
	 * @param id del jugador que espera
	 * @return si ha terminado el juego
	 * @throws InterruptedException
	 * 
	 * "Un jugador que ha sacado una bola, tiene que esperar a que todos los demás 
	 * jugadores de la misma jugada saquen su bola antes de decidir si continúa en el juego,
	 * ha perdido o es el ganador"
	 */
	public boolean esperaTodos(int id) throws InterruptedException {
		
		
		System.out.println("Jugador "+id+" termina jugada");
		
		return false; // cambiar
	}
	


}

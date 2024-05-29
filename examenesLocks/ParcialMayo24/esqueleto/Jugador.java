
public class Jugador extends Thread{
	
	private Juego j;
	private int id;
	private Color color;
	public Jugador(int id, Juego j) {
		this.j = j;
		this.id = id;
	}
	
	public void run() {
			boolean fin;
			try {
				do {
					color = j.sacarBola(id);		
					fin=j.esperaTodos(id);
				} while (color==Color.blanca && !fin);
				if (color==Color.blanca) {
					System.out.println("\nJugador "+id+" es el GANADOR y se lleva el trofeo a casa!!!");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
	}

}

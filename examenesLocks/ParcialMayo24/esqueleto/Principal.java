
public class Principal {
	public static void main(String[] args) {
		final int J = 6;
		Juego juego = new Juego(J);
		Jugador[] jugador = new Jugador[J];
		for (int i=0; i<jugador.length; i++)
			jugador[i] = new Jugador(i,juego);
		for (int i=0; i<jugador.length; i++)
			jugador[i].start();
		for (int i=0; i<jugador.length; i++)
			try {
				jugador[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("\nFin del programa");
	}
	

}


import java.util.*;

public class Urna {
	
	private static Random r = new Random();
	private Color[] urna;
	private int nBolas=0;

	
	public void llenoUrna(int NBolas){	
		nBolas=NBolas;
		urna = new Color[NBolas];
		int negra=r.nextInt(NBolas);
		for (int i=0; i<urna.length; i++) {
			urna[i] = Color.blanca;
		}
		urna[negra]=Color.negra;	
	}
	
	public void creoUrnaVacia(int tam) {
		urna = new Color[tam];
		nBolas = 0;
	}
	
	public Color sacoBola() {
		if (nBolas==0) {
			throw new RuntimeException("Urna Vacía");
		}
		Color bola = urna[nBolas-1];
		nBolas--;	
		return bola;
	}
	
	public String toString() {
		return Arrays.toString(urna);
	}

}

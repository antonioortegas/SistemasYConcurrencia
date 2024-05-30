import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Curso {

	//Numero maximo de alumnos cursando simultaneamente la parte de iniciacion
	private final int MAX_ALUMNOS_INI = 10;
	
	//Numero de alumnos por grupo en la parte avanzada
	private final int MAX_ALUMNOS_AV = 3;
	
	private ReentrantLock l = new ReentrantLock();

	private int nAlumnosIniciacion = 0;
	private int nAlumnosAvanzado = 0;
	private int alumnosTerminados = 0;

	private Condition puedeMatricularseIniciacionCondition = l.newCondition(); //si ini no esta lleno
	private Condition puedeMatricularseAvanzadoCondition = l.newCondition(); //si ava no esta lleno

	private Condition puedeEmpezarAvanzadoCondition = l.newCondition(); //espero a que seamos tres para empezar, cuando seamos tres cierro la entrada

	private boolean puedeTerminarAvanzado = false; //para esperar a que los tres del grupo terminen, cuando seamos tres abro
	private Condition puedeTerminarAvanzadoCondition = l.newCondition();

	//El alumno tendra que esperar si ya hay 10 alumnos cursando la parte de iniciacion
	public void esperaPlazaIniciacion(int id) throws InterruptedException{
		try {
			l.lock();
			while (nAlumnosIniciacion == MAX_ALUMNOS_INI) { // si esta lleno espero
				puedeMatricularseIniciacionCondition.await();
			}
			//cuando entro al curso
			nAlumnosIniciacion++;
			//Mensaje a mostrar cuando el alumno pueda conectarse y cursar la parte de iniciacion
			System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte iniciacion");
		} finally {
			l.unlock();
		}
	}

	//El alumno informa que ya ha terminado de cursar la parte de iniciacion
	public void finIniciacion(int id) throws InterruptedException {
		try {
			l.lock();
			nAlumnosIniciacion--;
			//Mensaje a mostrar para indicar que el alumno ha terminado la parte de principiantes
			System.out.println("PARTE INICIACION: Alumno " + id + " termina parte iniciacion");
			//Libera la conexion para que otro alumno pueda usarla
			puedeMatricularseIniciacionCondition.signalAll();
		} finally {
			l.unlock();
		}

	}
	
	/* El alumno tendra que esperar:
	 *   - si ya hay un grupo realizando la parte avanzada
	 *   - si todavia no estan los tres miembros del grupo conectados
	 */
	public void esperaPlazaAvanzado(int id) throws InterruptedException{
		try {
			l.lock();
				//Espera a que no haya otro grupo realizando esta parte
				while (nAlumnosAvanzado == MAX_ALUMNOS_AV) {
					puedeMatricularseAvanzadoCondition.await();
				}
				nAlumnosAvanzado++;
				if (nAlumnosAvanzado != 3) {
					System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + MAX_ALUMNOS_AV + " alumnos. Hay " + nAlumnosAvanzado + " alumnos.");
				} else {
					//Mensaje a mostrar cuando el alumno pueda empezar a cursar la parte avanzada
					System.out.println("PARTE AVANZADA: Hay " + nAlumnosAvanzado + " alumnos. Alumno " + id
							+ " empieza el proyecto");
					puedeEmpezarAvanzadoCondition.signalAll();
				}
				while (nAlumnosAvanzado != 3) {
					puedeEmpezarAvanzadoCondition.await();
				}
		} finally {
			l.unlock();
		}
	}
	
	/* El alumno:
	 *   - informa que ya ha terminado de cursar la parte avanzada 
	 *   - espera hasta que los tres miembros del grupo hayan terminado su parte 
	 */ 
	public void finAvanzado(int id) throws InterruptedException{
		try {
			l.lock();
			//Espera a que los 3 alumnos terminen su parte avanzada
			alumnosTerminados++;
			if (alumnosTerminados != 3) {
				//Mensaje a mostrar si el alumno tiene que esperar a que los otros miembros del grupo terminen
				System.out.println("PARTE AVANZADA: Alumno " + id + " termina su parte del proyecto. Espera al resto");
			} else {
				//Mensaje a mostrar cuando los tres alumnos del grupo han terminado su parte
				puedeTerminarAvanzado = true;
				puedeTerminarAvanzadoCondition.signalAll();
				System.out.println("PARTE AVANZADA: LOS " + MAX_ALUMNOS_AV + " ALUMNOS HAN TERMINADO EL CURSO");
			}
			while (!puedeTerminarAvanzado) {
				puedeTerminarAvanzadoCondition.await();
			}
			alumnosTerminados--;
			if (alumnosTerminados == 0) {
				puedeTerminarAvanzado = false;
				nAlumnosAvanzado = 0;
				puedeMatricularseAvanzadoCondition.signalAll();
			}

			
		} finally {
			l.unlock();
		}
		
	}
}

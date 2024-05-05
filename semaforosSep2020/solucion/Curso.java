package solucion;

import java.util.concurrent.Semaphore;

public class Curso {

	private final int MAX_ALUMNOS_INI = 10;
	private final int ALUMNOS_AV = 3;

	private int alumnosIniciacion = 0;
	private int alumnosEsperandoProyecto = 0;
	private int alumnosTerminadoProyecto = 0;

	private Semaphore mutex = new Semaphore(1);
	private Semaphore esperaCursarIniciacion = new Semaphore(1);
	private Semaphore esperaCursarAvanzado = new Semaphore(1);
	private Semaphore esperaIniciarProyecto = new Semaphore(0);
	private Semaphore esperaTerminarProyecto = new Semaphore(0);

	public void esperaPlazaIniciacion(int id) throws InterruptedException {
		//Solo hay 10 plazas para esta parte del curso
		//El alumno tendrá que esperar si ya hay 10 alumnos cursando la parte de inicializacion
		esperaCursarIniciacion.acquire();
		//El semaforo est� rojo
		mutex.acquire();
		System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte principiantes");
		alumnosIniciacion++;

		//�Lo pongo otra vez verde o lo dejo en rojo?
		if (alumnosIniciacion < MAX_ALUMNOS_INI) {
			esperaCursarIniciacion.release(); //lo pongo en verde
		}
		mutex.release();
	}

	public void finIniciacion(int id) throws InterruptedException {
		//El alumno termina esta parte de forma individual, no tiene que esperar por los demás
		mutex.acquire();
		System.out.println("PARTE INICIACION: Alumno " + id + " termina parte principiantes");
		alumnosIniciacion--;
		if (alumnosIniciacion == MAX_ALUMNOS_INI - 1) {
			esperaCursarIniciacion.release();

		}
		mutex.release();
	}

	public void esperaPlazaAvanzado(int id) throws InterruptedException {
		//Espera a que los 3 alumnos anteriores hayan terminado
		esperaCursarAvanzado.acquire();
		mutex.acquire();

		alumnosEsperandoProyecto++;
		//System.out.println("Hay " + alumnosEsperandoProyecto + " alumno(s) esperando");
		System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + ALUMNOS_AV + " alumnos");

		if (alumnosEsperandoProyecto < ALUMNOS_AV) {
			//Le da acceso al siguiente alumno - semaforo binario
			esperaCursarAvanzado.release();

			//Se bloquea si aún no son tres alumnos en el curso
			mutex.release();
			esperaIniciarProyecto.acquire();
			mutex.acquire();
		}

		System.out.println("PARTE AVANZADA: Hay " + ALUMNOS_AV + " alumnos. Alumno " + id + " empieza el proyecto");

		//IMPORTANTE: Despertado en cascada
		alumnosEsperandoProyecto--;
		//Despierta en cascada a los alumnos esperando
		if (alumnosEsperandoProyecto != 0)
			esperaIniciarProyecto.release();
		System.out.println("Numero permisos: " + esperaIniciarProyecto.availablePermits());
		mutex.release();
	}

	public void finAvanzado(int id) throws InterruptedException {
		//Espera a que los 3 alumnos terminen su parte avanzada
		mutex.acquire();
		alumnosTerminadoProyecto++;
		System.out.println("PARTE AVANZADA: Alumno " + id + " termina su parte del proyecto. Espera al resto");

		if (alumnosTerminadoProyecto != ALUMNOS_AV) {
			//Espera a que los compañeros terminen su parte del proyecto
			mutex.release();
			esperaTerminarProyecto.acquire();
			mutex.acquire();
		} else {
			//Ya han terminado los 3 alumnos
			System.out.println("PARTE AVANZADA: LOS " + ALUMNOS_AV + " ALUMNOS HAN TERMINADO SU PROYECTO");
		}

		//Despertamos en cascada
		alumnosTerminadoProyecto--;
		if (alumnosTerminadoProyecto != 0) {
			//Despierta al siguiente
			esperaTerminarProyecto.release();
		} else {
			//Avisamos a los siguientes ALUMNOS_AV
			esperaCursarAvanzado.release();
		}
		mutex.release();
	}
}

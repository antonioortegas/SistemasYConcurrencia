#include <stdio.h>
#include <stdlib.h>
#include "gestion_memoria.h"

#define MAX 1000

/* Crea la estructura utilizada para gestionar la memoria disponible.
 * Inicialmente, s�lo un nodo desde 0 a MAX
*/
void crear(T_Manejador* manejador){
    (*manejador) = (T_Manejador)malloc(sizeof(struct T_Nodo));
    (*manejador)->inicio = 0;
    (*manejador)->fin = MAX-1;
    (*manejador)->sig = NULL;
    printf("Creando memoria\n");
}

/* Destruye la estructura utilizada (libera todos los nodos de la lista.
 * El par�metro manejador debe terminar apuntando a NULL
*/
void destruir(T_Manejador* manejador){
    printf("Liberando memoria\n");
}

/* Devuelve en �dir� la direcci�n de memoria �simulada� (unsigned) donde comienza
 * el trozo de memoria continua de tama�o �tam� solicitada.
 * Si la operaci�n se pudo llevar a cabo, es decir, existe un trozo con capacidad
 * suficiente, devolvera TRUE (1) en �ok�; FALSE (0) en otro caso.
 */
void obtener(T_Manejador *manejador, unsigned tam, unsigned* dir, unsigned* ok){
    printf("Obteniendo memoria\n");
}

/* Muestra el estado actual de la memoria, bloques de memoria libre */
void mostrar (T_Manejador manejador){
    if(manejador == NULL){
        printf("No hay memoria\n");
        return;
    }
    T_Manejador aux = manejador;
    do
    {
        printf("Inicio: %d, Fin: %d\n", aux->inicio, aux->fin);
        aux = aux->sig;
    }while (aux->sig != NULL);
    printf("Mostrando memoria\n");
}

/* Devuelve el trozo de memoria continua de tama�o �tam� y que
 * comienza en �dir�.
 * Se puede suponer que se trata de un trozo obtenido previamente.
 */
void devolver(T_Manejador *manejador,unsigned tam,unsigned dir){
    printf("Devolviendo memoria\n");
}
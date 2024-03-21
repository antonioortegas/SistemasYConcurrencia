#include <stdio.h>
#include <stdlib.h>
#include "gestion_memoria.h"

#define MAX 1000

/* Crea la estructura utilizada para gestionar la memoria disponible.
 * Inicialmente, s�lo un nodo desde 0 a MAX
*/
void crear(T_Manejador* manejador){
    *manejador = (T_Manejador)malloc(sizeof(struct T_Nodo));
    (*manejador)->inicio=0;
    (*manejador)->fin=MAX-1;
    (*manejador)->sig=NULL;
}

/* Destruye la estructura utilizada (libera todos los nodos de la lista.
 * El par�metro manejador debe terminar apuntando a NULL
*/
void destruir(T_Manejador* manejador) {
    T_Manejador aux = NULL;
    while(*manejador!=NULL){
        aux = *manejador;
        *manejador = (*manejador)->sig;
        free(aux);
    }
}



/* Devuelve en �dir� la direcci�n de memoria �simulada� (unsigned) donde comienza
 * el trozo de memoria continua de tama�o �tam� solicitada.
 * Si la operaci�n se pudo llevar a cabo, es decir, existe un trozo con capacidad
 * suficiente, devolvera TRUE (1) en �ok�; FALSE (0) en otro caso.
 */
void obtener(T_Manejador *manejador, unsigned tam, unsigned* dir, unsigned* ok){
    *ok = 0;
    *dir = 0;
    T_Manejador actual = *manejador;
    T_Manejador anterior = NULL;

    while(actual!=NULL){
        
        if((actual->fin - actual->inicio + 1) >= tam){
            (*ok) = 1;
            (*dir) = actual->inicio;
            if((actual->fin - actual->inicio + 1) == tam){
                if(anterior==NULL){
                    (*manejador) = actual->sig;
                } else {
                    anterior->sig = actual->sig;
                }
                free(actual);
            } else {
                actual->inicio = (actual->inicio) + tam;
            }
            break;
        } else {
            anterior = actual;
            actual = actual->sig;
        }
    }
}



/* Muestra el estado actual de la memoria, bloques de memoria libre */
void mostrar (T_Manejador manejador){
    while(manejador!=NULL){
        printf("Desde %d a %d: Libre\n", manejador->inicio, manejador->fin);
        manejador = manejador->sig;
    }
    printf("-----\n");
}


/* Devuelve el trozo de memoria continua de tama�o �tam� y que
 * comienza en �dir�.
 * Se puede suponer que se trata de un trozo obtenido previamente.
 */
void devolver(T_Manejador *manejador, unsigned tam, unsigned dir) {
    T_Manejador actual = (*manejador);
    T_Manejador anterior = NULL;
    while(actual != NULL && dir > (actual->inicio)){
        anterior = actual;
        actual = actual->sig;
    }
    //He llegado donde tengo que insertar
    T_Manejador nuevo;
    nuevo = (T_Manejador)malloc(sizeof(struct T_Nodo));
    nuevo->inicio = dir;
    nuevo->fin = dir + tam - 1;
    nuevo->sig = actual;

    if(anterior == NULL){
        (*manejador) = nuevo;
    } else {
        anterior->sig = nuevo;
    }
}


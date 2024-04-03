/*
 ============================================================================
 Name        : Practica2B.c
 Author      : esc
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "arbolbb.h"

/**
 * Pide un n�mero "tam" al usuario, y
 * crea un fichero binario para escritura con el nombre "nfichero"
 * en que escribe "tam" numeros (unsigned int) aleatorios
 * Se utiliza srand(time(NULL)) para establecer la semilla (de la libreria time.h)
 * y rand()%100 para crear un n�mero aleatorio entre 0 y TAM.
 */
void creafichero(char* nfichero){
	FILE* fd;
	srand(time(NULL));
	int TAM;
	printf("Numero de elementos a generar: ");
	scanf("%d", &TAM);
	printf("%d", TAM);
	fd = fopen(nfichero,"wb");
	if (fd == NULL){
		perror("Error creando ficheroBinario.dat");
	}
	int number;
	for(int i=0; i<TAM; i++){
		number = rand()%TAM;
		fwrite(&number,sizeof(int),1,fd);
	}
	fclose(fd);
}
/**
 * Muestra por pantalla la lista de n�meros (unsigned int) almacenada
 * en el fichero binario "nfichero"
 */
void muestrafichero(char* nfichero){
	FILE* fd;
	if ((fd = fopen(nfichero,"rb"))==NULL){
		perror("Error abriendo ficheroBinario.dat");
	}
	int valor, leidos;
	while ((leidos = fread(&valor,sizeof(int),1,fd)) > 0){
		printf("%d ", valor);
	}
	fclose(fd);
}

/**
 * Guarda en el arbol "*miarbol" los n�meros almacenados en el fichero binario "nfichero"
 */

void cargaFichero(char* nfichero, T_Arbol* miarbol){
	FILE* fd;
	if ((fd = fopen(nfichero,"rb"))==NULL){
		perror("Error abriendo ficheroBinario.dat");
	}
	int valor, leidos;
	while ((leidos = fread(&valor,sizeof(int),1,fd)) > 0){
		Insertar(miarbol,valor);
	}
	fclose(fd);
}

int main(void) {
	//setvbuf(stdout,NULL,_IONBF,0);

	char nfichero[50];
	printf ("Introduce el nombre del fichero binario:\n");
	fflush(stdout);
	scanf ("%s",nfichero);
	fflush(stdin);
	creafichero(nfichero);
	printf("\nAhora lo leemos y mostramos:\n");
	muestrafichero(nfichero);
	fflush(stdout);
/* COMMENT TREE FUNCTIONS FOR NOW
*/
	printf ("\nAhora lo cargamos en el arbol\n");
	T_Arbol miarbol;
	Crear (&miarbol);
	cargaFichero(nfichero,&miarbol);
	printf ("\nY lo mostramos ordenado\n");
	Mostrar(miarbol);
	fflush(stdout);
	printf("\nAhora lo guardamos ordenado\n");
	FILE * fich;
	fich = fopen (nfichero, "wb");
	Salvar (miarbol, fich);
	fclose (fich);
	printf("\nY lo mostramos ordenado\n");
	muestrafichero(nfichero);
	Destruir (&miarbol);
	return EXIT_SUCCESS;
}

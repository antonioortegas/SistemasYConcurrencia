#include <stdio.h>
#include <stdlib.h>

/* Parte 1: algoritmo de descifrado
 * 	v: puntero a un bloque de 64 bits.
 * 	k: puntero a la clave para descifrar.
 * 	Sabiendo que "unsigned int" equivale a 4 bytes (32 bits)
 * 	Podemos usar la notaci�n de array con v y k
 * 	v[0] v[1] --- k[0] ... k[3]
 */
void decrypt(unsigned int *v, unsigned int *k)
{
	//Definir variables e inicializar los valores de delta y sum
	unsigned int sum = 0xC6EF3720;
	unsigned int delta = 0x9e3779b9;	

	//Repetir 32  veces (usar un bucle) la siguiente secuencia de operaciones de bajo nivel
	for (int i = 0; i < 32; i++){
		//Restar a v[1] el resultado de la operacion :
		// (v[0] desplazado a la izquierda 4 bits +k[2]) XOR (v[0] + sum)  XOR (v[0] desplazado a la derecha 5 bits)+k[3]
		v[1] -= ((v[0] << 4) + k[2]) ^ (v[0] + sum) ^ ((v[0] >> 5) + k[3]);

		//Restar a v[0] el resultado de la operacion:
		// (v[1] desplazado a la izquierda 4 bits + k[0]) XOR (v[1]+ sum)  XOR (v[1] desplazado a la derecha 5 bits)+k[1]
		v[0] -= ((v[1] << 4) + k[0]) ^ (v[1] + sum) ^ ((v[1] >> 5) + k[1]);

		//Restar a sum el valor de delta
		sum -= delta;
	}
}

/* Parte 2: Metodo main. Tenemos diferentes opciones para obtener el nombre del fichero cifrado y el descifrado
 * 1. Usar los argumentos de entrada (argv)
 * 2. Pedir que el usuario introduzca los nombres por teclado
 * 3. Definir arrays de caracteres con los nombres
 */
int main(){
	/*Declaraci�n de las variables necesarias, por ejemplo:
	* variables para los descriptores de los ficheros ( FILE * fent, *fsal)
	* la constante k inicializada con los valores de la clave
	* buffer para almacenar los datos (puntero a unsigned int, m�s adelante se reserva memoria din�mica */
	unsigned int v[2];
	unsigned int k[4] = {128, 129, 130, 131};
	FILE *fent, *fsal;
	unsigned int imgSize, imgSize2;
	unsigned int *buffer;

	/*Abrir fichero encriptado fent en modo lectura binario
	 * nota: comprobar que se ha abierto correctamente
	 * el nombre del fichero cifrado se pide al usuario*/

	char nombreFichero[256];
	printf("Introduce el nombre del fichero cifrado: ");
	scanf("%s", nombreFichero);

	fent = fopen(nombreFichero, "rb");
	if (fent == NULL){
		printf("Error al abrir el fichero cifrado\n");
		return 1;
	}

	/*Abrir/crear fichero fsal en modo escritura binario
	 * nota: comprobar que se ha abierto correctamente*/
	char nombreFicheroSalida[256];
	printf("Introduce el nombre del fichero descifrado: ");
	scanf("%s", nombreFicheroSalida);

	fsal = fopen(nombreFicheroSalida, "wb");
	if (fsal == NULL){
		printf("Error al abrir el fichero descifrado\n");
		return 1;
	}
	
   /*Al comienzo del fichero cifrado esta almacenado el tama�o en bytes que tendr� el fichero descifrado.
    * Leer este valor (imgSize)*/
   	fread(&imgSize, sizeof(unsigned int), 1, fent);
	printf("Tama�o del fichero descifrado: %d\n", imgSize);

	/*Reservar memoria din�mica para el buffer que almacenara el contenido del fichero cifrado
	 * nota1: si el tama�o del fichero descifrado (imgSize) no es m�ltiplo de 8 bytes,
	 * el fichero cifrado tiene adem�s un bloque de 8 bytes incompleto, por lo que puede que no coincida con imgSize
	 * nota2: al reservar memoria din�mica comprobar que se realiz� de forma correcta */
	buffer = (unsigned int *)malloc(imgSize);
	if (imgSize % 8 != 0) { //El tamaño no es divisible por 8
	//Cambiamos el valor de imgSize2 para poder leer relleno
		imgSize2 = imgSize + (8 - (imgSize % 8));
	}else{
		imgSize2 = imgSize;
	}

	/*Leer la informaci�n del fichero cifrado, almacenado el contenido en el buffer*/
	fread(buffer, sizeof(unsigned int), imgSize2/4, fent);


	/*Para cada bloque de 64 bits (8 bytes o dos unsigned int) del buffer, ejecutar el algoritmo de desencriptado*/
	for (int i = 0; i < imgSize2/8; i++){
		v[0] = buffer[i*2];
		v[1] = buffer[i*2 + 1];
		decrypt(v, k);
		buffer[i*2] = v[0];
		buffer[i*2 + 1] = v[1];
	}


    /*Guardar el contenido del buffer en el fichero fsal
     * nota: en fsal solo se almacenan tantos bytes como diga imgSize */
	fwrite(buffer, sizeof(unsigned int), imgSize/4, fsal);

	/*Cerrar los ficheros*/
	fclose(fent);
	fclose(fsal);

}



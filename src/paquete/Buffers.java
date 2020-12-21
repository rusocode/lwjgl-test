package paquete;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

import org.lwjgl.BufferUtils;

/* BUFFERS -> https://github.com/mattdesl/lwjgl-basics/wiki/Java-NIO-Buffers */

// Explicacion mas detalla de los buffers http://tutorials.jenkov.com/java-nio/buffers.html
public class Buffers {

	public static void main(String[] args) {

		// LWJGL incluye utilidades para crear facilmente buferes
		ByteBuffer buffer = BufferUtils.createByteBuffer(4); // Crea un buffer con capacidad de 4 bytes

		// Coloca el byte y mueve la posicion hacia adelante
		buffer.put((byte) 4);
		buffer.put((byte) 3);
		buffer.put((byte) 45);
		buffer.put((byte) 127);

		/* Una vez que necesite leer los datos, debe cambiar el buffer del modo de escritura al modo de lectura usando el metodo
		 * flip(). */
		buffer.flip(); // Restablece la posicion a cero

		// Itera cada posicion del buffer
		for (int i = 0; i < buffer.limit(); i++)
			System.out.println(buffer.get());

		// Despues de leer los datos hay que borrar todo el buffer para poder escribir nuevamente
		buffer.clear();

		// Para entender lo que esta sucediendo, comparemoslo con una matriz de Java

		// Crea la matriz de tamaño fijo
		byte[] array = new byte[4];

		// La posicion comienza en 0
		int posicion = 0;

		// Usando un "put" relativo, la posicion aumenta cada vez
		array[posicion++] = 4;
		array[posicion++] = 3;
		array[posicion++] = 45;
		array[posicion++] = 127;

		// "cambia" nuestra posicion/limite
		int limite = posicion;
		posicion = 0;

		for (int i = 0; i < limite; i++)
			// Usando un "get" relativo, la posicion aumenta cada vez
			System.out.println(array[posicion++]);

		/* Entonces, ¿como se relaciona esto con LWJGL y OpenGL? Hay dos formas comunes en las que usara buferes: escribiendo
		 * datos en GL (es decir, cargando datos de textura en la GPU) o leyendo datos de GL (es decir, leyendo datos de textura
		 * de la GPU u obteniendo un cierto valor del controlador). */

		// Digamos que estamos creando una textura RGBA azul 1x1, nuestra configuracion de bufer se veria asi:
		int width = 1; // 1 pixel de ancho
		int height = 1; // 1 pixel de alto
		int bpp = 4; // 4 bytes por pixel (RGBA) -> https://en.wikipedia.org/wiki/RGBA_color_model

		buffer = BufferUtils.createByteBuffer(width * height * bpp);

		// Pone los bytes Red, Green, Blue y Alpha en el buffer
		buffer.put((byte) 0x00).put((byte) 0x00).put((byte) 0xFF).put((byte) 0xFF);

		buffer.flip();

		// glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

	}
}

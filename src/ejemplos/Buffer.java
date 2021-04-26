package ejemplos;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glGetInteger;

import org.lwjgl.BufferUtils;

/* BUFFERS -> https://github.com/mattdesl/lwjgl-basics/wiki/Java-NIO-Buffers */

// Explicacion mas detalla de los buffers http://tutorials.jenkov.com/java-nio/buffers.html
public class Buffer {

	public static void main(String[] args) {

		/*
		 * Un buffer es simplemente un bloque de memoria que contiene algunos datos. Para nuestros propositos, puede
		 * considerarlo como una serie de elementos. Sin embargo, en lugar de acceso aleatorio (por ejemplo, matriz[i]), los
		 * buffers leen y escriben datos relativos a su posicion actual. Para demostrarlo, digamos que deseamos crear un buffer
		 * que contenga cuatro bytes y luego leer esos bytes. Lo crearias asi:
		 */

		ByteBuffer buffer = BufferUtils.createByteBuffer(4); // LWJGL incluye utilidades para crear facilmente buffers

		// Coloca el byte y mueve la posicion hacia adelante
		buffer.put((byte) 4);
		buffer.put((byte) 3);
		buffer.put((byte) 45);
		buffer.put((byte) 127);

		/*
		 * Una vez que necesite leer los datos, debe cambiar el buffer del modo escritura al modo lectura usando el metodo
		 * flip().
		 */
		buffer.flip(); // Restablece la posicion a cero

		// Itera cada posicion del buffer
		for (int i = 0; i < buffer.limit(); i++)
			System.out.println(buffer.get());

		// Despues de leer todos los datos, hay que borrar el buffer para poder escribir nuevamente sobre el
		buffer.clear();

		/* Para entender lo que esta sucediendo, comparemoslo con una matriz de Java. */

		// Crea un array de 4 elementos (tamaño fijo)
		byte[] array = new byte[4];

		// Inicializa la posicion en 0
		int posicion = 0;

		// Asigna el valor al array e incrementa la posicion (put relativo)
		array[posicion++] = 4;
		array[posicion++] = 3;
		array[posicion++] = 45;
		array[posicion++] = 127;

		// Metodo flip() casero :D
		int limite = posicion;
		posicion = 0;

		for (int i = 0; i < limite; i++)
			System.out.println(array[posicion++]); // get relativo

		/* . . . */

		/*
		 * Entonces, ¿Como se relaciona esto con LWJGL y OpenGL? Hay dos formas comunes en las que usara buffers: escribiendo
		 * datos en GL (es decir, cargando datos de textura en la GPU) o leyendo datos de GL (es decir, leyendo datos de textura
		 * de la GPU u obteniendo un cierto valor del controlador).
		 */

		// Digamos que estamos creando una textura RGBA rojo 1x1, nuestra configuracion del buffer se veria asi:
		int width = 1; // 1 pixel de ancho
		int height = 1; // 1 pixel de alto
		final int bpp = 4; // 4 bytes por pixel (RGBA) -> https://en.wikipedia.org/wiki/RGBA_color_model

		buffer = BufferUtils.createByteBuffer(width * height * bpp); // Crea una imagen de 1 pixel (4 bytes)

		// Coloca los bytes Red, Green, Blue y Alpha en el buffer
		buffer.put((byte) 0xFF).put((byte) 0x00).put((byte) 0x00).put((byte) 0xFF); // = 1 pixel rojo

		buffer.flip();

		// glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		// Ejemplo de como obtener datos de GL
		IntBuffer intBuffer = BufferUtils.createIntBuffer(16); // Crea un buffer con 16 elementos

		/*
		 * Esto llamara al meotodo put() relativo con el tamaño maximo de texturas, luego continue "poniendo" ceros hasta que se
		 * alcance la capacidad del buffer, entonces volteara nuestro buffer.
		 * 
		 * Como se describe en los documentos, GL_MAX_TEXTURE_SIZE nos dara un valor, pero como glGetInteger puede devolver
		 * hasta 16 elementos, LWJGL espera que nuestro buffer tenga al menos eso como capacidad. Siempre que sea posible, debe
		 * intentar reutilizar los buffers en lugar de crear siempre nuevos.
		 */
		// glGetInteger(GL_MAX_TEXTURE_SIZE);

		/*
		 * Dado que nuestro buffer ya esta volteado, nuestra posicion sera cero... para que podamos seguir adelante y tomar el
		 * primer elemento.
		 */
		int maxSize = glGetInteger(GL_MAX_TEXTURE_SIZE); // intBuffer.get();

	}
}

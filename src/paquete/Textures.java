package paquete;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Textures {

	/* Una imagen, como sabra, es simplemente una matriz de colores, renderizada en dos dimensiones, construida por pixeles
	 * individuales. Hay varias formas de almacenar una imagen, para este ejemplo se usara el formato RGBA. RGB se refiere a
	 * los canales rojo (red), verde (green) y azul (blue), y A (alpha) se refiere a la transparencia. Para este caso, cada
	 * pixel de la imagen se compone de 4 bytes.
	 * 
	 * Dado que una matriz de bytes puede volverse muy grande, generalmente usamos compresion como PNG o JPEG para disminuir
	 * el tamaño del archivo final y distribuir la imagen para la web/correo electronico/etc.
	 * 
	 * En OpenGL, usamos texturas para almacenar datos de imagenes. Las texturas OpenGL no solo almacenan datos de imagenes;
	 * son simplemente matrices flotantes almacenadas en la GPU, p. ej. util para el mapeo de sombras y otras tecnicas
	 * avanzadas.
	 * 
	 * Los pasos basicos para convertir una imagen en textura son los siguientes:
	 * 
	 * 1. Decodificar en bytes RGBA.
	 * 
	 * 2. Obtener una nueva identificacion de la textura.
	 * 
	 * 3. Enlazar esa textura.
	 * 
	 * 4. Configurar los parametros de la textura.
	 * 
	 * 5. Subir los bytes RGBA a OpenGL. */

	// Decodificacion de bytes PNG a RGBA
	/* OpenGL no sabe nada sobre GIF, PNG, JPEG, etc; solo comprende bytes y flotantes. Asi que necesitamos decodificar
	 * nuestra imagen PNG en un ByteBuffer. */

}

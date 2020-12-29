package paquete;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;

// Esta guia solo se enfocara en usar una unica unidad de textura -> https://github.com/mattdesl/lwjgl-basics/wiki/Textures
public class Texture {

	/* Una imagen, como sabra, es simplemente una matriz de colores, renderizada en dos dimensiones
	 * (https://es.wikipedia.org/wiki/Dimensi%C3%B3n), construida por pixeles individuales. Hay varias formas de almacenar
	 * una imagen, para este ejemplo se usara el formato RGBA ocupando 4 bytes por pixel. RGB se refiere a los canales rojo
	 * (red), verde (green) y azul (blue), y A (alpha) se refiere a la transparencia.
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

	public final int target = GL_TEXTURE_2D;
	public final int id;
	public final int width;
	public final int height;

	public static final int LINEAR = GL_LINEAR;
	public static final int NEAREST = GL_NEAREST;

	public static final int CLAMP = GL_CLAMP;
	public static final int CLAMP_TO_EDGE = GL_CLAMP_TO_EDGE; // GL_CLAMP_TO_EDGE es parte de GL12
	public static final int REPEAT = GL_REPEAT;

	public Texture(URL url) throws IOException {
		/* Para los juegos de estilo "pixel-art", generalmente la constante GL_NEAREST es adecuada para este caso, ya que
		 * conduce a una escala de borde duro sin desenfoque. */
		this(url, NEAREST);
	}

	public Texture(URL url, int filter) throws IOException {
		// Wrap (envoltura)
		/* Para renderizar un objeto (ej. img ladrillo), necesitamos darle a OpenGL cuatro vertices (En geometria, un vertice es
		 * el punto donde se encuentran dos o mas elementos unidimensionales). Como puede ver, terminamos con un cuadrilatero
		 * 2D. Cada vertice tiene una serie de atributos, que incluyen Posicion (x, y) y Coordenadas de textura (s, t). Las
		 * coordenadas de textura se definen en el espacio tangente, generalmente entre 0.0 y 1.0. Estos le dicen a OpenGL donde
		 * tomar muestras de nuestros datos de textura.
		 * 
		 * Nota: Esto depende de que nuestro sistema de coordenadas tenga un origen en la parte superior izquierda ("Y-abajo").
		 * Algunas bibliotecas, como LibGDX, utilizaran el origen inferior izquierdo ("Y-arriba"), por lo que los valores de
		 * Posicion y Coordenadas de textura pueden estar en un orden diferente.
		 * 
		 * Entonces, ¿Que sucede si usamos valores de coordenadas de textura menores que 0.0 o mayores que 1.0? Aqui es donde
		 * entra en juego el modo de envoltura. Le decimos a OpenGL como manejar valores fuera de las coordenadas de textura.
		 * Los dos modos mas comunes son GL_CLAMP_TO_EDGE, que simplemente muestrea el color del borde, y GL_REPEAT, que
		 * conducira a un patron repetido. Por ejemplo, el uso de 2.0 y GL_REPEAT hara que la imagen se repita dos veces dentro
		 * del ancho y alto que especificamos. */
		this(url, filter, CLAMP_TO_EDGE);
	}

	public Texture(URL url, int filter, int wrap) throws IOException {
		InputStream input = null;
		try {
			// Abre una conexion de la URL especificada y devuelve un InputStream para leer desde esa conexion
			input = url.openStream();

			// Inicializa el decodificador
			PNGDecoder dec = new PNGDecoder(input);

			// Almacena las dimensiones de la imagen
			width = dec.getWidth();
			height = dec.getHeight();

			// Estamos usando el formato RGBA, es decir, 4 componentes o "bytes por pixel"
			final int bpp = 4;

			// Crea un buffer que contendra los bytes totales de la imagen
			ByteBuffer buf = BufferUtils.createByteBuffer(width * height * bpp); // bpp * width * height

			// 1) Decodifica la imagen de tipo PNG en el buffer de bytes en formato RGBA
			dec.decode(buf, width * bpp, PNGDecoder.Format.RGBA);

			// Voltea el buffer en "modo lectura" para OpenGL
			buf.flip();

			// 2) Habilita el texturizado y genera una identificacion para que GL sepa que textura enlazar
			glEnable(target);
			id = glGenTextures();

			// 3) Enlaza la textura
			glBindTexture(target, id);

			/* Usa una alineacion de 1 para estar seguro. Esto le dice a OpenGL como descomprimir los bytes RGBA que
			 * especificaremos. */
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1); // Configura el modo de desempaquetar

			// 4) Configura los parametros
			// Configurar el filtrado, es decir, como OpenGL interpolara los pixeles al escalar hacia arriba o hacia abajo
			glTexParameteri(target, GL_TEXTURE_MIN_FILTER, filter);
			glTexParameteri(target, GL_TEXTURE_MAG_FILTER, filter);
			// Configura el modo de ajuste, es decir, como OpenGL manejara los pixeles fuera del rango esperado
			glTexParameteri(target, GL_TEXTURE_WRAP_S, wrap);
			glTexParameteri(target, GL_TEXTURE_WRAP_T, wrap);

			/* 5) La llamada a glTexImage2D es lo que configura la textura real en OpenGL. */
			glTexImage2D(target, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf); // Pasa los datos RGBA a OpenGL

			// Puede consultar el ancho y alto maximo de textura con lo siguiente
			// int maxSize = glGetInteger(GL_MAX_TEXTURE_SIZE);

			/* En general, la mayoria de las computadoras modernas permiten texturas de al menos 4096x4096, pero si quieres estar
			 * realmente seguro, puedes limitarte a 2048x2048. Si cree que va a trabajar con controladores antiguos o limitadores (o
			 * Android, iOS, WebGL), es posible que desee limitarse a 1024x1024. */

		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
	}

}

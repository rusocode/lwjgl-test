package paquete;

public class SpriteBatching {

	/* Si intentaramos usar debugTexture del tutorial de Textures para renderizar todos nuestros mosaicos y sprites, es
	 * probable que nos encontremos rapidamente con problemas de rendimiento. Esto se debe a que solo estamos enviando un
	 * sprite a la vez a la GPU. Lo que necesitamos es "agrupar" muchos sprites en la misma llamada de dibujo; para esto
	 * usamos un SpriteBatch.
	 * 
	 * Un sprite no es mas que un conjunto de vertices que forman una forma rectangular. Cada vertice contiene una serie de
	 * atributos que lo definen, como por ejemplo:
	 * 
	 * Position(x, y) - Donde se encuentra el vertice en la pantalla.
	 * 
	 * TexCoord(s, t) - Que region de nuestra textura queremos representar.
	 * 
	 * Color(r, g, b, a) - El color del vertice, utilizado para especificar el tinte o la transparencia. */

	// SpriteBatch spriteBatch = new SpriteBatch();

	// called on game creation
	public void create() {
		// create a single batcher we will use throughout our application
		// spriteBatch = new SpriteBatch();
	}

}

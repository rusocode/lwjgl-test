package paquete;

// La API lwjgl-basics incluye una implementacion minima de SpriteBatch de uso gratuito: esta modelada segun el batcher en LibGDX.
// https://github.com/mattdesl/lwjgl-basics/blob/master/src/mdesl/graphics/SpriteBatch.java
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

	public void create() {
		// Crea un solo lote que usaremos en toda nuestra aplicacion
		// spriteBatch = new SpriteBatch();
	}

	public void render() {
		// Preparar el lote para renderizar
		// spriteBatch.begin();

		// Dibuja todos los sprites
		// spriteBatch.draw(mySprite1, x, y);
		// spriteBatch.draw(mySprite2, x, y);

		// Finaliza el lote, vaciar los datos a la GPU
		// spriteBatch.end();
	}

	// Llamado cuando se cambia el tamaño de la pantalla
	public void resize(int width, int height) {
		// Notificar al sprite batch cada vez que se cambie el tamaño la pantalla
		// spriteBatch.resize(width, height);
	}

	// Explicacion detallada del SpriteBatch
	/* Primero comenzamos el lote (begin()), simplemente le decimos que esta en "modo de dibujo". Luego, se llama a
	 * spriteBatch.draw(...) que empuja la informacion del vertice del sprite (posicion, texcoord, color) en una pila muy
	 * grande. Los vertices no se pasan a la GPU hasta que ocurre una de las siguientes situaciones:
	 * 
	 * - El lote se ve obligado a procesarse con end() u otra llamada que vacia el lote, como flush().
	 * 
	 * - El usuario intenta dibujar un objeto que usa una textura diferente a la anterior. El lote debe lavarse y enlazar la
	 * nueva textura antes de que podamos continuar.
	 * 
	 * - Hemos alcanzado la capacidad de nuestra pila, por lo que necesitamos vaciarla para comenzar de nuevo.
	 * 
	 * Esta es la idea basica detras de un batcher de sprites. Como puede ver, el uso de muchas texturas dara lugar a muchas
	 * llamadas de dibujo (ya que el lote debera limpiarse para cada nueva textura). Es por eso que siempre se recomienda un
	 * atlas de texturas (tambien conocido como hoja de sprites); nos permite renderizar muchos sprites en una sola llamada
	 * de dibujo. */

	// TextureRegion
	/* Para obtener el mejor rendimiento, deberiamos usar un atlas de texturas y dibujar regiones (tambien conocidas como
	 * subimagenes) para crear los sprites de nuestro juego. Para esto tenemos una clase de utilidad, TextureRegion. Nos
	 * permite especificar en pixeles la posicion superior izquierda (x, y) y el tamaño (ancho, alto) de nuestra subimagen.
	 * 
	 * Podemos obtener una TextureRegion del mosaico con lo siguiente: */

	// Especifica x, y, ancho y alto del mosaico
	// region = new TextureRegion(64, 64, 64, 64);

	/* Como puede ver, la utilidad TextureRegion nos permite obtener subimagenes sin preocuparnos por calcular las
	 * coordenadas de la textura. Luego podemos renderizar el mosaico individual con nuestro lote de sprites asi: */

	// Dentro de SpriteBatch start/end...
	// spriteBatch.draw(region, x, y);

	// Color del vertice
	/* Podemos cambiar el tinte y la transparencia de nuestros sprites ajustando el color del lote, tambien conocido como
	 * "color de vertice".
	 * 
	 * El RGB resultante se multiplica por el color de la textura; asi que si usamos una textura blanca (1, 1, 1, 1) y
	 * especificamos un color de vertice de (1, 0, 0, 1), el resultado seria rojo. El componente Alfa del color nos permite
	 * ajustar la opacidad de los sprites renderizados en la pantalla. */

	// spriteBatch.begin();

	// Las llamadas a draw() ahora usaran 50% de opacidad
	// spriteBatch.setColor(1f, 1f, 1f, 0.5f);
	// spriteBatch.draw(...);
	// spriteBatch.draw(...);

	// Las llamadas a draw() ahora usaran 100% de opacidad (default)
	// spriteBatch.setColor(1f, 1f, 1f, 1f);
	// spriteBatch.draw(...);

	// spriteBatch.end();

	// ¡Triqngulos, no Cuadrados!
	/* En la serie anterior, hemos estado pensando en texturas como cuadriculas, pero en realidad la mayoria de los
	 * SpriteBatch usaran dos triangulos adyacentes para representar un sprite rectangular. Los vertices pueden ordenarse de
	 * manera diferente segun el motor (LibGDX tiende a usar el origen inferior izquierdo).
	 * 
	 * Un solo objeto tiene 2 triangulos o 6 vertices. Cada vertice tiene 8 atributos (X, Y, S, T, R, G, B, A) que juntos
	 * forman Posicion, TexCoord y Color. Esto significa que con cada sprite, ¡estamos empujando 48 flotadores a la pila! Un
	 * mezclador de sprites mas optimizado podria empaquetar el RGBA en un solo flotador, o podria renunciar a los colores
	 * de los vertices por completo. */
}

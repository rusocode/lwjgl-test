package paquete;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/* MONITOR -> https://github.com/mattdesl/lwjgl-basics/wiki/Display */

public class Game {

	// Ya sea para habilitar el VSync en hardware
	public static final boolean VSYNC = true;

	// Ancho y alto de la ventana
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	// Si usa el modo de pantalla completa
	public static final boolean FULLSCREEN = false;

	// Si nuestro bucle de juego se esta ejecutando
	protected boolean running = false;

	public static void main(String[] args) throws LWJGLException {
		new Game().start();
	}

	// Ejecuta el juego
	public void start() throws LWJGLException {
		// Configura la pantalla
		Display.setTitle("Test LWJGL");
		Display.setResizable(true);
		Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT)); // Resolucion de la pantalla
		Display.setVSyncEnabled(VSYNC); // Si el hardware VSync esta habilitado
		Display.setFullscreen(FULLSCREEN); // Si la pantalla completa esta habilitada

		// Crea y mustra la pantalla
		Display.create();

		// Cree el contexto OpenGL e inicializa cualquier recurso
		create();

		// Llamar a este metodo antes de ejecutar para configurar el tama�o inicial
		resize();

		running = true;

		// Mientras todavia se esta ejecutando y el usuario no ha cerrado la ventana, entonces...
		while (running && !Display.isCloseRequested()) {
			// Si se cambio el tama�o del juego, actualiza la proyeccion
			if (Display.wasResized()) resize();

			// Renderiza el juego
			render();

			// Voltea los buferes y sincroniza a 60 FPS
			Display.update();
			Display.sync(60);
		}

		// Elimina los recursos y destruye la ventana
		dispose();
		Display.destroy();
	}

	// Sal del bucle de juego y cierra la ventana
	public void exit() {
		running = false;
	}

	// Metodo para configura el juego y contexto
	protected void create() {
		// Los juegos 2D generalmente no requieren pruebas de profundidad
		glDisable(GL_DEPTH_TEST);

		// Habilita la combinacion
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Establecer claro a negro transparente
		glClearColor(0f, 0f, 0f, 0f);

		// ... Inicializar los recursos aqui ...
	}

	// Llamado a renderizar nuestro juego
	protected void render() {
		// Clear the screen
		glClear(GL_COLOR_BUFFER_BIT); // https://www.khronos.org/registry/OpenGL-Refpages/gl2.1/xhtml/glClear.xml
		// https://stackoverflow.com/questions/5479951/what-is-the-purpose-of-gl-color-buffer-bit-and-gl-depth-buffer-bit
		
		// ... render our game here ...
	}

	// Metodo para cambiar el tama�o del juego
	protected void resize() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		// ... Actualice la matriz de proyeccion aqui ...
	}

	// Metodo para destruir el juego
	protected void dispose() {
		// ... Deshacerse de las texturas, etc. ...
	}

}

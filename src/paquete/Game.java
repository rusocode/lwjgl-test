package paquete;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

/* MONITOR -> https://github.com/mattdesl/lwjgl-basics/wiki/Display */

public class Game {

	// Opcion para habilitar el VSync del hardware
	public static final boolean VSYNC = true;

	// Ancho y alto de la ventana
	public static final int WIDTH = 640; // 800
	public static final int HEIGHT = 480; // 600

	// Pantalla completa
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

		// Crea y muestra la pantalla
		Display.create();

		// Crea el contexto OpenGL e inicializa cualquier recurso
		create();

		// Llamar a este metodo antes de ejecutar el juego para configurar el tamaño inicial
		resize();

		running = true;

		// Mientras el juego se este ejecutando y no se cierre la ventana, entonces...
		while (running && !Display.isCloseRequested()) {
			// Si se cambio el tamaño del juego, actualiza la proyeccion
			if (Display.wasResized()) resize();

			// Renderiza el juego
			render();

			// Actualiza el contenido de la pantalla y comprueba la entrada (voltea los buffers)
			Display.update();
			// Sincroniza a 60 FPS
			Display.sync(60);
		}

		// Elimina los recursos y destruye la ventana
		dispose();
		Display.destroy();

	}

	// Cuando se cierra la ventana (metodo implicito)
	public void exit() {
		running = false;
	}

	// Configura el juego y contexto
	protected void create() {
		// Los juegos 2D generalmente no requieren pruebas de profundidad
		// glDisable(GL_DEPTH_TEST);

		// Habilita la combinacion
		// glEnable(GL_BLEND);
		// glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		/* Ingrese el estado requerido para modificar la proyeccion. Tenga en cuenta que, a diferencia de Java2D, el sistema de
		 * coordenadas del vertice no tiene que ser igual al espacio de coordenadas de la ventana. La invocacion a glOrtho crea
		 * un sistema de coordenadas de vertice 2D como este: */
		// Upper-Left: (0,0) Upper-Right: (640,0)
		// Bottom-Left: (0,480) Bottom-Right: (640,480)
		// Si omite la invocacion del metodo glOrtho, el espacio de coordenadas de proyeccion 2D predeterminado sera asi:
		// Upper-Left: (-1,+1) Upper-Right: (+1,+1)
		// Bottom-Left: (-1,-1) Bottom-Right: (+1,-1)
		glMatrixMode(GL_PROJECTION);
		// glLoadIdentity(); // Restablece cualquier matriz de proyeccion anterior
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		// Establece claro a negro transparente
		// glClearColor(0f, 0f, 0f, 0f);

		// ... Inicializar los recursos aqui ...
	}

	// Renderizado -> https://www.youtube.com/watch?v=cvcAjgMUPUA
	protected void render() {
		// Borra el contenido 2D de la ventana (limpia la pantalla)
		glClear(GL_COLOR_BUFFER_BIT); // https://www.khronos.org/registry/OpenGL-Refpages/gl2.1/xhtml/glClear.xml
		// https://stackoverflow.com/questions/5479951/what-is-the-purpose-of-gl-color-buffer-bit-and-gl-depth-buffer-bit

		// ">>" denota una pieza posiblemente modificada de la documentacion de OpenGL (http://www.opengl.org/sdk/docs/man/)
		// >> glBegin y glEnd delimitan los vertices que definen una primitiva o un grupo de primitivas similares
		// >> glBegin acepta un solo argumento que especifica como se interpretan los vertices
		/* Todas las proximas llamadas a vertices se tomaran como puntos de un cuadrilatero hasta que se llame a glEnd. Dado que
		 * esta primitiva requiere cuatro vertices, tendremos que llamar a glVertex cuatro veces. */
		glBegin(GL_QUADS);
		/* >> Los comandos glVertex se utilizan dentro de los pares glBegin/glEnd para especificar vertices de punto, linea y
		 * poligono. */
		// >> glColor establece el color actual (a todas las llamadas posteriores a glVertex se les asignara este color)
		// >> El numero despues de 'glVertex'/'glColor' indica la cantidad de componentes (xyzw / rgba)
		// >> El caracter despues del numero indica el tipo de argumentos
		// >> (para 'glVertex': d=Double, f=Float, i=Integer)
		// >> (para 'glColor': d=Double, f=Float, b=Signed Byte, ub=Unsigned Byte)
		glColor3f(1.0f, 0.0f, 0.0f); // Pure Green
		glVertex2i(0, 0); // Upper-left
		glColor3b((byte) 0, (byte) 127, (byte) 0); // Pure Red
		glVertex2d(640.0, 0.0); // Upper-right
		glColor3ub((byte) 255, (byte) 255, (byte) 255); // White
		glVertex2f(640.0f, 480.0f); // Bottom-right
		glColor3d(0.0d, 0.0d, 1.0d); // Pure Blue
		glVertex2i(0, 480); // Bottom-left
		// If we put another four calls to glVertex2i here, a second quadrilateral will be drawn.
		glEnd();

	}

	// Cambia el tamaño del juego
	protected void resize() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		// ... Actualice la matriz de proyeccion aqui ...
	}

	// Elimina los recursos
	protected void dispose() {
		// ... Deshacerse de las texturas, etc. ...
	}

}

package ejemplos;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// LWJGL maneja la ventana de forma estatica
import static org.lwjgl.opengl.GL11.*;

// Uso de las clases de teclado y mouse con LWJGL
public class Input {

	// Crea una lista constante de clase (estatica) para agregar formas (shapes) de tipo Box
	private static final List<Box> shapes = new ArrayList<Box>(5);

	private static final int BOX_HEIGHT = 50;
	private static final int BOX_WIDHT = 50;

	private static boolean somethingIsSelected = false; // ?

	public static void main(String args[]) {
		try {
			new Input().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	public void start() throws LWJGLException {

		Display.setTitle("Input Test");
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create(); // Crea una pantalla con el modo de visualizacion y el titulo especificados

		// Crea una caja en el eje de coordenadas especificado y la agrega a la coleccion de tipo List
		shapes.add(new Box(10, 10));

		create();

		// Bucle de renderizacion en donde se realiza el manejo de entradas, la logica del juego y la adm de recursos
		while (!Display.isCloseRequested()) {

			render();

			Display.update(); // Actualizacion del procesamiento
			Display.sync(60); // Sincroniza la pantalla a 60 fps (16.67 milisegundos)

		}

		Display.destroy();

	}

	private void create() {

		/* OpenGL es un motor basado en estados, lo que significa que mucho de estos metodos especifican estados. */

		// Creacion del lente para la transformacion de la escena 3D a 2D en la pantalla
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		/*
		 * La razon por la que tienes que invertir getDY() es porque OpenGL esta destinado a tener el origen en la parte
		 * superior izquierda.
		 */
		// Creacion del sistema de coordenadas de vertice 2D
		// Esquina superior izquierda: glOrtho(0, 640, 480, 0, 1, -1)
		// Esquina inferior izquierda: glOrtho(0, 640, 0, 480, 1, -1)
		glOrtho(0, 640, 0, 480, 1, -1);
		// Creacion de la camara
		glMatrixMode(GL_MODELVIEW);

	}

	private void render() {

		// Limpia la pantalla en cada renderizacion
		glClear(GL_COLOR_BUFFER_BIT);

		// Posicion
		// System.out.println("X=" + Mouse.getX() + ":Y=" + Mouse.getY());

		/* Keyboard.next() obtiene el proximo evento del teclado, capturando una sola pulsacion de tecla. */
		while (Keyboard.next()) {

			// Elimina todas las cajas
			if (Keyboard.isKeyDown(Keyboard.KEY_L)) shapes.clear();

		}

		/*
		 * Si se pulso la tecla C, entonces...
		 * Resto el ancho de la caja al limite x para no pasarse de este.
		 */
		if (Keyboard.isKeyDown(Keyboard.KEY_C))
			shapes.add(new Box((int) (Math.random() * (640 - BOX_WIDHT)), (int) (Math.random() * (480 - BOX_HEIGHT))));

		// Si se pulso la tecla escape, cierra la aplicacion
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Display.destroy();
			System.exit(0);
		}

		// getX() devuelve la posicion absoluta del eje x
		// getDX() devuelve el movimiento en el eje x desde la ultima vez que se llamo a getDX()

		// Controla cada caja con un for each
		for (final Box box : shapes) {

			// Cuando se selecciona la caja
			// "480 - Mouse.getY()" se invierte Y para el origen superior izquierdo
			if (Mouse.isButtonDown(0) && box.isInBounds(Mouse.getX(), Mouse.getY()) /* && !somethingIsSelected */) {
				// somethingIsSelected = true;
				box.selected = true;
			}

			// Cuando se suelta la caja
			if (Mouse.isButtonDown(1)) {
				box.selected = false;
				// somethingIsSelected = false;
			}

			if (box.selected) box.update(Mouse.getDX(), Mouse.getDY()); // "-Mouse.getDY()" se invierte Y para el origen superior izquierdo

			box.draw();
		}

	}

	private static class Box {

		public int x, y;
		public boolean selected = false;
		private float colorRed, colorBlue, colorGreen;

		Box(int x, int y) {
			this.x = x;
			this.y = y;

			Random randomGenerator = new Random();
			colorRed = randomGenerator.nextFloat();
			colorBlue = randomGenerator.nextFloat();
			colorGreen = randomGenerator.nextFloat();
		}

		// Si se selecciono la caja dentro sus limites
		boolean isInBounds(int mouseX, int mouseY) {
			return mouseX >= x && mouseX <= x + 50 && mouseY >= y && mouseY <= y + 50;
		}

		// dx/dy significan delta-x y delta-y
		void update(int dx, int dy) {
			x += dx;
			y += dy;
		}

		void draw() {

			glColor3f(colorRed, colorGreen, colorBlue);

			/* la mayoria de los dosificadores de sprites usan dos triangulos adyacentes para representar un sprite rectangular. */
			glBegin(GL_QUADS);
			// Conjuntos de vertices (puntos) que juntos forman una primitiva (triangulos), dando un cuadrado como resultado
			glVertex2i(x, y); // Posicion del primer vertice x=0;y=0
			glVertex2i(x + BOX_WIDHT, y); // Posicion del segundo vertice x=x+50;y=0
			glVertex2i(x + BOX_WIDHT, y + BOX_HEIGHT); // Posicion del tercer vertice x=x+50;y=y+50
			glVertex2i(x, y + BOX_HEIGHT); // Posicion del cuarto vertice x=0;y=y+50
			glEnd();

		}

	}
}

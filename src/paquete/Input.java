package paquete;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

// Uso de las clases de teclado y mouse con LWJGL

public class Input {

	private static final List<Box> shapes = new ArrayList<Box>(16);
	private static boolean somethingIsSelected = false;
	private static long lastColourChange;

	protected boolean running = false;

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

		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.setTitle("Input Demo");
		Display.create();

		// Crea una caja en el eje de coordenadas x=15:y=20 y la agrega a la coleccion de tipo List
		shapes.add(new Box(15, 20));

		create();

		running = true;

		while (running && !Display.isCloseRequested()) {

			render();

			Display.update();
			Display.sync(60);

		}

		Display.destroy();

	}

	private void create() {

		// Creacion del lente para la transformacion de la escena 3D a 2D en la pantalla
		glMatrixMode(GL_PROJECTION);
		// Creacion del sistema de coordenadas de vertice 2D
		glOrtho(0, 640, 480, 0, 1, -1);
		// Creacion de la camara
		glMatrixMode(GL_MODELVIEW);

	}

	private void render() {

		// Limpia la pantalla en cada renderizacion
		glClear(GL_COLOR_BUFFER_BIT);

		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_C && Keyboard.getEventKeyState()) {
				shapes.add(new Box(15, 15));
			}
		}

		// Si se preciono la tecla de escape, cierra la aplicacion
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Display.destroy();
			System.exit(0);
		}

		for (final Box box : shapes) {

			// Cuando se selecciona la caja
			if (Mouse.isButtonDown(0) && box.isInBounds(Mouse.getX(), 480 - Mouse.getY()) && !somethingIsSelected) {
				somethingIsSelected = true;
				box.selected = true;
			}

			if (Mouse.isButtonDown(2) && box.isInBounds(Mouse.getX(), 480 - Mouse.getY()) && !somethingIsSelected) {
				System.out.println("Mouse 2");
				if ((System.currentTimeMillis() - lastColourChange) >= 200 /* milliseconds */) {
					box.randomiseColors();
					lastColourChange = System.currentTimeMillis();
				}
			}

			// Cuando se suelta la caja
			if (Mouse.isButtonDown(1)) {
				box.selected = false;
				somethingIsSelected = false;
			}

			if (box.selected) box.update(Mouse.getDX(), -Mouse.getDY());

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

		boolean isInBounds(int mouseX, int mouseY) {
			return mouseX > x && mouseX < x + 50 && mouseY > y && mouseY < y + 50;
		}

		// dx/dy significan delta-x y delta-y
		void update(int dx, int dy) {
			x += dx;
			y += dy;
		}

		void randomiseColors() {
			Random randomGenerator = new Random();
			colorRed = randomGenerator.nextFloat();
			colorBlue = randomGenerator.nextFloat();
			colorGreen = randomGenerator.nextFloat();
		}

		void draw() {

			glColor3f(colorRed, colorGreen, colorBlue);

			glBegin(GL_QUADS);

			glVertex2f(x, y);
			glVertex2f(x + 50, y);
			glVertex2f(x + 50, y + 50);
			glVertex2f(x, y + 50);

			glEnd();
		}

	}
}

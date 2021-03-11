package _3D;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.util.Random;

public class ThreeDeeDemo {

	private final int width = 640;
	private final int height = 480;

	private Point[] points;

	// Velocidad a la que viaja la "camara"
	private float speed;

	public static void main(String[] args) {
		try {
			new ThreeDeeDemo().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	private void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();
		setUpEntities();

		while (!Display.isCloseRequested()) {

			update();
			render();
			input();

			Display.update();
			Display.sync(60);

		}

		Display.destroy();

	}

	private void setUpDisplay() {
		try {
			Display.setTitle("3D");
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	private void setUpOpenGL() {

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		/*
		 * Crea una perspectiva con un angulo de 30 grados (campo de vision), relacion de aspecto de 640/480, 0.001f zNear
		 * (cerca) y 100 zFar (lejos).
		 */
		// +x esta a la derecha
		// +y esta en la cima
		// +z es para la camara
		gluPerspective((float) 30, 640f / 480f, 0.001f, 100); // Diferencia entre el angulo y el zFar

		glMatrixMode(GL_MODELVIEW);

		// Se asegura de que los puntos mas cercanos a la camara se muestren delante de los puntos mas alejados
		glEnable(GL_DEPTH_TEST);
	}

	private void setUpEntities() {
		// Crea un array de Point con 1000 posiciones
		points = new Point[1000];
		Random random = new Random();

		// Crea un punto con:
		// x aleatoria entre -50 y +50
		// y aleatoria entre -50 y +50
		// z aleatoria entre 0 y -200
		for (int i = 0; i < points.length; i++)
			points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f, random.nextInt(200) - 200);

	}

	private void update() {
		// update()
	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Empuja la pantalla hacia adentro a la velocidad especificada
		glTranslatef(0, 0, speed); // update?

		glBegin(GL_POINTS);
		// Itera cada punto y lo dibuja en las coordenas aleatorias
		for (Point point : points)
			glVertex3f(point.x, point.y, point.z);
		glEnd();

	}

	private void input() {

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) speed += 0.01f; // Aumenta la velocidad
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) speed -= 0.01f; // Disminuye la velocidad

		// Iterar los eventos del teclado
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) speed = 0f; // Restablece la velocidad a cero

			// Restablece la velocidad a cero y restablece la posicion
			if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
				speed = 0; // f?
				glLoadIdentity();
			}

		}
	}

	private static class Point {

		final float x;
		final float y;
		final float z;

		public Point(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

}

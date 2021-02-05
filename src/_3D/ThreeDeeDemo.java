package _3D;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.util.Random;

public class ThreeDeeDemo {

	private final int width = 640;
	private final int height = 480;

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

		
		// Puntos aleatorios
		Point[] points = new Point[1000];
		Random random = new Random();

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

		/* Cree una nueva perspectiva con un angulo de 30 grados (campo de vision), relacion de aspecto de 640/480, 0.001f zNear
		 * (cerca), 100 zFar (lejos). */
		// +x esta a la derecha
		// +y esta en la cima
		// +z es para la camara
		gluPerspective((float) 30, 640f / 480f, 0.001f, 100);

		glMatrixMode(GL_MODELVIEW);

		// Asegurarse de que los puntos mas cercanos a la camara se muestren delante de los puntos mas alejados
		glEnable(GL_DEPTH_TEST);
	}

	private void update() {
		// update()
	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT);

		// draw()
	}

	private void input() {

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

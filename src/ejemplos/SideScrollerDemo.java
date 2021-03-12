package ejemplos;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

// Muestra una aplicacion de desplazamiento lateral para juegos 2D usando glPushMatrix y glPopMatrix

public class SideScrollerDemo {

	private final int width = 640;
	private final int height = 480;

	float translate_x;

	private void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();

		// Traslacion a lo largo del eje x
		translate_x = 0;

		while (!Display.isCloseRequested()) {

			render();

			Display.update();
			Display.sync(60);

		}

		Display.destroy();

	}

	private void setUpDisplay() {
		try {
			Display.setTitle("Demostracion de desplazamiento lateral");
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	private void setUpOpenGL() {
		/*
		 * Configure una presentacion ortografica donde (0, 0) es la esquina superior izquierda y (640, 480) es la esquina
		 * inferior derecha.
		 */
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT);

		// Coloca otra matriz, un clon de la actual, en la pila de matrices
		glPushMatrix();

		// Push the screen to the left or to the right, depending on translate_x.
		glTranslatef(translate_x, 0, 0);

		// If we're pressing the space-bar and the mouse is inside the window, increase/decrease our
		// translate_x by the dynamic x movement of the mouse.
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Mouse.getX() > 0 && Mouse.getX() < 639) {
			translate_x += Mouse.getDX();
		}

		// Retrieve the "true" coordinates of the mouse.
		float mousex = Mouse.getX() - translate_x;
		float mousey = 480 - Mouse.getY() - 1;

		System.out.println("Mouse: x " + mousex + ", y " + mousey);

		// Do some OpenGL rendering (code from SimpleOGLRenderer.java).
		glBegin(GL_QUADS);
		glVertex2i(400, 400); // Upper-left
		glVertex2i(450, 400); // Upper-right
		glVertex2i(450, 450); // Bottom-right
		glVertex2i(400, 450); // Bottom-left
		glEnd();

		glBegin(GL_LINES);
		glVertex2i(100, 100);
		glVertex2i(200, 200);
		glEnd();

		// Dispose of the translations on the matrix.
		glPopMatrix();

	}

	public static void main(String[] args) {
		try {
			new SideScrollerDemo().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

}

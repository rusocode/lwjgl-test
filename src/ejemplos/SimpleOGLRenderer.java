package ejemplos;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class SimpleOGLRenderer {

	private final int width = 640;
	private final int height = 480;

	private void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();

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
			Display.setTitle("OpenGL");
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	private void setUpOpenGL() {
		// La matriz projection controla la perspectiva aplicada a las primitivas; se utiliza de forma similar a modelview
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); /* Inicializar "projection" */
		glOrtho(0, 640, 0, 480, 1, -1);
		// Se establece la matriz modelview, que controla la posicion de la camara respecto a las primitivas que renderizamos
		glMatrixMode(GL_MODELVIEW);
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

	public static void main(String[] args) {
		try {
			new SimpleOGLRenderer().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

}

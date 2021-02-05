package _3D;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

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
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 1, -1);
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

}

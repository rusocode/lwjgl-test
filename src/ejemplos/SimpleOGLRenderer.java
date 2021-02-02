package ejemplos;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class SimpleOGLRenderer {

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	private static long lastFrame;

	public static void main(String[] args) {
		try {
			new SimpleOGLRenderer().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	public void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();
		setUpEntities();
		setUpTimer();

		while (!Display.isCloseRequested()) {

			update(Delta.getDelta());
			render();

			input();

			Display.update();
			Display.sync(60);

		}

		Display.destroy();

	}

	private static void setUpDisplay() {
		try {
			Display.setTitle("OpenGL");
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	private static void setUpOpenGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	private static void setUpEntities() {

	}

	private static void setUpTimer() {
		lastFrame = Delta.getTime();
	}

	private void update(int delta) {

		// update()

	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT);

		// draw()
	}

	private void input() {
	}

	private static class Delta {

		private static long getTime() {
			return (Sys.getTime() * 1000) / Sys.getTimerResolution();
		}

		private static int getDelta() {
			long currentTime = getTime();
			int delta = (int) (currentTime - lastFrame);
			lastFrame = getTime();
			return delta;
		}

	}

}

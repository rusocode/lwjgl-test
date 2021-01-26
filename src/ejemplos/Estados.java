package ejemplos;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

// Controlando los estados del juego
public class Estados {

	protected boolean running = false;

	private static enum States {
		INTRO, MAIN_MENU, GAME
	}

	private static States state = States.INTRO;

	public static void main(String args[]) {
		try {
			new Estados().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	public void start() throws LWJGLException {

		Display.setTitle("Estados del juego");
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create();

		create();

		running = true;

		while (running && !Display.isCloseRequested()) {

			checkInput();
			render();

			Display.update();
			Display.sync(60);

		}

		Display.destroy();

	}

	private void create() {
		glMatrixMode(GL_PROJECTION);
		glOrtho(0, 640, 0, 480, 1, -1); // esq inf izquierda
		glMatrixMode(GL_MODELVIEW);
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		switch (state) {
		case INTRO:
			glColor3f(1.0f, 0f, 0f);
			glRectf(0, 0, 640, 480);
			break;
		case GAME:
			glColor3f(0f, 1.0f, 0f);
			glRectf(0, 0, 640, 480);
			break;
		case MAIN_MENU:
			glColor3f(0f, 0f, 1.0f);
			glRectf(0, 0, 640, 480);
			break;
		}
	}

	private static void checkInput() {
		switch (state) {
		case INTRO:
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) state = States.MAIN_MENU;
			break;
		case GAME:
			if (Keyboard.isKeyDown(Keyboard.KEY_B)) state = States.MAIN_MENU;
			break;
		case MAIN_MENU:
			if (Keyboard.isKeyDown(Keyboard.KEY_C)) state = States.GAME;
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) state = States.INTRO;
			break;
		}
	}

}

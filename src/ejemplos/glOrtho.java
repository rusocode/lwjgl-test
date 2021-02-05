package ejemplos;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class glOrtho {

	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setTitle("Coordinate Systems");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// glOrtho(0, 640, 480, 0, 1, -1);
		glOrtho(-1, 1, -1, 1, -1, 1);
		glMatrixMode(GL_MODELVIEW);

		while (!Display.isCloseRequested()) {

			glClear(GL_COLOR_BUFFER_BIT);

			glBegin(GL_TRIANGLES);

			glVertex2f(-1, -1); // inf izq
			glVertex2f(1, -1); // inf der
			glVertex2f(1, 1); // sup der

			glEnd();

			Display.update();
			Display.sync(60);
		}

		Display.destroy();

	}

}

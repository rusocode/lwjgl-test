package paquete;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureDemo {

	protected boolean running = false;
	private Texture img;

	public static void main(String args[]) {
		try {
			new TextureDemo().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	public void start() throws LWJGLException {

		Display.setTitle("Texture Demo");
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create();

		img = loadTexture("ladrillo");

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

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 1, -1); // esq inf izquierda
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		// Habilita el texturizado 2D
		glEnable(GL_TEXTURE_2D);

	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT);

		img.bind(); // Enlaza la textura antes de renderizarla

		glBegin(GL_QUADS);
		glTexCoord2f(1, 1);
		glVertex2f(50, 50);
		glTexCoord2f(1, 0);
		glVertex2f(50, 0);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(0, 1);
		glVertex2f(0, 50);
		glEnd();
	}

	private Texture loadTexture(String key) {

		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + key + ".png")));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No se pudo encontrar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error de I/O", "Error", JOptionPane.ERROR_MESSAGE);
		}

		return null;
	}

}

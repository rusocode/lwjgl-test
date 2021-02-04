package minecraft2d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import static minecraft2d.World.*;

import static org.lwjgl.opengl.GL11.*;

public class Screen {

	private final int width = 640;
	private final int height = 480;

	private static BlockGrid grid;

	private static BlockType type = BlockType.AIR; // Bloque de aire por defecto
	private static int selector_x, selector_y;
	private static boolean mouseEnabled = true;

	public static void main(String[] args) {

		try {
			new Screen().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

	}

	public void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();

		grid = new BlockGrid();

		while (!Display.isCloseRequested()) {

			update();
			render();
			input();

			drawSelectionBox();

			Display.update();
			Display.sync(60);

			// Si se cambio el tamaño de la ventana, entonces...
			if (Display.wasResized()) resize();

		}

		Display.destroy();

	}

	private void setUpDisplay() {
		try {

			Display.setTitle("Minecraft 2D");
			Display.setResizable(false);

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
		// Si cambio el origen a la esquina inferior izquierda las texturas se ven al revez (wtf?)
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		//
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public static int getWidth() {
		return Display.getWidth();
	}

	public static int getHeight() {
		return Display.getHeight();
	}

	private void update() {

	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT);

		grid.draw();

	}

	private void resize() {

		// Especifica los parametros de transformacion de la ventana grafica para todas las ventanas graficas
		glViewport(0, 0, Display.getWidth(), Display.getHeight());

		World.setColumnas(Screen.getWidth() / BLOCK_SIZE);
		World.setFilas(Screen.getHeight() / BLOCK_SIZE);

		System.out.println(Display.getWidth() + "," + Display.getHeight());

	}

	private void input() {
		// Maneja las entradas del usuario por medio del mouse o el teclado
		mouse();
		keyboard();
	}

	// Dibuja el cuadro de seleccion
	private void drawSelectionBox() {
		glColor4f(1f, 1f, 1f, 0.5f); // Color blanco con 50% de transparencia
		new Block(type, selector_x * World.BLOCK_SIZE, selector_y * World.BLOCK_SIZE).draw();
		glColor4f(1f, 1f, 1f, 1f); // Color blanco con 100% de transparencia
	}

	private void mouse() {
		// Si el mouse esta habilitado, entonces...
		if (mouseEnabled || Mouse.isButtonDown(0)) {

			mouseEnabled = true;

			// Divide la posicion del mouse por el tamaño del bloque y lo redondea para obtener el numero exacto de la grilla
			selector_x = Math.round(Mouse.getX() / World.BLOCK_SIZE);
			selector_y = Math.round((height - Mouse.getY() - 1) / World.BLOCK_SIZE); // -1 ?

			// Si se hizo click reemplaza el bloque seleccionado
			if (Mouse.isButtonDown(0)) grid.setAt(type, selector_x, selector_y);

		}
	}

	private void keyboard() {
		// Mientras se haya leido un evento del teclado, entonces...
		while (Keyboard.next()) {

			// Deshabilita el mouse cuando se usa el teclado para que no se superpongan los eventos
			mouseEnabled = false;

			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
				// Se le suma 1 sin asignar para que no llege a 20, evitando asi un ArrayIndexOutOfBoundsException
				if (selector_x + 1 < World.columnas) selector_x += 1;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
				// Si x es mayor a 0, entonces...
				if (selector_x > 0) selector_x -= 1;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_UP && Keyboard.getEventKeyState()) {
				// Si y es mayor a 0, entonces...
				if (selector_y > 0) selector_y -= 1;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && Keyboard.getEventKeyState()) {
				// Si y es menor al limite de filas, entonces...
				if (selector_y + 1 < World.filas) selector_y += 1;
			}

			// Si el evento causado es igual a la tecla S, entonces...
			if (Keyboard.getEventKey() == Keyboard.KEY_S) grid.save(new File("save.xml"));
			if (Keyboard.getEventKey() == Keyboard.KEY_L) grid.load(new File("save.xml"));
			if (Keyboard.getEventKey() == Keyboard.KEY_1) type = BlockType.AIR;
			if (Keyboard.getEventKey() == Keyboard.KEY_2) type = BlockType.GRASS;
			if (Keyboard.getEventKey() == Keyboard.KEY_3) type = BlockType.DIRT;
			if (Keyboard.getEventKey() == Keyboard.KEY_4) type = BlockType.STONE;
			if (Keyboard.getEventKey() == Keyboard.KEY_5) type = BlockType.BRICK;
			if (Keyboard.getEventKey() == Keyboard.KEY_C) grid.clear(); // reset
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				Display.destroy();
				System.exit(0);
			}

		}
	}

}

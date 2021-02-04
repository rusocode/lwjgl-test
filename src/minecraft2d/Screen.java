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

	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGHT = 480;

	private static BlockGrid grid;

	private static BlockType selection = BlockType.AIR; // Bloque de aire por defecto
	private static int selector_x = 0, selector_y = 0;
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

			Display.setDisplayMode(new DisplayMode(DEFAULT_WIDTH, DEFAULT_HEIGHT));

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

	// Dibuja el cuadro seleccionado
	private static void drawSelectionBox() {

		int x = selector_x * World.BLOCK_SIZE;
		int y = selector_y * World.BLOCK_SIZE;
		int x2 = x + World.BLOCK_SIZE;
		int y2 = y + World.BLOCK_SIZE;

		// Si el bloque seleccionado no es aire o si
		if (grid.getAt(selector_x, selector_y).getType() != BlockType.AIR || selection == BlockType.AIR) {

			// Se deshace de las texturas enlazadas
			glBindTexture(GL_TEXTURE_2D, 0);

			// Crea un color blanco con 50% de transparencia
			glColor4f(1f, 1f, 1f, 0.5f);

			// Dibuja ese color en la primera celda (0,0)
			glBegin(GL_QUADS);
			glVertex2i(x, y); // esq sup izq (origen)
			glVertex2i(x2, y); // esq sup der
			glVertex2i(x2, y2); // esq inf der
			glVertex2i(x, y2); // esq inf izq
			glEnd();

			// Restablece la transparencia al 100%
			glColor4f(1f, 1f, 1f, 1f);

		} else {
			glColor4f(1f, 1f, 1f, 0.5f);
			new Block(selection, selector_x * World.BLOCK_SIZE, selector_y * World.BLOCK_SIZE).draw();
			glColor4f(1f, 1f, 1f, 1f);
		}
	}

	private static void input() {

		// Si el mouse esta habilitado o si se hizo click, entonces...
		if (mouseEnabled || Mouse.isButtonDown(0)) {

			mouseEnabled = true;

			// Obtiene la posicion del mouse (xy)
			int mousex = Mouse.getX();
			int mousey = Display.getHeight() - Mouse.getY() - 1; // -1 ?

			boolean mouseClicked = Mouse.isButtonDown(0);

			// Divide la posicion del mouse por el tamaño del bloque y lo redondea para obtener el numero exacto de la grilla
			selector_x = Math.round(mousex / World.BLOCK_SIZE);
			selector_y = Math.round(mousey / World.BLOCK_SIZE);

			// Reemplaza el bloque seleccionado
			if (mouseClicked) grid.setAt(selection, selector_x, selector_y);

		}

		// Mientras se haya leido un evento del teclado, entonces...
		while (Keyboard.next()) {

			// Deshabilita el mouse cuando se usa el teclado para que no se superpongan
			mouseEnabled = false;

			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
				if (!(selector_x + 1 >= World.columnas)) selector_x += 1;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
				if (!(selector_x - 1 < 0)) selector_x -= 1;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_UP && Keyboard.getEventKeyState()) {
				if (!(selector_y - 1 < 0)) selector_y -= 1;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && Keyboard.getEventKeyState()) {
				if (!(selector_y + 1 >= World.filas)) selector_y += 1;
			}

			// Si el evento causado es igual a la tecla S, entonces...
			if (Keyboard.getEventKey() == Keyboard.KEY_S) grid.save(new File("save.xml"));
			if (Keyboard.getEventKey() == Keyboard.KEY_L) grid.load(new File("save.xml"));
			if (Keyboard.getEventKey() == Keyboard.KEY_1) selection = BlockType.AIR;
			if (Keyboard.getEventKey() == Keyboard.KEY_2) selection = BlockType.GRASS;
			if (Keyboard.getEventKey() == Keyboard.KEY_3) selection = BlockType.DIRT;
			if (Keyboard.getEventKey() == Keyboard.KEY_4) selection = BlockType.STONE;
			if (Keyboard.getEventKey() == Keyboard.KEY_5) selection = BlockType.BRICK;
			if (Keyboard.getEventKey() == Keyboard.KEY_C) grid.clear(); // reset
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				Display.destroy();
				System.exit(0);
			}

		}

	}

}

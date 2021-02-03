package minecraft2d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.File;

import static minecraft2d.World.*;

import static org.lwjgl.opengl.GL11.*;

public class Boot {

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	private static BlockGrid grid;

	private static BlockType selection = BlockType.AIR; // Bloque de aire por defecto
	private static int selector_x = 0, selector_y = 0;
	private static boolean mouseEnabled = true;

	public static void main(String[] args) {

		try {
			new Boot().start();
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

			// drawSelectionBox();

			Display.update();
			Display.sync(60);

			// Si se cambio el tamaño de la ventana, entonces...
			if (Display.wasResized()) resize();

		}

		Display.destroy();

	}

	private void setUpDisplay() {
		try {
			Display.setTitle("Minecraft2D");
			Display.setResizable(true);
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
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
		/* glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); */
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

		World.setColumnas(Boot.getWidth() / BLOCK_SIZE);
		World.setFilas(Boot.getHeight() / BLOCK_SIZE);

		// System.out.println("Columnas=" + World.columnas + ", Filas=" + World.filas);

	}

	private static void drawSelectionBox() {
		int x = selector_x * World.BLOCK_SIZE;
		int y = selector_y * World.BLOCK_SIZE;
		int x2 = x + World.BLOCK_SIZE;
		int y2 = y + World.BLOCK_SIZE;
		if (grid.getAt(selector_x, selector_y).getType() != BlockType.AIR || selection == BlockType.AIR) {
			glBindTexture(GL_TEXTURE_2D, 0);
			glColor4f(1f, 1f, 1f, 0.5f);
			glBegin(GL_QUADS);
			glVertex2i(x, y);
			glVertex2i(x2, y);
			glVertex2i(x2, y2);
			glVertex2i(x, y2);
			glEnd();
			glColor4f(1f, 1f, 1f, 1f);
		} else {
			glColor4f(1f, 1f, 1f, 0.5f);
			new Block(selection, selector_x * World.BLOCK_SIZE, selector_y * World.BLOCK_SIZE).draw();
			glColor4f(1f, 1f, 1f, 1f);
		}
	}

	private static void input() {

		int mousex = Mouse.getX();
		int mousey = Display.getHeight() - Mouse.getY() - 1; // -1 ?

		if (Mouse.isButtonDown(0)) {

			int grid_x = Math.round(mousex / World.BLOCK_SIZE);
			int grid_y = Math.round(mousey / World.BLOCK_SIZE);

			// System.out.println(grid_x + ", " + grid_y);

			grid.setAt(selection, grid_x, grid_y);

		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_S) grid.save(new File("save.xml"));
			if (Keyboard.getEventKey() == Keyboard.KEY_L) grid.load(new File("save.xml"));
			if (Keyboard.getEventKey() == Keyboard.KEY_1) selection = BlockType.AIR;
			if (Keyboard.getEventKey() == Keyboard.KEY_2) selection = BlockType.GRASS;
			if (Keyboard.getEventKey() == Keyboard.KEY_3) selection = BlockType.DIRT;
			if (Keyboard.getEventKey() == Keyboard.KEY_4) selection = BlockType.STONE;
			if (Keyboard.getEventKey() == Keyboard.KEY_5) selection = BlockType.BRICK;
		}

	}

}

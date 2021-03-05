package minecraft2d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.File;

import static minecraft2d.World.*;

import static org.lwjgl.opengl.GL11.*;

public class Screen {

	private final int width = 640;
	private final int height = 480;

	private BlockGrid grid;

	private BlockType type = BlockType.BRICK; // Bloque de ladrillo seleccionado por defecto
	private int x, y;
	private boolean mouseEnabled = true;

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

		/*
		 * En realidad no se necesita limpiar la pantalla, ya que creamos bloques todo el tiempo y no trabajamos con la misma
		 * textura.
		 */
		glClear(GL_COLOR_BUFFER_BIT);

		grid.draw();

	}

	// Maneja las entradas del usuario por medio del mouse o el teclado
	private void input() {
		mouse();
		keyboard();
	}

	private void mouse() {
		// Si el mouse esta habilitado o si se hizo click izquierdo en la pantalla
		if (mouseEnabled || Mouse.isButtonDown(0)) {

			/*
			 * Se vuelve a hablitar cuando se hace click izquierdo en la pantalla, ya que al usar el teclado, el mouse queda
			 * deshabilitado y esta es la unica forma de habilitarlo (entrando a este metodo por asi decirlo).
			 */
			mouseEnabled = true;

			// Divide la posicion del mouse por el tamaño del bloque y lo redondea para obtener el numero exacto de la grilla
			x = Math.round(Mouse.getX() / World.BLOCK_SIZE);
			y = Math.round((height - Mouse.getY() - 1) / World.BLOCK_SIZE); // -1 ?

			// Si se hizo click izquierdo
			if (Mouse.isButtonDown(0)) grid.setAt(type, x, y); // Crea un nuevo bloque

		}
	}

	private void keyboard() {

		// Mientras se haya leido un evento del teclado
		while (Keyboard.next()) {

			/*
			 * Para usar el metodo getEventKey(), es necesario controlar las teclas con un getEventKeyState() ya que asegura de que
			 * solo registre las teclas que se presionan, no las que se liberan. El metodo isKeyDown() se puede usar para verificar
			 * las teclas presionadas, en lugar de presionar una tecla una vez con getKeyEvent().
			 */

			/*
			 * Calcula los limites de la matriz evitando sumar o restar una posicion fuera de los limites
			 * (ArrayIndexOutOfBoundsException).
			 * 
			 * Para el movimiento KEY_RIGHT, x solo tiene que llegar hasta 19 y no 20 (por eso el "x + 1" sin asignar), ya que 19 *
			 * 32 = 608, dejando el espacio sobrante para la textura de 32 pixeles (608 + 32 = 640 limite) sin pasar el limite del
			 * ancho de la pantalla.
			 */

			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) if (x + 1 < World.columnas) {
				x++;
				mouseEnabled = false; // Deshabilita el mouse cuando se usa el teclado para que no se superpongan los eventos
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) if (x > 0) {
				x--;
				mouseEnabled = false;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) if (y > 0) {
				y--;
				mouseEnabled = false;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) if (y + 1 < World.filas) {
				y++;
				mouseEnabled = false;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) grid.save(new File("save.xml"));
			if (Keyboard.isKeyDown(Keyboard.KEY_L)) grid.load(new File("save.xml"));
			if (Keyboard.isKeyDown(Keyboard.KEY_1)) type = BlockType.AIR;
			if (Keyboard.isKeyDown(Keyboard.KEY_2)) type = BlockType.GRASS;
			if (Keyboard.isKeyDown(Keyboard.KEY_3)) type = BlockType.DIRT;
			if (Keyboard.isKeyDown(Keyboard.KEY_4)) type = BlockType.STONE;
			if (Keyboard.isKeyDown(Keyboard.KEY_5)) type = BlockType.BRICK;
			if (Keyboard.isKeyDown(Keyboard.KEY_C)) grid.clear();
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Display.destroy();
				System.exit(0);
			}

		}

	}

	// Dibuja el cuadro de seleccion
	private void drawSelectionBox() {

		glColor4f(1f, 1f, 1f, 0.5f); // Color blanco con 50% de transparencia

		/*
		 * Es importante deshabilitar el mouse cuando se usa el teclado, ya que se van a estar tomando x e y de ambas entradas
		 * (mouse y teclado) y nunca se va a mover el cuadro de seleccion.
		 */
		new Block(type, x * World.BLOCK_SIZE, y * World.BLOCK_SIZE).draw();

		// ?
		glColor4f(1f, 1f, 1f, 1f); // Color blanco con 100% de transparencia
	}

	private void resize() {

		// Especifica los parametros de transformacion de la ventana grafica para todas las ventanas graficas
		glViewport(0, 0, Display.getWidth(), Display.getHeight());

		World.setColumnas(Screen.getWidth() / BLOCK_SIZE);
		World.setFilas(Screen.getHeight() / BLOCK_SIZE);

		System.out.println(Display.getWidth() + "," + Display.getHeight());

	}

}

package paquete;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Delta {

	/* En primer lugar, la hora del sistema se almacena en una variable de tipo long llamada "lastFrame". Luego, en el bucle
	 * del juego, se recupera y devuelve la cantidad de tiempo que ha pasado desde el ultimo fotograma.
	 * 
	 * La variable delta representa el tiempo transcurrido desde la ultima actualizacion del cuadro. Cuanto mayor sea el
	 * delta, menor sera la velocidad de fotogramas. Cuanto menor sea el delta, mayor sera la velocidad de fotogramas. Si la
	 * velocidad de fotogramas esta limitada a 60 fotogramas por segundo, nunca deberia existir un valor delta inferior a
	 * 16. */

	protected boolean running = false;

	private static long lastFrame;

	// Tiempo en milisegundos
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private static double getDelta() {
		long currentTime = getTime();
		// Diferencia entre el tiempo actual y el ultimo fotograma
		double delta = (double) (currentTime - lastFrame);
		lastFrame = getTime(); // Estable el tiempo actual en lastFrame
		return delta;
	}

	public static void main(String args[]) {
		try {
			new Delta().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	public void start() throws LWJGLException {

		Display.setTitle("Delta Demo");
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create();

		create();

		running = true;

		// En la primera vuelta del loop se calcula el tiempo del ultimo fotograma ya que nunca se inicializa
		lastFrame = getTime();

		while (running && !Display.isCloseRequested()) {

			render();

			System.out.println(getDelta());

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

	}

}

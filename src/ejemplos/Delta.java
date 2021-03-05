package ejemplos;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Delta {

	/*
	 * En primer lugar, la hora del sistema se almacena en una variable de tipo long llamada "lastFrame". Luego, en el bucle
	 * del juego, se recupera y devuelve la cantidad de tiempo que ha pasado desde el ultimo fotograma.
	 * 
	 * La variable delta representa el tiempo transcurrido desde la ultima actualizacion del cuadro. Cuanto mayor sea el
	 * delta, menor sera la velocidad de fotogramas. Cuanto menor sea el delta, mayor sera la velocidad de fotogramas. Si la
	 * velocidad de fotogramas esta limitada a 60 fotogramas por segundo, NUNCA deberia existir un valor delta inferior a
	 * 16.
	 */

	private static long lastFrame;

	private int x = 10, y = 10;

	/*
	 * La resolucion del temporizador se define como (de los documentos LWJGL)
	 * "el numero de tics que... el temporizador hace en un segundo". Divide el valor actual del temporizador en tics
	 * (getTime) por la resolucion del temporizador para obtener el tiempo en segundos. Como quiero el tiempo en
	 * milisegundos, lo multiplico por 1000.
	 * 
	 * Sys.getTime se creo para usos como LWJGL con aspectos como la precision y el rendimiento en mente.
	 * System.currentTimeMillis, sin embargo, no lo fue. Sin embargo, es mas sencillo.
	 */
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

		// En la primera vuelta del loop se calcula el tiempo del ultimo fotograma ya que nunca se inicializa
		lastFrame = getTime();

		while (!Display.isCloseRequested()) {

			render();

			int delta = (int) getDelta();

			System.out.println(delta);

			/*
			 * Sin el delta, se puede actualizar el "movimiento" del cuadro sumandole + 1 a x en cada vuelta del ciclo, siendo lo
			 * mismo que multiplicar el delta por 0.1.
			 */
			x += delta * 0.1; // Velocidad horizontal relentizada a 10 veces (0.1)
			y += delta * 0.1; // Velocidad vertical

			// Dibuja una caja en las coordenadas xy del primer vertice y en xy del segundo vertice de la esquina
			glRecti(x, y, x + 30, y + 30); // Esto es exactamente igual al modo inmediato

			Display.update();
			/*
			 * Lo importante es que Delta siempre sera mayor o igual a 16, por que lo unico que le puede pasar a nuestra velocidad
			 * de fotogramas es que disminuira.
			 */
			Display.sync(60); // Aunque se cambie la cantidad de fotogramas la velocidad del rectangulo siempre sera la misma

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

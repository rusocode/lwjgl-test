package entidades;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class EntityDemo {

	// El modificador de acceso default solo es accesible desde el propio paquete
	private static long lastFrame;

	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private static int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}

	/* Las clases internas estaticas solo tienen acceso a los miembros estaticos de la clase adjunta.
	 * 
	 * https://javadesdecero.es/poo/clases-internas/ */
	private static class Box extends AbstractMovableEntity {

		// Le pasa la posicion x e y, y el ancho y alto de la caja al constructor de la superclase
		public Box(double x, double y, double width, double height) {
			super(x, y, width, height);
		}

		@Override
		public void draw() {
			glRectd(x, y, x + width, y + height);
		}
	}

	private static class Point extends AbstractEntity {

		public Point(double x, double y) {
			super(x, y, 1, 1);
		}

		@Override
		public void draw() {
			glBegin(GL_POINTS);
			glVertex2d(x, y);
			glEnd();
		}

		@Override
		public void update(int delta) {
			// TODO Auto-generated method stub

		}

	}

	public static void main(String[] args) {
		try {
			new EntityDemo().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1); // 0 o 1?
		}
	}

	public void start() throws LWJGLException {

		Display.setTitle("Entity Demo");
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create();

		// Entidades del codigo de inicializacion
		MoveableEntity box = new Box(100, 100, 50, 50);
		Entity point = new Point(10, 10);

		lastFrame = getTime();

		create();

		while (!Display.isCloseRequested()) {

			render(box, point);

			Display.update();
			Display.sync(120);

		}

		Display.destroy();

	}

	private void create() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	private void render(MoveableEntity box, Entity point) {
		glClear(GL_COLOR_BUFFER_BIT);

		// Estable la posicion del point en la ubicacion del mouse
		point.setLocation(Mouse.getX(), Mouse.getY() - 1); // -1 ?

		// Calcula el delta
		int delta = getDelta();

		box.update(delta);
		point.update(delta);

		// Si hay colision entre el punto y la caja, entonces...
		if (box.intersects(point)) box.setDX(0.2); // Mueva la caja horizontalmente a una velocidad reducida a 9 veces

		// Si la caja llego a los limites del ancho de la ventana, entonces...
		calcularLimites(box);

		// Lo comento para que no se vea el punto
		// point.draw();
		box.draw(); // Rellena la caja por asi decirlo
	}

	private void calcularLimites(MoveableEntity box) {
		// Si llego al final (640) rebota hacia atras
		if (box.getX() + box.getWidth() >= 640) box.setDX(-0.2);
		// Si llego al principio (0) rebota hacia adelante
		if (box.getX() <= 0) box.setDX(0.2);
	}

	private void setInicio(MoveableEntity box) {
		// Establece los valores en el eje de coordenadas del juego
		box.setX(100);
		box.setY(100);

		// Nunca se mueve ya que DX esta en 0
		box.setDX(0);
	}

}

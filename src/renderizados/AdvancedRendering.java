package renderizados;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.FloatBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * Muestra una simulacion de particulas similar al espacio con tecnicas de renderizado avanzadas.
 * 
 */
public class AdvancedRendering {

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	/** Todos los diferentes modos de render. */
	private static enum RenderMode {
		IMMEDIATE, DISPLAY_LISTS, VERTEX_ARRAY_OBJECT, VERTEX_BUFFER_OBJECT
	}

	// Modo Inmediato por defecto
	RenderMode mode = RenderMode.IMMEDIATE;

	// Velocidad a la que viaja la "camara"
	private static float speed;

	private Point[] points;
	private Random random;

	private int displayList;
	private FloatBuffer vertexArray;
	private int vertexBufferObject;

	private float delta; // int?
	private static long lastFrame;

	private static float getDelta() { // No double?
		long time = getTime();
		float delta = (float) (time - lastFrame);
		lastFrame = time;
		return delta;
	}

	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();
		setUpEntities();

		/**
		 * Display lists
		 */

		// Genera un ID para la lista de visualizacion
		displayList = glGenLists(1);

		// Crea la lista de visualizacion usando el ID displayList
		// Todas las llamadas posteriores para renderizado se almacenaran en la lista de visualizacion
		glNewList(displayList, GL_COMPILE);
		glBegin(GL_POINTS);

		for (Point p : points)
			glVertex3f(p.x, p.y, p.z);

		glEnd();

		// Deja de almacenar llamadas en la lista de visualizacion y compila
		glEndList();

		/**
		 * Vertex Arrays and Vertex Buffer Objects
		 */

		/*
		 * Crea un FloatBuffer (arreglo complejo de flotantes) con la longitud de la cantidad de puntos * 3 (porque
		 * tenemos 3 vertices por punto).
		 */
		vertexArray = BufferUtils.createFloatBuffer(points.length * 3);
		// Itera los puntos y los guarda en el FloatBuffer
		for (Point p : points)
			vertexArray.put(new float[] { p.x, p.y, p.z });

		// Hace que el buffer sea legible para OpenGL
		vertexArray.flip();

		// Crear el identificador para el VBO
		vertexBufferObject = glGenBuffers();
		// Vincular el VBO para su uso (en este caso: almacenar informacion)
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
		// Almacena todo el contenido del FloatBuffer en el VBO
		glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
		// Desenlaza el VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		lastFrame = getTime();

		System.out.println("Modo de renderizado configurado a Immediate.");

		while (!Display.isCloseRequested()) {

			render();
			input();

			Display.update();
			Display.sync(30);
		}

		// Elimina la lista de visualizacion
		glDeleteLists(displayList, 1);
		// Elimina el VBO
		glDeleteBuffers(vertexBufferObject);

		Display.destroy();
		System.exit(0);
	}

	private void setUpDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Renderizados");
			Display.setVSyncEnabled(true);
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

		// Configura zFar en 1000000 para ver todos los puntos (propositos de evaluacion comparativa)
		gluPerspective((float) 30, Display.getWidth() / Display.getHeight(), 0.001f, 1000000000000L);
		glMatrixMode(GL_MODELVIEW);

		// Vuelve a verificar la validez del dibujo 3D
		glEnable(GL_DEPTH_TEST);
	}

	private void setUpEntities() {

		points = new Point[3000000];
		random = new Random();

		/*
		 * Modifique la variable zFar para adaptarme a points.length. Los puntos, no importa cuanto, ahora aparecen
		 * distribuidos uniformemente a lo largo de la pantalla.
		 */
		for (int i = 0; i < points.length; i++)
			points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f,
					random.nextInt(points.length / 50) - points.length / 50);

	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		/*
		 * Divide el delta entre 16 para obtener un delta de 16 a 60 fps. Si es 60 fps, el delta sera 1.0f y todas las
		 * llamadas de entrada seran las mismas que antes.
		 */
		delta = getDelta() / 16f;

		// Empuja la pantalla hacia adentro a la velocidad especificada
		glTranslatef(0, 0, speed * delta);

		switch (mode) {

		case DISPLAY_LISTS:
			// Dibuja nuestra lista de visualizacion
			glCallList(displayList);

			break;

		case VERTEX_BUFFER_OBJECT:
			// Habilita las matrices de vertices (VBO)
			glEnableClientState(GL_VERTEX_ARRAY);
			// Le dice a OpenGL que use nuestra VBO
			glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
			// Le dice a OpenGL que busque los datos en el VBO vinculado con 3 componentes (xyz) y con el tipo float
			glVertexPointer(3, GL_FLOAT, 0, 0L);
			/*
			 * Le dice a OpenGL que dibuje los datos proporcionados por el metodo de puntero como puntos. Longitud cantidad de
			 * puntos.
			 */
			glDrawArrays(GL_POINTS, 0, points.length);
			// Desenlaza el VBO
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			// Deshabilita las matrices de vertices
			glDisableClientState(GL_VERTEX_ARRAY);

			break;

		case VERTEX_ARRAY_OBJECT:
			// Habilita las matrices de vertices
			glEnableClientState(GL_VERTEX_ARRAY);
			// Le dice a OpenGL que busque los datos en el buffer vertexArray con 3 componentes (xyz)
			glVertexPointer(3, 0, vertexArray);
			/*
			 * Le dice a OpenGL que dibuje los datos proporcionados por el metodo de puntero como puntos. Longitud cantidad de
			 * puntos.
			 */
			glDrawArrays(GL_POINTS, 0, points.length);
			// Deshabilita las matrices de vertices
			glDisableClientState(GL_VERTEX_ARRAY);

			break;

		case IMMEDIATE:

			glBegin(GL_POINTS);

			for (Point p : points)
				glVertex3f(p.x, p.y, p.z);

			glEnd();

			break;
		}

	}

	private void input() {

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) speed += 0.01f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) speed -= 0.01f * delta;

		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				speed = 0f;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
				System.out.println("Modo de renderizado configurado a VBO.");
				mode = RenderMode.VERTEX_BUFFER_OBJECT;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
				System.out.println("Modo de renderizado configurado a VAO.");
				mode = RenderMode.VERTEX_ARRAY_OBJECT;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
				System.out.println("Modo de renderizado configurado a Display Lists.");
				mode = RenderMode.DISPLAY_LISTS;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
				System.out.println("Modo de renderizado configurado a Immediate.");
				mode = RenderMode.IMMEDIATE;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
				speed = 0;
				glLoadIdentity();
			}
		}
	}

	public static void main(String[] args) {
		try {
			new AdvancedRendering().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

	}

	private static class Point {

		final float x;
		final float y;
		final float z;

		public Point(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}
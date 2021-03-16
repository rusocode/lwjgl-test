package renderizados;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * Renderiza un triangulo de color usando un objeto de bufer de vertice (vertex buffer object).
 *
 */

public class VBO {

	private final int width = 640;
	private final int height = 480;

	private final int amountOfVertices = 3;
	private final int vertexSize = 3;
	private final int colorSize = 3;

	private FloatBuffer vertexData;
	private FloatBuffer colorData;

	// Administradores de memoria?
	private int vboVertexHandle;
	private int vboColorHandle;

	private void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();

		vertexData = BufferUtils.createFloatBuffer(amountOfVertices * vertexSize);
		vertexData.put(new float[] { -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0 });
		vertexData.flip();

		colorData = BufferUtils.createFloatBuffer(amountOfVertices * colorSize);
		colorData.put(new float[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 });
		colorData.flip();

		vboVertexHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboColorHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		while (!Display.isCloseRequested()) {

			render();

			Display.update();
			Display.sync(60);

		}

		Display.destroy();

	}

	private void setUpDisplay() {
		try {
			Display.setTitle("Vertex Buffer Demo");
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
		glOrtho(1, -1, 1, -1, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT);

		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glVertexPointer(vertexSize, GL_FLOAT, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glColorPointer(colorSize, GL_FLOAT, 0, 0L);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glDrawArrays(GL_TRIANGLES, 0, amountOfVertices);
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

	}

	public static void main(String[] args) {
		try {
			new VBO().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

}

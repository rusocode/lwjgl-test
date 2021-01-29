package pong;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import entidades.AbstractMovableEntity;
import ejemplos.Delta;

public class Pong {

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	private static boolean isRunning = true;
	private static Ball ball;
	private static Bat bat;

	private static long lastFrame;

	public static void main(String[] args) {
		try {
			new Pong().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	public void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();
		setUpEntities();
		setUpTimer();

		while (isRunning) {

			render();
			logic(getDelta());
			input();

			Display.update();
			Display.sync(60);

			if (Display.isCloseRequested()) isRunning = false;

		}

		Display.destroy();

	}

	private static void setUpDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Pong");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	private static void setUpOpenGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	private static void setUpEntities() {
		bat = new Bat(10, HEIGHT / 2 - 80 / 2, 10, 80);
		ball = new Ball(WIDTH / 2 - 10 / 2, HEIGHT / 2 - 10 / 2, 10, 10);
		ball.setDX(-.1);
	}

	private static void setUpTimer() {
		lastFrame = getTime();
	}

	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private static int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		ball.draw();
		bat.draw();
	}

	private void input() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) bat.setDY(.2);
		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) bat.setDY(-.2);
		else bat.setDY(0);
	}

	private void logic(int delta) {
		ball.update(delta);
		bat.update(delta);
		if (ball.getX() <= bat.getX() + bat.getWidth() && ball.getX() >= bat.getX() && ball.getY() >= bat.getY()
				&& ball.getY() <= bat.getY() + bat.getHeight()) {
			ball.setDX(0.3);
		}
	}

	private static class Bat extends AbstractMovableEntity {

		public Bat(double x, double y, double width, double height) {
			super(x, y, width, height);
		}

		@Override
		public void draw() {
			glRectd(x, y, x + width, y + height);
		}
	}

	private static class Ball extends AbstractMovableEntity {

		public Ball(double x, double y, double width, double height) {
			super(x, y, width, height);
		}

		@Override
		public void draw() {
			glRectd(x, y, x + width, y + height);
		}
	}

}

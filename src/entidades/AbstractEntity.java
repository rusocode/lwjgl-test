package entidades;

import java.awt.Rectangle;

// Valores de entidades absolutas

// Clase puente para evitar sobreescribir todos los metodos de la interfaz Entity
public abstract class AbstractEntity implements Entity {

	protected double x;
	protected double y;
	protected double width;
	protected double height;
	private final Rectangle hitbox = new Rectangle();

	public AbstractEntity(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public boolean intersects(Entity other) {

		// Establece los limites para la caja creada en la x, y, ancho y alto especificados
		hitbox.setBounds((int) x, (int) y, (int) width, (int) height);

		// Comprueba si la caja se cruza con el interior de la entidad recibida (point)
		return hitbox.intersects(other.getX(), other.getY(), other.getWidth(), other.getHeight());
	}

}

package entidades;

// Valores de entidades movibles

public abstract class AbstractMovableEntity extends AbstractEntity implements MoveableEntity {

	private double dx;
	private double dy;

	public AbstractMovableEntity(double x, double y, double width, double height) {
		super(x, y, width, height);
		// Por defecto la caja y el punto permanecen quietos
		this.dx = 0;
		this.dy = 0;
	}

	// Calcula la velocidad de una entidad
	@Override
	public void update(int delta) {
		this.x += delta * dx;
		this.y += delta * dy;
	}

	public double getDX() {
		return dx;
	}

	public double getDY() {
		return dy;
	}

	public void setDX(double dx) {
		this.dx = dx;
	}

	public void setDY(double dy) {
		this.dy = dy;
	}

	@Override
	public abstract void draw();
}
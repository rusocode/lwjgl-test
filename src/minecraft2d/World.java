package minecraft2d;

public class World {

	// Textura de 32 pixeles justo para 640 y 480?
	public static final int BLOCK_SIZE = 32;

	// Calcula el cantidad de columnas y filas dependiendo del tamaño de la ventana y la textura
	public static int columnas = Screen.getWidth() / BLOCK_SIZE;
	public static int filas = Screen.getHeight() / BLOCK_SIZE;

	public static int getColumnas() {
		return columnas;
	}

	public static void setColumnas(int columnas) {
		World.columnas = columnas;
	}

	public static int getFilas() {
		return filas;
	}

	public static void setFilas(int filas) {
		World.filas = filas;
	}

}

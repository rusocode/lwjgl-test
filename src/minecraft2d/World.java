package minecraft2d;

public class World {

	// Tama�o del bloque = 32 pixeles
	public static final int BLOCK_SIZE = 32;

	// Valores por defecto
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

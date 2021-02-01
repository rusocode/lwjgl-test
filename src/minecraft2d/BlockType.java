package minecraft2d;

public enum BlockType {

	// Almacena la ubicacion de las imagenes con el tipo de bloque
	STONE("res/img/stone.png"), AIR("res/img/air.png"), GRASS("res/img/grass.png"), DIRT("res/img/dirt.png");

	public final String location;

	BlockType(String location) {
		this.location = location;
	}

}
